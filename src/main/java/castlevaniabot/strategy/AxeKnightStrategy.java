package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;

public class AxeKnightStrategy implements Strategy {

    private static final int HOLY_WATER_RESET = 300;

    private int jumpCounter;
    private int weaponDelay;
    private int lastX;
    private int lastY;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public AxeKnightStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {
        jumpCounter = weaponDelay = 0;
    }

    @Override
    public void step() {

        final GameObject knight = b.getTargetedObject().getTarget();
        final int offsetX = (knight.x - lastX) << 4;
        final int offsetY = (knight.y - lastY) << 4;
        lastX = knight.x;
        lastY = knight.y;

        if (weaponDelay > 0) {
            --weaponDelay;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                weaponDelay = HOLY_WATER_RESET;
                b.useWeapon();
            }
        } else if (weaponDelay == 0 && b.getTargetedObject().getTarget().distanceX < 64
                && b.weapon == HOLY_WATER && b.hearts > 0) {
            if (!gameState.isWeaponing() && b.faceTarget() && b.canJump) {
                if (b.isUnderLedge()) {
                    weaponDelay = HOLY_WATER_RESET;
                    b.useWeapon();
                } else {
                    jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
                    if (b.getTargetedObject().getTarget().x < botState.getPlayerX()) {
                        b.goLeftAndJump();
                    } else {
                        b.goRightAndJump();
                    }
                }
            }
        } else if (gameState.getCurrentSubstage() == b.SUBSTAGE_1501 && b.weapon == BOOMERANG
                && b.hearts > 0) {
            if (!gameState.isWeaponing() && weaponDelay == 0 && b.faceTarget()) {
                if (b.shot > 1) {
                    weaponDelay = 90;
                }
                b.useWeapon();
            }
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (!gameState.isWeaponing() && b.faceTarget()) {
                b.whip();
            }
        } else if (b.getTargetedObject().getTarget().distanceX > 64) {
            gameState.getCurrentSubstage().moveTowardTarget(b.getTargetedObject().getTarget());
        }
    }
}