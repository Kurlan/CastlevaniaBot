package castlevaniabot.control;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.model.creativeelements.MovingPlatform;
import castlevaniabot.model.creativeelements.RedBones;
import castlevaniabot.model.gameelements.Coordinates;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.GameObjectType;
import castlevaniabot.model.gameelements.MapElement;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.model.gameelements.TileType;
import nintaco.api.API;
import nintaco.api.Colors;

import javax.inject.Inject;

import static castlevaniabot.model.creativeelements.Operations.GO_DOWN_STAIRS;
import static castlevaniabot.model.creativeelements.Operations.GO_UP_STAIRS;
import static castlevaniabot.model.creativeelements.Operations.WALK_CENTER_LEFT_JUMP;
import static castlevaniabot.model.creativeelements.Operations.WALK_CENTER_RIGHT_JUMP;
import static castlevaniabot.model.creativeelements.Operations.WALK_LEFT;
import static castlevaniabot.model.creativeelements.Operations.WALK_LEFT_EDGE_LEFT_JUMP;
import static castlevaniabot.model.creativeelements.Operations.WALK_LEFT_EDGE_RIGHT_JUMP;
import static castlevaniabot.model.creativeelements.Operations.WALK_LEFT_MIDDLE_LEFT_JUMP;
import static castlevaniabot.model.creativeelements.Operations.WALK_LEFT_MIDDLE_RIGHT_JUMP;
import static castlevaniabot.model.creativeelements.Operations.WALK_RIGHT;
import static castlevaniabot.model.creativeelements.Operations.WALK_RIGHT_EDGE_LEFT_JUMP;
import static castlevaniabot.model.creativeelements.Operations.WALK_RIGHT_EDGE_RIGHT_JUMP;
import static castlevaniabot.model.creativeelements.Operations.WALK_RIGHT_MIDDLE_LEFT_JUMP;
import static castlevaniabot.model.creativeelements.Operations.WALK_RIGHT_MIDDLE_RIGHT_JUMP;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Whip.WHIPS;
import static castlevaniabot.model.gameelements.TileType.BACK_PLATFORM;
import static castlevaniabot.model.gameelements.TileType.BACK_STAIRS;
import static castlevaniabot.model.gameelements.TileType.FORWARD_PLATFORM;
import static castlevaniabot.model.gameelements.TileType.FORWARD_STAIRS;
import static castlevaniabot.model.gameelements.TileType.isBack;
import static castlevaniabot.model.gameelements.TileType.isForward;
import static castlevaniabot.model.gameelements.TileType.isStairsPlatform;
import static java.lang.Math.abs;
import static nintaco.api.GamepadButtons.Left;
import static nintaco.api.GamepadButtons.Right;

public class PlayerController {

    public static final int WEAPON_DELAY = 16;

    // Parabolic jump path
    public static final int[] ABSOLUTE_JUMP_YS = {
            0,  5, 10, 13, 16, 19, 22, 25, 27, 29, 31, 32, 33, 34, 35, 35,
            36, 36, 36, 36, 36, 36, 36, 36, 36,
            35, 35, 34, 33, 32, 31, 29, 27, 25, 22, 19, 16, 13, 10,  5,  0, };

    // Parabolic jump velocities
    public static final int[] DELTA_JUMP_YS = new int[ABSOLUTE_JUMP_YS.length - 1];
    static {
        for(int i = DELTA_JUMP_YS.length - 1; i >= 0; --i) {
            DELTA_JUMP_YS[i] = ABSOLUTE_JUMP_YS[i + 1] - ABSOLUTE_JUMP_YS[i];
        }
    }

    // The change in height during a jump after a whip delay of 16 frames.
    private static final int[] JUMP_WHIP_OFFSETS = new int[DELTA_JUMP_YS.length];
    static {
        for(int i = JUMP_WHIP_OFFSETS.length - 1; i >= 0; --i) {
            int sum = 0;
            for(int j = 15; j >= 0; --j) {
                final int index = i - j;
                if (index > 0) {
                    sum += DELTA_JUMP_YS[i];
                } else {
                    sum += 8;
                }
            }
            JUMP_WHIP_OFFSETS[i] = sum;
        }
    }

