package castlevaniabot.strategy;

import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.creativeelements.RedBat;
import castlevaniabot.CastlevaniaBot;

public class RedBatStrategy extends Strategy {
   
  public RedBatStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override public void step() {
    
    final GameObject bat = b.getTargetedObject().getTarget();
    final RedBat redBat = b.getRedBat(bat);
    if (redBat == null) {
      return;
    }
    
    int t = redBat.t + 16;
    int d = 0;
    if (t >= RedBat.WAVE.length) {
      t -= RedBat.WAVE.length;
      d = 110;
    }
    final int dx = RedBat.WAVE[t][0] + d;
    final int batX = redBat.left ? (redBat.x0 - dx) : (redBat.x0 + dx);
    final int batY = redBat.y0 + RedBat.WAVE[t][1];
    final int offsetX = batX - bat.x;
    final int offsetY = batY - bat.y;
    
    if (bat.distanceX < 24) {
      final boolean flyingHigh = batY < b.playerY - 16;
      if (flyingHigh) {
        b.kneel();                           // duck under bat        
      } else if (!flyingHigh && b.canJump) {
        b.jump();                            // jump over bat
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget() && !b.weaponing) {
        b.whip();                            // stand whip bat
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();                          // kneel whip bat
        }
      }      
    } else if ((bat.left && bat.x1 > b.playerX + 24) 
        || (!bat.left && bat.x2 < b.playerX - 24)) {
      b.substage.moveTowardTarget(b.getTargetedObject().getTarget());
    }
  }
}