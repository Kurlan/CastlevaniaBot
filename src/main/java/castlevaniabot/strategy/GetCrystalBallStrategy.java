package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;

public class GetCrystalBallStrategy extends GetItemStrategy {

    private int jumpCounter;
    private int jumps;
    private boolean jumpRequested;

    public GetCrystalBallStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        super(b, botState, gameState, playerController);
    }

    @Override
    public void init() {
        super.init();
        jumpRequested = false;
        jumpCounter = 0;
        if (botState.getHearts() == 0 || botState.getWeapon() == NONE || botState.getWeapon() == STOPWATCH) {
            jumps = 2;
        } else if (botState.getHearts() == 1 || botState.getShot() == 1) {
            jumps = 1;
        } else {
            jumps = 0;
        }
    }

    @Override
    public void step() {

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                playerController.useWeapon(gameState);
                ++jumps;
            }
        } else if (jumpRequested) {
            if (botState.isCanJump()) {
                if (jumps == 0 && b.getTargetedObject().getTarget().playerFacing) {
                    gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
                } else if (jumps == 1 && !b.getTargetedObject().getTarget().playerFacing) {
                    gameState.getCurrentSubstage().moveTowardTarget(b.getTargetedObject().getTarget());
                } else {
                    jumpRequested = false;
                    jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
                    playerController.jump(botState);
                }
            }
        } else if (b.getTargetedObject().getTarget().distanceX < 18) {
            if (jumps < 2) {
                jumpRequested = true;
            } else {
                gameState.getCurrentSubstage().crystalBallAlmostAquired();
                super.step();
            }
        } else {
            super.step();
        }
    }
}