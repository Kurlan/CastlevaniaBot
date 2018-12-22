package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

public class FireColumnStrategy implements Strategy {

    private int done;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public FireColumnStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        done = 0;
    }

    @Override
    public void step() {

        if (done > 0) {
            --done;
            return;
        }
        if (gameState.isWeaponing()) {
            return;
        }

        final int targetX = botState.getTargetedObject().getTarget().x + ((botState.getPlayerX() < botState.getTargetedObject().getTarget().x) ? -32 : 32);
        if (botState.getPlayerX() == targetX) {
            if (botState.getTargetedObject().getTarget().playerFacing) {
                playerController.whip(gameState);
                done = 64;
            } else {
                gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget()); // walk past and turn around
            }
        } else {
            gameState.getCurrentSubstage().route(targetX, botState.getTargetedObject().getTarget().y);
        }
    }
}