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
import static castlevaniabot.model.gameelements.Addresses.BLOCK_060000;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_060001;
import static castlevaniabot.model.gameelements.GameObjectType.BONE_TOWER;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.GHOST;

public class Substage0600 extends Substage {
  
  private boolean blockWhipped0;
  private boolean blockBroken0;
  private boolean blockWhipped1;
  private boolean blockBroken1;
  private MapRoutes next2;
  private MapRoutes next1;
  private GameStateRestarter gameStateRestarter;

  public Substage0600(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("06-00-00"));
    next1 = allMapRoutes.get("06-00-01");
    next2 = allMapRoutes.get("06-00-02");
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    botState.getCrusherStrategy().init();
    blockWhipped1 = blockBroken1 = blockWhipped0 = blockBroken0 = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    
    if (obj.y >= 96 && obj.x >= 320 && obj.x < 512) {
      return;
    }
    
    if (obj.type == BONE_TOWER) {
      if (obj.distanceX < 80 && obj.y >= botState.getPlayerY() - 16 && obj.y <= botState.getPlayerY()) {
        obj.tier = 7;
      }
    } else if (obj.type == GHOST) {
      if (obj.distanceX < 80) {
        obj.tier = 8;
      }
    } else if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= botState.getPlayerY() - 32 && obj.y1 <= botState.getPlayerY())
              && ((obj.left && obj.x2 >= botState.getPlayerX() - 16)
                  || (!obj.left && obj.x1 <= botState.getPlayerX() + 16))) {
        obj.tier = 9;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case BLOCK:
          obj.tier = (botState.getPlayerX() >= 672 && botState.getPlayerY() >= 96) ? 5 : 1; break;
        case CANDLES: 
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
        case AXE_WEAPON:
        case BOOMERANG_WEAPON:
        case DAGGER_WEAPON:        
        case STOPWATCH_WEAPON:
          if (botState.getWeapon() != HOLY_WATER) {
            obj.tier = 5;
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
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    if (allStrategies.getCRUSHER().isActive()) {
      if (botState.getCurrentStrategy() != allStrategies.getCRUSHER()) {
        clearTarget(targetedObject);
        botState.setCurrentStrategy(allStrategies.getCRUSHER());
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
    }
  }
  
  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 512) {
      if (!blockBroken0 && api.readPPU(BLOCK_060000) == 0x00) {
        blockWhipped0 = blockBroken0 = true;
        mapRoutes = next1;
      }
      if (!blockWhipped0) {
        gameState.addBlock(704, 144, botState);
      }
    } else if (botState.getPlayerX() < 256) {
      if (!blockBroken1 && api.readPPU(BLOCK_060001) == 0x00) {
        blockWhipped1 = blockBroken1 = true;
        mapRoutes = next2;
      }
      if (!blockWhipped1) {
        gameState.addBlock(128, 160, botState);
      }
    }
    
    if (botState.getPlayerY() >= 96 && botState.getPlayerX() >= 512) {
      gameState.addDestination(521, 208, botState);
    } else {
      gameState.addDestination(680, 48, botState);
    }
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    route(41, 176, botState ,gameState);
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 672 && botState.getPlayerY() > 96) {
      route(759, 144, botState, gameState);
    } else if (botState.getPlayerY() <= 128) {
      route(727, 80, botState, gameState);
    } else {
      route(663, 208, botState, gameState);
    }
  }
}