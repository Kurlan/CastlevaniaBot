package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;
import castlevaniabot.substage.Substage;

import static castlevaniabot.Addresses.*;
import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage0700 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;
  
  public Substage0700(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    blockWhipped = blockBroken = false;
    mapRoutes = b.allMapRoutes.get("07-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == GHOST) {
      obj.tier = 8;
    } else if (obj.type == FLEAMAN) {
      obj.tier = 7;
    } else if (obj.type == WHITE_SKELETON) {
      obj.tier = 6;
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES: 
          obj.tier = 1; break;
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
          obj.tier = 5;
          break;
      }
    }    
  }

  @Override
  public void readGameObjects() {
    if (b.playerX < 384) {
      if (!blockBroken && api.readPPU(BLOCK_070000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("07-00-01");
      }
      if (!blockWhipped) {
        b.addBlock(288, 160);
      }
    }    
    b.addDestination(88, 48);
  }  

  @Override
  public void routeLeft() {
    if (b.playerY < 128) {
      route(88, 48);
    } else {
      route(41, 160);
    }
  }
  
  @Override
  public void routeRight() {
    route(743, 192);
  }

  @Override void blockWhipped() {
    blockWhipped = true;
  }
}