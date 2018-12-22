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

        final GameObject fireball = botState.getTargetedObject().getTarget();
        final int offsetX = (fireball.x - lastX) << 4;
        final int offsetY = (fireball.y - lastY) << 4;
        lastX = fireball.x;
        lastY = fireball.y;

        if (fireball.distanceX < 24) {
            final boolean flyingHigh = fireball.y < botState.getPlayerY() - 16;
            if (!botState.isAtTopOfStairs() && flyingHigh) {
                playerController.kneel();                          // duck under fireball
            } else if (!flyingHigh && botState.isCanJump()) {
                playerController.jump(botState);                     // jump over fireball
            }
        } else if (botState.isAtTopOfStairs()) {
            gameState.getCurrentSubstage().moveAwayFromTarget(botState.getTargetedObject().getTarget());
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                playerController.whip(gameState);                           // stand whip fireball
            }
        } else if (playerController.isTargetInKneelingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.kneel();
                if (botState.isKneeling() && !gameState.isWeaponing()) {
                    playerController.whip(gameState);                         // kneel whip fireball
                }
            }
        }
    }
}