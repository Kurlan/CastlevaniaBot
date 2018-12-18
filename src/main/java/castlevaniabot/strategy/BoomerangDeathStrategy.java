package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class BoomerangDeathStrategy implements Strategy {

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public BoomerangDeathStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState,
                                  final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject death = b.getTargetedObject().getTarget();

        if (botState.getHearts() > 0 && death.y2 >= botState.getPlayerY() - 32 && death.y1 <= botState.getPlayerY()
                && death.distanceX < 128) {
            if (b.faceTarget()) {
                playerController.useWeapon(gameState);
            }
        } else if (b.isTargetInStandingWhipRange()) {
            if (b.faceTarget()) {
                playerController.whip(gameState);
            }
        } else if (death.distanceX < 32) {
            moveAwayFrom(death);
        }
    }

    private void moveAwayFrom(final GameObject death) {
        switch (b.currentTile.getX()) {
            case 0:
            case 1:
            case 12:
                playerController.goRight(botState);
                break;
            case 2:
            case 13:
                gameState.getCurrentSubstage().route(128, 160, false);
                break;
            case 4:
                if (death.y < botState.getPlayerY() - 16) {
                    playerController.goLeft(botState);
                } else {
                    gameState.getCurrentSubstage().route(9, 128, false);
                }
                break;
            case 11:
                if (death.y < botState.getPlayerY() - 16) {
                    playerController.goRight(botState);
                } else {
                    gameState.getCurrentSubstage().route(238, 128, false);
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