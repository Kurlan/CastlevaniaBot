package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.GameObjectType;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static java.lang.Math.abs;

public class MummiesStrategy implements Strategy {

    private GameObject mummy1; // left mummy
    private GameObject mummy2; // right mummy
    private int mummyLastX1;
    private int mummyLastX2;
    private int mummyOffsetX1;
    private int mummyOffsetX2;
    private int weaponDelay;
    private int routeX;
    private int routeY;
    private int moveAway;
    private int moveAwayFromX;
    private boolean weaponedMummy1;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public MummiesStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        mummy1 = mummy2 = null;
        moveAway = moveAwayFromX = routeX = routeY = weaponDelay = mummyOffsetX1
                = mummyOffsetX2 = 0;
        weaponedMummy1 = ThreadLocalRandom.current().nextBoolean();
    }

    @Override
    public void step() {

        mummy1 = null;
        mummy2 = null;
        final GameObject[] objs = gameState.getGameObjects();
        for (int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = objs[i];
            if (obj.type == GameObjectType.MUMMY) {
                if (mummy1 == null) {
                    mummy1 = obj;
                } else {
                    mummy2 = obj;
                    if (mummy1.x > mummy2.x) {
                        final GameObject temp = mummy1;
                        mummy1 = mummy2;
                        mummy2 = temp;
                    }
                    break;
                }
            }
        }
        if (mummy1 == null) {
            gameState.getCurrentSubstage().bossDefeated();
            return;
        } else {
            mummyOffsetX1 = mummy1.x - mummyLastX1;
            mummyLastX1 = mummy1.x;
        }
        if (mummy2 != null) {
            mummyOffsetX2 = mummy2.x - mummyLastX2;
            mummyLastX2 = mummy2.x;
        }

        if (botState.getPlayerY() > 164) {
            if (b.hearts > 0 && botState.getWeapon() == BOOMERANG) {
                stepBoomerangStrategy();
                return;
            } else if (b.SUBSTAGE_0900.blockBroken) {
                stepGroundAssault();
                return;
            }
        }

        if (b.hearts > 0) {
            switch (botState.getWeapon()) {
                case BOOMERANG:
                    stepBoomerangStrategy();
                    break;
                case HOLY_WATER:
                    stepHolyWaterStrategy();
                    break;
                default:
                    stepNoWeaponsStrategy();
                    break;
            }
        } else {
            stepNoWeaponsStrategy();
        }
    }

    private void stepGroundAssault() {

        GameObject closestMummy = mummy1;
        if (mummy2 != null && mummy2.distanceX < mummy1.distanceX) {
            closestMummy = mummy2;
        }

        if (moveAway > 0) {
            --moveAway;
            gameState.getCurrentSubstage().moveAwayFromTarget(moveAwayFromX);
            return;
        }

        if (closestMummy.distanceX < 30 && botState.getPlayerX() > 1312 && botState.getPlayerX() < 1488) {
            moveAway = 17 + ThreadLocalRandom.current().nextInt(17);
            moveAwayFromX = closestMummy.x;
            gameState.getCurrentSubstage().moveAwayFromTarget(closestMummy.x);
            return;
        }

        final boolean inRange1 = b.isInStandingWhipRange(mummy1,
                mummyOffsetX1, 0);
        final boolean inRange2 = mummy2 != null && b.isInStandingWhipRange(mummy2,
                mummyOffsetX2, 0);
        final GameObject targetMummy;
        if (inRange1 && inRange2) {
            targetMummy = weaponedMummy1 ? mummy2 : mummy1;
        } else if (inRange1) {
            targetMummy = mummy1;
        } else if (inRange2) {
            targetMummy = mummy2;
        } else {
            targetMummy = null;
        }
        if (targetMummy != null && b.face(targetMummy) && !gameState.isWeaponing()) {
            weaponedMummy1 = !weaponedMummy1;
            playerController.whipOrWeapon(gameState, botState);
        }
    }

    private void stepBoomerangStrategy() {

        if (botState.getPlayerX() != 1512 || botState.getPlayerY() != 208 || !botState.isPlayerLeft()) {
            gameState.getCurrentSubstage().routeAndFace(1512, 208, true, false);
        } else if (!gameState.isWeaponing()) {
            playerController.whipOrWeapon(gameState, botState);
        }
    }

    private void stepNoWeaponsStrategy() {

        if (mummy1 != null) { // mummy1 is the left-most mummy
            if (mummy1.x < 1356) {
                routeX = 1322;
                routeY = 160;
            } else if (mummy1.x > 1364) {
                routeX = 1338;
                routeY = 176;
            }
        }

        if (botState.getPlayerX() != routeX || botState.getPlayerY() != routeY || botState.isPlayerLeft()) {
            gameState.getCurrentSubstage().routeAndFace(routeX, routeY, false);
        } else {
            playerController.kneel();
            if (b.kneeling && !gameState.isWeaponing()) {
                playerController.whip(gameState);
            }
        }
    }

    private void stepHolyWaterStrategy() {

        if (botState.getPlayerX() != 1290 || botState.getPlayerY() != 144 || botState.isPlayerLeft()) {
            gameState.getCurrentSubstage().routeAndFace(1290, 144, false);
        } else if (weaponDelay > 0) {
            --weaponDelay;
        } else if (!gameState.isWeaponing() && ((mummy1 != null && abs(mummy1.x - 1360) < 16)
                || (mummy2 != null && abs(mummy2.x - 1360) < 16))) {
            weaponDelay = 60;
            playerController.useWeapon(gameState);
        }
    }
}