package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.AXE;
import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.DAGGER;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;

public class MedusaStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private int weaponDelay;
    private int moveAway;
    private int routeX;
    private int routeY;
    private int axeScanCounter;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public MedusaStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState =gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        moveAway = 0;
        weaponDelay = 16;
        routeX = 40;
        routeY = 176;
        axeScanCounter = ThreadLocalRandom.current().nextInt(61);
    }

    public boolean isTimeFrozen() {
        return botState.getWeapon() == STOPWATCH && weaponDelay > 0;
    }

    @Override
    public void step() {

        final GameObject medusa = botState.getTargetedObject().getTarget();
        final int offsetX = (medusa.x - lastX) << 4;
        final int offsetY = (medusa.y - lastY) << 4;
        lastX = medusa.x;
        lastY = medusa.y;

        if (weaponDelay > 0) {
            --weaponDelay;
        }

        if (botState.getHearts() >= 5 && botState.getWeapon() == STOPWATCH) {
            stepStopwatchStrategy(medusa, offsetX, offsetY);
        } else if (botState.getHearts() > 0) {
            switch (botState.getWeapon()) {
                case AXE:
                    stepAxeStrategy(medusa, offsetX, offsetY);
                    break;
                case BOOMERANG:
                    stepBoomerangStrategy(medusa, offsetX, offsetY);
                    break;
                case DAGGER:
                    stepDaggerStrategy(medusa, offsetX, offsetY);
                    break;
                case HOLY_WATER:
                    stepHolyWaterStrategy(medusa, offsetX, offsetY);
                    break;
                default:
                    stepNoWeaponsStrategy(medusa, offsetX, offsetY);
                    break;
            }
        } else {
            stepNoWeaponsStrategy(medusa, offsetX, offsetY);
        }
    }

    private void stepNoWeaponsStrategy(final GameObject medusa, final int offsetX,
                                       final int offsetY) {

        if (medusa.distanceX > 64 || botState.getPlayerX() <= 25 || botState.getPlayerX() >= 727) {
            moveAway = 0;
        }

        if (moveAway > 0) {
            --moveAway;
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else if (playerController.isTargetInKneelingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.kneel();
                if (botState.isKneeling() && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            }
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                playerController.whip(gameState);
            }
        } else if (medusa.distanceX <= 40) {
            moveAway = 90 + ThreadLocalRandom.current().nextInt(61);
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget());
        }
    }

    private void stepAxeStrategy(final GameObject medusa, final int offsetX,
                                 final int offsetY) {

        if (axeScanCounter > 0) {
            --axeScanCounter;
        } else {
            axeScanCounter = ThreadLocalRandom.current().nextInt(61);
            for (int platformX = 1; platformX <= 15; ++platformX) {
                final int platformY = (platformX <= 13) ? 11 : 10;
                if (playerController.canHitTargetWithAxe(platformX, platformY, botState)) {
                    routeY = platformY << 4;
                    routeX = platformX << 4;
                    switch (platformX) {
                        case 1:
                            routeX += 10;
                            break;
                        case 15:
                            routeX += 6;
                            break;
                        default:
                            routeX += 8;
                            break;
                    }
                    break;
                }
            }
        }

        if (botState.getPlayerX() == routeX) {
            if (medusa.playerFacing) {
                if (weaponDelay == 0) {
                    playerController.useWeapon(gameState);
                    weaponDelay = 17;
                }
            } else {
                gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
            }
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                playerController.whip(gameState);
            }
        } else if (playerController.isTargetInKneelingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.kneel();
                if (botState.isKneeling() && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            }
        } else {
            gameState.getCurrentSubstage().route(routeX, routeY);
        }
    }

    private void stepBoomerangStrategy(final GameObject medusa, final int offsetX,
                                       final int offsetY) {

        if (medusa.distanceX > 64 || botState.getPlayerX() <= 25 || botState.getPlayerX() >= 727) {
            moveAway = 0;
        }

        if (moveAway > 0) {
            --moveAway;
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else if (botState.getPlayerY() - 24 >= medusa.y1 && botState.getPlayerY() - 24 <= medusa.y2) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                playerController.useWeapon(gameState);
            }
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                playerController.whip(gameState);
            }
        } else if (playerController.isTargetInKneelingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.kneel();
                if (botState.isKneeling() && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            }
        } else if (medusa.distanceX <= 40) {
            moveAway = 90 + ThreadLocalRandom.current().nextInt(61);
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget());
        }
    }

    private void stepDaggerStrategy(final GameObject medusa, final int offsetX,
                                    final int offsetY) {

        if (medusa.distanceX > 64 || botState.getPlayerX() <= 25 || botState.getPlayerX() >= 727) {
            moveAway = 0;
        }

        if (moveAway > 0) {
            --moveAway;
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else if (botState.getPlayerY() - 24 >= medusa.y1 && botState.getPlayerY() - 24 <= medusa.y2) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                playerController.useWeapon(gameState);
            }
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                playerController.whip(gameState);
            }
        } else if (playerController.isTargetInKneelingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.kneel();
                if (botState.isKneeling() && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            }
        } else if (medusa.distanceX <= 40) {
            moveAway = 90 + ThreadLocalRandom.current().nextInt(61);
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget());
        }
    }

    private void stepHolyWaterStrategy(final GameObject medusa, final int offsetX,
                                       final int offsetY) {

        if (medusa.distanceX > 64 || botState.getPlayerX() <= 25 || botState.getPlayerX() >= 727) {
            moveAway = 0;
        }

        if (moveAway > 0) {
            --moveAway;
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                playerController.whipOrWeapon(gameState, botState);
            }
        } else if (playerController.isTargetInKneelingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.kneel();
                if (botState.isKneeling() && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            }
        } else if (medusa.distanceX <= 40) {
            moveAway = 90 + ThreadLocalRandom.current().nextInt(61);
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget());
        }
    }

    private void stepStopwatchStrategy(final GameObject medusa, final int offsetX,
                                       final int offsetY) {

        if (medusa.distanceX > 64 || botState.getPlayerX() <= 25 || botState.getPlayerX() >= 727) {
            moveAway = 0;
        }

        if (moveAway > 0) {
            --moveAway;
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                if (weaponDelay == 0) {
                    weaponDelay = 180;
                    playerController.useWeapon(gameState);
                } else {
                    playerController.whip(gameState);
                }
            }
        } else if (playerController.isTargetInKneelingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                if (weaponDelay == 0) {
                    weaponDelay = 180;
                    playerController.useWeapon(gameState);
                } else {
                    playerController.kneel();
                    if (botState.isKneeling() && !gameState.isWeaponing()) {
                        playerController.whip(gameState);
                    }
                }
            }
        } else if (medusa.distanceX <= 40) {
            moveAway = 90 + ThreadLocalRandom.current().nextInt(61);
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget());
        }
    }
}