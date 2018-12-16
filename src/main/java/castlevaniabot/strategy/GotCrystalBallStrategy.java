package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;

import java.util.concurrent.ThreadLocalRandom;

public class GotCrystalBallStrategy implements Strategy {

    private int jumpDelay;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public GotCrystalBallStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
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