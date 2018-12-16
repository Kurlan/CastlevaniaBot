package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;

public class FireColumnStrategy implements Strategy {

    private int done;

    private final CastlevaniaBot b;
    private final BotState botState;

    public FireColumnStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
    }

    @Override
    public void init() {
        done = 0;
    }

    @Override
    public void step() {

        if (done > 0) {
            --done;
            return;
        }
        if (b.weaponing) {
            return;
        }

        final int targetX = b.getTargetedObject().getTarget().x + ((botState.getPlayerX() < b.getTargetedObject().getTarget().x) ? -32 : 32);
        if (botState.getPlayerX() == targetX) {
            if (b.getTargetedObject().getTarget().playerFacing) {
                b.whip();
                done = 64;
            } else {
                b.substage.moveAwayFromTarget(b.getTargetedObject().getTarget()); // walk past and turn around
            }
        } else {
            b.substage.route(targetX, b.getTargetedObject().getTarget().y);
        }
    }
}