package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_050100;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.SPEAR_KNIGHT;

public class Substage0501 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;  
   
  public Substage0501(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    blockWhipped = blockBroken = false;
    mapRoutes = b.allMapRoutes.get("05-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.x < 287) {
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
          if (b.weapon != HOLY_WATER || roundTile(obj.x) != 33) {
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
          obj.tier = 6;
          break;
      }
    }    
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    if (b.playerY > 144 && b.playerX < 287 && b.playerY > 32) {  
      if (b.strategy != b.MEDUSA_HEADS_PITS) {
        clearTarget(targetedObject);
        b.MEDUSA_HEADS_PITS.init();
        b.strategy = b.MEDUSA_HEADS_PITS; 
      }
    } else if (b.playerY <= 144 && b.playerX <= 255 && b.playerY > 32) {
      if (b.strategy != b.MEDUSA_HEADS_WALK) {
        clearTarget(targetedObject);
        b.MEDUSA_HEADS_WALK.init(true);
        b.strategy = b.MEDUSA_HEADS_WALK;  
      }
    } else if (b.playerY <= 112 && b.playerX >= 240 && b.playerX <= 387) {
      if (b.strategy != b.NO_JUMP_MOVING_PLATFORM) {
        clearTarget(targetedObject);
        b.NO_JUMP_MOVING_PLATFORM.init(352, 255, 112);
        b.strategy = b.NO_JUMP_MOVING_PLATFORM;
      }
    } else if (b.playerY <= 112 && b.playerX >= 384 && b.playerX <= 496) {
      if (b.strategy != b.JUMP_MOVING_PLATFORM) {
        clearTarget(targetedObject);
        b.JUMP_MOVING_PLATFORM.init(496, 368, 112);
        b.strategy = b.JUMP_MOVING_PLATFORM;
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }
  
  @Override
  public void readGameObjects() {
    if (b.playerX >= 512) {
      if (!blockBroken && api.readPPU(BLOCK_050100) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("05-01-01");
      }
      if (!blockWhipped) {
        b.addBlock(624, 80);
      }
    }
    if (b.playerX >= 480 || b.playerY >= 144) {
      b.addDestination(480, 112);
    } else {
      b.addDestination(25, 144);
    }
  }  

  @Override
  public void routeLeft() {
    if (b.playerY <= 144) {
      if (b.playerX < 272) {
        route(25, 144);
      } else if (b.playerX < 400) {
        route(352, 112);
      } else {
        route(480, 112);
      }
    } else if (b.playerX >= 224) {
      route(224, 176);
    } else {
      route(96, 192);
    }
  }
  
  @Override
  public void routeRight() {
    if (b.playerY <= 144) {
      if (b.playerX < 272) {
        route(255, 112);
      } else if (b.playerX < 400) {
        route(383, 112);
      } else {
        route(727, 112);
      }      
    } else {
      route(727, 176);      
    }
  }
}