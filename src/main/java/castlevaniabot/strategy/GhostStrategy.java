package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

public class GhostStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private int moveAwayCounter;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public GhostStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        moveAwayCounter = 0;
    }

    @Override
    public void step() {

        final GameObject ghost = botState.getTargetedObject().getTarget();
        final int offsetX = (botState.getTargetedObject().getTarget().x - lastX) << 4;
        final int offsetY = (botState.getTargetedObject().getTarget().y - lastY) << 4;
        lastX = botState.getTargetedObject().getTarget().x;
        lastY = botState.getTargetedObject().getTarget().y;

        if (gameState.isWeaponing()) {
            return;
        }

        if (ghost.distanceX >= 48) {
            moveAwayCounter = 0;
        }

        if (moveAwayCounter > 0) {
            --moveAwayCounter;
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else if (ghost.y2 < botState.getPlayerY() - 48 || ghost.y1 > botState.getPlayerY() + 16
                || ghost.distanceX > 48) {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget());
        } else if (ghost.distanceX < 20) {
            moveAwayCounter = 180 + ThreadLocalRandom.current().nextInt(11);
            ;
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.whip(gameState);                            // stand whip bat
            }
        } else if (playerController.isTargetInKneelingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.kneel();
                if (botState.isKneeling()) {
                    playerController.whip(gameState);                        // kneel whip bat
                }
            }
        }
    }
}