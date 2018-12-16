package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;

import static castlevaniabot.model.gameelements.GameObjectType.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class Substage0500 extends Substage {
  
  private boolean treasureTriggered;
  
  public Substage0500(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    treasureTriggered = false;
    mapRoutes = b.allMapRoutes.get("05-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.x >= 512) {
      return;
    }
    
    if (obj.type == SPEAR_KNIGHT) {
      if (obj.distanceX < 64 && obj.y >= b.playerY - 32 
          && obj.y - 32 <= b.playerY) {
        obj.tier = 5;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          if (b.weapon != HOLY_WATER || roundTile(obj.x) != 29) {
            obj.tier = 1; 
          }
          break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:
        case WHIP_UPGRADE:
        case INVISIBLE_POTION:
          obj.tier = 2; break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          obj.tier = 3; break;
        case AXE_WEAPON:
        case BOOMERANG_WEAPON:
        case DAGGER_WEAPON:        
        case STOPWATCH_WEAPON:
          if (b.weapon != HOLY_WATER) {
            obj.tier = 4;
          } else {
            b.avoid(obj);
          }
          break;          
        case HOLY_WATER_WEAPON:
        case PORK_CHOP:
          obj.tier = 4;
          break;
      }
    }    
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    if (b.playerX >= 544) {
      if (b.strategy != b.MEDUSA_HEADS_WALK) {
        clearTarget(targetedObject);
        b.MEDUSA_HEADS_WALK.init(true);
        b.strategy = b.MEDUSA_HEADS_WALK;
      }
    } else if (!treasureTriggered && b.playerX >= 288 && b.playerX < 320) {
      if (b.strategy != b.WAIT) {
        clearTarget(targetedObject);
        b.WAIT.init(304, 80);
        b.strategy = b.WAIT;
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }
  
  @Override
  public void readGameObjects() {
    b.addDestination(72, 48);
  }  

  @Override
  public void routeLeft() {
    route(41, 80);
  }
  
  @Override
  public void routeRight() {
    route(759, 112);
  }

  @Override
  public void treasureTriggered() {
    treasureTriggered = true;
  }
}