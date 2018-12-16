package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

public class SnakeStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final CastlevaniaBot b;
    private final BotState botState;

    public SnakeStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject snake = b.getTargetedObject().getTarget();
        final int offsetX = (snake.x - lastX) << 4;
        final int offsetY = (snake.y - lastY) << 4;
        lastX = snake.x;
        lastY = snake.y;

        if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget()) {
                b.kneel();
                if (b.kneeling && !b.weaponing) {
                    b.whip();
                }
            }
        }
    }
}