package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.AXE_KNIGHT;

public class DeathHallHolyWaterStrategy implements Strategy {

    private static final int HOLY_WATER_RESET = 180;

    private int jumpCounter;
    private int holyWaterDelay;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public DeathHallHolyWaterStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        jumpCounter = holyWaterDelay = 0;
    }

    @Override
    public void step() {

        if (holyWaterDelay > 0) {
            --holyWaterDelay;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                holyWaterDelay = HOLY_WATER_RESET;
                playerController.useWeapon(gameState);
            }
            return;
        } else if (holyWaterDelay == 0) {
            final GameObject[] objs = gameState.getGameObjects();
            for (int i = gameState.getObjsCount() - 1; i >= 0; --i) {
                final GameObject obj = objs[i];
                if (obj.type == AXE_KNIGHT && obj.x < botState.getPlayerX() && obj.distanceX < 64) {
                    jumpCounter = 9;
                    playerController.goLeftAndJump(botState);
                    return;
                }
            }
        }

        gameState.getCurrentSubstage().route(9, 128, false, botState, gameState);
    }
}