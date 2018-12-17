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

public class PlayerController {

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
