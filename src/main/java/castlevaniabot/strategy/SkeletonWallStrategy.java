package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
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

    private final CastlevaniaBot b;
    private final BotState botState;

    public SkeletonWallStrategy(final CastlevaniaBot b, final BotState botState) {
        this.b = b;
        this.botState = botState;
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

        if ((b.onStairs || b.onPlatform)
                && (botState.getPlayerX() != playerX || botState.getPlayerY() != playerY || !b.playerLeft)) {
            b.substage.routeAndFace(playerX, playerY, true);
            return;
        }

        final int offsetX = (skeleton.x - lastX) << 4;
        final int offsetY = (skeleton.y - lastY) << 4;
        lastX = skeleton.x;
        lastY = skeleton.y;

        if (b.isInStandingWhipRange(skeleton, offsetX, offsetY)) {
            if (!b.weaponing && b.face(skeleton)) {
                b.whip();
            }
        } else if (skeleton.y < botState.getPlayerY()
                && b.isInStandingWhipRange(skeleton, offsetX, offsetY + 32)) {
            if (b.face(skeleton) && b.canJump) {
                b.jump();
            }
        } else if (skeleton.y < botState.getPlayerY()
                && b.isInStandingWhipRange(skeleton, offsetX + 16, offsetY + 32)) {
            if (b.canJump) {
                b.pressLeftAndJump();
            }
        }
    }

    // Returns the white skeleton with the largest y-coordinate
    private GameObject findSkeleton() {
        GameObject skeleton = null;
        final GameObject[] objs = b.gameObjects;
        for (int i = b.objsCount - 1; i >= 0; --i) {
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