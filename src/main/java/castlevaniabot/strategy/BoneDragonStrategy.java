package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

public class BoneDragonStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public BoneDragonStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject head = botState.getTargetedObject().getTarget();
        final int offsetX = (head.x - lastX) << 4;
        final int offsetY = (head.y - lastY) << 4;
        lastX = head.x;
        lastY = head.y;

        final int playerX;
        final int playerY;
        if (botState.getPlayerX() > 448) {
            switch (botState.getWhipLength()) {
                case 0:
                    playerX = 652;
                    break;
                case 1:
                    playerX = 648;
                    break;
                default:
                    playerX = 632;
                    break;
            }
            playerY = 160;
        } else {
            switch (botState.getWhipLength()) {
                case 0:
                    playerX = 220;
                    break;
                case 1:
                    playerX = 216;
                    break;
                default:
                    playerX = 200;
                    break;
            }
            playerY = 192;
        }

        if (botState.getPlayerX() != playerX || botState.getPlayerY() != playerY || botState.isPlayerLeft()) {
            gameState.getCurrentSubstage().routeAndFace(playerX, playerY, false, botState, gameState);
        }
        if (playerController.isTargetInKneelingWhipRange(offsetX, offsetY, botState)) {
            playerController.kneel();
            if (botState.isKneeling() && !gameState.isWeaponing()) {
                playerController.whip(gameState);
            }
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (!gameState.isWeaponing()) {
                playerController.whip(gameState);
            }
        }
    }
}