package castlevaniabot.operation;

import castlevaniabot.BotState;
import castlevaniabot.GameState;

public class GameStateRestarter {
    public void restartSubstage(GameState gameState, BotState botState) {
        botState.setCurrentStrategy(null);
        gameState.setDraculaHeadTime(0);
        gameState.setSickleCount0(0);
        gameState.setSickleCount1(0);
        gameState.setMedusaHeadsCount0(0);
        gameState.setMedusaHeadsCount1(0);
        gameState.setRedBonesCount0(0);
        gameState.setRedBonesCount1(0);

        gameState.setBoneCount0(0);
        gameState.setBoneCount1(0);
        gameState.setRedBatsCount0(0);
        gameState.setRedBatsCount1(0);
    }

}
