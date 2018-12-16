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
    
    final GameObject ghoul = b.getTargetedObject().getTarget();
    final int offsetX = (ghoul.x - lastX) << 4;
    final int offsetY = (ghoul.y - lastY) << 4;
    lastX = ghoul.x;
    lastY = ghoul.y;
    
    if (b.weaponing) {      
      return;
    }    
    
    if (ghoul.y < botState.getPlayerY() - 16 && ghoul.y >= botState.getPlayerY() - 56) {
      b.substage.moveAwayFromTarget(b.getTargetedObject().getTarget());
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
    } else if (b.getTargetedObject().getTarget().distanceX >= 48) {
      b.substage.moveTowardTarget(b.getTargetedObject().getTarget());
    }
  }  
}