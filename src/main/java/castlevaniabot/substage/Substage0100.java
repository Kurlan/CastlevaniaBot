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
import static castlevaniabot.model.gameelements.Addresses.BLOCK_010000;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.GHOUL;
import static castlevaniabot.model.gameelements.GameObjectType.PANTHER;

public class Substage0100 extends Substage {

  private boolean blockWhipped;
  private boolean blockBroken;
  private MapRoutes next;
  private GameStateRestarter gameStateRestarter;

  public Substage0100( final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("01-00-00"));
    this.next = allMapRoutes.get("01-00-01");
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    botState.setCurrentStrategy(null);
    blockWhipped = blockBroken = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.type == GHOUL) {
      if (obj.distanceX < 80 && obj.distanceY < 16 
          && (obj.left ^ (botState.getPlayerX() > obj.x))) {
        obj.tier = 6;
      }
    } else if (obj.type == PANTHER) {
      if (obj.active) {
        if (obj.distanceY < 16) {
          obj.tier = 6;
        }
      } 
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES: 
          obj.tier = 1;
          switch (roundTile(obj.x)) { 
            case 58: obj.subTier = 1; break;
            case 62: obj.subTier = 2; break;              
          }
          break;          
        case BLOCK:             
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
        case STOPWATCH_WEAPON:  
          if (botState.getWeapon() != HOLY_WATER) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
          }
          break;          
        case HOLY_WATER_WEAPON: obj.tier = 5; break;
      }        
    }    
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0 && botState.getCurrentTile().getY() == 7
        && botState.getCurrentTile().getX() >= 52 && botState.getCurrentTile().getX() <= 56 && isPantherResting(gameState)) {
      if (botState.getCurrentStrategy() != allStrategies.getUSE_WEAPON()) {
        clearTarget(targetedObject);
        allStrategies.getUSE_WEAPON().init(879, 112, true, false);
        botState.setCurrentStrategy(allStrategies.getUSE_WEAPON());
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies, botState, gameState);
    }
  }
  
  private boolean isPantherResting(GameState gameState) {
    final GameObject[] objs = gameState.getGameObjects();
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      if (!obj.active && obj.type == PANTHER && obj.x > 912) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    if (botState.getPlayerY() >= 160 && botState.getPlayerX() >= 944 && botState.getPlayerX()< 1136) {
      if (!blockBroken && api.readPPU(BLOCK_010000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = next;
      }
      if (!blockWhipped) {
        gameState.addBlock(1008, 144, botState);
      }
    }
    if (botState.getPlayerX() < 768) {
      gameState.addDestination(992, 144, botState);  // central platform
    } else {
      gameState.addDestination(1520, 112, botState); // exit door
    }
  }
  
  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    route(9, 208, botState, gameState);
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 1392 && botState.getPlayerY() <= 112) {
      route(1511, 112, botState, gameState);
    } else {
      route(1511, 208, botState, gameState);
    }
  }  
  
  @Override
  public void blockWhipped(BotState botState) {
    blockWhipped = true;
  }  
}