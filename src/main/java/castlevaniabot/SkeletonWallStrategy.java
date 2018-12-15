package castlevaniabot;

import static castlevaniabot.GameObjectType.*;

// Used in substages 17-00 and 17-01 to kill the 3 white skeletons
public class SkeletonWallStrategy extends Strategy {
  
//  private int jumpCounter;
  private int lastX;
  private int lastY;    
  private int playerX;
  private int playerY;
  private int timeout;
  private int minY;
  
  boolean skeletonSpawned;
  boolean done;

  public SkeletonWallStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  void init(final int playerX, final int playerY) {
    init(playerX, playerY, 0);
  }
  
  void init(final int playerX, final int playerY, final int minY) {
    this.playerX = playerX;
    this.playerY = playerY;
    this.minY = minY;
    skeletonSpawned = done = false;
    timeout = 60;
  }

  @Override void step() {
    
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
        && (b.playerX != playerX || b.playerY != playerY || !b.playerLeft)) {
      b.substage.routeAndFace(playerX, playerY, true, false);
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
    } else if (skeleton.y < b.playerY 
        && b.isInStandingWhipRange(skeleton, offsetX, offsetY + 32)) {
      if (b.face(skeleton) && b.canJump) {
        b.jump();
      }
    } else if (skeleton.y < b.playerY 
        && b.isInStandingWhipRange(skeleton, offsetX + 16, offsetY + 32)) {
      if (b.canJump) {
        b.pressLeftAndJump();
      }
    }
  } 
  
  // Returns the white skeleton with the largest y-coordinate
  private GameObject findSkeleton() {
    GameObject skeleton = null;
    final GameObject[] objs = b.objs;    
    for(int i = b.objsCount - 1; i >= 0; --i) {
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