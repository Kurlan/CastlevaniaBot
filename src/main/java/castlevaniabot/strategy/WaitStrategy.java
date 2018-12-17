package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.strategy.WaitStrategy.WaitType.KNEEL;
import static castlevaniabot.strategy.WaitStrategy.WaitType.STAND;

public class WaitStrategy implements Strategy {

    public static enum WaitType {
        STAND,
        KNEEL,
        WALK_LEFT,
        WALK_RIGHT,
    }

    private static final int WAIT_DELAY = 150;

    private int playerX;
    private int playerY;
    private int delay;
    private WaitType waitType;
    private boolean inPosition;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public WaitStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    public void init(final int playerX, final int playerY) {
        init(playerX, playerY, STAND);
    }

    public void init(final int playerX, final int playerY, final WaitType waitType) {
        init(playerX, playerY, waitType, (waitType == KNEEL ? 1 : WAIT_DELAY)
                + ThreadLocalRandom.current().nextInt(11));
    }

    public void init(final int playerX, final int playerY, final WaitType waitType,
                     final int delay) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.waitType = waitType;
        this.delay = delay;
        inPosition = false;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {
        if (botState.getPlayerX() == playerX && botState.getPlayerY() == playerY) {
            inPosition = true;
        }
        if (!inPosition) {
            gameState.getCurrentSubstage().route(playerX, playerY);
        } else if (delay > 0) {
            switch (waitType) {
                case KNEEL:
                    playerController.kneel();
                    break;
                case WALK_LEFT:
                    playerController.goLeft(botState);
                    break; // walk against wall
                case WALK_RIGHT:
                    playerController.goRight(botState);
                    break; // walk against wall
            }
            if (--delay == 0) {
                gameState.getCurrentSubstage().treasureTriggered();
            }
        }
    }
}