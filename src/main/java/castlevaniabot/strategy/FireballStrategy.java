package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

public class FireballStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final CastlevaniaBot b;
    private final BotState botState;

    public FireballStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
    }

    @Override
    public void init() {
    }

    @Override
    public void step() {

        final GameObject fireball = b.getTargetedObject().getTarget();
        final int offsetX = (fireball.x - lastX) << 4;
        final int offsetY = (fireball.y - lastY) << 4;
        lastX = fireball.x;
        lastY = fireball.y;

        if (fireball.distanceX < 24) {
            final boolean flyingHigh = fireball.y < botState.getPlayerY() - 16;
            if (!b.atTopOfStairs && flyingHigh) {
                b.kneel();                          // duck under fireball
            } else if (!flyingHigh && b.canJump) {
                b.jump();                           // jump over fireball
            }
        } else if (b.atTopOfStairs) {
            b.substage.moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget() && !b.weaponing) {
                b.whip();                           // stand whip fireball
            }
        } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget()) {
                b.kneel();
                if (b.kneeling && !b.weaponing) {
                    b.whip();                         // kneel whip fireball
                }
            }
        }
    }
}