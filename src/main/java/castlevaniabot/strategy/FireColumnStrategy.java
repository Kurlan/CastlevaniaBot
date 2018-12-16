package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;

public class FireColumnStrategy extends Strategy {
  
  private int done;
    
  public FireColumnStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override public void init() {
    done = 0;
  }

  @Override public void step() {
    
    if (done > 0) {
      --done;
      return;
    }
    if (b.weaponing) {
      return;
    }
    
    final int targetX = b.getTargetedObject().getTarget().x + ((b.playerX < b.getTargetedObject().getTarget().x) ? -32 : 32);
    if (b.playerX == targetX) {
      if (b.getTargetedObject().getTarget().playerFacing) {
        b.whip();
        done = 64;
      } else {
        b.substage.moveAwayFromTarget(b.getTargetedObject().getTarget()); // walk past and turn around
      }
    } else {
      b.substage.route(targetX, b.getTargetedObject().getTarget().y);
    }
  }  
}