package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.creativeelements.RedBat;
import castlevaniabot.model.gameelements.GameObject;

public class RedBatStrategy implements Strategy {

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public RedBatStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
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
        final RedBat redBat = b.getRedBat(bat);
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
            } else if (!flyingHigh && b.canJump) {
                playerController.jump(botState);                         // jump over bat
            }
        } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget() && !gameState.isWeaponing()) {
                playerController.whip(gameState);                            // stand whip bat
            }
        } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            if (b.faceTarget()) {
                playerController.kneel();
                if (b.kneeling && !gameState.isWeaponing()) {
                    playerController.whip(gameState);                          // kneel whip bat
                }
            }
        } else if ((bat.left && bat.x1 > botState.getPlayerX() + 24)
                || (!bat.left && bat.x2 < botState.getPlayerX() - 24)) {
            gameState.getCurrentSubstage().moveTowardTarget(b.getTargetedObject().getTarget());
        }
    }
}