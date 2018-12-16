package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.Strategy;

import static castlevaniabot.model.gameelements.GameObjectType.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class Substage1501 extends Substage {
  
  private boolean bossTriggered;
  private boolean aboutToGetCrystalBall; 
  private boolean bossDefeated;
  private boolean whippedCandles;
  
  public Substage1501(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    whippedCandles = bossDefeated = aboutToGetCrystalBall = bossTriggered 
        = false;
    mapRoutes = b.allMapRoutes.get("15-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    switch(obj.type) {
      case DEATH:
        obj.tier = 6;
        break;
      case SICKLE:
        if (obj.distanceX < 80) {          
          obj.tier = 7;
          if (b.isInStandingWhipRange(obj)) {
            obj.subTier = 4;
          } else if (b.isInKneelingWhipRange(obj)) {
            obj.subTier = 3;
          } else if (obj.y2 <= b.playerY && obj.y1 >= b.playerY - 32) {
            obj.subTier = 2;
          } else if (obj.y1 <= b.playerY && obj.y2 >= b.playerY - 32) {
            obj.subTier = 1;
          }
        }
        break;
      
      // Interestingly, the best medusa heads strategy appears to be to ignore
      // them completely.  All the medusa heads logic has been commented out.
        
//      case MEDUSA_HEAD:
//        if (obj.distanceX < 24) {
//          obj.tier = 8;
//        }
//        break;
        
      case AXE:
        if (obj.distanceX < 128 && obj.y1 <= b.playerY 
            && obj.y2 >= b.playerY - 32) {
          obj.tier = 7;
        }        
        break;
      case AXE_KNIGHT:
        if (!b.onStairs && obj.distanceX < 128 && obj.y1 <= b.playerY + 16
            && obj.y2 >= b.playerY - 64) {
          obj.tier = 6;
        }        
        break;
      case DESTINATION:
        obj.tier = 0;
        break;
      default:
        if (obj.distance < HORIZON) {
          switch(obj.type) {
            case CANDLES:
              if (bossDefeated) {
                switch(roundTile(obj.x)) {
                  case 6:
                    if (b.playerX < obj.x - 24) {
                      obj.tier = 1;
                    }
                    break;
                  case 14:
                    obj.platformX = 14;
                    obj.tier = 1;
                    break;
                  default:
                    obj.tier = 1;
                    break;
                }
              } else if (!bossTriggered 
                  && (b.playerX < 256 || (obj.x < 480 && obj.x < b.playerX))) {
                obj.tier = 1; 
              }  
              break;
            case BLOCK: 
              obj.tier = 2; break;
            case MONEY_BAG:
            case SMALL_HEART:
            case LARGE_HEART:        
            case INVISIBLE_POTION:
            case WHIP_UPGRADE:
              obj.tier = 3; break;
            case CROSS:          
            case DOULE_SHOT:
            case TRIPLE_SHOT:
              obj.tier = 5; break;
            case DAGGER_WEAPON:
              if (b.weapon == NONE || b.weapon == STOPWATCH) {
                obj.tier = 5;
              } else {
                b.avoid(obj);
              }
              break;        
            case AXE_WEAPON:
              if (b.weapon != HOLY_WATER && b.weapon != BOOMERANG) {
                obj.tier = 5;
              } else {
                b.avoid(obj);
              }
              break;          
            case STOPWATCH_WEAPON:  
              if (b.weapon == NONE) {
                obj.tier = 5;
              } else {
                b.avoid(obj);
              }
              break;          
            case HOLY_WATER_WEAPON:
            case PORK_CHOP:
            case EXTRA_LIFE:
              obj.tier = 5; break;
            case CRYSTAL_BALL:
              bossDefeated = true;
              obj.tier = 0;
              break;         
          }
        }        
        break;
    }    
  }
  
  @Override
  public void route(final int targetX, final int targetY,
                    final boolean checkForEnemies) {

    if (bossDefeated) {
      // crystal ball X +/- 20
      if (b.playerX == 108 && targetX >= 144 && !b.playerLeft) {
        b.pressRightAndJump();
      } else if (b.playerX == 148 && targetX <= 112 && b.playerLeft) {
        b.pressLeftAndJump();
      } else {
        super.route(targetX, targetY, checkForEnemies);
      }
    } else {      
      super.route(targetX, targetY, checkForEnemies);
    }
  }  
  
  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    
    if (b.playerX == 800 && b.weapon == BOOMERANG && b.hearts > 0 
        && !b.weaponing) {
      b.useWeapon(); // hit candles with boomerang
    } 
    
    if (b.strategy == b.getAllStrategies().getWHIP()) {
      if (whippedCandles) {
        super.pickStrategy(targetedObject);
      }
    } else if (bossDefeated) {
      if (!whippedCandles && b.playerX >= 224) {
        if (b.strategy != b.getAllStrategies().getWHIP()) {
          clearTarget(targetedObject);
          b.getAllStrategies().getWHIP().init(238, 128, true, 0, true, false, 36);
          b.strategy = b.getAllStrategies().getWHIP();
        }
      } else {
        super.pickStrategy(targetedObject);
      }
    } else if (b.strategy == b.getAllStrategies().getHOLY_WATER_DEATH() && b.getAllStrategies().getHOLY_WATER_DEATH().done) {
      bossDefeated = true;
      super.pickStrategy(targetedObject);
    } else if (bossTriggered) {
      if (b.weapon == HOLY_WATER && b.hearts > 0) {
        b.getAllStrategies().getHOLY_WATER_DEATH().step();
      } else {
        super.pickStrategy(targetedObject);
      }
    } else if (b.playerX < 128) {
      bossTriggered = true;
      if (b.weapon == HOLY_WATER && b.hearts > 0) {
        clearTarget(targetedObject);
        b.getAllStrategies().getHOLY_WATER_DEATH().init();
        b.strategy = b.getAllStrategies().getHOLY_WATER_DEATH();
      }
    } else if (b.strategy == b.getAllStrategies().getDEATH_HALL_HOLY_WATER()) {
      if (b.weapon == HOLY_WATER && b.hearts > 0) {
        b.strategy.step();
      } else {
        super.pickStrategy(targetedObject);
      }      
    } else if (b.weapon == HOLY_WATER && b.hearts > 0) {
      if (b.strategy != b.getAllStrategies().getDEATH_HALL_HOLY_WATER()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getDEATH_HALL_HOLY_WATER().init();
        b.strategy = b.getAllStrategies().getDEATH_HALL_HOLY_WATER();
      } else {
        super.pickStrategy(targetedObject);
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }
  
  @Override
  Strategy selectStrategy(final GameObject target) {
    if (target == null) {
      if (aboutToGetCrystalBall) {
        return b.getAllStrategies().getGOT_CRYSTAL_BALL();
      } else {
        return super.selectStrategy(target);
      }
    } else if (target.type == MEDUSA_HEAD) {
      return b.getAllStrategies().getMEDUSA_HEAD();
    } else {
      return super.selectStrategy(target);
    }    
  }  

  @Override
  public void readGameObjects() {
    if (!bossDefeated) {
      if (bossTriggered && b.strategy != b.getAllStrategies().getHOLY_WATER_DEATH()) {
        b.addDestination(80, 160);
      } else {
        b.addDestination(9, 128);
      }
    }
  }  

  @Override
  public void routeLeft() {
    route(9, 128);
  }
  
  @Override
  public void routeRight() {
    route(1006, 192);
  }
  
  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }  

  @Override
  public void whipUsed() {
    whippedCandles = true;
  }
}