package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

public class FishmanStrategy extends Strategy {
  
  private int lastX;
  private int lastY;
  private boolean usedHolyWater;
  
  public FishmanStrategy(final CastlevaniaBot b) {
    super(b);
  }  

  @Override public void init() {
    usedHolyWater = false;
  }

  @Override public void step() {
    
    final GameObject fishman = b.target;
    final int offsetX = (fishman.x - lastX) << 4;
    final int offsetY = (fishman.y - lastY) << 4;
    lastX = fishman.x;
    lastY = fishman.y;
    
    if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (!b.weaponing && b.faceTarget()) {
        if (usedHolyWater) {
          b.whip();
        } else {
          usedHolyWater = b.grind();
        }
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
      if (b.faceTarget() && b.canJump) {
        b.jump();
      }
    } else if (fishman.distanceX < 24) {
      b.substage.moveAwayFromTarget();
    }     
  }
}