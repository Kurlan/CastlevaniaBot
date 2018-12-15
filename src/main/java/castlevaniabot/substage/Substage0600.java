package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;
import castlevaniabot.substage.Substage;

import static castlevaniabot.Addresses.*;
import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage0600 extends Substage {
  
  private boolean blockWhipped0;
  private boolean blockBroken0;
  private boolean blockWhipped1;
  private boolean blockBroken1;
   
  public Substage0600(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    b.CRUSHER.init();
    blockWhipped1 = blockBroken1 = blockWhipped0 = blockBroken0 = false;
    mapRoutes = b.allMapRoutes.get("06-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.y >= 96 && obj.x >= 320 && obj.x < 512) {
      return;
    }
    
    if (obj.type == BONE_TOWER) {
      if (obj.distanceX < 80 && obj.y >= b.playerY - 16 && obj.y <= b.playerY) {
        obj.tier = 7;
      }
    } else if (obj.type == GHOST) {
      if (obj.distanceX < 80) {
        obj.tier = 8;
      }
    } else if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= b.playerY - 32 && obj.y1 <= b.playerY)
              && ((obj.left && obj.x2 >= b.playerX - 16) 
                  || (!obj.left && obj.x1 <= b.playerX + 16))) {
        obj.tier = 9;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case BLOCK:
          obj.tier = (b.playerX >= 672 && b.playerY >= 96) ? 5 : 1; break;
        case CANDLES: 
          obj.tier = 2; break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:
        case WHIP_UPGRADE:
        case INVISIBLE_POTION:
          obj.tier = 3; break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          obj.tier = 4; break;
        case AXE_WEAPON:
        case BOOMERANG_WEAPON:
        case DAGGER_WEAPON:        
        case STOPWATCH_WEAPON:
          if (b.weapon != HOLY_WATER) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;          
        case HOLY_WATER_WEAPON:
        case PORK_CHOP:
          obj.tier = 6;
          break;
      }
    }    
  }

  @Override
  public void pickStrategy() {
    if (b.CRUSHER.isActive()) {
      if (b.strategy != b.CRUSHER) {
        clearTarget();
        b.strategy = b.CRUSHER;
      }
    } else {
      super.pickStrategy();
    }
  }
  
  @Override
  public void readGameObjects() {
    if (b.playerX >= 512) {
      if (!blockBroken0 && api.readPPU(BLOCK_060000) == 0x00) {
        blockWhipped0 = blockBroken0 = true;
        mapRoutes = b.allMapRoutes.get("06-00-01");
      }
      if (!blockWhipped0) {
        b.addBlock(704, 144);
      }
    } else if (b.playerX < 256) {
      if (!blockBroken1 && api.readPPU(BLOCK_060001) == 0x00) {
        blockWhipped1 = blockBroken1 = true;
        mapRoutes = b.allMapRoutes.get("06-00-02");
      }
      if (!blockWhipped1) {
        b.addBlock(128, 160);
      }
    }
    
    if (b.playerY >= 96 && b.playerX >= 512) {
      b.addDestination(521, 208);
    } else {
      b.addDestination(680, 48);
    }
  }  

  @Override
  public void routeLeft() {
    route(41, 176);
  }
  
  @Override
  public void routeRight() {
    if (b.playerX >= 672 && b.playerY > 96) {
      route(759, 144);
    } else if (b.playerY <= 128) {
      route(727, 80);
    } else {
      route(663, 208);
    }
  }
}