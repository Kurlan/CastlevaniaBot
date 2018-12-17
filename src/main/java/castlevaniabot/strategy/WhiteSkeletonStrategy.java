package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;

public class WhiteSkeletonStrategy implements Strategy {

    private static final int HOLY_WATER_RESET = 240;

    private int jumpCounter;
    private int lastX;
    private int lastY;
    private int holyWaterDelay;
    private boolean drawingTowardHolyWater;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public WhiteSkeletonStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        jumpCounter = holyWaterDelay = 0;
        drawingTowardHolyWater = false;
    }

    @Override
    public void step() {

        final GameObject skeleton = b.getTargetedObject().getTarget();
        final int offsetX = (skeleton.x - lastX) << 4;
        final int offsetY = (skeleton.y - lastY) << 4;
        lastX = skeleton.x;
        lastY = skeleton.y;

        if (skeleton.distanceX > 112) {
            holyWaterDelay = 0;
            drawingTowardHolyWater = false;
        }
        if (holyWaterDelay > 0 && --holyWaterDelay == 0) {
            drawingTowardHolyWater = false;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                holyWaterDelay = HOLY_WATER_RESET;
                drawingTowardHolyWater = true;
                b.useWeapon();
            }
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (!gameState.isWeaponing() && b.faceTarget()) {
                if (holyWaterDelay > 0) {
                    b.whip();
                } else if (b.grind()) {
                    holyWaterDelay = HOLY_WATER_RESET;
                }
            }
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
            if (b.faceTarget() && b.canJump) {
                playerController.jump(botState);
            }
        } else if (drawingTowardHolyWater) {
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else if (!botState.isOnStairs() && holyWaterDelay == 0 && b.weapon == HOLY_WATER
                && b.hearts > 0 && skeleton.distanceX < 96
                && skeleton.distanceY <= 36) {
            if (!gameState.isWeaponing() && b.faceTarget() && b.canJump) {
                if (b.isUnderLedge()) {
                    holyWaterDelay = HOLY_WATER_RESET;
                    drawingTowardHolyWater = true;
                    b.useWeapon();
                } else {
                    jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
                    playerController.jump(botState);
                }
            }
        } else {
            gameState.getCurrentSubstage().moveTowardTarget(b.getTargetedObject().getTarget());
        }
    }
}