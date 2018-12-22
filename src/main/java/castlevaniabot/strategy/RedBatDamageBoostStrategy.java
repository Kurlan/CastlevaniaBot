package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.RED_BAT;

public class RedBatDamageBoostStrategy implements Strategy {

    private boolean batSpawned;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;
    private final RedBatStrategy redBatStrategy;

    public RedBatDamageBoostStrategy(final BotState botState, final GameState gameState, final PlayerController playerController, final RedBatStrategy redBatStrategy) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
        this.redBatStrategy = redBatStrategy;
    }

    @Override
    public void init() {
        batSpawned = false;
    }

    @Override
    public void step() {

        if (batSpawned) {
            if (botState.isPlayerLeft()) {
                playerController.goRightAndJump(botState);
                botState.getDamageBoostSublevel().redBatDamageBoostDone();
            } else {
                playerController.goLeft(botState);
            }
        } else {
            final GameObject bat = gameState.getType(RED_BAT);
            if (botState.getPlayerX() != 195 || botState.getPlayerY() != 144 || botState.isPlayerLeft()) {
                if (bat != null) {
                    botState.getTargetedObject().setTarget(bat);
                    redBatStrategy.step();
                } else if (botState.getPlayerY() != 144 || botState.getPlayerX() < 191) {
                    gameState.getCurrentSubstage().route(191, 144);
                } else if (botState.getPlayerX() < 195) {
                    playerController.goRight(botState);
                }
            } else if (bat != null) {
                if (bat.left && bat.x >= botState.getPlayerX() - 16) {
                    if (bat.x < 272) {
                        batSpawned = true;
                        playerController.goLeft(botState);
                    }
                } else {
                    botState.getTargetedObject().setTarget(bat);
                    redBatStrategy.step();
                }
            }
        }
    }
}