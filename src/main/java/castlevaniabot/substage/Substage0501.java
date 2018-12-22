package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.AllStrategies;
import nintaco.api.API;

import java.util.Map;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_050100;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.SPEAR_KNIGHT;

public class Substage0501 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;
  private MapRoutes next;

  public Substage0501(final BotState botState, final API api, PlayerController playerController, GameState gameState, Map<String, MapRoutes> allMapRoutes) {
    super(botState, api, playerController, gameState, allMapRoutes.get("05-01-00"));
    next = allMapRoutes.get("05-01-01");
  }

  @Override
  public void init() {
    super.init();
    blockWhipped = blockBroken = false;
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
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies) {
    if (botState.getPlayerY() > 144 && botState.getPlayerX() < 287 && botState.getPlayerY() > 32) {
      if (botState.getCurrentStrategy()!= allStrategies.getMEDUSA_HEADS_PITS()) {
        clearTarget(targetedObject);
        allStrategies.getMEDUSA_HEADS_PITS().init();
        botState.setCurrentStrategy(allStrategies.getMEDUSA_HEADS_PITS());
      }
    } else if (botState.getPlayerY() <= 144 && botState.getPlayerX() <= 255 && botState.getPlayerY() > 32) {
      if (botState.getCurrentStrategy() != allStrategies.getMEDUSA_HEADS_WALK()) {
        clearTarget(targetedObject);
        allStrategies.getMEDUSA_HEADS_WALK().init(true);
        botState.setCurrentStrategy(allStrategies.getMEDUSA_HEADS_WALK());
      }
    } else if (botState.getPlayerY() <= 112 && botState.getPlayerX() >= 240 && botState.getPlayerX() <= 387) {
      if (botState.getCurrentStrategy() != allStrategies.getNO_JUMP_MOVING_PLATFORM()) {
        clearTarget(targetedObject);
        allStrategies.getNO_JUMP_MOVING_PLATFORM().init(352, 255, 112);
        botState.setCurrentStrategy(allStrategies.getNO_JUMP_MOVING_PLATFORM());
      }
    } else if (botState.getPlayerY() <= 112 && botState.getPlayerX() >= 384 && botState.getPlayerX() <= 496) {
      if (botState.getCurrentStrategy() != allStrategies.getJUMP_MOVING_PLATFORM()) {
        clearTarget(targetedObject);
        allStrategies.getJUMP_MOVING_PLATFORM().init(496, 368, 112);
        botState.setCurrentStrategy(allStrategies.getJUMP_MOVING_PLATFORM());
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies);
    }
  }
  
  @Override
  public void readGameObjects() {
    if (botState.getPlayerX() >= 512) {
      if (!blockBroken && api.readPPU(BLOCK_050100) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = next;
      }
      if (!blockWhipped) {
        gameState.addBlock(624, 80, botState);
      }
    }
    if (botState.getPlayerX() >= 480 || botState.getPlayerY() >= 144) {
      gameState.addDestination(480, 112, botState);
    } else {
      gameState.addDestination(25, 144, botState);
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