    // For a given height, this is how long to delay whipping after a jump.
    public static final int[] JUMP_WHIP_DELAYS = new int[37];
    static {
        for(int i = 0; i <= 36; ++i) {
            int index = -1;
            for(int j = 16; j < ABSOLUTE_JUMP_YS.length; ++j) {
                if (ABSOLUTE_JUMP_YS[j] == i) {
                    index = j;
                    break;
                } else if (ABSOLUTE_JUMP_YS[j] < i) {
                    if (abs(ABSOLUTE_JUMP_YS[j - 1] - i) < abs(ABSOLUTE_JUMP_YS[j] - i)) {
                        index = j - 1;
                    } else {
                        index = j;
                    }
                    break;
                }
            }
            JUMP_WHIP_DELAYS[i] = index - 16;
        }
        JUMP_WHIP_DELAYS[36] = 7;
    }

    private final GamePad gamePad;
    private final API api;

    @Inject
    public PlayerController(GamePad gamePad, API api) {
        this.gamePad = gamePad;
        this.api = api;
    }

    public void executeOperation(final MapElement[][] map, final int width,
                                 final int operation, final int stepX, final int stepY,
                                 final boolean checkForEnemies, BotState botState, GameState gameState, Coordinates currentTile) {

        switch(operation) {

            case WALK_LEFT:
                walk(Left, stepX, stepY, checkForEnemies, botState, gameState, currentTile);
                break;
            case WALK_RIGHT:
                walk(Right, stepX, stepY, checkForEnemies, botState, gameState, currentTile);
                break;

            case WALK_CENTER_LEFT_JUMP:
                walkAndJump(map, width, 8, Left, stepX, stepY, checkForEnemies, botState, currentTile, gameState);
                break;
            case WALK_RIGHT_MIDDLE_LEFT_JUMP:
                walkAndJump(map, width, 13, Left, stepX, stepY, checkForEnemies, botState, currentTile, gameState);
                break;
            case WALK_LEFT_MIDDLE_LEFT_JUMP:
                walkAndJump(map, width, 2, Left, stepX, stepY, checkForEnemies, botState, currentTile, gameState);
                break;
            case WALK_RIGHT_EDGE_LEFT_JUMP:
                walkAndJump(map, width, 19, Left, stepX, stepY, checkForEnemies, botState, currentTile, gameState);
                break;
            case WALK_LEFT_EDGE_LEFT_JUMP:
                walkAndJump(map, width, -4, Left, stepX, stepY, checkForEnemies, botState, currentTile, gameState);
                break;

            case WALK_CENTER_RIGHT_JUMP:
                walkAndJump(map, width, 8, Right, stepX, stepY, checkForEnemies, botState, currentTile, gameState);
                break;
            case WALK_RIGHT_MIDDLE_RIGHT_JUMP:
                walkAndJump(map, width, 13, Right, stepX, stepY, checkForEnemies, botState, currentTile, gameState);
                break;
            case WALK_LEFT_MIDDLE_RIGHT_JUMP:
                walkAndJump(map, width, 2, Right, stepX, stepY, checkForEnemies, botState, currentTile, gameState);
                break;
            case WALK_RIGHT_EDGE_RIGHT_JUMP:
                walkAndJump(map, width, 19, Right, stepX, stepY, checkForEnemies, botState, currentTile, gameState);
                break;
            case WALK_LEFT_EDGE_RIGHT_JUMP:
                walkAndJump(map, width, -4, Right, stepX, stepY, checkForEnemies, botState, currentTile, gameState);
                break;

            case GO_UP_STAIRS:
                goUpStairs(map, width, botState, currentTile);
                break;
            case GO_DOWN_STAIRS:
                goDownStairs(map, width, botState, currentTile);
                break;
        }
    }

    public void goLeft(BotState botState) {
        if (botState.getPlayerX() < botState.getAvoidX() || botState.getPlayerX() >= botState.getAvoidX() + 16) {
            gamePad.pressLeft();
        }
    }

    public void goRight(BotState botState) {
        if (botState.getPlayerX() <= botState.getAvoidX() - 16 || botState.getPlayerX() > botState.getAvoidX()) {
            gamePad.pressRight();
        }
    }

    public void goRightAndJump(BotState botState) {
        if (botState.getJumpDelay() == 0 && (botState.getPlayerX() <= botState.getAvoidX() - 58 || botState.getPlayerX() > botState.getAvoidX())) {
            botState.setJumpDelay(2); // Low number enables jumps against walls.
            gamePad.pressRight();
            gamePad.pressA();
        }
    }

    public void goLeftAndJump(BotState botState) {
        if (botState.getJumpDelay() == 0 && (botState.getPlayerX() < botState.getAvoidX() || botState.getPlayerX() >= botState.getAvoidX() + 58)) {
            botState.setJumpDelay(2); // Low number enables jumps against walls.
            gamePad.pressLeft();
            gamePad.pressA();
        }
    }

