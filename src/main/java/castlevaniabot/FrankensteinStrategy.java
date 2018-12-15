package castlevaniabot;

import java.util.concurrent.*;
import static java.lang.Math.*;
import static castlevaniabot.Weapon.*;

public class FrankensteinStrategy extends Strategy {
  
  private GameObject frank;
  private int lastFrankX;
  private int frankVx;
  private int avoidFrank;
  
  private GameObject igor; 
  private int lastIgorX;
  private int lastIgorY;
  private int igorVx;
  private int igorVy;
  private int avoidIgor;
  private boolean igorLeft;
  
  private GameObject fireball;
  private int lastFireballX;
  private int lastFireballY;
  private int fireballVx;
  private int fireballVy;
  private int fireballDist;
  
  public boolean done;
  
  public FrankensteinStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override
  public void init() {
    frank = igor = fireball = null;
    avoidIgor = avoidFrank = 0;
    done = false;
  }

  @Override
  public void step() {
    
    updateObjects();
    
    if (b.hearts > 0) {
      switch(b.weapon) {        
        case AXE:        stepAxeStrategy();       break;
        case BOOMERANG:  stepBoomerangStrategy(); break;
        case DAGGER:     stepBoomerangStrategy(); break;
        case HOLY_WATER: stepNoWeaponsStrategy(); break;        
        default:         stepNoWeaponsStrategy(); break;
      }      
    } else {
      stepNoWeaponsStrategy();
    }
  } 
  
  private void stepAxeStrategy() {
    
    boolean canWalkTowardFrank = true;
    
    if (handleFireball()) {
      avoidIgor = avoidFrank = 0;
      canWalkTowardFrank = false;
    } 
    
    if (handleIgor()) {
      avoidFrank = 0;
      canWalkTowardFrank = false;
    } 
    
    if (frank != null) {
      final int offsetX = frankVx << 4;
      if (avoidFrank > 0) {
        --avoidFrank;
        b.substage.moveAwayFromTarget(frank.x);
      } else if (frank.distanceX < 24) {
        avoidFrank = 30 + ThreadLocalRandom.current().nextInt(31);
        b.substage.moveAwayFromTarget(frank.x);
      } else if (b.isInStandingWhipRange(frank, offsetX, 0)) {
        if (!b.weaponing && b.face(frank)) {
          b.whip();
        }
      } else if (b.isInKneelingWhipRange(frank, offsetX, 0)) {
        b.kneel();
        if (!b.weaponing && b.kneeling && b.face(frank)) {
          b.whip();
        }
      } else if (!b.weaponing && b.canHitWithAxe(b.playerX >> 4, b.playerY >> 4, 
          offsetX, 0, frank) && b.face(frank)) {
        b.useWeapon();
      } else if (canWalkTowardFrank) {
        b.substage.moveToward(frank);
      }
    }    
  }
  
  private void stepBoomerangStrategy() {
       
    if (handleFireball()) {
      avoidIgor = avoidFrank = 0;
    } 
    
    if (handleIgor()) {
      avoidFrank = 0;
    } 
    
    if (frank != null) {
      final int offsetX = frankVx << 4;
      if (avoidFrank > 0) {
        --avoidFrank;
        b.substage.moveAwayFromTarget(frank.x);
      } else if (frank.distanceX < 24) {
        avoidFrank = 30 + ThreadLocalRandom.current().nextInt(31);
        b.substage.moveAwayFromTarget(frank.x);
      } else if (b.isInStandingWhipRange(frank, offsetX, 0)) {
        if (!b.weaponing && b.face(frank)) {
          if (b.playerY == frank.y) {
            b.whipOrWeapon();
          } else {
            b.whip();
          }
        }
      } else if (b.isInKneelingWhipRange(frank, offsetX, 0)) {
        b.kneel();
        if (!b.weaponing && b.kneeling && b.face(frank)) {
          b.whip();
        }
      } else {
        if (frank.x < 992 && !b.weaponing && b.face(frank) 
            && b.playerY == frank.y) {
          b.whipOrWeapon();
        } else {
          b.substage.moveToward(frank);
        }
      }
    }    
  }
  
  private void stepNoWeaponsStrategy() {
    
    boolean canWalkTowardFrank = true;
    
    if (handleFireball()) {
      avoidIgor = avoidFrank = 0;
      canWalkTowardFrank = false;
    } 
    
    if (handleIgor()) {
      avoidFrank = 0;
      canWalkTowardFrank = false;
    } 
    
    if (frank != null) {
      final int offsetX = frankVx << 4;
      if (avoidFrank > 0) {
        --avoidFrank;
        b.substage.moveAwayFromTarget(frank.x);
      } else if (frank.distanceX < 24) {
        avoidFrank = 30 + ThreadLocalRandom.current().nextInt(31);
        b.substage.moveAwayFromTarget(frank.x);
      } else if (b.isInStandingWhipRange(frank, offsetX, 0)) {
        if (!b.weaponing && b.face(frank)) {
          b.whipOrWeapon();
        }
      } else if (b.isInKneelingWhipRange(frank, offsetX, 0)) {
        b.kneel();
        if (!b.weaponing && b.kneeling && b.face(frank)) {
          b.whip();
        }
      } else if (canWalkTowardFrank) {
        b.substage.moveToward(frank);
      }
    }
  }
  
