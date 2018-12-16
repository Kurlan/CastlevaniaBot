package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.gameelements.GameObject;

public class BoneDragonStrategy implements Strategy {

    private int lastX;
    private int lastY;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public BoneDragonStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject head = b.getTargetedObject().getTarget();
        final int offsetX = (head.x - lastX) << 4;
        final int offsetY = (head.y - lastY) << 4;
        lastX = head.x;
        lastY = head.y;

        final int playerX;
        final int playerY;
        if (botState.getPlayerX() > 448) {
            switch (b.whipLength) {
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
            switch (b.whipLength) {
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

        if (botState.getPlayerX() != playerX || botState.getPlayerY() != playerY || b.playerLeft) {
            gameState.getCurrentSubstage().routeAndFace(playerX, playerY, false);
        }
        if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            b.kneel();
            if (b.kneeling && !gameState.isWeaponing()) {
                b.whip();
            }
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (!gameState.isWeaponing()) {
                b.whip();
            }
        }
    }
}