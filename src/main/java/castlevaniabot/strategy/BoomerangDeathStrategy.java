package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.gameelements.GameObject;

public class BoomerangDeathStrategy implements Strategy {

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public BoomerangDeathStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject death = b.getTargetedObject().getTarget();

        if (b.hearts > 0 && death.y2 >= botState.getPlayerY() - 32 && death.y1 <= botState.getPlayerY()
                && death.distanceX < 128) {
            if (b.faceTarget()) {
                b.useWeapon();
            }
        } else if (b.isTargetInStandingWhipRange()) {
            if (b.faceTarget()) {
                b.whip();
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
                b.goRight();
                break;
            case 2:
            case 13:
                gameState.getCurrentSubstage().route(128, 160, false);
                break;
            case 4:
                if (death.y < botState.getPlayerY() - 16) {
                    b.goLeft();
                } else {
                    gameState.getCurrentSubstage().route(9, 128, false);
                }
                break;
            case 11:
                if (death.y < botState.getPlayerY() - 16) {
                    b.goRight();
                } else {
                    gameState.getCurrentSubstage().route(238, 128, false);
                }
                break;
            case 3:
            case 14:
            case 15:
                b.goLeft();
                break;
            default:
                if (death.x < botState.getPlayerX()) {
                    b.goRight();
                } else {
                    b.goLeft();
                }
                break;
        }
    }
}