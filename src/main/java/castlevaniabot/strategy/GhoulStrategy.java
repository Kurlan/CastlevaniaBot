package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class GhoulStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private boolean usedHolyWater;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public GhoulStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        usedHolyWater = false;
    }

    @Override
    public void step() {

        final GameObject ghoul = botState.getTargetedObject().getTarget();
        final int offsetX = (ghoul.x - lastX) << 4;
        final int offsetY = (ghoul.y - lastY) << 4;
        lastX = ghoul.x;
        lastY = ghoul.y;

        if (gameState.isWeaponing()) {
            return;
        }

        if (ghoul.y < botState.getPlayerY() - 16 && ghoul.y >= botState.getPlayerY() - 56) {
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else if (botState.isCanJump() && ghoul.distanceX < 24 && ghoul.distanceY < 8) {
            playerController.jump(botState);
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                if (usedHolyWater) {
                    playerController.whip(gameState);
                } else {
                    usedHolyWater = playerController.grind(gameState, botState);
                }
            }
        } else if (botState.getTargetedObject().getTarget().distanceX >= 48) {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget());
        }
    }
}