package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.creativeelements.RedBones;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

public class RedSkeletonStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private int moveAway;
    private boolean usedHolyWater;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public RedSkeletonStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        usedHolyWater = false;
        moveAway = 0;
    }

    @Override
    public void step() {

        final GameObject skeleton = botState.getTargetedObject().getTarget();
        final int offsetX = (skeleton.x - lastX) << 4;
        final int offsetY = (skeleton.y - lastY) << 4;
        lastX = skeleton.x;
        lastY = skeleton.y;

        if (isNotCloseToRedBones()) {
            if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
                if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState)) {
                    if (usedHolyWater) {
                        playerController.whip(gameState);
                    } else {
                        usedHolyWater = playerController.grind(gameState, botState);
                    }
                    return;
                }
            } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY + 32, botState)) {
                if (playerController.faceTarget(botState, gameState) && botState.isCanJump()) {
                    playerController.jump(botState);
                    return;
                }
            }
        }

        if (moveAway > 0) {
            --moveAway;
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget(), botState, gameState);
        } else if (skeleton.distanceX < 32) {
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget(), botState, gameState);
            moveAway = 17 + ThreadLocalRandom.current().nextInt(17);
        }
    }

    // Do not whip a red skeleton when it's standing near a pile of red bones;
    // otherwise, it might end up in an infinite loop alternating between them.
    private boolean isNotCloseToRedBones() {
        final RedBones[] redBones0 = gameState.getRedBones0();
        for (int i = gameState.getBoneCount0() - 1; i >= 0; --i) {
            final RedBones redBones = redBones0[i];
            if (abs(botState.getTargetedObject().getTarget().x - redBones.x) < 64
                    && abs(botState.getTargetedObject().getTarget().y - redBones.y) <= 4) {
                return false;
            }
        }
        return true;
    }
}