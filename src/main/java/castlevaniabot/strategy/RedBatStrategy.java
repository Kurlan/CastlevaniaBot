package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.creativeelements.RedBat;
import castlevaniabot.model.gameelements.GameObject;

public class RedBatStrategy implements Strategy {

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public RedBatStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        final GameObject bat = botState.getTargetedObject().getTarget();
        final RedBat redBat = gameState.getRedBat(bat);
        if (redBat == null) {
            return;
        }

        int t = redBat.t + 16;
        int d = 0;
        if (t >= RedBat.WAVE.length) {
            t -= RedBat.WAVE.length;
            d = 110;
        }
        final int dx = RedBat.WAVE[t][0] + d;
        final int batX = redBat.left ? (redBat.x0 - dx) : (redBat.x0 + dx);
        final int batY = redBat.y0 + RedBat.WAVE[t][1];
        final int offsetX = batX - bat.x;
        final int offsetY = batY - bat.y;

        if (bat.distanceX < 24) {
            final boolean flyingHigh = batY < botState.getPlayerY() - 16;
            if (flyingHigh) {
                playerController.kneel();                           // duck under bat
            } else if (!flyingHigh && botState.isCanJump()) {
                playerController.jump(botState);                         // jump over bat
            }
        } else if (playerController.isTargetInStandingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState) && !gameState.isWeaponing()) {
                playerController.whip(gameState);                            // stand whip bat
            }
        } else if (playerController.isTargetInKneelingWhipRange(offsetX, offsetY, botState)) {
            if (playerController.faceTarget(botState, gameState)) {
                playerController.kneel();
                if (botState.isKneeling() && !gameState.isWeaponing()) {
                    playerController.whip(gameState);                          // kneel whip bat
                }
            }
        } else if ((bat.left && bat.x1 > botState.getPlayerX() + 24)
                || (!bat.left && bat.x2 < botState.getPlayerX() - 24)) {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget());
        }
    }
}