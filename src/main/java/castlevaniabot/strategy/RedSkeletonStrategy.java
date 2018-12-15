package castlevaniabot.strategy;

import castlevaniabot.GameObject;
import castlevaniabot.RedBones;
import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.*;
import static java.lang.Math.*;

public class RedSkeletonStrategy extends Strategy {
  
  private int lastX;
  private int lastY;
  private int moveAway;
  private boolean usedHolyWater;  
  
  public RedSkeletonStrategy(final CastlevaniaBot b) {
    super(b);
  }  

  @Override public void init() {
    usedHolyWater = false;
    moveAway = 0;
  }

  @Override public void step() {
    
    final GameObject skeleton = b.target;
    final int offsetX = (skeleton.x - lastX) << 4;
    final int offsetY = (skeleton.y - lastY) << 4;
    lastX = skeleton.x;
    lastY = skeleton.y;
    
    if (isNotCloseToRedBones()) {
      if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
        if (!b.weaponing && b.faceTarget()) {
          if (usedHolyWater) {
            b.whip();
          } else {
            usedHolyWater = b.grind();
          }
          return;
        }
      } else if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
        if (b.faceTarget() && b.canJump) {
          b.jump();
          return;
        }
      }
    }
    
    if (moveAway > 0) {
      --moveAway;
      b.substage.moveAwayFromTarget();      
    } else if (skeleton.distanceX < 32) {
      b.substage.moveAwayFromTarget();
      moveAway = 17 + ThreadLocalRandom.current().nextInt(17);
    }   
  }
  
  // Do not whip a red skeleton when it's standing near a pile of red bones;
  // otherwise, it might end up in an infinite loop alternating between them.
  private boolean isNotCloseToRedBones() {
    final RedBones[] redBones0 = b.redBones0;
    for(int i = b.redBonesCount0 - 1; i >= 0; --i) {
      final RedBones redBones = redBones0[i];
      if (abs(b.target.x - redBones.x) < 64 
          && abs(b.target.y - redBones.y) <= 4) {
        return false;
      }
    }
    return true;
  }
}