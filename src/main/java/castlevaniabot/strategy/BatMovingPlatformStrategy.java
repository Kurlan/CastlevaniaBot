package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.creativeelements.MovingPlatform;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.FISHMAN;
import static castlevaniabot.model.gameelements.GameObjectType.RED_BAT;

public class BatMovingPlatformStrategy implements Strategy {

    private static final int BAT_TIMER_THRESHOLD = 120;

    public boolean done;

    private int lastY0;
    private int lastY1;
    private int lastY2;
    private int lastX;
    private int lastPlatformX;
    private int lastFishmanX;
    private int lastFishmanY;
    private int batTimer;
    private boolean jumpRequested;
    private boolean left;
    private GameObject lastBat;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public BatMovingPlatformStrategy(final BotState botState, final GameState gameState,
                                     final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        left = jumpRequested = done = false;
        batTimer = BAT_TIMER_THRESHOLD;
        lastBat = null;
    }

    @Override
    public void step() {

        if (done) {
            return;
        }

        ++batTimer;
        final GameObject bat = getRedBat();
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

            batTimer = BAT_TIMER_THRESHOLD;

            if (playerController.isInStandingWhipRange(bat, offsetX, offsetY, botState)) {
                if (playerController.face(bat, botState) && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            } else if (playerController.isInKneelingWhipRange(bat, offsetX, offsetY, botState)) {
                if (playerController.face(bat, botState)) {
                    playerController.kneel();
                    if (botState.isKneeling() && !gameState.isWeaponing()) {
                        playerController.whip(gameState);
                    }
                }
            }
        } else if (lastBat != null) {
            batTimer = 0;
        }
        lastBat = bat;

        final MovingPlatform platform = getMovingPlatform();
        if (platform != null) {
            final int vx = platform.x1 - lastPlatformX;
            lastPlatformX = platform.x1;
            if (vx < 0) {
                left = true;
            } else if (vx > 0) {
                left = false;
            }
        }

        final GameObject fishman = getFishman();
        if (fishman != null) {
            final int offsetX = (fishman.x - lastFishmanX) << 4;
            final int offsetY = (fishman.y - lastFishmanY) << 4;
            lastFishmanX = fishman.x;
            lastFishmanY = fishman.y;

            if (playerController.isInStandingWhipRange(fishman, offsetX, offsetY, botState)) {
                if (playerController.face(fishman, botState) && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            }
        }

        if (bat == null && fishman == null && (botState.isPlayerLeft() || (botState.getPlayerX() > 400
                && (botState.getPlayerX() - (botState.getCurrentTile().getX() << 4)) < 19))) {
            playerController.goRight(botState);
        }

        if (platform != null) {
            if (botState.getPlayerX() + 4 >= platform.x1 && botState.getPlayerX() - 4 <= platform.x2) {
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

                if (botState.getPlayerX() >= 856 && botState.getPlayerX() < 936) {
                    playerController.kneel();
                }
            } else if (botState.isCanJump() && batTimer < BAT_TIMER_THRESHOLD) {
                if (botState.getPlayerX() > 400) {
                    if (left && platform.x1 <= 812) {
                        jumpRequested = true;
                        batTimer = BAT_TIMER_THRESHOLD;
                    }
                } else {
                    if ((left && platform.x1 < 296) || (!left && platform.x1 < 246)) {
                        jumpRequested = true;
                        batTimer = BAT_TIMER_THRESHOLD;
                    }
                }
            }
        }

        if ((botState.getPlayerX() >= 352 && botState.getPlayerX() < 388)
                || (botState.getPlayerX() >= 948 && botState.getPlayerX() < 964)) {
            jumpRequested = true;
        }

        if (jumpRequested) {
            if (botState.isPlayerLeft()) {
                playerController.goRight(botState);
            } else {
                playerController.goRightAndJump(botState);
                jumpRequested = false;
                if ((botState.getPlayerX() >= 380 && botState.getPlayerX() < 388)
                        || (botState.getPlayerX() >= 956 && botState.getPlayerX() < 964)) {
                    done = true;
                }
            }
        }
    }

    private MovingPlatform getMovingPlatform() {
        return (gameState.getMovingPlatformsCount() > 0) ? gameState.getMovingPlatforms()[0] : null;
    }

    private GameObject getRedBat() {
        final GameObject[] objs = gameState.getGameObjects();
        for (int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            if (objs[i].type == RED_BAT) {
                return objs[i];
            }
        }
        return null;
    }

    private GameObject getFishman() {
        final GameObject[] objs = gameState.getGameObjects();
        for (int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            if (objs[i].type == FISHMAN) {
                return objs[i];
            }
        }
        return null;
    }
}