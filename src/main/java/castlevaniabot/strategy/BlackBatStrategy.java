package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class BlackBatStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public BlackBatStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
    }

    @Override
    public void step() {

        final GameObject bat = b.getTargetedObject().getTarget();
        final int offsetX = (bat.x - lastX) << 4;
        final int offsetY = (bat.y - lastY) << 4;
        lastX = bat.x;
        lastY = bat.y;

        if (bat.distanceX < 24) {
            final boolean flyingHigh = bat.y + offsetY < botState.getPlayerY() - 16;
            if (!botState.isAtTopOfStairs() && flyingHigh) {
                playerController.kneel();                      // duck under bat
            } else if (!flyingHigh && botState.isCanJump()) {
                playerController.jump(botState);                           // jump over bat
            }
        } else if (botState.isAtTopOfStairs()) {
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (playerController.faceTarget(botState, gameState, b.getTargetedObject()) && !gameState.isWeaponing()) {
                playerController.whip(gameState);                   // stand whip bat
            }
        } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            if (playerController.faceTarget(botState, gameState, b.getTargetedObject())) {
                playerController.kneel();
                if (botState.isKneeling() && !gameState.isWeaponing()) {
                    playerController.whip(gameState);                     // kneel whip bat
                }
            }
        } else if (bat.distanceX - offsetX < 24 && offsetY != 0) {
            gameState.getCurrentSubstage().moveAwayFromTarget(b.getTargetedObject().getTarget());
        } else {
            gameState.getCurrentSubstage().moveTowardTarget(b.getTargetedObject().getTarget());
        }
    }
}