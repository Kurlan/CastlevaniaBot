package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.DEATH;

public class SickleStrategy implements Strategy {

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public SickleStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
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

        final GameObject death = gameState.getType(DEATH);
        if (death != null && !gameState.isWeaponing()) {
            if (botState.getHearts() > 0 && death.y2 >= botState.getPlayerY() - 32 && death.y1 <= botState.getPlayerY()
                    && death.distanceX < 128) {
                if (playerController.face(death, botState)) {
                    playerController.useWeapon(gameState);
                }
                return;
            } else if (playerController.isInStandingWhipRange(death, botState)) {
                if (playerController.face(death, botState)) {
                    playerController.whip(gameState);
                }
                return;
            }
        }

        final GameObject sickle = botState.getTargetedObject().getTarget();

        if (sickle.distanceX < 32
                && (sickle.y2 <= botState.getPlayerY() - 32 || sickle.y1 >= botState.getPlayerY())) {
            moveAwayFrom(sickle);
        } else if (b.isTargetInStandingWhipRange()) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                playerController.whip(gameState);
            }
        } else if (b.isTargetInKneelingWhipRange()) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.kneel();
                if (botState.isKneeling() && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            }
        } else if (!gameState.isWeaponing() && botState.getHearts() > 0 && sickle.y2 >= botState.getPlayerY() - 32
                && sickle.y1 <= botState.getPlayerY()) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.useWeapon(gameState);
            }
        }
    }

    private void moveAwayFrom(final GameObject sickle) {
        switch (botState.getCurrentTile().getX()) {
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
                gameState.getCurrentSubstage().route(9, 128, false);
                break;
            case 11:
                gameState.getCurrentSubstage().route(238, 128, false);
                break;
            case 3:
            case 14:
            case 15:
                playerController.goLeft(botState);
                break;
            default:
                if (sickle.x < botState.getPlayerX()) {
                    playerController.goRight(botState);
                } else {
                    playerController.goLeft(botState);
                }
                break;
        }
    }
}