package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.creativeelements.Bone;

public class BoneStrategy implements Strategy {

    private Bone bone;

    private final CastlevaniaBot b;
    private final BotState botState;

    public BoneStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
    }

    public void init(final Bone bone) {
        this.bone = bone;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {
        if (!b.onStairs) {
            if (bone.left) {
                b.substage.routeLeft();
            } else {
                b.substage.routeRight();
            }
        }
    }
}