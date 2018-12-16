package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;

import static java.lang.Math.abs;

public class RedBonesStrategy implements Strategy {

    private boolean playerLeft;
    private int targetX;

    private final CastlevaniaBot b;
    private final BotState botState;

    public RedBonesStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
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
                b.pressLeft();
            } else {
                b.pressRight();
            }
        } else if (distanceX < 32) {
            b.substage.moveAwayFromTarget(targetX);
        }
    }

}