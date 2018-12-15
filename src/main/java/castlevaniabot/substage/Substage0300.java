package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;
import castlevaniabot.strategy.Strategy;

import static castlevaniabot.Addresses.*;
import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage0300 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;
  private boolean aboutToGetCrystalBall;
 
  public Substage0300(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    aboutToGetCrystalBall = blockWhipped = blockBroken = false;
    mapRoutes = b.allMapRoutes.get("03-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= b.playerY - 32 && obj.y1 <= b.playerY)
              && ((obj.left && obj.x2 >= b.playerX - 16) 
                  || (!obj.left && obj.x1 <= b.playerX + 16))) {
        obj.tier = 7;
      }
    } else if (obj.type == DESTINATION || obj.type == PHANTOM_BAT 
        || obj.type == CRYSTAL_BALL) {
      obj.tier = 0;
    } else if (obj.type == GHOUL) {
      if (obj.distanceX < 80 && obj.y <= b.playerY + 8 
          && obj.y >= b.playerY - 56
              && (obj.left ^ (b.playerX > obj.x))) {
        obj.tier = 6;
      }
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          if (roundTile(obj.x) != 22 || b.weapon != HOLY_WATER) {
            obj.tier = 1;
            if ((obj.y < 160) ^ (b.playerY >= 160)) {
              obj.subTier = 1;
            }
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
        case DAGGER_WEAPON:
        case STOPWATCH_WEAPON:
        case AXE_WEAPON:
          if (b.playerX < 512) {
            if (b.weapon != HOLY_WATER) {
              obj.tier = 5;
            } else {
              b.avoid(obj);
            }
          }
          break;          
        case HOLY_WATER_WEAPON: obj.tier = 5; break;
      }        
    }    
  }  
  
  @Override
  Strategy selectStrategy(final GameObject target) {
    if (target == null && aboutToGetCrystalBall) {
      return b.GOT_CRYSTAL_BALL;
    } else {
      return super.selectStrategy(target);
    }
  }
  
  @Override
  public void readGameObjects() {
    if (b.playerX >= 640) {
      if (!blockBroken && api.readPPU(BLOCK_030000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("03-00-01");
      }
      if (!blockWhipped) {
        b.addBlock(688, 176);
      }
    } 
    if (!blockWhipped) {
      b.addDestination(751, 208);
    }
  }
  
  @Override
  public void routeLeft() {
    if (b.playerY <= 160 && b.playerX < 320) {
      route(9, 112);
    } else {
      route(9, 208);
    }
  }
  
  @Override
  public void routeRight() {
    if (b.playerY <= 160 && b.playerX > 704) {
      route(751, 144);
    } else {
      route(751, 208);
    }
  }  
  
  @Override
  public void blockWhipped() {
    blockWhipped = true;
  }  
  
  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }
}