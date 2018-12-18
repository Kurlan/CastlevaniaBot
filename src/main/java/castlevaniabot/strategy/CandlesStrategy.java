package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.control.PlayerController.JUMP_WHIP_DELAYS;
import static nintaco.util.MathUtil.clamp;

public class CandlesStrategy implements Strategy {

    private int jumpCounter;
    private int done;
    private int delayJump;
    private boolean usedHolyWater;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public CandlesStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        jumpCounter = 0;
        done = 0;
        usedHolyWater = false;
        delayJump = ThreadLocalRandom.current().nextInt(3);
    }

    @Override
    public void step() {
        if (done > 0) {
            --done;
            return;
        }
        if (gameState.isWeaponing()) {
            return;
        }
        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                useWeapon();
            }
        } else {
            final int playerY = b.getTargetedObject().getTarget().platformY << 4;
            int playerX = b.getTargetedObject().getTarget().platformX << 4;
            final boolean playerLeft = playerX > b.getTargetedObject().getTarget().x;
            playerX += playerLeft ? 1 : 14;
            if (botState.getPlayerX() == playerX && botState.getPlayerY() == playerY
                    && botState.isPlayerLeft() == playerLeft) {
                final int height = botState.getPlayerY() - b.getTargetedObject().getTarget().y;
                if (height < 16) {
                    playerController.kneel();
                    if (b.kneeling) {
                        playerController.whip(gameState);
                        done = 64;
                    }
                } else if (height > 16) {
                    if (b.canJump) {
                        if (delayJump > 0) {
                            --delayJump;
                        } else {
                            jumpCounter = JUMP_WHIP_DELAYS[clamp(height - 12, 0, 36)];
                            if (botState.getWhipLength() == 0) {
                                jumpCounter -= 6;
                            }
                            playerController.jump(botState);
                        }
                    }
                } else {
                    useWeapon();
                }
            } else {
                gameState.getCurrentSubstage().routeAndFace(playerX, playerY, playerLeft);
            }
        }
    }

    private void useWeapon() {
        if (!usedHolyWater && b.getTargetedObject().getTarget().active) { // active indicates grindable
            usedHolyWater = playerController.grind(gameState, botState);
        } else {
            playerController.whip(gameState);
        }
        done = 64;
        gameState.getCurrentSubstage().candlesWhipped(b.getTargetedObject().getTarget());
    }
}