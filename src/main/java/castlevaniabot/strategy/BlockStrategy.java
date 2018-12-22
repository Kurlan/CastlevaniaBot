package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import java.util.concurrent.ThreadLocalRandom;

public class BlockStrategy implements Strategy {

    private int jumpCounter;
    private int delayJump;
    private int delayWhip;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public BlockStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        jumpCounter = 0;
        delayJump = ThreadLocalRandom.current().nextInt(3);
        delayWhip = 1 + ThreadLocalRandom.current().nextInt(3);
    }

    @Override
    public void step() {
        if (gameState.isWeaponing()) {
            return;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                playerController.whip(gameState);
            }
        } else if (botState.isOnPlatform() && botState.getTargetedObject().getTarget().playerFacing
                && botState.getTargetedObject().getTarget().y >= botState.getPlayerY() - 24 && botState.getTargetedObject().getTarget().y <= botState.getPlayerY()
                && playerController.isTargetInStandingWhipRange(botState)) {
            whipBlock();
        } else {
            final int playerX = (botState.getTargetedObject().getTarget().platformX << 4) + 8;
            final int playerY = botState.getTargetedObject().getTarget().platformY << 4;
            final boolean playerLeft = playerX > botState.getTargetedObject().getTarget().x;
            if (botState.getPlayerX() == playerX && botState.getPlayerY() == playerY
                    && botState.isPlayerLeft() == playerLeft) {
                if (botState.getPlayerY() - botState.getTargetedObject().getTarget().y > 32) {
                    if (botState.isCanJump()) {
                        if (delayJump > 0) {
                            --delayJump;
                        } else {
                            jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
                            playerController.jump(botState);
                        }
                    }
                } else {
                    whipBlock();
                }
            } else {
                gameState.getCurrentSubstage().routeAndFace(playerX, playerY, playerLeft, botState, gameState);
            }
        }
    }

    private void whipBlock() {
        if (delayWhip > 0) {
            --delayWhip;
        } else {
            playerController.whip(gameState);
            gameState.getCurrentSubstage().blockWhipped(botState);
        }
    }
}