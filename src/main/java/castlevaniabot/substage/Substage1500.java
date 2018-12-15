package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;

import static castlevaniabot.Addresses.*;
import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage1500 extends Substage {
  
  private boolean blockWhipped1;
  private boolean blockBroken1;

  private boolean blockWhipped2;
  private boolean blockBroken2;   
  
  public Substage1500(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    blockWhipped1 = blockBroken1 = blockWhipped2 = blockBroken2 = false;
    mapRoutes = b.allMapRoutes.get("15-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == BONE_TOWER) {
      if (obj.distanceX < 160 && obj.y >= b.playerY - 16 
          && obj.y <= b.playerY) {
        obj.tier = 8;
      }
    } else if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= b.playerY - 32 && obj.y1 <= b.playerY)
              && ((obj.left && obj.x2 >= b.playerX - 16) 
                  || (!obj.left && obj.x1 <= b.playerX + 16))) {
        obj.tier = 9;
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
      
      if (b.playerY <= 96 && (b.onStairs || obj.y > 96)) {
        return;
      }
      
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 38: // boomerang
              if (b.weapon != HOLY_WATER) {
                obj.tier = 1;
              }
              break;
            case 62:
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
  
  @Override
  public void pickStrategy() {
    if (b.onStairs && b.playerY <= 160 && b.playerX < 672 
        && b.isTypeInBounds(RED_SKELETON, 584, 0, 624, 112)) {
      if (b.strategy != null) {
        clearTarget();
        setStrategy(null);
      }
      if (b.playerY < 128) {
        b.pressDown();
      }
    } else {
      super.pickStrategy();
    }
  }

  @Override
  public void readGameObjects() {
    if (b.playerX >= 848 && b.playerY <= 96) {
      final boolean block1 = api.readPPU(BLOCK_150000) == 0x00;
      final boolean block2 = api.readPPU(BLOCK_150001) == 0x00;
      if (!blockBroken1 && block1) {
        blockWhipped1 = blockBroken1 = true;
        if (block1 && block2) {
          mapRoutes = b.allMapRoutes.get("15-00-01");
        }
      }
      if (!blockBroken2 && block2) {
        blockWhipped2 = blockBroken2 = true;
        if (block1 && block2) {
          mapRoutes = b.allMapRoutes.get("15-00-01");
        }
      }
      if (!blockWhipped1) {
        b.addBlock(992, 64);
      }
      if (!blockWhipped2) {
        b.addBlock(992, 80);
      }
    }    

    b.addDestination(936, 48);
  }  

  @Override
  public void routeLeft() {
    if (b.playerY >= 128) {
      route(553, 192);
    } else {
      route(521, 96);
    }
  }
  
  @Override
  public void routeRight() {
    if (b.playerY >= 128) {
      if (b.onStairs) {
        route(992, 80);
      } else {
        route(1006, 192);
      }
    } else {
      route(983, 96);
    }
  }
  
  @Override
  public void blockWhipped() {
    if (blockWhipped1) {
      blockWhipped2 = true;
    } else {
      blockWhipped1 = true;
    }
  }  
}