package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;

public class BoneTowerStrategy implements Strategy {

    private int moveAway;
    private boolean usedHolyWater;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public BoneTowerStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState =gameState;
    }

    @Override
    public void init() {
        usedHolyWater = false;
    }

    @Override
    public void step() {

        final GameObject tower = b.getTargetedObject().getTarget();

        if (gameState.isWeaponing()) {
            return;
        }

        if (moveAway > 0) {
            --moveAway;
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else if (b.isTargetInStandingWhipRange()) {
            if (b.faceTarget()) {
                if (usedHolyWater || b.weapon != HOLY_WATER || b.hearts == 0
                        || tower.distanceX > 48) {
                    b.whip();
                } else {
                    usedHolyWater = true;
                    b.whipOrWeapon();
                }
            }
        } else if (tower.distanceX < 24) {
            moveAway = 30 + ThreadLocalRandom.current().nextInt(11);
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else {
            gameState.getCurrentSubstage().moveTowardTarget(b.getTargetedObject().getTarget());
        }
    }
}