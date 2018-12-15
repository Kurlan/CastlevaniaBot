package castlevaniabot;

import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage1300 extends Substage {
  
  private boolean waited;
  
  public Substage1300(final CastlevaniaBot b) {
    super(b);
  }

  @Override void init() {
    super.init();
    waited = false;
    mapRoutes = b.allMapRoutes.get("13-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) { 
    
    if (obj.type == FLEAMAN) {
      if (obj.distanceX < 128 && obj.y1 <= b.playerY 
          && obj.y2 >= b.playerY - 48) {
        obj.tier = 7;
      }
    } else if (obj.type == WHITE_SKELETON) {
      if (obj.distanceX < 128 && obj.y1 <= b.playerY + 16
          && obj.y2 >= b.playerY - 64) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          if (obj.y > 132 && b.whipLength != 2) {
            obj.tier = 6;
          } else if (roundTile(obj.x) != 30 || b.playerX < 480) {            
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
          if (obj.y > 132 && b.whipLength != 2) {
            obj.tier = 7;
          } else {
            obj.tier = 3; 
          }
          break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          obj.tier = 4; break;
        case DAGGER_WEAPON:
          if (b.weapon == NONE || b.weapon == STOPWATCH) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;        
        case BOOMERANG_WEAPON:
          if (b.weapon != HOLY_WATER) {
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
          if (obj.y > 132 && b.whipLength != 2) {
            obj.tier = 8;
          } else {
            obj.tier = 5; 
          }
          break;
      }
    }    
  }

  @Override void pickStrategy() {
    
    if (b.strategy == b.WAIT) {
      final GameObject skeleton = b.getType(WHITE_SKELETON);
      if (waited || (skeleton != null && (skeleton.x < b.playerX - 48 
          || skeleton.y > 132))) {
        super.pickStrategy();
      }
    } else if (b.playerX >= 368 && b.playerY > 160 && !b.isObjectBelow(132)) {
      final GameObject skeleton = b.getType(WHITE_SKELETON);
      if (skeleton != null && skeleton.y <= 132 
          && b.playerX < skeleton.x) {
        clearTarget();
        b.WAIT.init(493, 192);
        b.strategy = b.WAIT;
        waited = false;
      } else {
        super.pickStrategy();
      }
    } else {
      super.pickStrategy();
    }
  }

  @Override void readGameObjects() {   
    b.addDestination(88, 48);
  }  

  @Override void routeLeft() {
    if (b.playerY < 144) {
      route(9, 96);
    } else {
      route(9, 192);
    }    
  }
  
  @Override void routeRight() {
    if (b.playerY < 144) {
      route(503, 128);
    } else {
      route(503, 192);
    }
  }
  
  @Override void treasureTriggered() {
    waited = true;
  }
}