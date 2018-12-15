package castlevaniabot.strategy;

import castlevaniabot.GameObject;
import castlevaniabot.CastlevaniaBot;

import static castlevaniabot.GameObjectType.*;

public class SickleStrategy extends Strategy {
  
  public SickleStrategy(final CastlevaniaBot b) {
    super(b);
  }  

  @Override public void step() {
    
    final GameObject death = b.getType(DEATH);
    if (death != null && !b.weaponing) {
      if (b.hearts > 0 && death.y2 >= b.playerY - 32 && death.y1 <= b.playerY
          && death.distanceX < 128) {
        if (b.face(death)) {
          b.useWeapon();
        }
        return;
      } else if (b.isInStandingWhipRange(death)) {
        if (b.face(death)) {
          b.whip();
        }
        return;
      }
    }
    
    final GameObject sickle = b.target;

    if (sickle.distanceX < 32 
        && (sickle.y2 <= b.playerY - 32 || sickle.y1 >= b.playerY)) {
      moveAwayFrom(sickle);        
    } else if (b.isTargetInStandingWhipRange()) {
      if (b.faceTarget() && !b.weaponing) {
        b.whip();
      }
    } else if (b.isTargetInKneelingWhipRange()) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();
        }
      }
    } else if (!b.weaponing && b.hearts > 0 && sickle.y2 >= b.playerY - 32 
        && sickle.y1 <= b.playerY) {
      if (b.faceTarget()) {
        b.useWeapon();
      }
    } 
  }
  
  private void moveAwayFrom(final GameObject sickle) {
    switch(b.tileX) {
      case 0:
      case 1: 
      case 12:
        b.pressRight();
        break;            
      case 2:       
      case 13:
        b.substage.route(128, 160, false);
        break;
      case 4:
        b.substage.route(9, 128, false);
        break;
      case 11:
        b.substage.route(238, 128, false);
        break;
      case 3:              
      case 14:
      case 15:
        b.pressLeft();
        break;
      default:
        if (sickle.x < b.playerX) {
          b.pressRight();
        } else {
          b.pressLeft();
        }
        break;
    }
  }
}