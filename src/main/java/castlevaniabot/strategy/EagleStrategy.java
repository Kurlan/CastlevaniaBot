package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class EagleStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;


    public EagleStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
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

        final GameObject eagle = b.getTargetedObject().getTarget();
        final int offsetX = (eagle.x - lastX) << 4;
        final int offsetY = (eagle.y - lastY) << 4;
        lastX = eagle.x;
        lastY = eagle.y;

        if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (!gameState.isWeaponing() && playerController.faceFlyingTarget(botState, b.getTargetedObject())) {
                playerController.whip(gameState);
            }
        } else if (!botState.isOnStairs() && b.isTargetInKneelingWhipRange(offsetY, offsetY)) {
            if (playerController.faceFlyingTarget(botState, b.getTargetedObject())) {
                playerController.kneel();
                if (b.kneeling && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            }
        }
    }
}