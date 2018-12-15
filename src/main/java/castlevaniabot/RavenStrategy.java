package castlevaniabot;

import java.util.concurrent.*;

public class RavenStrategy extends Strategy {
  
  private int jumpCounter;
  private int lastX;
  private int lastY; 
  private int moveAway;

  public RavenStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override void init() {
    moveAway = jumpCounter = 0;
  }
  
  @Override void step() {
    
    final GameObject raven = b.target;
    final int offsetX = (raven.x - lastX) << 4;
    final int offsetY = (raven.y - lastY) << 4;
    lastX = raven.x;
    lastY = raven.y;
    
    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        b.whip();
      }
    } else if (moveAway > 0) {
      --moveAway;
      b.substage.routeLeft();
    } else if (!b.onStairs && b.onPlatform && raven.active 
        && raven.y1 > b.playerY) {
      moveAway = 64 + ThreadLocalRandom.current().nextInt(11);
      b.substage.routeLeft();
    } else if (!b.weaponing && b.faceTarget()) {
      if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
        b.whip();
      } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
        b.kneel();
        if (b.kneeling) {
          b.whip();
        }
      } else if (b.canJump) {
        final int whipDelay = b.isTargetInJumpingWhipRange(offsetX, offsetY);
        if (whipDelay > 0) {
          jumpCounter = whipDelay;
          b.jump();
        }
      }
    }
  }  
}