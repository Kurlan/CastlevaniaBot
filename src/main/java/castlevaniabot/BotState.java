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
    private int weapon;
    private boolean atBottomOfStairs;

    private int hearts;
    private int shot;

    private boolean atTopOfStairs;
    private boolean kneeling;

    private boolean canJump;
}
