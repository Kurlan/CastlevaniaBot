package castlevaniabot;

import static castlevaniabot.Addresses.*;
import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage1301 extends Substage {
  
  private boolean treasureTriggered;
  
  private boolean blockWhipped1;
  private boolean blockBroken1;

  private boolean blockWhipped2;
  private boolean blockBroken2;  
   
  public Substage1301(final CastlevaniaBot b) {
    super(b);
  }

  @Override void init() {
    super.init();
    treasureTriggered = blockWhipped1 = blockBroken1 = blockWhipped2 
        = blockBroken2 = false;
    mapRoutes = b.allMapRoutes.get("13-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) { 
    
    if (obj.type == FLEAMAN) {
      if (obj.distanceX < 64 && obj.y1 <= b.playerY 
          && obj.y2 >= b.playerY - 48) {
        obj.tier = 9;
      }
    } else if (obj.type == WHITE_SKELETON) {
      if (obj.distanceX < 128 && obj.y1 <= b.playerY + 16
          && obj.y2 >= b.playerY - 64) {
        obj.tier = 8;
      }
    } else if (obj.type == RED_SKELETON) {
      if (obj.distanceX < 64 && obj.distanceY <= 4) {
        obj.tier = 7;
      }
    } else if (obj.type == RED_BONES || obj.type == RED_SKELETON_RISING) {      
      if (obj.distanceY <= 4 && (obj.distanceX <= 32 
          || (obj.playerFacing && obj.distanceX < 48))) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 62: 
              obj.tier = 3;
              obj.subTier = 1;
              break;
            case 66:
              obj.tier = 3;
              obj.subTier = 2;
              break;
            default:
              if ((obj.x >= 320 || obj.y <= 104) 
                  && (obj.x < 480 || obj.x >= 832)) {
                obj.tier = 1; 
              }
              break;
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
        case EXTRA_LIFE:
          obj.tier = 5; break;
      }
    }    
  }

  @Override void pickStrategy() {
    if (!treasureTriggered && b.playerY <= 96 && b.playerX >= 336 
        && b.playerX < 416) {
      if (b.strategy != b.WAIT) {
        clearTarget();
        b.WAIT.init(407, 96, WaitStrategy.WaitType.WALK_RIGHT);
        b.strategy = b.WAIT;
      }
    } else {
      super.pickStrategy();
    }
  }

  @Override void readGameObjects() {   
    if (b.playerX >= 416 && b.playerX < 480) {
      if (!blockBroken1 && api.readPPU(BLOCK_130100) == 0x00) {
        blockWhipped1 = blockBroken1 = true;
        mapRoutes = b.allMapRoutes.get("13-01-01");
      }
      if (!blockWhipped1) {
        b.addBlock(432, 96);
      }
    } else if (b.playerX >= 1024) {
      if (!blockBroken2 && api.readPPU(BLOCK_130101) == 0x00) {
        blockWhipped2 = blockBroken2 = true;
        mapRoutes = b.allMapRoutes.get("13-01-02");
      }
      if (!blockWhipped2) {
        b.addBlock(1088, 160);
      }
    }        
    
    b.addDestination(1255, 192);
  }  

  @Override void routeLeft() {
    if (b.playerX < 320 && b.playerY > 104) {
      route(9, 192);
    } else if (b.playerX < 416 && b.playerY <= 104) {
      route(9, 96);
    } else {
      route(361, 192);
    }
  }
  
  @Override void routeRight() {
    if (b.playerX < 320 && b.playerY > 104) {
      route(311, 192);
    } else if (b.playerX < 416 && b.playerY <= 104) {
      route(407, 96);
    } else {
      route(1255, 192);
    }
  }
  
  @Override void blockWhipped() {
    if (b.playerX > 448) {
      blockWhipped2 = true;
    } else {
      blockWhipped1 = true;
    }
  }  

  @Override void treasureTriggered() {
    treasureTriggered = true;
  }
}