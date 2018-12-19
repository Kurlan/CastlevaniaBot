package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class FishmanStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private boolean usedHolyWater;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public FishmanStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
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

        final GameObject fishman = b.getTargetedObject().getTarget();
        final int offsetX = (fishman.x - lastX) << 4;
        final int offsetY = (fishman.y - lastY) << 4;
        lastX = fishman.x;
        lastY = fishman.y;

        if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState, b.getTargetedObject())) {
                if (usedHolyWater) {
                    playerController.whip(gameState);
                } else {
                    usedHolyWater = playerController.grind(gameState, botState);
                }
            }
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
            if (playerController.faceTarget(botState, gameState, b.getTargetedObject()) && b.canJump) {
                playerController.jump(botState);
            }
        } else if (fishman.distanceX < 24) {
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        }
    }
}