package castlevaniabot.control;

import nintaco.api.API;

import javax.inject.Inject;

import static nintaco.api.GamepadButtons.A;
import static nintaco.api.GamepadButtons.B;
import static nintaco.api.GamepadButtons.Down;
import static nintaco.api.GamepadButtons.Left;
import static nintaco.api.GamepadButtons.Right;
import static nintaco.api.GamepadButtons.Start;
import static nintaco.api.GamepadButtons.Up;

public class GamePad {
    private final API api;

    @Inject
    public GamePad(API api) {
        this.api = api;
    }

    public void pressDown() {
        api.writeGamepad(0, Down, true);
    }

    public void pressUp() {
        api.writeGamepad(0, Up, true);
    }

    public void pressLeft() {
        api.writeGamepad(0, Left, true);
    }

    public void pressRight() {
        api.writeGamepad(0, Right, true);
    }

    public void pressA() {
        api.writeGamepad(0, A, true);
    }

    public void pressStart() {
        api.writeGamepad(0, Start, true);
    }

    public void pressB() {
        api.writeGamepad(0, B, true);
    }
}
