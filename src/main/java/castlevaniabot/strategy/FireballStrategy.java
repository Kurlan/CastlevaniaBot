package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

public class FireballStrategy extends Strategy {
  
  private int lastX;
  private int lastY;  
  
  public FireballStrategy(final CastlevaniaBot b) {
    super(b);
  }  

  @Override public void init() {
  }

  @Override public void step() {
    
    final GameObject fireball = b.target;
    final int offsetX = (fireball.x - lastX) << 4;
    final int offsetY = (fireball.y - lastY) << 4;
    lastX = fireball.x;
    lastY = fireball.y;    
    
    if (fireball.distanceX < 24) {
      final boolean flyingHigh = fireball.y < b.playerY - 16;
      if (!b.atTopOfStairs && flyingHigh) {
        b.kneel();                          // duck under fireball        
      } else if (!flyingHigh && b.canJump) {
        b.jump();                           // jump over fireball
      }
    } else if (b.atTopOfStairs) {
      b.substage.moveAwayFromTarget();
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget() && !b.weaponing) {
        b.whip();                           // stand whip fireball
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();                         // kneel whip fireball
        }
      }
    }
  }
}