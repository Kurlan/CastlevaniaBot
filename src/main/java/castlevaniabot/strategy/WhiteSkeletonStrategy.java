package castlevaniabot.strategy;

import castlevaniabot.BotState;
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

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public WhiteSkeletonStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
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

        final GameObject skeleton = botState.getTargetedObject().getTarget();
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
                playerController.useWeapon(gameState);
            }
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState)) {
                if (holyWaterDelay > 0) {
                    playerController.whip(gameState);
                } else if (playerController.grind(gameState, botState)) {
                    holyWaterDelay = HOLY_WATER_RESET;
                }
            }
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY + 32, botState)) {
            if (playerController.faceTarget(botState, gameState) && botState.isCanJump()) {
                playerController.jump(botState);
            }
        } else if (drawingTowardHolyWater) {
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget(), botState, gameState);
        } else if (!botState.isOnStairs() && holyWaterDelay == 0 && botState.getWeapon() == HOLY_WATER
                && botState.getHearts() > 0 && skeleton.distanceX < 96
                && skeleton.distanceY <= 36) {
            if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState) && botState.isCanJump()) {
                if (playerController.isUnderLedge(botState, gameState)) {
                    holyWaterDelay = HOLY_WATER_RESET;
                    drawingTowardHolyWater = true;
                    playerController.useWeapon(gameState);
                } else {
                    jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
                    playerController.jump(botState);
                }
            }
        } else {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget(), botState, gameState);
        }
    }
}