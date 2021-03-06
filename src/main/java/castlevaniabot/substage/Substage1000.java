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
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.FISHMAN;
import static castlevaniabot.model.gameelements.GameObjectType.RED_BAT;

public class Substage1000 extends Substage {
  
  private boolean whippedHolyWaterCandle;
  private GameStateRestarter gameStateRestarter;
  
  public Substage1000(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("10-00-00"));
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    whippedHolyWaterCandle = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    
    if (obj.type == RED_BAT) {
      if (obj.distanceX < 96 && obj.y + 88 >= botState.getPlayerY()
          && obj.y - 40 <= botState.getPlayerY() && ((obj.left && obj.x >= botState.getPlayerX() - 40)
              || (!obj.left && obj.x <= botState.getPlayerX() + 40))) {
        obj.tier = 6;
      }
    } else if (obj.type == FISHMAN) {
      if (obj.distance > 64) {
        obj.distance = obj.distanceX >> 4;
      }
      if (obj.x <= botState.getPlayerX() + 64 && obj.x >= botState.getPlayerX() - 24
          && obj.y <= botState.getPlayerY() + 4) {
        obj.tier = 5;
      }      
    } else if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= botState.getPlayerY() - 32 && obj.y1 <= botState.getPlayerY())
              && ((obj.left && obj.x2 >= botState.getPlayerX() - 16)
                  || (!obj.left && obj.x1 <= botState.getPlayerX() + 16))) {
        obj.tier = 7;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 14: // stopwatch
              if (botState.getWeapon() == NONE) {
                obj.tier = 1;
              }
              break;
            case 46: // holy water
              if (botState.getWeapon() != HOLY_WATER) {
                obj.tier = 1;
                obj.platformX = 45; // strike very close to the candles
              }
              break;
            default:
              if (obj.x < 168 || (botState.getWhipLength() != 2
                  && (obj.x < 232 || obj.x > 576) 
                      && (obj.x < 1088 || obj.x > 1280))) {
                obj.tier = 1;
              } 
              break;
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
          if (botState.getWeapon() != BOOMERANG && botState.getWeapon() != HOLY_WATER) {
            obj.tier = 4;
          } else {
            playerController.avoid(obj, botState);
          }
          break;
        case BOOMERANG_WEAPON:       
          if (botState.getWeapon() != HOLY_WATER) {
            obj.tier = 4;
          } else {
            playerController.avoid(obj, botState);
          }
          break;           
        case DAGGER_WEAPON:        
          if (botState.getWeapon() == NONE || botState.getWeapon() == STOPWATCH) {
            obj.tier = 4;
          } else {
            playerController.avoid(obj, botState);
          }
          break;            
        case STOPWATCH_WEAPON:
          if (botState.getWeapon() == NONE) {
            obj.tier = 4;
          } else {
            playerController.avoid(obj, botState);
          }
          break;          
        case HOLY_WATER_WEAPON:
        case PORK_CHOP:
          obj.tier = 8;
          break;
      }
    }    
  }
  
  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    if (botState.getCurrentStrategy() == allStrategies.getBAT_MOVING_PLATFORM() && botState.getPlayerY() > 112) {
      if (allStrategies.getBAT_MOVING_PLATFORM().done) {
        super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
      }
    } else if (botState.getCurrentStrategy() == allStrategies.getBAT_DUAL_PLATFORMS()) {
      if (allStrategies.getBAT_DUAL_PLATFORMS().done) {
        super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
      }
    } else if (botState.getPlayerX() == 991 && botState.getPlayerY() == 160
        && !gameState.isTypePresent(RED_BAT)) {
      clearTarget(targetedObject);
      allStrategies.getBAT_DUAL_PLATFORMS().init();
      botState.setCurrentStrategy(allStrategies.getBAT_DUAL_PLATFORMS());
    } else if ((botState.getPlayerX() == 223 || botState.getPlayerX() == 767) && botState.getPlayerY() == 160
        && !gameState.isTypePresent(RED_BAT)) {
      clearTarget(targetedObject);
      allStrategies.getBAT_MOVING_PLATFORM().init();
      botState.setCurrentStrategy(allStrategies.getBAT_MOVING_PLATFORM());
    } else if (!whippedHolyWaterCandle && botState.getWeapon() != HOLY_WATER
        && botState.getPlayerY() <= 96 && botState.getPlayerX() >= 720 && botState.getPlayerX() <= 740) {
      if (botState.getCurrentStrategy() != allStrategies.getWHIP()) {
        clearTarget(targetedObject);
        allStrategies.getWHIP().init(732, 96, false, 40);
        botState.setCurrentStrategy(allStrategies.getWHIP());
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
    }
  }  

  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    if (botState.getPlayerX() < 240) {
      gameState.addDestination(223, 160, botState);
    } else if (botState.getPlayerX() < 784) {
      gameState.addDestination(767, 160, botState);
    } else if (botState.getPlayerX() < 1008) {
      gameState.addDestination(991, 160, botState);
    } else {
      gameState.addDestination(1512, 48, botState);
    }
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    if (botState.getPlayerX() < 240) {
      route(9, 160, botState, gameState);
    } else if (botState.getPlayerX() < 784) {
      route(384, 160, botState, gameState);
    } else if (botState.getPlayerX() < 1008) {
      route(960, 160, botState, gameState);
    } else {
      route(1280, 160, botState, gameState);
    }
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    if (botState.getPlayerX() < 240) {
      route(223, 160, botState, gameState);
    } else if (botState.getPlayerX() < 784) {
      route(767, 160, botState, gameState);
    } else if (botState.getPlayerX() < 1008) {
      route(991, 160, botState, gameState);
    } else {
      route(1527, 112, botState, gameState);
    }
  }

  @Override
  public void whipUsed() {
    whippedHolyWaterCandle = true;
  }
}