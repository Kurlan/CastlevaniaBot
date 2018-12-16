package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;

import java.util.concurrent.*;
import static castlevaniabot.model.gameelements.Addresses.*;
import static castlevaniabot.model.gameelements.GameObjectType.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class Substage0201 extends Substage {
  
  private int blocksWhipped;
  private boolean blocksBroken;
  private boolean useRedBatDamageBoost;
  
  public Substage0201(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override public void init() {
    super.init();
    blocksWhipped = 0;
    blocksBroken = false;
    useRedBatDamageBoost = ThreadLocalRandom.current().nextBoolean();
    mapRoutes = b.allMapRoutes.get("02-01-00");
  }  
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    if (obj.type == RED_BAT) {
      if (obj.distanceX < 104 && obj.y + 88 >= b.playerY 
          && obj.y - 40 <= b.playerY && ((obj.left && obj.x >= b.playerX - 40) 
              || (!obj.left && obj.x <= b.playerX + 40))) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON && (b.currentTile.getX() < 16 ^ (obj.x >= 272))) {
      switch(obj.type) {
        case BLOCK:
         obj.tier = 1; break;
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 22:
              if (b.playerY >= 160) {
                obj.tier = 2; 
                obj.subTier = 3;
              }
              break;
            case 19: obj.tier = 2; obj.subTier = 2; break;
            case 26: obj.tier = 2; obj.subTier = 1; break;
            default: obj.tier = 2; break;
          }
          break;
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
  public void pickStrategy(TargetedObject targetedObject) {
    
    if (useRedBatDamageBoost && b.playerY == 144 && b.playerX >= 128 
        && b.playerX < 208 && b.playerY < 200) {
      if (b.strategy != b.RED_BAT_DAMAGE_BOOST) {
        clearTarget(targetedObject);
        b.strategy = b.RED_BAT_DAMAGE_BOOST;
        b.strategy.init();
      }
      return;
    }
    
    super.pickStrategy(targetedObject);
  }  
  
  @Override
  public void readGameObjects() {
    if (b.currentTile.getX() < 16) {
      b.addDestination(72, 224);
      if (b.currentTile.getX() >= 8 && b.currentTile.getY() >= 10) {
        if (!blocksBroken && api.readPPU(BLOCK_020100) == 0x00 
            && api.readPPU(BLOCK_020101) == 0x00) {
          blocksWhipped = 2;
          blocksBroken = true;
          mapRoutes = b.allMapRoutes.get("02-01-01");
        } 
        if (blocksWhipped < 2) {
          b.addBlock(256, 176);
          b.addBlock(256, 192);
        }
      }
    } else {
      b.addDestination(496, 112);
    }
  }
  
  @Override
  public void routeLeft() {
    if (b.playerX < 256) {
      if (b.playerY < 160) {
        route(8, 112);
      } else {
        route(24, 208);
      }
    } else {
      if (b.playerY < 160) {
        route(264, 128);
      } else {
        route(328, 208);
      }      
    }
  }
  
  @Override
  public void routeRight() {
    if (b.playerX < 256) {
      route(248, 208);
    } else {
      if (b.playerY < 160) {
        route(504, 112);
      } else {
        route(488, 208);
      }      
    }
  }  
  
  @Override
  public void blockWhipped() {
    ++blocksWhipped;
  }
  
  public void redBatDamageBoostDone() {
    useRedBatDamageBoost = false;
  }
}