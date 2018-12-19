package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class AxeStrategy implements Strategy {

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    private int lastX;
    private int lastY;

    public AxeStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject axe = botState.getTargetedObject().getTarget();
        final int offsetX = (axe.x - lastX) << 4;
        final int offsetY = (axe.y - lastY) << 4;
        lastX = axe.x;
        lastY = axe.y;

        if (axe.distanceX < 24) {
            final boolean flyingHigh = axe.y + offsetY < botState.getPlayerY() - 16;
            if (!botState.isAtTopOfStairs() && flyingHigh) {
                playerController.kneel();
            } else if (!flyingHigh && botState.isCanJump()) {
                playerController.jump(botState);
            }
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState)) {
                playerController.whip(gameState);
            }
        } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            playerController.kneel();
            if (botState.isKneeling() && !gameState.isWeaponing() && playerController.faceTarget(botState, gameState)) {
                playerController.whip(gameState);
            }
        }
    }
}