package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.control.PlayerController.JUMP_WHIP_DELAYS;
import static nintaco.util.MathUtil.clamp;

public class WhipStrategy implements Strategy {

    private int jumpCounter;
    private int done;
    private int playerX;
    private int playerY;
    private int delayAfterUse;
    private int delayJump;
    private int targetHeight;
    private boolean jump;
    private boolean playerLeft;
    private boolean jumpToward;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public WhipStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    void init(final int playerX, final int playerY, final boolean playerLeft) {
        init(playerX, playerY, playerLeft, 0, false, false, 0);
    }

    public void init(final int playerX, final int playerY, final boolean playerLeft,
                     final int delayAfterUse) {
        init(playerX, playerY, playerLeft, delayAfterUse, false, false, 0);
    }

    public void init(final int playerX, final int playerY, final boolean playerLeft,
                     final int delayAfterUse, final boolean jump,
                     final boolean jumpToward, final int targetHeight) {

        jumpCounter = done = 0;
        delayJump = ThreadLocalRandom.current().nextInt(3);

        this.playerX = playerX;
        this.playerY = playerY;
        this.playerLeft = playerLeft;
        this.jump = jump;
        this.jumpToward = jumpToward;
        this.targetHeight = targetHeight;
        this.delayAfterUse = (delayAfterUse == 0) ? 0 : delayAfterUse
                + ThreadLocalRandom.current().nextInt(11);
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
                useWhip();
            }
        } else if (botState.getPlayerX() != playerX || botState.getPlayerY() != playerY
                || botState.isPlayerLeft() != playerLeft) {
            gameState.getCurrentSubstage().routeAndFace(playerX, playerY, playerLeft);
        } else if (jump && botState.isCanJump()) {
            if (delayJump > 0) {
                --delayJump;
            } else {
                jumpCounter = JUMP_WHIP_DELAYS[clamp(targetHeight, 0, 36)];
                if (botState.getWhipLength() == 0) {
                    jumpCounter -= 6;
                }
                if (jumpToward) {
                    if (playerLeft) {
                        playerController.goLeftAndJump(botState);
                    } else {
                        playerController.goRightAndJump(botState);
                    }
                } else {
                    playerController.jump(botState);
                }
            }
        } else {
            useWhip();
        }
    }

    private void useWhip() {
        playerController.whip(gameState);
        if (delayAfterUse == 0) {
            gameState.getCurrentSubstage().whipUsed();
        } else {
            done = delayAfterUse;
        }
    }
}