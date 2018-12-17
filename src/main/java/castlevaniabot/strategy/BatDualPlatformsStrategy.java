package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.creativeelements.MovingPlatform;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.FISHMAN;
import static castlevaniabot.model.gameelements.GameObjectType.RED_BAT;

public class BatDualPlatformsStrategy implements Strategy {

    public boolean done;

    private GameObject bat;
    private int lastY0;
    private int lastY1;
    private int lastY2;
    private int lastX;

    private MovingPlatform platform;
    private int lastPlatformX;
    private boolean left;

    private GameObject fishman;
    private int lastFishmanX;
    private int lastFishmanY;

    private int justWhipped;
    private boolean hangRequested;
    private boolean walkRequested;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public BatDualPlatformsStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        justWhipped = 0;
        hangRequested = walkRequested = left = done = false;
    }

    @Override
    public void step() {

        if (done) {
            return;
        } else if (botState.getPlayerX() >= 1280) {
            done = true;
        }

        updateObjects();

        if (fishman != null) {
            final int offsetX = (fishman.x - lastFishmanX) << 4;
            final int offsetY = (fishman.y - lastFishmanY) << 4;
            lastFishmanX = fishman.x;
            lastFishmanY = fishman.y;

            if (b.isInStandingWhipRange(fishman, offsetX, offsetY)) {
                if (b.face(fishman) && !gameState.isWeaponing()) {
                    b.whip();
                }
            }
        }

        if (bat != null) {
            final int offsetX = (bat.x > lastX) ? 16 : -16;
            lastX = bat.x;
            final int offsetY;
            if (bat.y > lastY2) {
                offsetY = 8;
            } else if (bat.y < lastY2) {
                offsetY = -8;
            } else {
                offsetY = 0;
            }
            lastY2 = lastY1;
            lastY1 = lastY0;
            lastY0 = bat.y;

            if (b.isInStandingWhipRange(bat, offsetX, offsetY)) {
                if (b.face(bat) && !gameState.isWeaponing()) {
                    b.whip();
                    justWhipped = 4;
                }
            } else if (b.isInKneelingWhipRange(bat, offsetX, offsetY)) {
                if (b.face(bat)) {
                    b.kneel();
                    if (b.kneeling && !gameState.isWeaponing()) {
                        b.whip();
                        justWhipped = 4;
                    }
                }
            }
        }

        if (hangRequested) {
            if (botState.getPlayerX() < 995) {
                playerController.goRight(botState);
            } else {
                hangRequested = false;
                walkRequested = true;
            }
        }

        if (walkRequested) {
            if (platform == null || (!left && platform.x1 > 1008)
                    || (botState.getPlayerX() + 4 >= platform.x1 && botState.getPlayerX() - 4 <= platform.x2)) {
                walkRequested = false;
            } else if (platform.x1 < 1000) {
                playerController.goRight(botState);
            }
        } else if (justWhipped > 0 && --justWhipped == 0 && bat == null) {
            hangRequested = true;
        }

        if (platform != null && botState.getPlayerX() + 4 >= platform.x1
                && botState.getPlayerX() - 4 <= platform.x2) {
            final int cx = platform.x1 + 24;
            if (botState.getPlayerX() == cx) {
                if (botState.isPlayerLeft()) {
                    playerController.goLeft(botState);
                }
            } else if (botState.getPlayerX() < cx) {
                playerController.goRight(botState);
            } else {
                playerController.goLeft(botState);
            }

            if (botState.getPlayerX() >= 1016 && botState.getPlayerX() < 1064) {
                b.kneel();
            } else if ((platform.x1 >= 1087 && platform.x1 <= 1089)
                    || (platform.x1 >= 1231 && platform.x1 <= 1233)) {
                playerController.goRightAndJump(botState);
            }
        }
    }

    private void updateObjects() {

        bat = fishman = null;
        final GameObject[] objs = gameState.getGameObjects();
        for (int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = objs[i];
            if (obj.type == RED_BAT) {
                bat = obj;
            } else if (obj.type == FISHMAN) {
                fishman = obj;
            }
        }

        platform = null;
        final MovingPlatform[] movingPlatforms = b.getGameState().getMovingPlatforms();
        for (int i = b.getGameState().getMovingPlatformsCount() - 1; i >= 0; --i) {
            final MovingPlatform p = movingPlatforms[i];
            if (p.x2 >= botState.getPlayerX() && (platform == null
                    || (p.x2 - botState.getPlayerX() < platform.x2 - botState.getPlayerX()))) {
                platform = p;
            }
        }
        if (platform != null) {
            final int vx = platform.x1 - lastPlatformX;
            lastPlatformX = platform.x1;
            if (vx > 0) {
                left = false;
            } else if (vx < 0) {
                left = true;
            }
        }
    }
}