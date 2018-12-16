package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.Addresses.*;
import static castlevaniabot.model.gameelements.GameObjectType.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class Substage1700 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;
  
  private int stopWatchDelay;
  private boolean killedSkeleton;
  
  public Substage1700(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    stopWatchDelay = 0;
    killedSkeleton = blockWhipped = blockBroken = false;
    mapRoutes = b.allMapRoutes.get("17-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == EAGLE) {
      if (obj.distanceX < 64 && obj.y1 <= b.playerY 
          && obj.y2 >= b.playerY - 40) {
        obj.tier = 8;
      }
    } else if (obj.type == FLEAMAN) {
      if (!b.onStairs && obj.distanceX < 48 && obj.y1 <= b.playerY 
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
          switch(roundTile(obj.x)) {
            case 10: // skip this candle
              break;
            case 34:
              if (obj.x < b.playerX) {
                obj.tier = 1;
              }
              break;
            case 46:
              if (b.playerX < obj.x) {
                obj.tier = 1;
              }
              break;
            default:
              obj.tier = 1;
              break;
          }  
          break;
        case BLOCK: 
          obj.tier = 2; break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:        
        case INVISIBLE_POTION:
          if (b.playerX >= 496) {
            obj.tier = 3;
          }
          break;
        case WHIP_UPGRADE:
          obj.tier = 3; break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          if (b.playerX >= 496) {
            obj.tier = 4; 
          }
          break;
        case DAGGER_WEAPON:       
        case BOOMERANG_WEAPON:
        case AXE_WEAPON:                         
        case HOLY_WATER_WEAPON:
          if (b.playerX >= 496) {
            if (b.weapon != STOPWATCH) {
              obj.tier = 5; 
            } else {
              b.avoid(obj);
            }
          }
          break;
        case PORK_CHOP:
        case EXTRA_LIFE:
        case STOPWATCH_WEAPON:
          if (b.playerX >= 496) {
            obj.tier = 5; 
          }
          break;
      }
    }    
  }
  
  @Override
  public void pickStrategy() {
    
    if (b.weapon == STOPWATCH && b.playerX < 496) {
      b.strategy = null;
      if (stopWatchDelay > 0 && --stopWatchDelay == 180) {
        b.useWeapon();
      } 
      if (stopWatchDelay == 0 && b.playerX < 272 && b.weapon == STOPWATCH 
          && b.hearts >= 5) {
        stopWatchDelay = 181;        
      } else if (stopWatchDelay < 179) {
        route(104, 48, false);
      }
    } else if (!killedSkeleton) {
      if (b.strategy == b.SKELETON_WALL) {
        if (b.SKELETON_WALL.done) {
          killedSkeleton = true;
          super.pickStrategy();
        }
      } else {
        clearTarget();
        b.SKELETON_WALL.init(736, 128);
        b.strategy = b.SKELETON_WALL;
      }
    } else {
      super.pickStrategy();
    }
  }  

  @Override
  public void readGameObjects() {
    if (b.playerX < 224) {
      if (!blockBroken && api.readPPU(BLOCK_170000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("17-00-01");
      }
      if (!blockWhipped) {
        b.addBlock(160, 80);
      }
    }    

    if (b.playerX >= 496) {
      b.addDestination(680, 48);
    } else {
      b.addDestination(104, 48);
    }
  }  

  @Override
  public void routeLeft() {
    if (b.playerX >= 496) {
      route(521, 160);
    } else if (b.playerX >= 248 && b.playerY < 168) {
      route(256, 96);
    } else if (!b.onStairs && b.playerX > 152 && b.playerX < 208) {
      route(160, 192);
    } else {
      route(41, 96);
    }
  }
  
  @Override
  public void routeRight() {
    if (b.playerX >= 496) {
      if (b.onStairs || (b.playerY <= 96 && b.playerX < 648)) {
        route(680, 48);
      } else {
        route(750, 128);
      }
    } else if (b.playerX >= 248 && b.playerY < 168) {
      route(471, 160);
    } else if (!b.onStairs && b.playerX > 152 && b.playerX < 208) {
      route(287, 192);
    } else if (b.onStairs && b.playerX < 120) {
      route(104, 48);
    } else {
      route(287, 192);
    }
  }
  
  @Override
  public void blockWhipped() {
    blockWhipped = true;
  }  
}