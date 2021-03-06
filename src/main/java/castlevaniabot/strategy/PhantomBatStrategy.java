package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.AXE;
import static castlevaniabot.model.creativeelements.Weapon.DAGGER;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static java.lang.Math.abs;

public class PhantomBatStrategy implements Strategy {

    private static final int[] AXE_SCANS = {9, 0, 8, 1, 7, 2, 6, 3, 5, 4};

    private int lastX;
    private int lastY;
    private int weaponDelay;
    private int jumpCounter;
    private int routeX;
    private int axeScanCounter;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public PhantomBatStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        routeX = 648;
        axeScanCounter = 110 + ThreadLocalRandom.current().nextInt(23);
    }

    @Override
    public void step() {

        final GameObject bat = botState.getTargetedObject().getTarget();
        final int offsetX = (bat.x - lastX) << 4;
        final int offsetY = (bat.y - lastY) << 4;
        lastX = bat.x;
        lastY = bat.y;

        if (weaponDelay > 0) {
            --weaponDelay;
        }

        if (botState.getHearts() >= 5 && botState.getWeapon() == STOPWATCH) {
            stepStopwatchStrategy(bat, offsetX, offsetY);
        } else if (botState.getHearts() > 0) {
            switch (botState.getWeapon()) {
                case HOLY_WATER:
                    stepHolyWaterStrategy(bat);
                    break;
                case AXE:
                    stepAxeStrategy(bat, offsetX, offsetY);
                    break;
                case DAGGER:
                    stepDaggerStrategy(bat, offsetX, offsetY);
                    break;
                default:
                    stepNoWeaponsStrategy(bat, offsetX, offsetY);
                    break;
            }
        } else {
            stepNoWeaponsStrategy(bat, offsetX, offsetY);
        }
    }

    private void stepDaggerStrategy(final GameObject bat, final int offsetX,
                                    final int offsetY) {

        if (gameState.isWeaponing() || weaponDelay > 0) {
            return;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
                    playerController.whip(gameState);
                } else {
                    weaponDelay = 17;
                    playerController.useWeapon(gameState);
                }
            }
        } else if (botState.isCanJump() && bat.distanceX < 40 && bat.distanceY < 36) {
            if (botState.getPlayerX() < 560 || botState.getPlayerX() >= 696) {
                playerController.kneel();
            } else if (botState.getPlayerX() > 522 && botState.getPlayerX() < 750) {
                gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget(), botState, gameState);
            }
        } else if (botState.getPlayerX() == 736 && botState.getPlayerY() == 144) {
            if (botState.isPlayerLeft()) {
                if (botState.isCanJump()) {
                    final int y = botState.getPlayerY() - 24;
                    final int yJump = y - 32;
                    if ((yJump >= bat.y1 && yJump <= bat.y2)
                            || playerController.isTargetInStandingWhipRange(offsetX, offsetY + 32, botState)) {
                        jumpCounter = (botState.getWhipLength() == 0) ? 16 : 10;
                        playerController.jump(botState);
                    } else if (y >= bat.y1 && y <= bat.y2) {
                        weaponDelay = 17;
                        playerController.useWeapon(gameState);
                    } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
                        playerController.whip(gameState);
                    }
                }
            } else {
                gameState.getCurrentSubstage().route(744, 144, botState, gameState);
            }
        } else {
            gameState.getCurrentSubstage().route(736, 144, botState, gameState);
        }
    }

    private void stepAxeStrategy(final GameObject bat, final int offsetX,
                                 final int offsetY) {

        if (botState.getPlayerX() >= 664) {
            stepNoWeaponsStrategy(bat, offsetX, offsetY);
            return;
        } else if (gameState.isWeaponing()) {
            return;
        }

        if (axeScanCounter > 0) {
            --axeScanCounter;
        } else {
            axeScanCounter = 110 + ThreadLocalRandom.current().nextInt(23);
            for (int i = AXE_SCANS.length - 1; i >= 0; --i) {
                final int platformX = 32 + AXE_SCANS[i];
                if (playerController.canHitTargetWithAxe(platformX, 13, botState)) {
                    routeX = platformX << 4;
                    switch (AXE_SCANS[i]) {
                        case 0:
                            routeX += 10;
                            break;
                        case 9:
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
            if (bat.playerFacing) {
                if (weaponDelay == 0) {
                    playerController.useWeapon(gameState);
                    weaponDelay = 17;
                }
            } else {
                gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget(), botState, gameState);
            }
        } else {
            gameState.getCurrentSubstage().route(routeX, 208, botState, gameState);
        }
    }

    private void stepStopwatchStrategy(final GameObject bat, final int offsetX,
                                       final int offsetY) {

        if (botState.getPlayerX() >= 664) {
            stepNoWeaponsStrategy(bat, offsetX, offsetY);
            return;
        } else if (gameState.isWeaponing()) {
            return;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                playerController.whip(gameState);
            }
        } else if (weaponDelay > 0) {
            if (bat.distanceX == 48) {
                if (bat.playerFacing) {
                    if (botState.isCanJump()) {
                        if (playerController.isTargetInStandingWhipRange(offsetX, offsetY + 32, botState)) {
                            jumpCounter = (botState.getWhipLength() == 0) ? 16 : 10;
                            playerController.jump(botState);
                        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
                            playerController.whip(gameState);
                        }
                    }
                } else {
                    gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget(), botState, gameState);
                }
            } else if (bat.distanceX > 48) {
                gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget(), botState, gameState);
            } else {
                gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget(), botState, gameState);
            }
        } else if (botState.isCanJump() && bat.y >= 160 && bat.distanceX >= 48
                && bat.distanceX <= 96) {
            weaponDelay = 180;
            playerController.useWeapon(gameState);
        } else {
            stepNoWeaponsStrategy(bat, offsetX, offsetY);
        }
    }

    private void stepHolyWaterStrategy(final GameObject bat) {
        if (botState.getPlayerY() == 208 && abs(botState.getPlayerX() - 712) < 2) {
            if (botState.isPlayerLeft()) {
                if (bat.x > 640 && bat.y > 120 && weaponDelay == 0) {
                    weaponDelay = 80;
                    playerController.whipOrWeapon(gameState, botState);
                }
            } else {
                gameState.getCurrentSubstage().routeLeft(botState, gameState);
            }
        } else {
            gameState.getCurrentSubstage().route(712, 208, botState, gameState);
        }
    }

    private void stepNoWeaponsStrategy(final GameObject bat, final int offsetX,
                                       final int offsetY) {

        if (gameState.isWeaponing()) {
            return;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                playerController.whip(gameState);
            }
        } else if (botState.isCanJump() && bat.distanceX < 40 && bat.distanceY < 36) {
            if (botState.getPlayerX() < 560 || botState.getPlayerX() >= 696) {
                playerController.kneel();
            } else if (botState.getPlayerX() > 522 && botState.getPlayerX() < 750) {
                gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget(), botState, gameState);
            }
        } else if (botState.getPlayerY() == 208 && botState.getPlayerX() == 522) {
            if (botState.isPlayerLeft()) {
                gameState.getCurrentSubstage().route(521, 208, botState, gameState);
            } else if (botState.isCanJump()) {
                if (playerController.isTargetInStandingWhipRange(offsetX, offsetY + 32, botState)) {
                    jumpCounter = (botState.getWhipLength() == 0) ? 16 : 10;
                    playerController.jump(botState);
                } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
                    playerController.whip(gameState);
                }
            }
        } else {
            gameState.getCurrentSubstage().route(522, 208, botState, gameState);
        }
    }
}