package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

public class CookieMonsterStrategy implements Strategy {

    private GameObject head;
    private GameObject item;
    private int jumpCounter;
    private int playerX;
    private boolean playerLeft;

    public boolean done;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public CookieMonsterStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        item = head = null;
        jumpCounter = playerX = 0;
        done = playerLeft = false;
    }

    @Override
    public void step() {

        if (done) {
            return;
        }

        updateObjects();

        if (jumpCounter > 0) {
            if (--jumpCounter == 0) {
                playerController.whipOrWeapon(gameState, botState);
            }
        }

        if (head == null) {
            done = true;
        } else {
            if (item != null) {
                playerX = item.x;
                playerLeft = item.x < botState.getPlayerX();
            } else if (botState.getPlayerX() < head.x) {
                playerX = head.x - 48;
                playerLeft = false;
            } else {
                playerX = head.x + 48;
                playerLeft = true;
            }

            if (head.distanceX > 24) {
                if (playerX < 10) {
                    playerX = 10;
                    playerLeft = false;
                } else if (playerX > 238) {
                    playerX = 238;
                    playerLeft = true;
                }
            } else {
                if (playerX < 10) {
                    playerX = head.x + 48;
                    playerLeft = true;
                } else if (playerX > 238) {
                    playerX = head.x - 48;
                    playerLeft = false;
                }
            }

            if (botState.getPlayerX() != playerX || botState.isPlayerLeft() != playerLeft) {
                gameState.getCurrentSubstage().routeAndFace(playerX, 192, playerLeft, false);
            } else if (b.canJump) {
                playerController.jump(botState);
                jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
            }
        }
    }

    private void updateObjects() {
        item = head = null;
        final GameObject[] objs = gameState.getGameObjects();
        for (int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = objs[i];
            switch (obj.type) {
                case COOKIE_MONSTER_HEAD:
                    head = obj;
                    break;
                case DOULE_SHOT:
                case TRIPLE_SHOT:
                case LARGE_HEART:
                case PORK_CHOP:
                case HOLY_WATER_WEAPON:
                    item = obj;
                    break;
                case CRYSTAL_BALL:
                    done = true;
                    break;
            }
        }
    }
}