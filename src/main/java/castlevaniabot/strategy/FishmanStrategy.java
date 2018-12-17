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
            if (!gameState.isWeaponing() && b.faceTarget()) {
                if (usedHolyWater) {
                    playerController.whip(gameState);
                } else {
                    usedHolyWater = b.grind();
                }
            }
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
            if (b.faceTarget() && b.canJump) {
                playerController.jump(botState);
            }
        } else if (fishman.distanceX < 24) {
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        }
    }
}