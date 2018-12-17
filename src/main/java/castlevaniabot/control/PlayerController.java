package castlevaniabot.control;

import castlevaniabot.BotState;
import castlevaniabot.model.gameelements.Coordinates;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TileType;

import javax.inject.Inject;

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
