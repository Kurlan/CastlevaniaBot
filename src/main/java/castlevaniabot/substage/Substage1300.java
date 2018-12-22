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

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FLEAMAN;
import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

public class Substage1300 extends Substage {
  
  private boolean waited;
  private GameStateRestarter gameStateRestarter;
  
  public Substage1300(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("13-00-00"));
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    waited = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    
    if (obj.type == FLEAMAN) {
      if (obj.distanceX < 128 && obj.y1 <= botState.getPlayerY()
          && obj.y2 >= botState.getPlayerY() - 48) {
        obj.tier = 7;
      }
    } else if (obj.type == WHITE_SKELETON) {
      if (obj.distanceX < 128 && obj.y1 <= botState.getPlayerY() + 16
          && obj.y2 >= botState.getPlayerY() - 64) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          if (obj.y > 132 && botState.getWhipLength() != 2) {
            obj.tier = 6;
          } else if (roundTile(obj.x) != 30 || botState.getPlayerX() < 480) {
            obj.tier = 1; 
          }
          break;
        case BLOCK: 
          obj.tier = 2; break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:        
        case INVISIBLE_POTION:
        case WHIP_UPGRADE:
          if (obj.y > 132 && botState.getWhipLength() != 2) {
            obj.tier = 7;
          } else {
            obj.tier = 3; 
          }
          break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          obj.tier = 4; break;
        case DAGGER_WEAPON:
          if (botState.getWeapon() == NONE || botState.getWeapon() == STOPWATCH) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
          }
          break;        
        case BOOMERANG_WEAPON:
          if (botState.getWeapon() != HOLY_WATER) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
          }
          break;
        case AXE_WEAPON:
          if (botState.getWeapon() != HOLY_WATER && botState.getWeapon() != BOOMERANG) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
          }
          break;          
        case STOPWATCH_WEAPON:  
          if (botState.getWeapon() == NONE) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
          }
          break;          
        case HOLY_WATER_WEAPON:
        case PORK_CHOP:
          if (obj.y > 132 && botState.getWhipLength() != 2) {
            obj.tier = 8;
          } else {
            obj.tier = 5; 
          }
          break;
      }
    }    
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    
    if (botState.getCurrentStrategy() == allStrategies.getWAIT()) {
      final GameObject skeleton = gameState.getType(WHITE_SKELETON);
      if (waited || (skeleton != null && (skeleton.x < botState.getPlayerX() - 48
          || skeleton.y > 132))) {
        super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
      }
    } else if (botState.getPlayerX() >= 368 && botState.getPlayerY() > 160 && !gameState.isObjectBelow(132)) {
      final GameObject skeleton = gameState.getType(WHITE_SKELETON);
      if (skeleton != null && skeleton.y <= 132 
          && botState.getPlayerX() < skeleton.x) {
        clearTarget(targetedObject);
        allStrategies.getWAIT().init(493, 192);
        botState.setCurrentStrategy(allStrategies.getWAIT());
        waited = false;
      } else {
        super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
    }
  }

  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    gameState.addDestination(88, 48, botState);
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    if (botState.getPlayerY() < 144) {
      route(9, 96, botState, gameState);
    } else {
      route(9, 192, botState, gameState);
    }    
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    if (botState.getPlayerY() < 144) {
      route(503, 128, botState, gameState);
    } else {
      route(503, 192, botState, gameState);
    }
  }
  
  @Override
  public void treasureTriggered(BotState botState) {
    waited = true;
  }
}