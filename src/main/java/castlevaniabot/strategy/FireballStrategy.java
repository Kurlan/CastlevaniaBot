package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class FireballStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public FireballStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState =gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
    }

    @Override
    public void step() {

        final GameObject fireball = b.getTargetedObject().getTarget();
        final int offsetX = (fireball.x - lastX) << 4;
        final int offsetY = (fireball.y - lastY) << 4;
        lastX = fireball.x;
        lastY = fireball.y;

        if (fireball.distanceX < 24) {
            final boolean flyingHigh = fireball.y < botState.getPlayerY() - 16;
            if (!b.atTopOfStairs && flyingHigh) {
                playerController.kneel();                          // duck under fireball
            } else if (!flyingHigh && b.canJump) {
                playerController.jump(botState);                     // jump over fireball
            }
        } else if (b.atTopOfStairs) {
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget() && !gameState.isWeaponing()) {
                playerController.whip(gameState);                           // stand whip fireball
            }
        } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget()) {
                playerController.kneel();
                if (b.kneeling && !gameState.isWeaponing()) {
                    playerController.whip(gameState);                         // kneel whip fireball
                }
            }
        }
    }
}