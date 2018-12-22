package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import static java.lang.Math.abs;

public class RedBonesStrategy implements Strategy {

    private boolean playerLeft;
    private int targetX;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public RedBonesStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        targetX = botState.getTargetedObject().getTarget().x;
        playerLeft = botState.isPlayerLeft();
    }

    @Override
    public void step() {
        final int distanceX = abs(targetX - botState.getPlayerX());
        if (distanceX <= 16) {
            // When standing on red bones, continue walking in direction facing.
            if (playerLeft) {
                playerController.goLeft(botState);
            } else {
                playerController.goRight(botState);
            }
        } else if (distanceX < 32) {
            gameState.getCurrentSubstage().moveAwayFromTarget(targetX, botState, gameState);
        }
    }

}