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

        final GameObject death = b.getType(DEATH);
        if (death != null && !gameState.isWeaponing()) {
            if (b.hearts > 0 && death.y2 >= botState.getPlayerY() - 32 && death.y1 <= botState.getPlayerY()
                    && death.distanceX < 128) {
                if (b.face(death)) {
                    b.useWeapon();
                }
                return;
            } else if (b.isInStandingWhipRange(death)) {
                if (b.face(death)) {
                    b.whip();
                }
                return;
            }
        }

        final GameObject sickle = b.getTargetedObject().getTarget();

        if (sickle.distanceX < 32
                && (sickle.y2 <= botState.getPlayerY() - 32 || sickle.y1 >= botState.getPlayerY())) {
            moveAwayFrom(sickle);
        } else if (b.isTargetInStandingWhipRange()) {
            if (b.faceTarget() && !gameState.isWeaponing()) {
                b.whip();
            }
        } else if (b.isTargetInKneelingWhipRange()) {
            if (b.faceTarget()) {
                b.kneel();
                if (b.kneeling && !gameState.isWeaponing()) {
                    b.whip();
                }
            }
        } else if (!gameState.isWeaponing() && b.hearts > 0 && sickle.y2 >= botState.getPlayerY() - 32
                && sickle.y1 <= botState.getPlayerY()) {
            if (b.faceTarget()) {
                b.useWeapon();
            }
        }
    }

    private void moveAwayFrom(final GameObject sickle) {
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