    public void goUpStairs(final MapElement[][] map, final int width, BotState botState, Coordinates currentTile) {
        if (botState.isOnStairs()) {
            gamePad.pressUp();
        } else if (botState.isOverHangingLeft()) {
            goRight(botState);
        } else if (botState.isOverHangingRight()) {
            goLeft(botState);
        } else if (botState.isOnPlatform()) {
            final int x = botState.getPlayerX() & 0x0F;
            final int tileType = map[currentTile.getY() - 1][currentTile.getX()].tileType;
            if (tileType == BACK_STAIRS || (currentTile.getX() < width - 1
                    && map[currentTile.getY() - 1][currentTile.getX() + 1].tileType == FORWARD_STAIRS)) {
                if (x < 15) {
                    goRight(botState);
                } else {
                    gamePad.pressUp();
                }
            } else if (tileType == FORWARD_STAIRS || (currentTile.getX() > 0
                    && map[currentTile.getY() - 1][currentTile.getX() - 1].tileType == BACK_STAIRS)) {
                if (x > 0) {
                    goLeft(botState);
                } else {
                    gamePad.pressUp();
                }
            }
        }
    }

    public void kneel() {
        gamePad.pressDown();
    }

    public void useWeapon(GameState gameState) {
        if (!gameState.isWeaponing()) {
            gameState.setWeaponDelay(WEAPON_DELAY);
            gamePad.pressUp();
            gamePad.pressB();
        }
    }

    public void whip(GameState gameState) {
        if (!gameState.isWeaponing()) {
            gameState.setWeaponDelay(WEAPON_DELAY);
            gamePad.pressB();
        }
    }

    public void whipOrWeapon(GameState gameState, BotState botState) {
        if (!gameState.isWeaponing()) {
            gameState.setWeaponDelay(WEAPON_DELAY);
            if (!botState.isAtBottomOfStairs()) {
                gamePad.pressUp();
            }
            gamePad.pressB();
        }
    }

    public void jump(BotState botState) {
        if (botState.getJumpDelay() == 0) {
            botState.setJumpDelay(JUMP_WHIP_OFFSETS.length - 1);
            gamePad.pressA();
        }
    }

    // Use holy water if possible to grind for double and triple shots, else whip.
    public boolean grind(GameState gameState, BotState botState) {
        if (!gameState.isWeaponing()) {
            gameState.setWeaponDelay(WEAPON_DELAY);
            if (!botState.isAtBottomOfStairs() && botState.getWeapon() == HOLY_WATER && botState.getHearts() > 5 && botState.getShot() < 3) {
                gamePad.pressUp();
                gamePad.pressB();
                return true;
            } else {
                gamePad.pressB();
                return false;
            }
        } else {
            return false;
        }
    }

    public void goDownStairs(final MapElement[][] map, final int width, BotState botState, Coordinates currentTile) {
        if (botState.isOnStairs()) {
            gamePad.pressDown();
        } else if (botState.isOnPlatform()) {
            final int x = botState.getPlayerX() & 0x0F;
            final int tileType = map[currentTile.getY()][currentTile.getX()].tileType;
            if (tileType == FORWARD_PLATFORM || (currentTile.getX() < width - 1
                    && isBack(map[currentTile.getY()][currentTile.getX() + 1].tileType))) {
                if (x < 15) {
                    goRight(botState);
                } else {
                    gamePad.pressDown();
                }
            } else if (tileType == BACK_PLATFORM || (currentTile.getX() > 0
                    && isForward(map[currentTile.getY()][currentTile.getX() - 1].tileType))) {
                if (x > 0) {
                    goLeft(botState);
                } else {
                    gamePad.pressDown();
                }
            }
        }
    }

    public void goAndJump(final int direction, BotState botState) {
        if (direction == Left) {
            goLeftAndJump(botState);
        } else {
            goRightAndJump(botState);
        }
    }

    public void go(final int direction, BotState botState) {
        if (direction == Left) {
            goLeft(botState);
        } else {
            goRight(botState);
        }
    }

