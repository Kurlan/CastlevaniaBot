package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;

public class GetItemStrategy implements Strategy {

    private int error;

    private final CastlevaniaBot b;
    private final BotState botState;

    public GetItemStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
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
                b.kneel();
            } else if (b.canJump && b.getTargetedObject().getTarget().y < botState.getPlayerY() - 32) {
                b.jump();
            }
        } else {
            b.substage.route(x, y);
        }
    }
}