package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.DEATH;

public class SickleStrategy implements Strategy {

    private final CastlevaniaBot b;
    private final BotState botState;

    public SickleStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject death = b.getType(DEATH);
        if (death != null && !b.weaponing) {
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
            if (b.faceTarget() && !b.weaponing) {
                b.whip();
            }
        } else if (b.isTargetInKneelingWhipRange()) {
            if (b.faceTarget()) {
                b.kneel();
                if (b.kneeling && !b.weaponing) {
                    b.whip();
                }
            }
        } else if (!b.weaponing && b.hearts > 0 && sickle.y2 >= botState.getPlayerY() - 32
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
                b.pressRight();
                break;
            case 2:
            case 13:
                b.substage.route(128, 160, false);
                break;
            case 4:
                b.substage.route(9, 128, false);
                break;
            case 11:
                b.substage.route(238, 128, false);
                break;
            case 3:
            case 14:
            case 15:
                b.pressLeft();
                break;
            default:
                if (sickle.x < botState.getPlayerX()) {
                    b.pressRight();
                } else {
                    b.pressLeft();
                }
                break;
        }
    }
}