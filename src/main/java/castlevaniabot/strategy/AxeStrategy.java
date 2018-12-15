package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;

public class AxeStrategy extends Strategy {
  
  private int lastX;
  private int lastY;  

  public AxeStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void step() {
    
    final GameObject axe = b.target;
    final int offsetX = (axe.x - lastX) << 4;
    final int offsetY = (axe.y - lastY) << 4;
    lastX = axe.x;
    lastY = axe.y;    
    
    if (axe.distanceX < 24) {
      final boolean flyingHigh = axe.y + offsetY < b.playerY - 16;
      if (!b.atTopOfStairs && flyingHigh) {
        b.kneel();
      } else if (!flyingHigh && b.canJump) {
        b.jump();
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (!b.weaponing && b.faceTarget()) {
        b.whip();
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      b.kneel();
      if (b.kneeling && !b.weaponing && b.faceTarget()) {
        b.whip();
      }
    }
  }  
}