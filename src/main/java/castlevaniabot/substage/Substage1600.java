package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.strategy.Strategy;

import static castlevaniabot.model.gameelements.GameObjectType.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class Substage1600 extends Substage {
  
  public Substage1600(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    mapRoutes = b.allMapRoutes.get("16-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= b.playerY - 32 && obj.y1 <= b.playerY)
              && ((obj.left && obj.x2 >= b.playerX - 16) 
                  || (!obj.left && obj.x1 <= b.playerX + 16))) {
        obj.tier = 6;
      }
    } else if (obj.type == PHANTOM_BAT) {
      obj.tier = 1;
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 94:
              if (b.playerX < obj.x) {
                obj.tier = 2;
              }
              break;
            case 50:
              obj.tier = 2;
              obj.platformX = (b.whipLength == 2) ? 53 : 52;
              break;
            case 6:            
            case 86:
            case 90:
              obj.tier = 2;
              break;
          }
          break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:        
        case INVISIBLE_POTION:
        case WHIP_UPGRADE:
          if (obj.x < 128 || obj.x >= 1344) {
            obj.tier = 3; 
          }  
          break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          if (obj.x < 128 || obj.x >= 1344) {
            obj.tier = 4; 
          }      
        case DAGGER_WEAPON:       
        case BOOMERANG_WEAPON:
        case AXE_WEAPON:                         
        case HOLY_WATER_WEAPON:
          if (b.weapon != STOPWATCH && (obj.x < 128 || obj.x >= 1344)) {
            obj.tier = 5; 
          } else {
            b.avoid(obj);
          }
          break;
        case PORK_CHOP:
        case EXTRA_LIFE:
        case STOPWATCH_WEAPON:
          obj.tier = 5; break;
      }
    }    
  }

  @Override
  Strategy selectStrategy(final GameObject target) {
    if (target == null || target.type != PHANTOM_BAT) {
      return super.selectStrategy(target);
    } else {
      return b.GIANT_BAT;
    }
  }

  @Override
  public void readGameObjects() {
    b.addDestination(41, 128);
  }  

  @Override
  public void routeLeft() {
    if (b.playerX > 1328 && b.playerY > 144) {
      route(1344, 192);
    } else {
      route(41, 128);
    }
  }
  
  @Override
  public void routeRight() {
    route(1527, 192);
  }  
}