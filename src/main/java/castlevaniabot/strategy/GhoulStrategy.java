package castlevaniabot.strategy;

import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.CastlevaniaBot;

public class GhoulStrategy extends Strategy {

  private int lastX;
  private int lastY;  
  private boolean usedHolyWater;

  public GhoulStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override public void init() {
    usedHolyWater = false;
  }

  @Override public void step() {
    
    final GameObject ghoul = b.target;
    final int offsetX = (ghoul.x - lastX) << 4;
    final int offsetY = (ghoul.y - lastY) << 4;
    lastX = ghoul.x;
    lastY = ghoul.y;
    
    if (b.weaponing) {      
      return;
    }    
    
    if (ghoul.y < b.playerY - 16 && ghoul.y >= b.playerY - 56) {
      b.substage.moveAwayFromTarget();
    } else if (b.canJump && ghoul.distanceX < 24 && ghoul.distanceY < 8) {
      b.jump();
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        if (usedHolyWater) {
          b.whip();
        } else {
          usedHolyWater = b.grind();
        }
      }
    } else if (b.target.distanceX >= 48) {
      b.substage.moveTowardTarget();
    }
  }  
}