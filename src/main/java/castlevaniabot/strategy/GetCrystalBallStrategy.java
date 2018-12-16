package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;

public class GetCrystalBallStrategy extends GetItemStrategy {

    private int jumpCounter;
    private int jumps;
    private boolean jumpRequested;

    private final CastlevaniaBot b;
    private final BotState botState;

    public GetCrystalBallStrategy(final CastlevaniaBot b, final BotState botState) {
        super(b, botState);
        this.b = b;
        this.botState = botState;
    }

    @Override
    public void init() {
        super.init();
        jumpRequested = false;
        jumpCounter = 0;
        if (b.hearts == 0 || b.weapon == NONE || b.weapon == STOPWATCH) {
            jumps = 2;
        } else if (b.hearts == 1 || b.shot == 1) {
            jumps = 1;
        } else {
            jumps = 0;
        }
    }

    @Override
    public void step() {

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                b.useWeapon();
                ++jumps;
            }
        } else if (jumpRequested) {
            if (b.canJump) {
                if (jumps == 0 && b.getTargetedObject().getTarget().playerFacing) {
                    b.substage.moveAwayFromTarget(b.getTargetedObject().getTarget());
                } else if (jumps == 1 && !b.getTargetedObject().getTarget().playerFacing) {
                    b.substage.moveTowardTarget(b.getTargetedObject().getTarget());
                } else {
                    jumpRequested = false;
                    jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
                    b.jump();
                }
            }
        } else if (b.getTargetedObject().getTarget().distanceX < 18) {
            if (jumps < 2) {
                jumpRequested = true;
            } else {
                b.substage.crystalBallAlmostAquired();
                super.step();
            }
        } else {
            super.step();
        }
    }
}