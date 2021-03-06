package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

// Used in substages 17-00 and 17-01 to kill the 3 white skeletons
public class SkeletonWallStrategy implements Strategy {

    //  private int jumpCounter;
    private int lastX;
    private int lastY;
    private int playerX;
    private int playerY;
    private int timeout;
    private int minY;

    boolean skeletonSpawned;
    public boolean done;

    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public SkeletonWallStrategy(final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    public void init(final int playerX, final int playerY) {
        init(playerX, playerY, 0);
    }

    public void init(final int playerX, final int playerY, final int minY) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.minY = minY;
        skeletonSpawned = done = false;
        timeout = 60;
    }

    @Override
    public void init() {

    }

    @Override
    public void step() {

        if (done) {
            return;
        }

        final GameObject skeleton = findSkeleton();
        if (skeleton == null) {
            if (skeletonSpawned || --timeout == 0) {
                done = true;
            }
            return;
        } else if (!skeletonSpawned) {
            skeletonSpawned = true;
            lastX = skeleton.x;
            lastY = skeleton.y;
        }

        if ((botState.isOnStairs() || botState.isOnPlatform())
                && (botState.getPlayerX() != playerX || botState.getPlayerY() != playerY || !botState.isPlayerLeft())) {
            gameState.getCurrentSubstage().routeAndFace(playerX, playerY, true, botState, gameState);
            return;
        }

        final int offsetX = (skeleton.x - lastX) << 4;
        final int offsetY = (skeleton.y - lastY) << 4;
        lastX = skeleton.x;
        lastY = skeleton.y;

        if (playerController.isInStandingWhipRange(skeleton, offsetX, offsetY, botState)) {
            if (!gameState.isWeaponing() && playerController.face(skeleton, botState)) {
                playerController.whip(gameState);
            }
        } else if (skeleton.y < botState.getPlayerY()
                && playerController.isInStandingWhipRange(skeleton, offsetX, offsetY + 32, botState)) {
            if (playerController.face(skeleton, botState) && botState.isCanJump()) {
                playerController.jump(botState);
            }
        } else if (skeleton.y < botState.getPlayerY()
                && playerController.isInStandingWhipRange(skeleton, offsetX + 16, offsetY + 32, botState)) {
            if (botState.isCanJump()) {
                playerController.goLeftAndJump(botState);
            }
        }
    }

    // Returns the white skeleton with the largest y-coordinate
    private GameObject findSkeleton() {
        GameObject skeleton = null;
        final GameObject[] objs = gameState.getGameObjects();
        for (int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = objs[i];
            if (obj.type == WHITE_SKELETON && obj.y >= minY) {
                if (skeleton == null || obj.y > skeleton.y) {
                    skeleton = obj;
                }
            }
        }
        return skeleton;
    }
}