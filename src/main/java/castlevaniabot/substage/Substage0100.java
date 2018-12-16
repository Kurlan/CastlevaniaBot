package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;

import static castlevaniabot.Addresses.*;
import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage0100 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;
  
  public Substage0100(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    blockWhipped = blockBroken = false;
    mapRoutes = b.allMapRoutes.get("01-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.type == GHOUL) {
      if (obj.distanceX < 80 && obj.distanceY < 16 
          && (obj.left ^ (b.playerX > obj.x))) {
        obj.tier = 6;
      }
    } else if (obj.type == PANTHER) {
      if (obj.active) {
        if (obj.distanceY < 16) {
          obj.tier = 6;
        }
      } 
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES: 
          obj.tier = 1;
          switch (roundTile(obj.x)) { 
            case 58: obj.subTier = 1; break;
            case 62: obj.subTier = 2; break;              
          }
          break;          
        case BLOCK:             
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
        case STOPWATCH_WEAPON:  
          if (b.weapon != HOLY_WATER) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;          
        case HOLY_WATER_WEAPON: obj.tier = 5; break;
      }        
    }    
  }

  @Override
  public void pickStrategy() {
    if (b.weapon == HOLY_WATER && b.hearts > 0 && b.tile.getY() == 7
        && b.tile.getX() >= 52 && b.tile.getX() <= 56 && isPantherResting()) {
      if (b.strategy != b.USE_WEAPON) {
        clearTarget();
        b.USE_WEAPON.init(879, 112, true, false);
        b.strategy = b.USE_WEAPON;
      }
    } else {
      super.pickStrategy();
    }
  }
  
  private boolean isPantherResting() {
    final GameObject[] objs = b.gameObjects;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      if (!obj.active && obj.type == PANTHER && obj.x > 912) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public void readGameObjects() {
    if (b.playerY >= 160 && b.playerX >= 944 && b.playerX < 1136) {
      if (!blockBroken && api.readPPU(BLOCK_010000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("01-00-01");
      }
      if (!blockWhipped) {
        b.addBlock(1008, 144);
      }
    }
    if (b.playerX < 768) {
      b.addDestination(992, 144);  // central platform
    } else {
      b.addDestination(1520, 112); // exit door
    }
  }
  
  @Override
  public void routeLeft() {
    route(9, 208);
  }
  
  @Override
  public void routeRight() {
    if (b.playerX >= 1392 && b.playerY <= 112) {
      route(1511, 112);
    } else {
      route(1511, 208);
    }
  }  
  
  @Override
  public void blockWhipped() {
    blockWhipped = true;
  }  
}