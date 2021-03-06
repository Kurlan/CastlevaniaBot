package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class PantherStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public PantherStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject panther = botState.getTargetedObject().getTarget();
        final int offsetX = (panther.x - lastX) << 4;
        final int offsetY = (panther.y - lastY) << 4;
        lastX = panther.x;
        lastY = panther.y;

        if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState)
                && playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            playerController.whip(gameState);
        }
    }
}