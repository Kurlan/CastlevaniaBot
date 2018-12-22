package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;

public class GetItemStrategy implements Strategy {

    private int error;

    protected final BotState botState;
    protected final GameState gameState;
    protected final PlayerController playerController;

    public GetItemStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        error = (botState.getTargetedObject().getTarget().type == DESTINATION) ? 0
                : (ThreadLocalRandom.current().nextInt(7) - 3);
    }

    @Override
    public void step() {

        final int y = botState.getTargetedObject().getTarget().platformY << 4;
        int x = botState.getTargetedObject().getTarget().supportX;
        if (botState.getTargetedObject().getTarget().y <= y) {
            final int t = (x & 0xF) + error;
            if ((t & 0xF) == t) {
                x += error;
            }
        }

        if (botState.getTargetedObject().getTarget().type != DESTINATION && botState.getPlayerX() == x && botState.getPlayerY() == y) {
            if (botState.getTargetedObject().getTarget().y > y) {
                playerController.kneel();
            } else if (botState.isCanJump() && botState.getTargetedObject().getTarget().y < botState.getPlayerY() - 32) {
                playerController.jump(botState);
            }
        } else {
            gameState.getCurrentSubstage().route(x, y);
        }
    }
}