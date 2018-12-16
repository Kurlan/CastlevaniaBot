package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.gameelements.GameObject;

public class PantherStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public PantherStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject panther = b.getTargetedObject().getTarget();
        final int offsetX = (panther.x - lastX) << 4;
        final int offsetY = (panther.y - lastY) << 4;
        lastX = panther.x;
        lastY = panther.y;

        if (!b.weaponing && b.faceTarget()
                && b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            b.whip();
        }
    }
}