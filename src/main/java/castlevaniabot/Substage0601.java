package castlevaniabot;

import java.util.concurrent.*;
import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage0601 extends Substage {
  
  private int walkDelay;
  private boolean reachedBoss;
  private boolean aboutToGetCrystalBall;  
   
  public Substage0601(final CastlevaniaBot b) {
    super(b);
  }

  @Override void init() {
    super.init();
    walkDelay = 0;
    aboutToGetCrystalBall = reachedBoss = false;
    mapRoutes = b.allMapRoutes.get("06-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.x >= 256 && obj.x < 592) {
      return;
    }
      
    if (obj.type == SNAKE) {
      if (!b.MEDUSA.isTimeFrozen()) {
        if (obj.distanceX < 64) {
          if (obj.left) {
            if (obj.x2 > b.playerX - 16) {
              obj.tier = 7;
            }
          } else {
            if (obj.x1 < b.playerX + 16) {
              obj.tier = 7;
            }
          }
        }
      }
    } else if (obj.type == MEDUSA) {
      obj.tier = 6;
    } else if (obj.type == DESTINATION || obj.type == CRYSTAL_BALL) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES: 
          obj.tier = 1; break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:
        case WHIP_UPGRADE:
        case INVISIBLE_POTION:
          obj.tier = 2; break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          obj.tier = 3; break;
        case AXE_WEAPON:
        case BOOMERANG_WEAPON:
        case DAGGER_WEAPON:        
        case STOPWATCH_WEAPON:
          if (b.playerX >= 256) {
            if (b.weapon != HOLY_WATER) {
              obj.tier = 4;
            } else {
              b.avoid(obj);
            }
          }
          break;          
        case HOLY_WATER_WEAPON:
          if (b.playerX >= 256) {
            obj.tier = 5;
          }
          break;
        case PORK_CHOP:
          obj.tier = 5;
          break;
      }
    }    
  }

  @Override void pickStrategy() {

    if (!reachedBoss && b.playerX <= 40) {
      reachedBoss = true;
      b.pressRight();
    }

    if (walkDelay > 0) {
      if (--walkDelay == 0) {
        b.pressLeft();
      }
    } else if (b.playerX == 493) {      
      b.strategy = null;
      walkDelay = 150 + ThreadLocalRandom.current().nextInt(11);
    } else if (b.playerX >= 256 && b.playerX < 608) {
      if (b.strategy != b.MEDUSA_HEADS_WALK) {
        clearTarget();
        b.MEDUSA_HEADS_WALK.init(true);
        b.strategy = b.MEDUSA_HEADS_WALK;
      }
    } else {
      super.pickStrategy();
    }
  }
  
  @Override Strategy selectStrategy(final GameObject target) {
    if (target == null && aboutToGetCrystalBall) {
      return b.GOT_CRYSTAL_BALL;
    } else {
      return super.selectStrategy(target);
    }
  }  
  
  @Override void readGameObjects() {    
    if (!reachedBoss) {
      b.addDestination(40, 176);
    }
  }  

  @Override void routeLeft() {
    route(25, 176);
  }
  
  @Override void routeRight() {
    route(727, 176);
  }
  
  @Override void crystalBallAlmostAquired() {  
    aboutToGetCrystalBall = true;
  }  
}