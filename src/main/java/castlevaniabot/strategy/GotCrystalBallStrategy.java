package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import java.util.concurrent.ThreadLocalRandom;

public class GotCrystalBallStrategy implements Strategy {

    private int jumpDelay;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public GotCrystalBallStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        jumpDelay = 0;
    }

    @Override
    public void step() {
        if (jumpDelay > 0) {
            if (--jumpDelay == 0) {
                playerController.whip(gameState);
            }
        } else if (b.canJump) {
            playerController.jump(botState);
            jumpDelay = 16 + ThreadLocalRandom.current().nextInt(11);
        }
    }
}