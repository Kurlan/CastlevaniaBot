package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import java.util.concurrent.ThreadLocalRandom;

public class MedusaHeadsPitsStrategy implements Strategy {

    private static enum State {
        WALK_TO_EDGE,
        RUN_FOR_IT,
        WAIT_FOR_HEAD,
        DONE,
    }

    private State state;
    private boolean damageBoost;
    private int delay;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public MedusaHeadsPitsStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        state = State.WALK_TO_EDGE;
        damageBoost = ThreadLocalRandom.current().nextBoolean();
        delay = 48;
    }

    @Override
    public void step() {
        switch (state) {
            case WALK_TO_EDGE:
                if (b.currentTile.getX() != 7 || b.currentTile.getY() != 12) {
                    gameState.getCurrentSubstage().route(127, 192);
                } else if (botState.getPlayerX() < 131) {
                    playerController.goRight(botState);
                } else {
                    b.goRightAndJump();
                    state = State.RUN_FOR_IT;
                }
                break;
            case RUN_FOR_IT:
                if (damageBoost && botState.getPlayerX() == 232) {
                    state = State.WAIT_FOR_HEAD;
                } else if (botState.getPlayerX() == 328 && botState.getPlayerY() == 176) {
                    state = State.DONE;
                } else {
                    gameState.getCurrentSubstage().route(328, 176);
                }
                break;
            case WAIT_FOR_HEAD:
                if (--delay == 0) {
                    b.jump();
                    state = State.DONE;
                }
                break;
        }
    }
}