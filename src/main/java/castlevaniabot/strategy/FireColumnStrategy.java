package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;

public class FireColumnStrategy implements Strategy {

    private int done;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public FireColumnStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
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
        if (gameState.isWeaponing()) {
            return;
        }

        final int targetX = b.getTargetedObject().getTarget().x + ((botState.getPlayerX() < b.getTargetedObject().getTarget().x) ? -32 : 32);
        if (botState.getPlayerX() == targetX) {
            if (b.getTargetedObject().getTarget().playerFacing) {
                b.whip();
                done = 64;
            } else {
                gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget()); // walk past and turn around
            }
        } else {
            gameState.getCurrentSubstage().route(targetX, b.getTargetedObject().getTarget().y);
        }
    }
}