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

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.SPEAR_KNIGHT;

public class Substage0500 extends Substage {
  
  private boolean treasureTriggered;
  private GameStateRestarter gameStateRestarter;
  
  public Substage0500(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("05-00-00"));
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    treasureTriggered = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    
    if (obj.x >= 512) {
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
          if (botState.getWeapon() != HOLY_WATER || roundTile(obj.x) != 29) {
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
          obj.tier = 4;
          break;
      }
    }    
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 544) {
      if (botState.getCurrentStrategy() != allStrategies.getMEDUSA_HEADS_WALK()) {
        clearTarget(targetedObject);
        allStrategies.getMEDUSA_HEADS_WALK().init(true);
        botState.setCurrentStrategy(allStrategies.getMEDUSA_HEADS_WALK());
      }
    } else if (!treasureTriggered && botState.getPlayerX() >= 288 && botState.getPlayerX() < 320) {
      if (botState.getCurrentStrategy() != allStrategies.getWAIT()) {
        clearTarget(targetedObject);
        allStrategies.getWAIT().init(304, 80);
        botState.setCurrentStrategy(allStrategies.getWAIT());
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
    }
  }
  
  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    gameState.addDestination(72, 48, botState);
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    route(41, 80, botState, gameState);
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    route(759, 112, botState, gameState);
  }

  @Override
  public void treasureTriggered(BotState botState) {
    treasureTriggered = true;
  }
}