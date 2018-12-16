package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

public class BlackBatStrategy extends Strategy {
  
  private int lastX;
  private int lastY;
  
  public BlackBatStrategy(final CastlevaniaBot b) {
    super(b);
  }  

  @Override public void init() {
  }

  @Override public void step() {
    
    final GameObject bat = b.target;
    final int offsetX = (bat.x - lastX) << 4;
    final int offsetY = (bat.y - lastY) << 4;
    lastX = bat.x;
    lastY = bat.y;
    
    if (bat.distanceX < 24) {
      final boolean flyingHigh = bat.y + offsetY < b.playerY - 16;
      if (!b.atTopOfStairs && flyingHigh) {
        b.kneel();                          // duck under bat        
      } else if (!flyingHigh && b.canJump) {
        b.jump();                           // jump over bat
      }
    } else if (b.atTopOfStairs) {
      b.substage.moveAwayFromTarget();
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget() && !b.weaponing) {
        b.whip();                         // stand whip bat
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();                         // kneel whip bat
        }
      }      
    } else if (bat.distanceX - offsetX < 24 && offsetY != 0) {
      b.substage.moveAwayFromTarget();
    } else {
      b.substage.moveTowardTarget();
    }
  }
}