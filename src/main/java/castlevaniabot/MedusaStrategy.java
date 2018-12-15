package castlevaniabot;

import static castlevaniabot.Weapon.*;
import java.util.concurrent.ThreadLocalRandom;

public class MedusaStrategy extends Strategy {
  
  private int lastX;
  private int lastY;
  private int weaponDelay;
  private int moveAway;
  private int routeX;
  private int routeY;
  private int axeScanCounter;  

  public MedusaStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    moveAway = 0;
    weaponDelay = 16;
    routeX = 40;
    routeY = 176;
    axeScanCounter = ThreadLocalRandom.current().nextInt(61);
  }
  
  public boolean isTimeFrozen() {
    return b.weapon == STOPWATCH && weaponDelay > 0;
  }
  
  @Override
  public void step() {
    
    final GameObject medusa = b.target;
    final int offsetX = (medusa.x - lastX) << 4;
    final int offsetY = (medusa.y - lastY) << 4;
    lastX = medusa.x;
    lastY = medusa.y;
    
    if (weaponDelay > 0) {
      --weaponDelay;
    }
    
    if (b.hearts >= 5 && b.weapon == STOPWATCH) {
      stepStopwatchStrategy(medusa, offsetX, offsetY);
    } else if (b.hearts > 0) {
      switch(b.weapon) {
        case AXE:        stepAxeStrategy(medusa, offsetX, offsetY);       break;
        case BOOMERANG:  stepBoomerangStrategy(medusa, offsetX, offsetY); break;
        case DAGGER:     stepDaggerStrategy(medusa, offsetX, offsetY);    break;
        case HOLY_WATER: stepHolyWaterStrategy(medusa, offsetX, offsetY); break;
        default:         stepNoWeaponsStrategy(medusa, offsetX, offsetY); break;
      }      
    } else {
      stepNoWeaponsStrategy(medusa, offsetX, offsetY);
    }    
  }

  private void stepNoWeaponsStrategy(final GameObject medusa, final int offsetX, 
      final int offsetY) {
    
    if (medusa.distanceX > 64 || b.playerX <= 25 || b.playerX >= 727) {
      moveAway = 0;
    }
    
    if (moveAway > 0) {
      --moveAway;
      b.substage.moveAwayFromTarget();    
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();
        }
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget() && !b.weaponing) {
        b.whip();
      }
    } else if (medusa.distanceX <= 40) {
      moveAway = 90 + ThreadLocalRandom.current().nextInt(61);
      b.substage.moveAwayFromTarget();
    } else {
      b.substage.moveTowardTarget();
    }
  }
  
  private void stepAxeStrategy(final GameObject medusa, final int offsetX, 
      final int offsetY) {
    
    if (axeScanCounter > 0) {
      --axeScanCounter;
    } else {
      axeScanCounter = ThreadLocalRandom.current().nextInt(61);
      for(int platformX = 1; platformX <= 15; ++platformX) {
        final int platformY = (platformX <= 13) ? 11 : 10;
        if (b.canHitTargetWithAxe(platformX, platformY)) {
          routeY = platformY << 4;
          routeX = platformX << 4;          
          switch(platformX) {
            case  1: routeX += 10; break;
            case 15: routeX +=  6; break;
            default: routeX +=  8; break;
          }
          break;
        }
      }
    }    

    if (b.playerX == routeX) {
      if (medusa.playerFacing) {
        if (weaponDelay == 0) {
          b.useWeapon();
          weaponDelay = 17;
        }
      } else {
        b.substage.moveAwayFromTarget();
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget() && !b.weaponing) {
        b.whip();
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();
        }
      }      
    } else {
      b.substage.route(routeX, routeY);
    }
  }
  
  private void stepBoomerangStrategy(final GameObject medusa, final int offsetX, 
      final int offsetY) {
    
    if (medusa.distanceX > 64 || b.playerX <= 25 || b.playerX >= 727) {
      moveAway = 0;
    }
    
    if (moveAway > 0) {
      --moveAway;
      b.substage.moveAwayFromTarget();  
    } else if (b.playerY - 24 >= medusa.y1 && b.playerY - 24 <= medusa.y2) {
      if (b.faceTarget() && !b.weaponing) {
        b.useWeapon();
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget() && !b.weaponing) {
        b.whip();
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();
        }
      }      
    } else if (medusa.distanceX <= 40) {
      moveAway = 90 + ThreadLocalRandom.current().nextInt(61);
      b.substage.moveAwayFromTarget();
    } else {
      b.substage.moveTowardTarget();
    }    
  }
  
  private void stepDaggerStrategy(final GameObject medusa, final int offsetX, 
      final int offsetY) {
    
    if (medusa.distanceX > 64 || b.playerX <= 25 || b.playerX >= 727) {
      moveAway = 0;
    }
    
    if (moveAway > 0) {
      --moveAway;
      b.substage.moveAwayFromTarget();  
    } else if (b.playerY - 24 >= medusa.y1 && b.playerY - 24 <= medusa.y2) {
      if (b.faceTarget() && !b.weaponing) {
        b.useWeapon();
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget() && !b.weaponing) {
        b.whip();
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();
        }
      }      
    } else if (medusa.distanceX <= 40) {
      moveAway = 90 + ThreadLocalRandom.current().nextInt(61);
      b.substage.moveAwayFromTarget();
    } else {
      b.substage.moveTowardTarget();
    }    
  }
  
  private void stepHolyWaterStrategy(final GameObject medusa, final int offsetX, 
      final int offsetY) {
    
    if (medusa.distanceX > 64 || b.playerX <= 25 || b.playerX >= 727) {
      moveAway = 0;
    }
    
    if (moveAway > 0) {
      --moveAway;
      b.substage.moveAwayFromTarget();    
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget() && !b.weaponing) {
        b.whipOrWeapon();
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();
        }
      }      
    } else if (medusa.distanceX <= 40) {
      moveAway = 90 + ThreadLocalRandom.current().nextInt(61);
      b.substage.moveAwayFromTarget();
    } else {
      b.substage.moveTowardTarget();
    }    
  }
  
  private void stepStopwatchStrategy(final GameObject medusa, final int offsetX, 
      final int offsetY) {
    
    if (medusa.distanceX > 64 || b.playerX <= 25 || b.playerX >= 727) {
      moveAway = 0;
    }
    
    if (moveAway > 0) {
      --moveAway;
      b.substage.moveAwayFromTarget();    
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget() && !b.weaponing) {
        if (weaponDelay == 0) {
          weaponDelay = 180;
          b.useWeapon();
        } else {
          b.whip();
        }
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        if (weaponDelay == 0) {
          weaponDelay = 180;
          b.useWeapon();
        } else {
          b.kneel();
          if (b.kneeling && !b.weaponing) {
            b.whip();
          }
        }
      }      
    } else if (medusa.distanceX <= 40) {
      moveAway = 90 + ThreadLocalRandom.current().nextInt(61);
      b.substage.moveAwayFromTarget();
    } else {
      b.substage.moveTowardTarget();
    }    
  }
}