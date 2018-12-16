package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

public class EagleStrategy extends Strategy {
  
  private int lastX;
  private int lastY;  

  public EagleStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void step() {
    
    final GameObject eagle = b.getTargetedObject().getTarget();
    final int offsetX = (eagle.x - lastX) << 4;
    final int offsetY = (eagle.y - lastY) << 4;
    lastX = eagle.x;
    lastY = eagle.y;

    if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (!b.weaponing && b.faceFlyingTarget()) {
        b.whip();
      }
    } else if (!b.onStairs && b.isTargetInKneelingWhipRange(offsetY, offsetY)) {
      if (b.faceFlyingTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();
        }
      } 
    }  
  }  
}