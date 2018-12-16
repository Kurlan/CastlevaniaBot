package castlevaniabot.strategy;

import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.CastlevaniaBot;

public class PantherStrategy extends Strategy {

  private int lastX;
  private int lastY;  

  public PantherStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override public void step() {
    
    final GameObject panther = b.getTargetedObject().getTarget();
    final int offsetX = (panther.x - lastX) << 4;
    final int offsetY = (panther.y - lastY) << 4;
    lastX = panther.x;
    lastY = panther.y;    
    
    if (!b.weaponing && b.faceTarget() 
        && b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      b.whip();
    }
  }  
}