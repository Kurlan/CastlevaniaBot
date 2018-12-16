package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
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

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public PhantomBatStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {
        routeX = 648;
        axeScanCounter = 110 + ThreadLocalRandom.current().nextInt(23);
    }

    @Override
    public void step() {

        final GameObject bat = b.getTargetedObject().getTarget();
        final int offsetX = (bat.x - lastX) << 4;
        final int offsetY = (bat.y - lastY) << 4;
        lastX = bat.x;
        lastY = bat.y;

        if (weaponDelay > 0) {
            --weaponDelay;
        }

        if (b.hearts >= 5 && b.weapon == STOPWATCH) {
            stepStopwatchStrategy(bat, offsetX, offsetY);
        } else if (b.hearts > 0) {
            switch (b.weapon) {
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

        if (b.weaponing || weaponDelay > 0) {
            return;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
                    b.whip();
                } else {
                    weaponDelay = 17;
                    b.useWeapon();
                }
            }
        } else if (b.canJump && bat.distanceX < 40 && bat.distanceY < 36) {
            if (botState.getPlayerX() < 560 || botState.getPlayerX() >= 696) {
                b.kneel();
            } else if (botState.getPlayerX() > 522 && botState.getPlayerX() < 750) {
                gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
            }
        } else if (botState.getPlayerX() == 736 && botState.getPlayerY() == 144) {
            if (b.playerLeft) {
                if (b.canJump) {
                    final int y = botState.getPlayerY() - 24;
                    final int yJump = y - 32;
                    if ((yJump >= bat.y1 && yJump <= bat.y2)
                            || b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
                        jumpCounter = (b.whipLength == 0) ? 16 : 10;
                        b.jump();
                    } else if (y >= bat.y1 && y <= bat.y2) {
                        weaponDelay = 17;
                        b.useWeapon();
                    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
                        b.whip();
                    }
                }
            } else {
                gameState.getCurrentSubstage().route(744, 144);
            }
        } else {
            gameState.getCurrentSubstage().route(736, 144);
        }
    }

    private void stepAxeStrategy(final GameObject bat, final int offsetX,
                                 final int offsetY) {

        if (botState.getPlayerX() >= 664) {
            stepNoWeaponsStrategy(bat, offsetX, offsetY);
            return;
        } else if (b.weaponing) {
            return;
        }

        if (axeScanCounter > 0) {
            --axeScanCounter;
        } else {
            axeScanCounter = 110 + ThreadLocalRandom.current().nextInt(23);
            for (int i = AXE_SCANS.length - 1; i >= 0; --i) {
                final int platformX = 32 + AXE_SCANS[i];
                if (b.canHitTargetWithAxe(platformX, 13)) {
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
                    b.useWeapon();
                    weaponDelay = 17;
                }
            } else {
                gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
            }
        } else {
            gameState.getCurrentSubstage().route(routeX, 208);
        }
    }

    private void stepStopwatchStrategy(final GameObject bat, final int offsetX,
                                       final int offsetY) {

        if (botState.getPlayerX() >= 664) {
            stepNoWeaponsStrategy(bat, offsetX, offsetY);
            return;
        } else if (b.weaponing) {
            return;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                b.whip();
            }
        } else if (weaponDelay > 0) {
            if (bat.distanceX == 48) {
                if (bat.playerFacing) {
                    if (b.canJump) {
                        if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
                            jumpCounter = (b.whipLength == 0) ? 16 : 10;
                            b.jump();
                        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
                            b.whip();
                        }
                    }
                } else {
                    gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
                }
            } else if (bat.distanceX > 48) {
                gameState.getCurrentSubstage().moveTowardTarget(b.getTargetedObject().getTarget());
            } else {
                gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
            }
        } else if (b.canJump && bat.y >= 160 && bat.distanceX >= 48
                && bat.distanceX <= 96) {
            weaponDelay = 180;
            b.useWeapon();
        } else {
            stepNoWeaponsStrategy(bat, offsetX, offsetY);
        }
    }

    private void stepHolyWaterStrategy(final GameObject bat) {
        if (botState.getPlayerY() == 208 && abs(botState.getPlayerX() - 712) < 2) {
            if (b.playerLeft) {
                if (bat.x > 640 && bat.y > 120 && weaponDelay == 0) {
                    weaponDelay = 80;
                    b.whipOrWeapon();
                }
            } else {
                gameState.getCurrentSubstage().routeLeft();
            }
        } else {
            gameState.getCurrentSubstage().route(712, 208);
        }
    }

    private void stepNoWeaponsStrategy(final GameObject bat, final int offsetX,
                                       final int offsetY) {

        if (b.weaponing) {
            return;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                b.whip();
            }
        } else if (b.canJump && bat.distanceX < 40 && bat.distanceY < 36) {
            if (botState.getPlayerX() < 560 || botState.getPlayerX() >= 696) {
                b.kneel();
            } else if (botState.getPlayerX() > 522 && botState.getPlayerX() < 750) {
                gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
            }
        } else if (botState.getPlayerY() == 208 && botState.getPlayerX() == 522) {
            if (b.playerLeft) {
                gameState.getCurrentSubstage().route(521, 208);
            } else if (b.canJump) {
                if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
                    jumpCounter = (b.whipLength == 0) ? 16 : 10;
                    b.jump();
                } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
                    b.whip();
                }
            }
        } else {
            gameState.getCurrentSubstage().route(522, 208);
        }
    }
}