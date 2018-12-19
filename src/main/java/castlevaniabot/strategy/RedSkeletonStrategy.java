package castlevaniabot.strategy;

import java.util.concurrent.ThreadLocalRandom;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.creativeelements.RedBones;
import castlevaniabot.model.gameelements.GameObject;

import static java.lang.Math.abs;

public class RedSkeletonStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private int moveAway;
    private boolean usedHolyWater;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public RedSkeletonStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
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

        final GameObject skeleton = b.getTargetedObject().getTarget();
        final int offsetX = (skeleton.x - lastX) << 4;
        final int offsetY = (skeleton.y - lastY) << 4;
        lastX = skeleton.x;
        lastY = skeleton.y;

        if (isNotCloseToRedBones()) {
            if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
                if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState, b.getTargetedObject())) {
                    if (usedHolyWater) {
                        playerController.whip(gameState);
                    } else {
                        usedHolyWater = playerController.grind(gameState, botState);
                    }
                    return;
                }
            } else if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
                if (playerController.faceTarget(botState, gameState, b.getTargetedObject()) && b.canJump) {
                    playerController.jump(botState);
                    return;
                }
            }
        }

        if (moveAway > 0) {
            --moveAway;
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else if (skeleton.distanceX < 32) {
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
            moveAway = 17 + ThreadLocalRandom.current().nextInt(17);
        }
    }

    // Do not whip a red skeleton when it's standing near a pile of red bones;
    // otherwise, it might end up in an infinite loop alternating between them.
    private boolean isNotCloseToRedBones() {
        final RedBones[] redBones0 = b.redBones0;
        for (int i = b.redBonesCount0 - 1; i >= 0; --i) {
            final RedBones redBones = redBones0[i];
            if (abs(b.getTargetedObject().getTarget().x - redBones.x) < 64
                    && abs(b.getTargetedObject().getTarget().y - redBones.y) <= 4) {
                return false;
            }
        }
        return true;
    }
}