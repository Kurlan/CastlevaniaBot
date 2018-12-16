package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

public class GhostStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private int moveAwayCounter;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public GhostStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {
        moveAwayCounter = 0;
    }

    @Override
    public void step() {

        final GameObject ghost = b.getTargetedObject().getTarget();
        final int offsetX = (b.getTargetedObject().getTarget().x - lastX) << 4;
        final int offsetY = (b.getTargetedObject().getTarget().y - lastY) << 4;
        lastX = b.getTargetedObject().getTarget().x;
        lastY = b.getTargetedObject().getTarget().y;

        if (b.weaponing) {
            return;
        }

        if (ghost.distanceX >= 48) {
            moveAwayCounter = 0;
        }

        if (moveAwayCounter > 0) {
            --moveAwayCounter;
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else if (ghost.y2 < botState.getPlayerY() - 48 || ghost.y1 > botState.getPlayerY() + 16
                || ghost.distanceX > 48) {
            gameState.getCurrentSubstage().moveTowardTarget(b.getTargetedObject().getTarget());
        } else if (ghost.distanceX < 20) {
            moveAwayCounter = 180 + ThreadLocalRandom.current().nextInt(11);
            ;
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget()) {
                b.whip();                            // stand whip bat
            }
        } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget()) {
                b.kneel();
                if (b.kneeling) {
                    b.whip();                          // kneel whip bat
                }
            }
        }
    }
}