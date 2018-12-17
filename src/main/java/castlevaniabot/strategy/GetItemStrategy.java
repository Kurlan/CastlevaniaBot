package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;

public class GetItemStrategy implements Strategy {

    private int error;

    protected final CastlevaniaBot b;
    protected final BotState botState;
    protected final GameState gameState;
    protected final PlayerController playerController;

    public GetItemStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        error = (b.getTargetedObject().getTarget().type == DESTINATION) ? 0
                : (ThreadLocalRandom.current().nextInt(7) - 3);
    }

    @Override
    public void step() {

        final int y = b.getTargetedObject().getTarget().platformY << 4;
        int x = b.getTargetedObject().getTarget().supportX;
        if (b.getTargetedObject().getTarget().y <= y) {
            final int t = (x & 0xF) + error;
            if ((t & 0xF) == t) {
                x += error;
            }
        }

        if (b.getTargetedObject().getTarget().type != DESTINATION && botState.getPlayerX() == x && botState.getPlayerY() == y) {
            if (b.getTargetedObject().getTarget().y > y) {
                playerController.kneel();
            } else if (b.canJump && b.getTargetedObject().getTarget().y < botState.getPlayerY() - 32) {
                playerController.jump(botState);
            }
        } else {
            gameState.getCurrentSubstage().route(x, y);
        }
    }
}