    public void walk(final int direction, final int stepX, final int stepY,
                      final boolean checkForEnemies, BotState botState, GameState gameState, Coordinates currentTile) {
        if (botState.isOnStairs()) {
            gamePad.pressUp();
        } else if (checkForEnemies && stepY > currentTile.getY()) {
            final int x = botState.getPlayerX() & 0xF;
            if (botState.isOverHangingLeft() && direction == Left && x < 13) {
                if (!isEnemyInBounds((stepX << 4) - 24, botState.getPlayerY() - 32, botState.getPlayerX() + 24,
                        stepY << 4, gameState)) {
                    goLeft(botState);
                }
            } else if (botState.isOverHangingRight() && direction == Right && x > 2) {
                if (!isEnemyInBounds(botState.getPlayerX() - 24, botState.getPlayerY() - 32, (stepX << 4) + 40,
                        stepY << 4, gameState)) {
                    goRight(botState);
                }
            } else {
                go(direction, botState);
            }
        } else {
            go(direction, botState);
        }
    }

    public boolean isEnemyInBounds(final int x1, final int y1, final int x2,
                                   final int y2, GameState gameState) {

        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            final GameObjectType type = obj.type;
            if (type.enemy && obj.x1 <= x2 && obj.x2 >= x1 && obj.y2 >= y1
                    && obj.y1 <= y2) {
                return true;
            }
        }

