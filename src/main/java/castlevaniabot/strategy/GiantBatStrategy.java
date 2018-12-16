package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

public class GiantBatStrategy implements Strategy {

    private int jumpCounter;
    private int lastX;
    private int lastY;
    private boolean whipped;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public GiantBatStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {
        jumpCounter = 0;
        whipped = false;
    }

    @Override
    public void step() {

        final GameObject bat = b.getTargetedObject().getTarget();
        final int offsetX = (bat.x - lastX) << 4;
        final int offsetY = (bat.y - lastY) << 4;
        lastX = bat.x;
        lastY = bat.y;

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                b.whip();
                whipped = true;
            }
        } else if (bat.y1 > botState.getPlayerY()
                && bat.y1 + offsetY <= botState.getPlayerY()
                && bat.y2 + offsetY >= botState.getPlayerY() - 32
                && bat.x2 + offsetX >= botState.getPlayerX() - 8
                && bat.x1 + offsetX <= botState.getPlayerX() + 8
                && b.canJump) {

            final int landX = botState.getPlayerX() - 37;
            if (landX >= 0
                    && gameState.getCurrentSubstage().mapRoutes.map[b.currentTile.getY()][landX >> 4].height == 0) {
                b.goLeftAndJump();
                return;
            }
        } else if (!whipped
                && bat.x2 < botState.getPlayerX() - 24
                && bat.y2 < botState.getPlayerY() - 48
                && bat.x2 > botState.getPlayerX() - b.getWhipRadius() - 16
                && b.canJump) {

            final int landX = botState.getPlayerX() - 37;
            if (landX >= 0
                    && gameState.getCurrentSubstage().mapRoutes.map[b.currentTile.getY()][landX >> 4].height == 0) {
                b.goLeftAndJump();
                jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
                return;
            }
        }

        gameState.getCurrentSubstage().route(41, 128, false);
    }
}