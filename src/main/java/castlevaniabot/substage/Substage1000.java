package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;

import static castlevaniabot.model.gameelements.GameObjectType.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class Substage1000 extends Substage {
  
  private boolean whippedHolyWaterCandle;
  
  public Substage1000(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    b.strategy = null;
    whippedHolyWaterCandle = false;
    mapRoutes = b.allMapRoutes.get("10-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == RED_BAT) {
      if (obj.distanceX < 96 && obj.y + 88 >= b.playerY 
          && obj.y - 40 <= b.playerY && ((obj.left && obj.x >= b.playerX - 40) 
              || (!obj.left && obj.x <= b.playerX + 40))) {
        obj.tier = 6;
      }
    } else if (obj.type == FISHMAN) {
      if (obj.distance > 64) {
        obj.distance = obj.distanceX >> 4;
      }
      if (obj.x <= b.playerX + 64 && obj.x >= b.playerX - 24 
          && obj.y <= b.playerY + 4) {
        obj.tier = 5;
      }      
    } else if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= b.playerY - 32 && obj.y1 <= b.playerY)
              && ((obj.left && obj.x2 >= b.playerX - 16) 
                  || (!obj.left && obj.x1 <= b.playerX + 16))) {
        obj.tier = 7;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 14: // stopwatch
              if (b.weapon == NONE) {
                obj.tier = 1;
              }
              break;
            case 46: // holy water
              if (b.weapon != HOLY_WATER) {
                obj.tier = 1;
                obj.platformX = 45; // strike very close to the candles
              }
              break;
            default:
              if (obj.x < 168 || (b.whipLength != 2 
                  && (obj.x < 232 || obj.x > 576) 
                      && (obj.x < 1088 || obj.x > 1280))) {
                obj.tier = 1;
              } 
              break;
          } 
          break;
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
          if (b.weapon != BOOMERANG && b.weapon != HOLY_WATER) {
            obj.tier = 4;
          } else {
            b.avoid(obj);
          }
          break;
        case BOOMERANG_WEAPON:       
          if (b.weapon != HOLY_WATER) {
            obj.tier = 4;
          } else {
            b.avoid(obj);
          }
          break;           
        case DAGGER_WEAPON:        
          if (b.weapon == NONE || b.weapon == STOPWATCH) {
            obj.tier = 4;
          } else {
            b.avoid(obj);
          }
          break;            
        case STOPWATCH_WEAPON:
          if (b.weapon == NONE) {
            obj.tier = 4;
          } else {
            b.avoid(obj);
          }
          break;          
        case HOLY_WATER_WEAPON:
        case PORK_CHOP:
          obj.tier = 8;
          break;
      }
    }    
  }
  
  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    if (b.strategy == b.getAllStrategies().getBAT_MOVING_PLATFORM() && b.playerY > 112) {
      if (b.getAllStrategies().getBAT_MOVING_PLATFORM().done) {
        super.pickStrategy(targetedObject);
      }
    } else if (b.strategy == b.getAllStrategies().getBAT_DUAL_PLATFORMS()) {
      if (b.getAllStrategies().getBAT_DUAL_PLATFORMS().done) {
        super.pickStrategy(targetedObject);
      }
    } else if (b.playerX == 991 && b.playerY == 160 
        && !b.isTypePresent(RED_BAT)) {
      clearTarget(targetedObject);
      b.getAllStrategies().getBAT_DUAL_PLATFORMS().init();
      b.strategy = b.getAllStrategies().getBAT_DUAL_PLATFORMS();
    } else if ((b.playerX == 223 || b.playerX == 767) && b.playerY == 160 
        && !b.isTypePresent(RED_BAT)) {
      clearTarget(targetedObject);
      b.getAllStrategies().getBAT_MOVING_PLATFORM().init();
      b.strategy = b.getAllStrategies().getBAT_MOVING_PLATFORM();
    } else if (!whippedHolyWaterCandle && b.weapon != HOLY_WATER 
        && b.playerY <= 96 && b.playerX >= 720 && b.playerX <= 740) {
      if (b.strategy != b.getAllStrategies().getWHIP()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getWHIP().init(732, 96, false, 40);
        b.strategy = b.getAllStrategies().getWHIP();
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }  

  @Override
  public void readGameObjects() {
    if (b.playerX < 240) {
      b.addDestination(223, 160);
    } else if (b.playerX < 784) {
      b.addDestination(767, 160);
    } else if (b.playerX < 1008) {
      b.addDestination(991, 160);
    } else {
      b.addDestination(1512, 48);
    }
  }  

  @Override
  public void routeLeft() {
    if (b.playerX < 240) {
      route(9, 160);
    } else if (b.playerX < 784) {
      route(384, 160);
    } else if (b.playerX < 1008) {
      route(960, 160);
    } else {
      route(1280, 160);
    }
  }
  
  @Override
  public void routeRight() {
    if (b.playerX < 240) {
      route(223, 160);
    } else if (b.playerX < 784) {
      route(767, 160);
    } else if (b.playerX < 1008) {
      route(991, 160);
    } else {
      route(1527, 112);
    }
  }

  @Override
  public void whipUsed() {
    whippedHolyWaterCandle = true;
  }
}