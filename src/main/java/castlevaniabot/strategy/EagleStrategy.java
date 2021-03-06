package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class EagleStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;


    public EagleStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState =gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject eagle = botState.getTargetedObject().getTarget();
        final int offsetX = (eagle.x - lastX) << 4;
        final int offsetY = (eagle.y - lastY) << 4;
        lastX = eagle.x;
        lastY = eagle.y;

        if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (!gameState.isWeaponing() && playerController.faceFlyingTarget(botState)) {
                playerController.whip(gameState);
            }
        } else if (!botState.isOnStairs() && playerController.isTargetInKneelingWhipRange(offsetY, offsetY, botState)) {
            if (playerController.faceFlyingTarget(botState)) {
                playerController.kneel();
                if (botState.isKneeling() && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            }
        }
    }
}