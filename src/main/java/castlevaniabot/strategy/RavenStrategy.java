package castlevaniabot.strategy;

import java.util.concurrent.ThreadLocalRandom;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class RavenStrategy implements Strategy {

    private int jumpCounter;
    private int lastX;
    private int lastY;
    private int moveAway;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public RavenStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        moveAway = jumpCounter = 0;
    }

    @Override
    public void step() {

        final GameObject raven = b.getTargetedObject().getTarget();
        final int offsetX = (raven.x - lastX) << 4;
        final int offsetY = (raven.y - lastY) << 4;
        lastX = raven.x;
        lastY = raven.y;

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                playerController.whip(gameState);
            }
        } else if (moveAway > 0) {
            --moveAway;
            gameState.getCurrentSubstage().routeLeft();
        } else if (!botState.isOnStairs() && botState.isOnPlatform() && raven.active
                && raven.y1 > botState.getPlayerY()) {
            moveAway = 64 + ThreadLocalRandom.current().nextInt(11);
            gameState.getCurrentSubstage().routeLeft();
        } else if (!gameState.isWeaponing() && playerController.faceTarget(botState, gameState, b.getTargetedObject())) {
            if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
                playerController.whip(gameState);
            } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
                playerController.kneel();
                if (b.kneeling) {
                    playerController.whip(gameState);
                }
            } else if (b.canJump) {
                final int whipDelay = b.isTargetInJumpingWhipRange(offsetX, offsetY);
                if (whipDelay > 0) {
                    jumpCounter = whipDelay;
                    playerController.jump(botState);
                }
            }
        }
    }
}