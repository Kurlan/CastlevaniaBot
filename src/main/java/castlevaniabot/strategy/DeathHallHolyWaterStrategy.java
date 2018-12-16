package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.AXE_KNIGHT;

public class DeathHallHolyWaterStrategy implements Strategy {

    private static final int HOLY_WATER_RESET = 180;

    private int jumpCounter;
    private int holyWaterDelay;

    private final CastlevaniaBot b;
    private final BotState botState;

    public DeathHallHolyWaterStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
    }

    @Override
    public void init() {
        jumpCounter = holyWaterDelay = 0;
    }

    @Override
    public void step() {

        if (holyWaterDelay > 0) {
            --holyWaterDelay;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                holyWaterDelay = HOLY_WATER_RESET;
                b.useWeapon();
            }
            return;
        } else if (holyWaterDelay == 0) {
            final GameObject[] objs = b.gameObjects;
            for (int i = b.objsCount - 1; i >= 0; --i) {
                final GameObject obj = objs[i];
                if (obj.type == AXE_KNIGHT && obj.x < botState.getPlayerX() && obj.distanceX < 64) {
                    jumpCounter = 9;
                    b.pressLeftAndJump();
                    return;
                }
            }
        }

        b.substage.route(9, 128, false);
    }
}