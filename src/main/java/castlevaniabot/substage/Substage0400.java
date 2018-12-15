package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;

import java.util.concurrent.*;
import static castlevaniabot.Addresses.*;
import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage0400 extends Substage {
  
  private int blocksWhipped;
  private boolean blocksBroken;
  private boolean triggedTreasure;
  private boolean collectTreasure;
  
  public Substage0400(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    blocksWhipped = 0;
    triggedTreasure = blocksBroken = false;
    collectTreasure = ThreadLocalRandom.current().nextBoolean();
    mapRoutes = b.allMapRoutes.get("04-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    if (obj.type == BLACK_BAT) {
      if (obj.active && obj.distanceX < 96 && obj.y + 88 >= b.playerY 
          && obj.y - 40 <= b.playerY && ((obj.left && obj.x >= b.playerX - 40) 
              || (!obj.left && obj.x <= b.playerX + 40))) {
        obj.tier = 7;
      }
    } else if (obj.type == SPEAR_KNIGHT) {
      if (obj.distanceX < 64 && obj.y >= b.playerY - 32 
          && obj.y - 32 <= b.playerY) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case BLOCK: 
          obj.tier = 1; break;
        case CANDLES:
          if (roundTile(obj.x) != 21 || b.weapon != HOLY_WATER) {
            obj.tier = 2;
          }
          break;
        case MONEY_BAG:
          if (collectTreasure) {
            obj.tier = 3;
          }
          break;
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
          obj.tier = 5;
          break;
      }
    }    
  }
  
  @Override
  public void readGameObjects() {
    if (!blocksBroken && api.readPPU(BLOCK_040000) == 0x00 
        && api.readPPU(BLOCK_040001) == 0x00) {
      blocksWhipped = 2;
      blocksBroken = true;
      mapRoutes = b.allMapRoutes.get("04-00-01");
    } 
    if (blocksWhipped < 2) {
      b.addBlock(480, 112);
      b.addBlock(480, 128);      
    }
    
    if (b.playerX >= 476) {
      triggedTreasure = true;
    }
    if (blocksBroken && !triggedTreasure) {
      b.addDestination(476, 144);
    } else {
      b.addDestination(392, 48);
    }
  }  

  @Override
  public void routeLeft() {
    if (b.playerY >= 176) {
      route(281, 208);
    } else {
      route(281, 96);
    }
  }
  
  @Override
  public void routeRight() {
    if (b.playerY >= 176) {
      route(471, 208);
    } else {
      route(471, 144);
    }
  }

  @Override
  public void blockWhipped() {
    ++blocksWhipped;
  }
}