package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.RED_BAT;

public class RedBatDamageBoostStrategy implements Strategy {

    private boolean batSpawned;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;

    public RedBatDamageBoostStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
    }

    @Override
    public void init() {
        batSpawned = false;
    }

    @Override
    public void step() {

        if (batSpawned) {
            if (b.playerLeft) {
                b.goRightAndJump();
                b.SUBSTAGE_0201.redBatDamageBoostDone();
            } else {
                b.goLeft();
            }
        } else {
            final GameObject bat = b.getType(RED_BAT);
            if (botState.getPlayerX() != 195 || botState.getPlayerY() != 144 || b.playerLeft) {
                if (bat != null) {
                    b.getTargetedObject().setTarget(bat);
                    b.getAllStrategies().getRED_BAT().step();
                } else if (botState.getPlayerY() != 144 || botState.getPlayerX() < 191) {
                    gameState.getCurrentSubstage().route(191, 144);
                } else if (botState.getPlayerX() < 195) {
                    b.goRight();
                }
            } else if (bat != null) {
                if (bat.left && bat.x >= botState.getPlayerX() - 16) {
                    if (bat.x < 272) {
                        batSpawned = true;
                        b.goLeft();
                    }
                } else {
                    b.getTargetedObject().setTarget(bat);
                    b.getAllStrategies().getRED_BAT().step();
                }
            }
        }
    }
}