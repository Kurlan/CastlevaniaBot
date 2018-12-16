package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.creativeelements.MedusaHead;
import castlevaniabot.model.gameelements.GameObject;

// No longer used!
public class MedusaHeadStrategy implements Strategy {

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public MedusaHeadStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject head = b.getTargetedObject().getTarget();
        final MedusaHead medusaHead = b.getMedusaHead(head);
        if (medusaHead == null) {
            return;
        }

        int t = medusaHead.t + 16;
        int d = 0;
        if (t >= MedusaHead.WAVE.length) {
            t -= MedusaHead.WAVE.length;
            d = 126;
        }
        final int dx = MedusaHead.WAVE[t][0] + d;
        final int headX = medusaHead.left ? (medusaHead.x0 - dx)
                : (medusaHead.x0 + dx);
        final int headY = medusaHead.y0 + MedusaHead.WAVE[t][1];
        final int offsetX = headX - head.x;
        final int offsetY = headY - head.y;

        if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget() && !b.weaponing) {
                b.whip();
            }
        } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget()) {
                b.kneel();
                if (b.kneeling && !b.weaponing) {
                    b.whip();
                }
            }
        }
    }
}