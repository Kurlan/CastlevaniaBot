package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import java.util.concurrent.ThreadLocalRandom;

public class UseWeaponStrategy implements Strategy {

    private int jumpCounter;
    private int done;
    private int playerX;
    private int playerY;
    private boolean jump;
    private boolean faceLeft;
    private int delayAfterUse;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public UseWeaponStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    public void init(final int playerX, final int playerY, final boolean jump,
                     final boolean faceLeft) {
        init(playerX, playerY, jump, faceLeft, 170);
    }

    void init(final int playerX, final int playerY, final boolean jump,
              final boolean faceLeft, final int delayAfterUse) {
        jumpCounter = done = 0;
        this.playerX = playerX;
        this.playerY = playerY;
        this.jump = jump;
        this.faceLeft = faceLeft;
        this.delayAfterUse = delayAfterUse
                + ThreadLocalRandom.current().nextInt(11);
        ;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {
        if (done > 0) {
            if (--done == 0) {
                gameState.getCurrentSubstage().weaponUsed();
            }
            return;
        }
        if (gameState.isWeaponing()) {
            return;
        }
        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                useWeapon();
            }
        } else if (botState.getPlayerX() != playerX || botState.getPlayerY() != playerY) {
            gameState.getCurrentSubstage().route(playerX, playerY);
        } else if (faceLeft != botState.isPlayerLeft()) {
            // walk past and turn around
            if (faceLeft) {
                gameState.getCurrentSubstage().routeRight();
            } else {
                gameState.getCurrentSubstage().routeLeft();
            }
        } else if (jump && botState.isCanJump()) {
            jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
            playerController.jump(botState);
        } else {
            useWeapon();
        }
    }

    private void useWeapon() {
        playerController.useWeapon(gameState);
        done = delayAfterUse;
    }
}