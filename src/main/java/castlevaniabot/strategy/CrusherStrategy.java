package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import nintaco.api.API;
import nintaco.api.ApiSource;

import static castlevaniabot.model.gameelements.Addresses.CRUSHER;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;

public class CrusherStrategy implements Strategy {

    private static enum State {
        INACTIVE,
        WALK_TO_LEFT_WALL,
        WAIT_FOR_CRUSHER_2,
        WALK_BETWEEN_CRUSHERS_1_AND_2,
        WHIP_CANDLE_1_2,
        WAIT_FOR_CRUSHER_1,
        WALK_BETWEEN_CRUSHERS_0_AND_1,
        WHIP_CANDLE_0_1,
        WAIT_FOR_CRUSHER_0,
        WALK_IN_FRONT_OF_CRUSHER_0,
        WHIP_CANDLE_0,
    }

    private final API api = ApiSource.getAPI();

    private final int[] ys = new int[3];
    private final boolean[] ascendings = new boolean[3];

    private State state;
    private int delay;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public CrusherStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        state = State.INACTIVE;
        delay = 20;
    }

    @Override
    public void step() {

        readCrushers();

        switch (state) {
            case WALK_TO_LEFT_WALL:
                if (botState.getPlayerX() != 521 || botState.getPlayerY() != 208) {
                    gameState.getCurrentSubstage().route(521, 208, botState, gameState);
                } else {
                    state = State.WAIT_FOR_CRUSHER_2;
                }
                break;
            case WAIT_FOR_CRUSHER_2:
                if (ascendings[2] && ys[2] == 4) {
                    playerController.goLeftAndJump(botState);
                    state = State.WALK_BETWEEN_CRUSHERS_1_AND_2;
                }
                break;
            case WALK_BETWEEN_CRUSHERS_1_AND_2:
                if (botState.getPlayerX() == 456 && botState.getPlayerY() == 176) {
                    state = State.WHIP_CANDLE_1_2;
                } else {
                    gameState.getCurrentSubstage().route(456, 176, botState, gameState);
                }
                break;
            case WHIP_CANDLE_1_2:
                if (delay > 0) {
                    if (--delay == 0) {
                        state = State.WAIT_FOR_CRUSHER_1;
                    }
                } else if (botState.isPlayerLeft()) {
                    playerController.goRight(botState);
                } else {
                    playerController.whip(gameState);
                    delay = 48;
                }
                break;
            case WAIT_FOR_CRUSHER_1:
                if (ascendings[1] && ys[1] == 3) {
                    state = State.WALK_BETWEEN_CRUSHERS_0_AND_1;
                }
                break;
            case WALK_BETWEEN_CRUSHERS_0_AND_1:
                if (botState.getPlayerX() == 392 && botState.getPlayerY() == 176) {
                    state = State.WHIP_CANDLE_0_1;
                } else {
                    gameState.getCurrentSubstage().route(392, 176, botState, gameState);
                }
                break;
            case WHIP_CANDLE_0_1:
                if (delay > 0) {
                    if (--delay == 0) {
                        state = State.WAIT_FOR_CRUSHER_0;
                    }
                } else if (botState.isPlayerLeft()) {
                    playerController.goRight(botState);
                } else {
                    playerController.whip(gameState);
                    delay = 48;
                }
                break;
            case WAIT_FOR_CRUSHER_0:
                if (ascendings[0] && ys[0] == 3) {
                    state = State.WALK_IN_FRONT_OF_CRUSHER_0;
                }
                break;
            case WALK_IN_FRONT_OF_CRUSHER_0:
                if (botState.getPlayerX() == 328 && botState.getPlayerY() == 176) {
                    state = State.WHIP_CANDLE_0;
                } else {
                    gameState.getCurrentSubstage().route(328, 176, botState, gameState);
                }
                break;
            case WHIP_CANDLE_0:
                if (delay > 0) {
                    if (--delay == 0) {
                        init();
                    }
                } else if (botState.isPlayerLeft()) {
                    playerController.goRight(botState);
                } else {
                    playerController.whip(gameState);
                    delay = 48;
                }
                break;
        }
    }

    public boolean isActive() {

        if (state == State.INACTIVE && botState.getPlayerX() >= 512 && botState.getPlayerX() <= 671
                && botState.getPlayerY() >= 96) {
            final GameObject[] objs = gameState.getGameObjects();
            outer:
            {
                for (int i = gameState.getObjsCount() - 1; i >= 0; --i) {
                    final GameObject obj = objs[i];
                    if (obj.type != DESTINATION && obj.x2 >= 512 && obj.x1 <= 671
                            && obj.y2 >= 96) {
                        break outer;
                    }
                }
                if (delay > 0) {
                    --delay;
                } else {
                    state = State.WALK_TO_LEFT_WALL;
                }
            }
        }

        return state != State.INACTIVE;
    }

    private void readCrushers() {
        for (int i = 2; i >= 0; --i) {
            final int address = CRUSHER + (i << 3);
            for (int j = 0; j < 8; ++j) {
                if (api.readPPU(address + (j << 5)) != 0) {
                    if (ys[i] < j) {
                        ascendings[i] = false;
                    } else if (ys[i] > j) {
                        ascendings[i] = true;
                    }
                    ys[i] = j;
                    break;
                }
            }
        }
    }
}