  private boolean handleIgor() {
    
    if (igor == null || igor.distanceX > 80 || igor.y2 < b.playerY - 56) {
      return false;
    }
    
    final int vx = igorVx << 4;
    final int vy = igorVy << 4;
    
    if (avoidIgor > 0) {
      --avoidIgor;
      if (igorLeft) {
        b.substage.routeLeft();
      } else {
        b.substage.routeRight();
      }
      return true;
    } else if (b.isInStandingWhipRange(igor, vx, vy)) {
      if (!b.weaponing && b.face(igor)) {
        b.whip();
      }
      return true;
    } else if (b.isInKneelingWhipRange(igor, vx, vy)) {
      b.kneel();
      if (b.kneeling && !b.weaponing && b.face(igor)) {
        b.whip();
      }
      return true;
    } else if ((vy < 0 && igor.y < b.playerY - 16 && igor.distanceX < 56) 
        || (igor.x1 + vx <= b.playerX + 8 
            && igor.x2 + vx >= b.playerX - 8 
            && igor.y2 + vy >= b.playerY - 32 
            && igor.y1 + vy <= b.playerY)) {
      avoidIgor = 23 + ThreadLocalRandom.current().nextInt(17);
      igorLeft = vx > 0;      
      return true;
    }
    
    return false;
  }
  
  private boolean handleFireball() {
    
    if (fireball == null || fireballDist > 9216) {
      return false;
    }
    
    if (fireball.left) {
      if (fireball.x2 < b.playerX - 16) {
        return false;
      }
    } else {
      if (fireball.x1 > b.playerX + 16) {
        return false;
      }
    }
    
    if (fireball.y2 >= b.playerY - 32 && fireball.y1 <= b.playerY) {
      final int offsetX = fireballVx << 4;
      final int offsetY = fireballVy << 4;
      if (fireball.distanceX < 24) {
        final boolean flyingHigh = fireball.y < b.playerY - 16;
        if (flyingHigh) {
          b.kneel();
          return true;
        } else if (!flyingHigh && b.canJump) {
          b.jump();
          return true;
        }         
      } else if (b.isInStandingWhipRange(fireball, offsetX, offsetY)) {
        if (b.face(fireball) && !b.weaponing) {
          b.whip();          
          return true;
        }        
      } else if (b.isInKneelingWhipRange(fireball, offsetX, offsetY)) {
        if (b.face(fireball)) {
          b.kneel();
          if (b.kneeling && !b.weaponing) {
            b.whip();
            return true;
          }          
        }        
      }
    }
    
    return false;
  }
  
  private void updateObjects() {
    
    final int px = b.playerX;
    final int py = b.playerY - 16;
    
    frank = igor = fireball = null;
    fireballDist = Integer.MAX_VALUE;
    final GameObject[] objs = b.gameObjects;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      switch(obj.type) {
        case FRANKENSTEIN: 
          frank = obj;
          frankVx = obj.x - lastFrankX; 
          lastFrankX = obj.x;
          break;
        case FLEAMAN:
          igor = obj;
          igorVx = obj.x - lastIgorX;
          igorVy = obj.y - lastIgorY;
          lastIgorX = obj.x;
          lastIgorY = obj.y;
          break;
        case FIREBALL: {
          final int dx = obj.x - px;
          final int dy = obj.y - py;
          final int dist = dx * dx + dy * dy;
          if (dist < fireballDist) {
            fireballDist = dist;
            fireball = obj;
          }
          break;
        }
      }
    }
    if (fireball != null) {
      final int vx = fireball.x - lastFireballX;
      if (signum(vx) != signum(fireballVx) || abs(vx) > 8) {
        fireballVx = 0;
      } else if (abs(vx) > abs(fireballVx)) {
        fireballVx = vx;
      }
      final int vy = fireball.y - lastFireballY;
      if (signum(vy) != signum(fireballVy) || abs(vy) > 8) {
        fireballVy = 0;
      } else if (abs(vy) > abs(fireballVy)) {
        fireballVx = vy;
      }
      lastFireballX = fireball.x;
      lastFireballY = fireball.y;
    }
    
    if (frank == null && igor == null && fireball == null) {
      done = true;
    }
  }  
}