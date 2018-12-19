package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.creativeelements.MedusaHead;
import castlevaniabot.model.gameelements.GameObject;

// No longer used!
public class MedusaHeadStrategy implements Strategy {

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public MedusaHeadStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
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

        final GameObject head = b.getTargetedObject().getTarget();
        final MedusaHead medusaHead = b.getMedusaHead(head);
        if (medusaHead == null) {
            return;
        }

        int t = medusaHead.t + 16;
        int d = 0;
        if (t >= MedusaHead.WAVE.length) {
            t -= MedusaHead.WAVE.length;
            d = 126;
        }
        final int dx = MedusaHead.WAVE[t][0] + d;
        final int headX = medusaHead.left ? (medusaHead.x0 - dx)
                : (medusaHead.x0 + dx);
        final int headY = medusaHead.y0 + MedusaHead.WAVE[t][1];
        final int offsetX = headX - head.x;
        final int offsetY = headY - head.y;

        if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
            if (playerController.faceTarget(botState, gameState, b.getTargetedObject()) && !gameState.isWeaponing()) {
                playerController.whip(gameState);
            }
        } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
            if (playerController.faceTarget(botState, gameState, b.getTargetedObject())) {
                playerController.kneel();
                if (b.kneeling && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                }
            }
        }
    }
}