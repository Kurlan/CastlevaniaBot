package castlevaniabot.strategy;

import castlevaniabot.GameObject;
import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.*;

public class GhostStrategy extends Strategy {

  private int lastX;
  private int lastY;
  private int moveAwayCounter;
  
  public GhostStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override
  public void init() {
    moveAwayCounter = 0;
  }

  @Override
  public void step() {
    
    final GameObject ghost = b.target;
    final int offsetX = (b.target.x - lastX) << 4;
    final int offsetY = (b.target.y - lastY) << 4;
    lastX = b.target.x;
    lastY = b.target.y;
    
    if (b.weaponing) {
      return;
    }
    
    if (ghost.distanceX >= 48) {
      moveAwayCounter = 0;
    }
        
    if (moveAwayCounter > 0) {
      --moveAwayCounter;
      b.substage.moveAwayFromTarget();
    } else if (ghost.y2 < b.playerY - 48 || ghost.y1 > b.playerY + 16 
        || ghost.distanceX > 48) {
      b.substage.moveTowardTarget();
    } else if (ghost.distanceX < 20) {
      moveAwayCounter = 180 + ThreadLocalRandom.current().nextInt(11);;
      b.substage.moveAwayFromTarget();
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.whip();                            // stand whip bat
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling) {
          b.whip();                          // kneel whip bat
        }
      }      
    }
  }  
}