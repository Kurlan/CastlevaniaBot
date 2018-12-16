package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

public class BoomerangDeathStrategy extends Strategy {

  public BoomerangDeathStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void step() {
    
    final GameObject death = b.getTargetedObject().getTarget();

    if (b.hearts > 0 && death.y2 >= b.playerY - 32 && death.y1 <= b.playerY
        && death.distanceX < 128) {
      if (b.faceTarget()) {
        b.useWeapon();
      }
    } else if (b.isTargetInStandingWhipRange()) {
      if (b.faceTarget()) {
        b.whip();
      }
    } else if (death.distanceX < 32) {
      moveAwayFrom(death);
    }  
  } 
  
  private void moveAwayFrom(final GameObject death) {
    switch(b.currentTile.getX()) {
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
        if (death.y < b.playerY - 16) {
          b.pressLeft();
        } else {
          b.substage.route(9, 128, false);
        }
        break;
      case 11:
        if (death.y < b.playerY - 16) {
          b.pressRight();
        } else {
          b.substage.route(238, 128, false);
        }
        break;
      case 3:              
      case 14:
      case 15:
        b.pressLeft();
        break;
      default:
        if (death.x < b.playerX) {
          b.pressRight();
        } else {
          b.pressLeft();
        }
        break;
    }
  }  
}