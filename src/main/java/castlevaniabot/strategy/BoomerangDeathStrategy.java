package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class BoomerangDeathStrategy implements Strategy {

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public BoomerangDeathStrategy(final BotState botState, final GameState gameState,
                                  final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject death = botState.getTargetedObject().getTarget();

        if (botState.getHearts() > 0 && death.y2 >= botState.getPlayerY() - 32 && death.y1 <= botState.getPlayerY()
                && death.distanceX < 128) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.useWeapon(gameState);
            }
        } else if (playerController.isTargetInStandingWhipRange(botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.whip(gameState);
            }
        } else if (death.distanceX < 32) {
            moveAwayFrom(death);
        }
    }

    private void moveAwayFrom(final GameObject death) {
        switch (botState.getCurrentTile().getX()) {
            case 0:
            case 1:
            case 12:
                playerController.goRight(botState);
                break;
            case 2:
            case 13:
                gameState.getCurrentSubstage().route(128, 160, false, botState, gameState);
                break;
            case 4:
                if (death.y < botState.getPlayerY() - 16) {
                    playerController.goLeft(botState);
                } else {
                    gameState.getCurrentSubstage().route(9, 128, false, botState, gameState);
                }
                break;
            case 11:
                if (death.y < botState.getPlayerY() - 16) {
                    playerController.goRight(botState);
                } else {
                    gameState.getCurrentSubstage().route(238, 128, false, botState, gameState);
                }
                break;
            case 3:
            case 14:
            case 15:
                playerController.goLeft(botState);
                break;
            default:
                if (death.x < botState.getPlayerX()) {
                    playerController.goRight(botState);
                } else {
                    playerController.goLeft(botState);
                }
                break;
        }
    }
}