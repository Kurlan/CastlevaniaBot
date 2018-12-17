package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.DEATH;

public class HolyWaterDeathStrategy implements Strategy {

    private boolean deathSpawned;

    private int jumpCounter;
    private int jumpDelay;

    public boolean done;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public HolyWaterDeathStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        jumpDelay = jumpCounter = 0;
        done = deathSpawned = false;
    }

    @Override
    public void step() {

        if (done) {
            return;
        }

        GameObject death = null;
        final GameObject[] objs = b.gameObjects;
        for (int i = b.objsCount - 1; i >= 0; --i) {
            final GameObject obj = objs[i];
            if (obj.type == DEATH) {
                death = obj;
                break;
            }
        }
        if (death != null) {
            deathSpawned = true;
        }

        if (death == null && deathSpawned) {
            done = true;
            return;
        }

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                b.whip();
            }
        } else if (b.currentTile.getX() != 11) {
            gameState.getCurrentSubstage().route(191, 160, false);
        } else if (botState.getPlayerX() != 195) {
            playerController.goRight(botState);
        } else if (jumpDelay > 0) {
            if (b.canJump && --jumpDelay == 0) {
                jumpCounter = 4;
                b.jump();
            }
        } else if (botState.isOnPlatform()) {
            jumpDelay = 2;
            b.useWeapon();
        }
    }
}