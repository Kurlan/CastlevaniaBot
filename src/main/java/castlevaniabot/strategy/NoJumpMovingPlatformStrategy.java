package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.creativeelements.MovingPlatform;

import static java.lang.Math.abs;

public class NoJumpMovingPlatformStrategy implements Strategy {

    private static enum State {
        WALK_TO_POINT_1,
        WAIT_FOR_PLATFORM,
        WALK_ONTO_PLATFORM,
        WAIT_FOR_PLATFORM_TO_MOVE,
        WALK_OFF_OF_PLATFORM,
        DONE,
    }

    private State state;
    private int playerX1;
    private int playerX2;
    private int playerY;
    private int minX;
    private int maxX;
    private int lastX;
    private boolean approaching;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public NoJumpMovingPlatformStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    // playerX1 and playerX2 are the tips of the chasam edge blocks
    public void init(final int playerX1, final int playerX2, final int playerY) {
        this.playerX1 = playerX1;
        this.playerX2 = playerX2;
        this.playerY = playerY;
        if (playerX1 < playerX2) {
            minX = playerX1;
            maxX = playerX2;
        } else {
            minX = playerX2;
            maxX = playerX1;
        }
        state = State.WALK_TO_POINT_1;
        approaching = false;
        lastX = -512;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final MovingPlatform platform = b.getGameState().getMovingPlatform(minX, maxX);
        if (platform == null) {
            approaching = false;
            lastX = -512;
            return;
        }

        final int vx = (lastX >= 0) ? (platform.x1 - lastX) : 0;
        if (vx != 0) {
            approaching = (playerX1 > playerX2) == (vx > 0);
        }
        lastX = platform.x1;

        switch (state) {
            case WALK_TO_POINT_1:
                if (botState.getPlayerX() != playerX1 || botState.getPlayerY() != playerY) {
                    gameState.getCurrentSubstage().route(playerX1, playerY);
                } else {
                    state = State.WAIT_FOR_PLATFORM;
                }
                break;
            case WAIT_FOR_PLATFORM:
                if (approaching) {
                    if (playerX1 > playerX2) {
                        if (abs(platform.x2 - playerX1) < 3) {
                            state = State.WALK_ONTO_PLATFORM;
                        }
                    } else {
                        if (abs(platform.x1 - playerX1) < 3) {
                            state = State.WALK_ONTO_PLATFORM;
                        }
                    }
                }
                break;
            case WALK_ONTO_PLATFORM:
                if (abs(platform.x1 + 16 - botState.getPlayerX()) < 2) {
                    state = State.WAIT_FOR_PLATFORM_TO_MOVE;
                }
                if (playerX1 > playerX2) {
                    b.pressLeft();
                } else {
                    b.pressRight();
                }
                break;
            case WAIT_FOR_PLATFORM_TO_MOVE:
                if (playerX1 > playerX2) {
                    if (abs(platform.x1 - playerX2) < 16) {
                        state = State.WALK_OFF_OF_PLATFORM;
                    }
                } else {
                    if (abs(platform.x2 - playerX2) < 16) {
                        state = State.WALK_OFF_OF_PLATFORM;
                    }
                }
                break;
            case WALK_OFF_OF_PLATFORM:
                if (botState.getPlayerX() == playerX2 && botState.getPlayerY() == playerY) {
                    state = State.DONE;
                } else if (playerX1 > playerX2) {
                    b.pressLeft();
                } else {
                    b.pressRight();
                }
                break;
        }
    }
}