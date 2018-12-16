package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.strategy.WaitStrategy;

import static castlevaniabot.model.gameelements.GameObjectType.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class Substage0801 extends Substage {
  
  private boolean treasureTriggered;  
  
  public Substage0801(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    treasureTriggered = false;
    mapRoutes = b.allMapRoutes.get("08-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == RAVEN) {
      obj.tier = 7;
    } else if (obj.type == BONE_TOWER) {
      if (obj.distanceX < 80 && obj.y >= b.playerY - 16 && obj.y <= b.playerY) {
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
        case CANDLES:
          if (b.weapon != HOLY_WATER || b.playerX < 664 
              || roundTile(obj.x) != 42) {
            // Hit stopwatch candle even if current weapon is holy water to
            // reduce risk of hitting it while attacking raven.
            obj.tier = 1;
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
  public void pickStrategy() {
    if (!treasureTriggered && b.playerX >= 627 && b.playerX < 659) {
      if (b.strategy != b.WAIT) {
        clearTarget();
        b.WAIT.init(643, 160, WaitStrategy.WaitType.KNEEL);
        b.strategy = b.WAIT;
      }
    } else {
      super.pickStrategy();
    }
  }

  @Override
  public void readGameObjects() {
    b.addDestination(1255, 128);
  }  

  @Override
  public void routeLeft() {
    route(512, 160);
  }
  
  @Override
  public void routeRight() {
    route(1255, 128);
  }
  
  @Override
  public void treasureTriggered() {
    treasureTriggered = true;
  }  

  @Override
  public void candlesWhipped(final GameObject candle) {
    if (b.weapon != NONE && roundTile(candle.x) == 42) { // stopwatch
      delayPlayer();
    }
  }
}