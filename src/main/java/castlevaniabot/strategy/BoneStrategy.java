package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.creativeelements.Bone;

public class BoneStrategy implements Strategy {

    private Bone bone;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public BoneStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    public void init(final Bone bone) {
        this.bone = bone;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {
        if (!botState.isOnStairs()) {
            if (bone.left) {
                gameState.getCurrentSubstage().routeLeft();
            } else {
                gameState.getCurrentSubstage().routeRight();
            }
        }
    }
}