package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.ThreadLocalRandom;

public class BlockStrategy implements Strategy {

    private int jumpCounter;
    private int delayJump;
    private int delayWhip;

    private final CastlevaniaBot b;
    private final BotState botState;

    public BlockStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
    }

    @Override
    public void init() {
        jumpCounter = 0;
        delayJump = ThreadLocalRandom.current().nextInt(3);
        delayWhip = 1 + ThreadLocalRandom.current().nextInt(3);
    }

    @Override
    public void step() {
        if (b.weaponing) {
            return;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                b.whip();
            }
        } else if (b.onPlatform && b.getTargetedObject().getTarget().playerFacing
                && b.getTargetedObject().getTarget().y >= botState.getPlayerY() - 24 && b.getTargetedObject().getTarget().y <= botState.getPlayerY()
                && b.isTargetInStandingWhipRange()) {
            whipBlock();
        } else {
            final int playerX = (b.getTargetedObject().getTarget().platformX << 4) + 8;
            final int playerY = b.getTargetedObject().getTarget().platformY << 4;
            final boolean playerLeft = playerX > b.getTargetedObject().getTarget().x;
            if (botState.getPlayerX() == playerX && botState.getPlayerY() == playerY
                    && b.playerLeft == playerLeft) {
                if (botState.getPlayerY() - b.getTargetedObject().getTarget().y > 32) {
                    if (b.canJump) {
                        if (delayJump > 0) {
                            --delayJump;
                        } else {
                            jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
                            b.jump();
                        }
                    }
                } else {
                    whipBlock();
                }
            } else {
                b.substage.routeAndFace(playerX, playerY, playerLeft);
            }
        }
    }

    private void whipBlock() {
        if (delayWhip > 0) {
            --delayWhip;
        } else {
            b.whip();
            b.substage.blockWhipped();
        }
    }
}