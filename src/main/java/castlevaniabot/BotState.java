package castlevaniabot;

import castlevaniabot.strategy.Strategy;
import lombok.Data;

@Data
public class BotState {
    private int playerX;
    private int playerY;
    private Strategy currentStrategy;

    private int avoidX;
    private int whipLength;
    private boolean onStairs;

    private int jumpDelay;

    private boolean overHangingLeft;
    private boolean overHangingRight;
    private boolean onPlatform;
    private boolean playerLeft;
}