        return false;
    }

    public void walkAndJump(final MapElement[][] map, final int width,
                             int offsetX, final int direction, final int stepX, final int stepY,
                             final boolean checkForEnemies, BotState botState, Coordinates currentTile, GameState gameState) {

        switch (offsetX) {
            case 8:
                if (currentTile.getX() == 0 || map[currentTile.getY() - 1][currentTile.getX() - 1].height == 0
                        || map[currentTile.getY() - 2][currentTile.getX() - 1].height == 0) {
                    offsetX = 10;
                } else if (currentTile.getX() == width - 1 || map[currentTile.getY() - 1][currentTile.getX() + 1].height == 0
                        || map[currentTile.getY() - 2][currentTile.getX() + 1].height == 0) {
                    offsetX = 6;
                } break;
            case 19:
                if (direction == Left) {
                    offsetX = 18;
                }
                break;
            case -4:
                if (direction == Right) {
                    offsetX = -3;
                }
                break;
        }

        final int x = botState.getPlayerX() - (currentTile.getX() << 4);
        if (x == offsetX) {
            if (botState.isPlayerLeft() ^ (direction == Right)) {
                if (botState.getJumpDelay() == 0) {
                    if (checkForEnemies) {
                        if (direction == Left) {
                            if (!isEnemyInBounds((stepX << 4) - 48, botState.getPlayerY() - 64,
                                    botState.getPlayerX() + 24, stepY << 4, gameState)) {
                                goLeftAndJump(botState);
                            }
                        } else {
                            if (!isEnemyInBounds(botState.getPlayerX() - 24, botState.getPlayerY() - 64,
                                    (stepX << 4) + 64, stepY << 4, gameState)) {
                                goRightAndJump(botState);
                            }
                        }
                    } else {
                        goAndJump(direction, botState);
                    }
                }
            } else if (direction == Left) {
                goRight(botState);                   // walk past and turn around
            } else {
                goLeft(botState);                  // walk past and turn around
            }
        } else if (x > offsetX) {
            goLeft(botState);
        } else {
            goRight(botState);;
        }
    }


    public boolean face(final GameObject obj, BotState botState) {
        if (obj.playerFacing) {
            return true;
        } else if (botState.isOnStairs() && botState.getPlayerY() >= 56 && botState.getPlayerY() <= 200) {
            gamePad.pressDown();
        } else if (botState.getPlayerX() < obj.x) {
            goRight(botState);
        } else {
            goLeft(botState);
        }
        return false;
    }


    public boolean faceTarget(BotState botState, GameState gameState) {
        if (botState.getTargetedObject().getTarget().playerFacing) {
            return true;
        } else if (botState.isOnStairs() && botState.getPlayerY() >= 56 && botState.getPlayerY() <= 200) {
            gamePad.pressDown();
        } else {
            gameState.getCurrentSubstage().moveTowardTarget(botState.getTargetedObject().getTarget());
        }
        return false;
    }

    public boolean faceFlyingTarget(BotState botState) {
        if (botState.getTargetedObject().getTarget().playerFacing) {
            return true;
        } else if (botState.isOnStairs() && botState.getPlayerY() >= 56 && botState.getPlayerY() <= 200) {
            gamePad.pressDown();
        } else if (botState.getPlayerX() < botState.getTargetedObject().getTarget().x) {
            goRight(botState);
        } else {
            goLeft(botState);
        }
        return false;
    }

    public boolean isAtBottomOfStairs(BotState botState, GameState gameState, Coordinates currentTile) {

        if (botState.isOnStairs() || !botState.isOnPlatform()) {
            return false;
        }

        final MapElement[][] map = gameState.getCurrentSubstage().mapRoutes.map;
        final int tileType = map[currentTile.getY() - 1][currentTile.getX()].tileType;
        return (tileType == BACK_STAIRS || (currentTile.getX() < gameState.getCurrentSubstage().mapRoutes.width - 1
                && map[currentTile.getY() - 1][currentTile.getX() + 1].tileType == FORWARD_STAIRS))
                || (tileType == FORWARD_STAIRS || (currentTile.getX() > 0
                && map[currentTile.getY() - 1][currentTile.getX() - 1].tileType == BACK_STAIRS));
    }

    public boolean isAtTopOfStairs(BotState botState,GameState gameState, Coordinates currentTile) {

        if (botState.isOnStairs() || !botState.isOnPlatform()) {
            return false;
        }

        final MapElement[][] map = gameState.getCurrentSubstage().mapRoutes.map;
        final int tileType = map[currentTile.getY()][currentTile.getX()].tileType;
        return isStairsPlatform(tileType) || (currentTile.getX() < gameState.getCurrentSubstage().mapRoutes.width - 1
                && isBack(map[currentTile.getY()][currentTile.getX() + 1].tileType))
                || (currentTile.getX() > 0 && isForward(map[currentTile.getY()][currentTile.getX() - 1].tileType));
    }

    public boolean isOnOrInPlatform(final MapRoutes mapRoutes, int x, int y, Coordinates currentTile) {
        if (x < 0 || y < 0) {
            return false;
        }
        y >>= 4;
        if (y >= 0x0F) {
            return false;
        }
        x >>= 4;
        if (x >= mapRoutes.width) {
            return false;
        }
        return mapRoutes.getDistance(x, y, currentTile) < 32;
    }

    // (x, y) are absolute coordinates, not currentTile coordinates
    public boolean isOnPlatform(int x, int y, MapRoutes mapRoutes) {
        if (x < 0 || y < 0 || (y & 0x0E) != 0) {
            return false;
        }
        y >>= 4;
        if (y >= 0x0F) {
            return false;
        }
        x >>= 4;
        if (x >= mapRoutes.width) {
            return false;
        }
        return TileType.isPlatform(mapRoutes.map[y][x].tileType);
    }

    // Unused methods from original.

    private boolean isTypeInRange(final GameObjectType type, final int x1, final int x2, GameState gameState) {

        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            if (obj.type == type && obj.x1 <= x2 && obj.x2 >= x1) {
                return true;
            }
        }

        return false;
    }

    private int countTypeInBounds(final GameObjectType type, final int x1, final int y1,
                          final int x2, final int y2, GameState gameState) {

        int count = 0;
        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            if (obj.type == type && obj.x1 <= x2 && obj.x2 >= x1 && obj.y2 >= y1
                    && obj.y1 <= y2) {
                ++count;
            }
        }
        return count;
    }

    private boolean isObjectInBounds(final GameObject obj, final int x1, final int y1,
                             final int x2, final int y2) {
        return obj.x1 <= x2 && obj.x2 >= x1 && obj.y2 >= y1 && obj.y1 <= y2;
    }


    private boolean faceFlying(final GameObject obj, BotState botState, GameState gameState) {
        if (obj.playerFacing) {
            return true;
        } else if (botState.isOnStairs() && botState.getPlayerY() >= 56 && botState.getPlayerY() <= 200) {
            gamePad.pressDown();
        } else {
            gameState.getCurrentSubstage().moveToward(obj);
        }
        return false;
    }

    boolean isPlayerInRange(final int x1, final int x2, BotState botState) {
        return botState.getPlayerX() >= x1 && botState.getPlayerX() <= x2;
    }

    private void printGameObject(final GameObjectType type, GameState gameState) {
        for(int i = 0; i < gameState.getObjsCount(); ++i) {
            if (gameState.getGameObjects()[i].type == type) {
                System.out.print(gameState.getGameObjects()[i] + " ");
            }
        }
        System.out.println();
    }

    private void printGameObjects(GameState gameState, TargetedObject targetedObject, RedBones[] redBones0, int redBonesCount0) {
        for(int i = redBonesCount0 - 1; i >= 0; --i) {
            System.out.print(redBones0[i] + " ");
        }
        for(int i = 0; i < gameState.getObjsCount(); ++i) {
            System.out.print(gameState.getGameObjects()[i] + " ");
        }
        if (targetedObject.getTarget() != null) {
            System.out.format("* %s", targetedObject.getTarget());
        }
        System.out.println();
    }

    private void paintGameObjects(GameState gameState, RedBones[] redBones0, int redBonesCount0, TargetedObject targetedObject) {
        api.setColor(Colors.YELLOW);
        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            api.drawRect(obj.x1 - gameState.getCameraX(), obj.y1, obj.x2 - obj.x1 + 1,
                    obj.y2 - obj.y1 + 1);
        }
        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            api.drawRect(obj.x1 - gameState.getCameraX(), obj.y1, obj.x2 - obj.x1 + 1,
                    obj.y2 - obj.y1 + 1);
        }
        api.setColor(Colors.CYAN);
        for(int i = redBonesCount0 - 1; i >= 0; --i) {
            final RedBones bones = redBones0[i];
            api.drawRect(bones.x - 8 - gameState.getCameraX(), bones.y - 16, 16, 16);
        }
        if (targetedObject.getTarget() != null) {
            api.setColor(Colors.RED);
            api.drawRect(targetedObject.getTarget().x1 - gameState.getCameraX(), targetedObject.getTarget().y1, targetedObject.getTarget().x2 - targetedObject.getTarget().x1 + 1,
                    targetedObject.getTarget().y2 - targetedObject.getTarget().y1 + 1);
        }
        api.setColor(Colors.GREEN);

        for(int i = gameState.getMovingPlatformsCount() - 1; i >= 0; --i) {
            final MovingPlatform p = gameState.getMovingPlatforms()[i];
            api.drawRect(p.x1 - gameState.getCameraX(), p.y, p.x2 - p.x1 + 1, 8);
        }
    }

    public boolean isTargetInStandingWhipRange(final int xOffset, final int yOffset, BotState botState) {
        return WHIPS[botState.getWhipLength()][0].inRange(botState.getTargetedObject().getTarget(), xOffset, yOffset, botState);
    }


    public int getWhipRadius(BotState botState) {
        return WHIPS[botState.getWhipLength()][0].getRadius();
    }

    boolean isObjectAbove(final int y, GameState gameState) {

        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            if (obj.y1 <= y) {
                return true;
            }
        }

        return false;
    }

    boolean isEnemyBelow(final int y, GameState gameState) {

        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            if (obj.type.enemy && obj.y2 >= y) {
                return true;
            }
        }

        return false;
    }

    boolean isEnemyAbove(final int y, GameState gameState) {

        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            if (obj.type.enemy && obj.y1 <= y) {
                return true;
            }
        }

        return false;
    }

    boolean isTypeAbove(final GameObjectType type, final int y, GameState gameState) {

        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            if (obj.type == type && obj.y1 <= y) {
                return true;
            }
        }

        return false;
    }

    boolean isTypeBelow(final GameObjectType type, final int y, GameState gameState) {

        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            if (obj.type == type && obj.y2 >= y) {
                return true;
            }
        }

        return false;
    }

    boolean isTypeLeft(final GameObjectType type, final int x, GameState gameState) {

        for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = gameState.getGameObjects()[i];
            if (obj.type == type && obj.x1 <= x) {
                return true;
            }
        }
        return false;
    }

