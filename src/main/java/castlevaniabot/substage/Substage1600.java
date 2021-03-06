package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.operation.GameStateRestarter;
import castlevaniabot.strategy.AllStrategies;
import castlevaniabot.strategy.Strategy;
import nintaco.api.API;

import java.util.Map;

import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.PHANTOM_BAT;

public class Substage1600 extends Substage {
  private GameStateRestarter gameStateRestarter;

  public Substage1600(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("16-00-00"));
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    
    if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= botState.getPlayerY() - 32 && obj.y1 <= botState.getPlayerY())
              && ((obj.left && obj.x2 >= botState.getPlayerX() - 16)
                  || (!obj.left && obj.x1 <= botState.getPlayerX() + 16))) {
        obj.tier = 6;
      }
    } else if (obj.type == PHANTOM_BAT) {
      obj.tier = 1;
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 94:
              if (botState.getPlayerX() < obj.x) {
                obj.tier = 2;
              }
              break;
            case 50:
              obj.tier = 2;
              obj.platformX = (botState.getWhipLength() == 2) ? 53 : 52;
              break;
            case 6:            
            case 86:
            case 90:
              obj.tier = 2;
              break;
          }
          break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:        
        case INVISIBLE_POTION:
        case WHIP_UPGRADE:
          if (obj.x < 128 || obj.x >= 1344) {
            obj.tier = 3; 
          }  
          break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          if (obj.x < 128 || obj.x >= 1344) {
            obj.tier = 4; 
          }      
        case DAGGER_WEAPON:       
        case BOOMERANG_WEAPON:
        case AXE_WEAPON:                         
        case HOLY_WATER_WEAPON:
          if (botState.getWeapon() != STOPWATCH && (obj.x < 128 || obj.x >= 1344)) {
            obj.tier = 5; 
          } else {
            playerController.avoid(obj, botState);
          }
          break;
        case PORK_CHOP:
        case EXTRA_LIFE:
        case STOPWATCH_WEAPON:
          obj.tier = 5; break;
      }
    }    
  }

  @Override
  Strategy selectStrategy(final GameObject target, AllStrategies allStrategies) {
    if (target == null || target.type != PHANTOM_BAT) {
      return super.selectStrategy(target, allStrategies);
    } else {
      return allStrategies.getGIANT_BAT();
    }
  }

  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    gameState.addDestination(41, 128, botState);
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    if (botState.getPlayerX() > 1328 && botState.getPlayerY() > 144) {
      route(1344, 192, botState, gameState);
    } else {
      route(41, 128, botState, gameState);
    }
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    route(1527, 192, botState, gameState);
  }  
}