package castlevaniabot.strategy;

import castlevaniabot.BotState;
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

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public MedusaHeadsPitsStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
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
                if (botState.getCurrentTile().getX() != 7 || botState.getCurrentTile().getY() != 12) {
                    gameState.getCurrentSubstage().route(127, 192, botState, gameState);
                } else if (botState.getPlayerX() < 131) {
                    playerController.goRight(botState);
                } else {
                    playerController.goRightAndJump(botState);
                    state = State.RUN_FOR_IT;
                }
                break;
            case RUN_FOR_IT:
                if (damageBoost && botState.getPlayerX() == 232) {
                    state = State.WAIT_FOR_HEAD;
                } else if (botState.getPlayerX() == 328 && botState.getPlayerY() == 176) {
                    state = State.DONE;
                } else {
                    gameState.getCurrentSubstage().route(328, 176, botState, gameState);
                }
                break;
            case WAIT_FOR_HEAD:
                if (--delay == 0) {
                    playerController.jump(botState);
                    state = State.DONE;
                }
                break;
        }
    }
}