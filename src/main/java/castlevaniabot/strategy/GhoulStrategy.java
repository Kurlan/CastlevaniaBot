package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.gameelements.GameObject;

public class GhoulStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private boolean usedHolyWater;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public GhoulStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {
        usedHolyWater = false;
    }

    @Override
    public void step() {

        final GameObject ghoul = b.getTargetedObject().getTarget();
        final int offsetX = (ghoul.x - lastX) << 4;
        final int offsetY = (ghoul.y - lastY) << 4;
        lastX = ghoul.x;
        lastY = ghoul.y;

        if (gameState.isWeaponing()) {
            return;
        }

        if (ghoul.y < botState.getPlayerY() - 16 && ghoul.y >= botState.getPlayerY() - 56) {
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else if (b.canJump && ghoul.distanceX < 24 && ghoul.distanceY < 8) {
            b.jump();
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget()) {
                if (usedHolyWater) {
                    b.whip();
                } else {
                    usedHolyWater = b.grind();
                }
            }
        } else if (b.getTargetedObject().getTarget().distanceX >= 48) {
            gameState.getCurrentSubstage().moveTowardTarget(b.getTargetedObject().getTarget());
        }
    }
}