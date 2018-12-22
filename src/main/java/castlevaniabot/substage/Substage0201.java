package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.operation.GameStateRestarter;
import castlevaniabot.strategy.AllStrategies;
import nintaco.api.API;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_020100;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_020101;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.RED_BAT;

public class Substage0201 extends Substage {
  
  private int blocksWhipped;
  private boolean blocksBroken;
  private boolean useRedBatDamageBoost;
  private MapRoutes next;
  private GameStateRestarter gameStateRetarter;

  public Substage0201(final API api, PlayerController playerController,Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("02-01-00"));
    next = allMapRoutes.get("02-01-01");
    this.gameStateRetarter = gameStateRestarter;
  }
  
  @Override public void init(BotState botState, GameState gameState) {
    gameStateRetarter.restartSubstage(gameState, botState);
    blocksWhipped = 0;
    blocksBroken = false;
    useRedBatDamageBoost = ThreadLocalRandom.current().nextBoolean();
  }  
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    if (obj.type == RED_BAT) {
      if (obj.distanceX < 104 && obj.y + 88 >= botState.getPlayerY()
          && obj.y - 40 <= botState.getPlayerY() && ((obj.left && obj.x >= botState.getPlayerX() - 40)
              || (!obj.left && obj.x <= botState.getPlayerX() + 40))) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON && (botState.getCurrentTile().getX() < 16 ^ (obj.x >= 272))) {
      switch(obj.type) {
        case BLOCK:
         obj.tier = 1; break;
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 22:
              if (botState.getPlayerY() >= 160) {
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
          if (botState.getWeapon() != HOLY_WATER) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
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
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    
    if (useRedBatDamageBoost && botState.getPlayerY() == 144 && botState.getPlayerX() >= 128
        && botState.getPlayerX() < 208 && botState.getPlayerY() < 200) {
      if (botState.getCurrentStrategy() != allStrategies.getRED_BAT_DAMAGE_BOOST()) {
        clearTarget(targetedObject);
        botState.setCurrentStrategy(allStrategies.getRED_BAT_DAMAGE_BOOST());
        botState.getCurrentStrategy().init();
      }
      return;
    }
    
    super.pickStrategy(targetedObject, allStrategies, botState, gameState);
  }  
  
  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    if (botState.getCurrentTile().getX() < 16) {
      gameState.addDestination(72, 224, botState);
      if (botState.getCurrentTile().getX() >= 8 && botState.getCurrentTile().getY() >= 10) {
        if (!blocksBroken && api.readPPU(BLOCK_020100) == 0x00 
            && api.readPPU(BLOCK_020101) == 0x00) {
          blocksWhipped = 2;
          blocksBroken = true;
          mapRoutes = next;
        } 
        if (blocksWhipped < 2) {
          gameState.addBlock(256, 176, botState);
          gameState.addBlock(256, 192, botState);
        }
      }
    } else {
      gameState.addDestination(496, 112, botState);
    }
  }
  
  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    if (botState.getPlayerX() < 256) {
      if (botState.getPlayerY() < 160) {
        route(8, 112, botState, gameState);
      } else {
        route(24, 208, botState, gameState);
      }
    } else {
      if (botState.getPlayerY() < 160) {
        route(264, 128, botState, gameState);
      } else {
        route(328, 208, botState, gameState);
      }      
    }
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    if (botState.getPlayerX() < 256) {
      route(248, 208, botState, gameState);
    } else {
      if (botState.getPlayerY() < 160) {
        route(504, 112, botState, gameState);
      } else {
        route(488, 208, botState, gameState);
      }      
    }
  }  
  
  @Override
  public void blockWhipped(BotState botState) {
    ++blocksWhipped;
  }
  
  public void redBatDamageBoostDone() {
    useRedBatDamageBoost = false;
  }
}