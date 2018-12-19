package castlevaniabot.strategy;

import java.util.concurrent.ThreadLocalRandom;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

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
    private final PlayerController playerController;

    public AxeKnightStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
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
                playerController.useWeapon(gameState);
            }
        } else if (weaponDelay == 0 && b.getTargetedObject().getTarget().distanceX < 64
                && botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0) {
            if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState, b.getTargetedObject()) && b.canJump) {
                if (b.isUnderLedge()) {
                    weaponDelay = HOLY_WATER_RESET;
                    playerController.useWeapon(gameState);
                } else {
                    jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
                    if (b.getTargetedObject().getTarget().x < botState.getPlayerX()) {
                        playerController.goLeftAndJump(botState);
                    } else {
                        playerController.goRightAndJump(botState);
                    }
                }
            }
        } else if (gameState.getCurrentSubstage() == b.SUBSTAGE_1501 && botState.getWeapon() == BOOMERANG
                && botState.getHearts() > 0) {
            if (!gameState.isWeaponing() && weaponDelay == 0 && playerController.faceTarget(botState, gameState, b.getTargetedObject())) {
                if (botState.getShot() > 1) {
                    weaponDelay = 90;
                }
                playerController.useWeapon(gameState);
            }
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState, b.getTargetedObject())) {
                playerController.whip(gameState);
            }
        } else if (b.getTargetedObject().getTarget().distanceX > 64) {
            gameState.getCurrentSubstage().moveTowardTarget(b.getTargetedObject().getTarget());
        }
    }
}