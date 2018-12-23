package castlevaniabot;

import castlevaniabot.model.gameelements.Coordinates;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.CookieMonsterStrategy;
import castlevaniabot.strategy.CrusherStrategy;
import castlevaniabot.strategy.DraculaStrategy;
import castlevaniabot.strategy.FrankensteinStrategy;
import castlevaniabot.strategy.HolyWaterDeathStrategy;
import castlevaniabot.strategy.MedusaStrategy;
import castlevaniabot.strategy.Strategy;
import castlevaniabot.substage.Substage0201;
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

    private TargetedObject targetedObject;
    private Coordinates currentTile;
    private Substage0201 damageBoostSublevel;
    private CrusherStrategy crusherStrategy;
    private MedusaStrategy medusaStrategy;
    private FrankensteinStrategy frankensteinStrategy;
    private HolyWaterDeathStrategy holyWaterDeathStrategy;
    private DraculaStrategy draculaStrategy;
    private CookieMonsterStrategy cookieMonsterStrategy;
}