/*
    // Returns the whip delay after jumping or -1 if not in range.
    int isTargetInJumpingWhipRange() {
        for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {
            if (WHIPS[botState.getWhipLength()][0].inRange(targetedObject.getTarget(), 0,
                    WHIP_HEIGHT_AND_DELAY[i][0], botState)) {
                return WHIP_HEIGHT_AND_DELAY[i][1];
            }
        }
        return -1;
    }

    // Returns the whip delay after jumping or -1 if not in range.
    int isInJumpingWhipRange(final GameObject obj, final int xOffset,
        final int yOffset) {
        for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {
          if (WHIPS[botState.getWhipLength()][0].inRange(obj, xOffset,
              yOffset + WHIP_HEIGHT_AND_DELAY[i][0], botState)) {
            return WHIP_HEIGHT_AND_DELAY[i][1];
          }
        }
        return -1;
      }


  // Returns the whip delay after jumping or -1 if not in range.
  int isInJumpingWhipRange(final GameObject obj) {
    for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {
      if (WHIPS[botState.getWhipLength()][0].inRange(obj, 0,
          WHIP_HEIGHT_AND_DELAY[i][0], botState)) {
        return WHIP_HEIGHT_AND_DELAY[i][1];
      }
    }
    return -1;
  }
*/
}
