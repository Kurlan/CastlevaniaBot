package castlevaniabot.control;

import castlevaniabot.BotState;

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
}
