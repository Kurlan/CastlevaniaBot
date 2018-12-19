package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class SpearKnightStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private boolean usedHolyWater;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public SpearKnightStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
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

        final GameObject knight = botState.getTargetedObject().getTarget();
        final int offsetX = (knight.x - lastX) << 4;
        final int offsetY = (knight.y - lastY) << 4;
        lastX = knight.x;
        lastY = knight.y;

        if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState)) {
                useWeapon();
            }
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY + 32, botState)) {
            if (playerController.faceTarget(botState, gameState) && botState.isCanJump()) {
                playerController.jump(botState);
            }
        } else if (knight.distanceX < 24) {
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else if (knight.distanceX > 32) {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget());
        }
    }

    private void useWeapon() {
        if (!usedHolyWater) {
            usedHolyWater = playerController.grind(gameState, botState);
        } else {
            playerController.whip(gameState);
        }
    }
}