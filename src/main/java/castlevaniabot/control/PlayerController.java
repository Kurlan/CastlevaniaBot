package castlevaniabot.control;

import castlevaniabot.BotState;
import castlevaniabot.model.gameelements.Coordinates;
import castlevaniabot.model.gameelements.MapElement;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TileType;

import javax.inject.Inject;

import static castlevaniabot.model.gameelements.TileType.BACK_PLATFORM;
import static castlevaniabot.model.gameelements.TileType.BACK_STAIRS;
import static castlevaniabot.model.gameelements.TileType.FORWARD_PLATFORM;
import static castlevaniabot.model.gameelements.TileType.FORWARD_STAIRS;
import static castlevaniabot.model.gameelements.TileType.isBack;
import static castlevaniabot.model.gameelements.TileType.isForward;
import static java.lang.Math.abs;

public class PlayerController {

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

    @Inject
    public PlayerController(GamePad gamePad) {
        this.gamePad = gamePad;
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

    public void jump(BotState botState) {
        if (botState.getJumpDelay() == 0) {
            botState.setJumpDelay(JUMP_WHIP_OFFSETS.length - 1);
            gamePad.pressA();
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
}
