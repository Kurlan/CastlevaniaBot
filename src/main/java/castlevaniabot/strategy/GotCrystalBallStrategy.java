package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.ThreadLocalRandom;

public class GotCrystalBallStrategy implements Strategy {

    private int jumpDelay;

    private final CastlevaniaBot b;
    private final BotState botState;

    public GotCrystalBallStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
    }

    @Override
    public void init() {
        jumpDelay = 0;
    }

    @Override
    public void step() {
        if (jumpDelay > 0) {
            if (--jumpDelay == 0) {
                b.whip();
            }
        } else if (b.canJump) {
            b.jump();
            jumpDelay = 16 + ThreadLocalRandom.current().nextInt(11);
        }
    }
}