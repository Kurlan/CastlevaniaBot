package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

public class FleamanStrategy implements Strategy {

    private int lastX;
    private int lastY;
    private int move;
    private boolean left;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public FleamanStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {
        lastX = lastY = move = 0;
    }

    @Override
    public void step() {

        final GameObject fleaman = b.getTargetedObject().getTarget();
        final int vx;
        final int vy;
        if (lastX != 0) {
            vx = (fleaman.x - lastX) << 4;
            vy = (fleaman.y - lastY) << 4;
        } else {
            vx = vy = 0;
        }
        lastX = fleaman.x;
        lastY = fleaman.y;

        if (move > 0) {
            --move;
            if (left) {
                gameState.getCurrentSubstage().routeLeft();
            } else {
                gameState.getCurrentSubstage().routeRight();
            }
        } else if (b.isTargetInStandingWhipRange(vx, vy)) {
            if (!gameState.isWeaponing() && b.faceTarget()) {
                b.whip();
            }
        } else if (b.isTargetInKneelingWhipRange(vx, vy)) {
            b.kneel();
            if (b.kneeling && !gameState.isWeaponing() && b.faceTarget()) {
                b.whip();
            }
        } else if ((vy < 0 && fleaman.y < botState.getPlayerY() - 16 && fleaman.distanceX < 56)
                || (fleaman.x1 + vx <= botState.getPlayerX() + 8
                && fleaman.x2 + vx >= botState.getPlayerX() - 8
                && fleaman.y2 + vy >= botState.getPlayerY() - 32
                && fleaman.y1 + vy <= botState.getPlayerY())) {
            move = 23 + ThreadLocalRandom.current().nextInt(17);
            left = vx > 0;
        }
    }
}