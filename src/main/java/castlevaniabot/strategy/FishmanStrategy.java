package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

public class FishmanStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private boolean usedHolyWater;

    private final CastlevaniaBot b;
    private final BotState botState;

    public FishmanStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
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
            if (!b.weaponing && b.faceTarget()) {
                if (usedHolyWater) {
                    b.whip();
                } else {
                    usedHolyWater = b.grind();
                }
            }
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
            if (b.faceTarget() && b.canJump) {
                b.jump();
            }
        } else if (fishman.distanceX < 24) {
            b.substage.moveAwayFromTarget(b.getTargetedObject().getTarget());
        }
    }
}