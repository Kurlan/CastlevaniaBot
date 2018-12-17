package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;

import static java.lang.Math.abs;

public class RedBonesStrategy implements Strategy {

    private boolean playerLeft;
    private int targetX;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public RedBonesStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        targetX = b.getTargetedObject().getTarget().x;
        playerLeft = b.playerLeft;
    }

    @Override
    public void step() {
        final int distanceX = abs(targetX - botState.getPlayerX());
        if (distanceX <= 16) {
            // When standing on red bones, continue walking in direction facing.
            if (playerLeft) {
                playerController.goLeft(botState);
            } else {
                b.goRight();
            }
        } else if (distanceX < 32) {
            gameState.getCurrentSubstage().moveAwayFromTarget(targetX);
        }
    }

}