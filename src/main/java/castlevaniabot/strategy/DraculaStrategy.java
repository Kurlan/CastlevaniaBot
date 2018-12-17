package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

public class DraculaStrategy implements Strategy {

    private GameObject head;
    private GameObject lastHead;
    private GameObject fireball;
    private int jumpCounter;
    private int playerX;
    private boolean playerLeft;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public DraculaStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        lastHead = head = fireball = null;
        jumpCounter = playerX = 0;
        playerLeft = false;
    }

    @Override
    public void step() {

        updateObjects();

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                b.whip();
            }
        }

        if (fireball != null && b.canJump
                && fireball.left == (botState.getPlayerX() < fireball.x)
                && fireball.distanceX < 48) {
            playerController.jump(botState);
            jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
            return;
        }

        if (head != null) {

            if (lastHead == null) {
                // head just spawned
                if (botState.getPlayerX() < head.x) {
                    playerX = head.x - 32;
                    playerLeft = false;
                } else {
                    playerX = head.x + 32;
                    playerLeft = true;
                }
                if (playerX < 10) {
                    playerX = head.x + 32;
                    playerLeft = true;
                } else if (playerX > 238) {
                    playerX = head.x - 32;
                    playerLeft = false;
                }
            }

            if (botState.getPlayerX() != playerX || botState.isPlayerLeft() != playerLeft) {
                gameState.getCurrentSubstage().routeAndFace(playerX, 192, playerLeft, false);
            }
        }
        lastHead = head;
    }

    private void updateObjects() {
        head = fireball = null;
        final GameObject[] objs = gameState.getGameObjects();
        for (int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = objs[i];
            switch (obj.type) {
                case DRACULA_HEAD:
                    head = obj;
                    break;
                case FIREBALL:
                    fireball = obj;
                    break;
                case COOKIE_MONSTER_HEAD:
                    b.getAllStrategies().getCOOKIE_MONSTER().init();
                    botState.setCurrentStrategy(b.getAllStrategies().getCOOKIE_MONSTER());
                    break;
                case CRYSTAL_BALL:
                    b.getAllStrategies().getCOOKIE_MONSTER().done = true;
                    botState.setCurrentStrategy(b.getAllStrategies().getCOOKIE_MONSTER());
                    break;
            }
        }
    }
}