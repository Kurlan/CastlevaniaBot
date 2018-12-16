package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

public class BlackBatStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final CastlevaniaBot b;
    private final BotState botState;

    public BlackBatStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
    }

    @Override
    public void init() {
    }

    @Override
    public void step() {

        final GameObject bat = b.getTargetedObject().getTarget();
        final int offsetX = (bat.x - lastX) << 4;
        final int offsetY = (bat.y - lastY) << 4;
        lastX = bat.x;
        lastY = bat.y;

        if (bat.distanceX < 24) {
            final boolean flyingHigh = bat.y + offsetY < botState.getPlayerY() - 16;
            if (!b.atTopOfStairs && flyingHigh) {
                b.kneel();                          // duck under bat
            } else if (!flyingHigh && b.canJump) {
                b.jump();                           // jump over bat
            }
        } else if (b.atTopOfStairs) {
            b.substage.moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget() && !b.weaponing) {
                b.whip();                         // stand whip bat
            }
        } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget()) {
                b.kneel();
                if (b.kneeling && !b.weaponing) {
                    b.whip();                         // kneel whip bat
                }
            }
        } else if (bat.distanceX - offsetX < 24 && offsetY != 0) {
            b.substage.moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else {
            b.substage.moveTowardTarget(b.getTargetedObject().getTarget());
        }
    }
}