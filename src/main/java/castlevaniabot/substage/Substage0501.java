package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_050100;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.SPEAR_KNIGHT;

public class Substage0501 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;  
   
  public Substage0501(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState) {
    super(b, botState, api, playerController, gameState);
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
      if (obj.distanceX < 64 && obj.y >= botState.getPlayerY() - 32
          && obj.y - 32 <= botState.getPlayerY()) {
        obj.tier = 5;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          if (botState.getWeapon() != HOLY_WATER || roundTile(obj.x) != 33) {
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
          if (botState.getWeapon() != HOLY_WATER) {
            obj.tier = 4;
          } else {
            playerController.avoid(obj, botState);
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
    if (botState.getPlayerY() > 144 && botState.getPlayerX() < 287 && botState.getPlayerY() > 32) {
      if (botState.getCurrentStrategy()!= b.getAllStrategies().getMEDUSA_HEADS_PITS()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getMEDUSA_HEADS_PITS().init();
        botState.setCurrentStrategy(b.getAllStrategies().getMEDUSA_HEADS_PITS());
      }
    } else if (botState.getPlayerY() <= 144 && botState.getPlayerX() <= 255 && botState.getPlayerY() > 32) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getMEDUSA_HEADS_WALK()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getMEDUSA_HEADS_WALK().init(true);
        botState.setCurrentStrategy(b.getAllStrategies().getMEDUSA_HEADS_WALK());
      }
    } else if (botState.getPlayerY() <= 112 && botState.getPlayerX() >= 240 && botState.getPlayerX() <= 387) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getNO_JUMP_MOVING_PLATFORM()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getNO_JUMP_MOVING_PLATFORM().init(352, 255, 112);
        botState.setCurrentStrategy(b.getAllStrategies().getNO_JUMP_MOVING_PLATFORM());
      }
    } else if (botState.getPlayerY() <= 112 && botState.getPlayerX() >= 384 && botState.getPlayerX() <= 496) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getJUMP_MOVING_PLATFORM()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getJUMP_MOVING_PLATFORM().init(496, 368, 112);
        botState.setCurrentStrategy(b.getAllStrategies().getJUMP_MOVING_PLATFORM());
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }
  
  @Override
  public void readGameObjects() {
    if (botState.getPlayerX() >= 512) {
      if (!blockBroken && api.readPPU(BLOCK_050100) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("05-01-01");
      }
      if (!blockWhipped) {
        b.addBlock(624, 80);
      }
    }
    if (botState.getPlayerX() >= 480 || botState.getPlayerY() >= 144) {
      b.addDestination(480, 112);
    } else {
      b.addDestination(25, 144);
    }
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerY() <= 144) {
      if (botState.getPlayerX() < 272) {
        route(25, 144);
      } else if (botState.getPlayerX() < 400) {
        route(352, 112);
      } else {
        route(480, 112);
      }
    } else if (botState.getPlayerX() >= 224) {
      route(224, 176);
    } else {
      route(96, 192);
    }
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerY() <= 144) {
      if (botState.getPlayerX() < 272) {
        route(255, 112);
      } else if (botState.getPlayerX() < 400) {
        route(383, 112);
      } else {
        route(727, 112);
      }      
    } else {
      route(727, 176);      
    }
  }
}