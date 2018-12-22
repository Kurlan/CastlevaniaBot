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

import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_170000;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.EAGLE;
import static castlevaniabot.model.gameelements.GameObjectType.FLEAMAN;
import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

public class Substage1700 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;
  
  private int stopWatchDelay;
  private boolean killedSkeleton;
  private MapRoutes next;
  private GameStateRestarter gameStateRestarter;

  public Substage1700(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("17-00-00"));
    this.next = allMapRoutes.get("17-00-01");
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    stopWatchDelay = 0;
    killedSkeleton = blockWhipped = blockBroken = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    
    if (obj.type == EAGLE) {
      if (obj.distanceX < 64 && obj.y1 <= botState.getPlayerY()
          && obj.y2 >= botState.getPlayerY() - 40) {
        obj.tier = 8;
      }
    } else if (obj.type == FLEAMAN) {
      if (!botState.isOnStairs() && obj.distanceX < 48 && obj.y1 <= botState.getPlayerY()
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
          switch(roundTile(obj.x)) {
            case 10: // skip this candle
              break;
            case 34:
              if (obj.x < botState.getPlayerX()) {
                obj.tier = 1;
              }
              break;
            case 46:
              if (botState.getPlayerX() < obj.x) {
                obj.tier = 1;
              }
              break;
            default:
              obj.tier = 1;
              break;
          }  
          break;
        case BLOCK: 
          obj.tier = 2; break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:        
        case INVISIBLE_POTION:
          if (botState.getPlayerX() >= 496) {
            obj.tier = 3;
          }
          break;
        case WHIP_UPGRADE:
          obj.tier = 3; break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          if (botState.getPlayerX() >= 496) {
            obj.tier = 4; 
          }
          break;
        case DAGGER_WEAPON:       
        case BOOMERANG_WEAPON:
        case AXE_WEAPON:                         
        case HOLY_WATER_WEAPON:
          if (botState.getPlayerX() >= 496) {
            if (botState.getWeapon() != STOPWATCH) {
              obj.tier = 5; 
            } else {
              playerController.avoid(obj, botState);
            }
          }
          break;
        case PORK_CHOP:
        case EXTRA_LIFE:
        case STOPWATCH_WEAPON:
          if (botState.getPlayerX() >= 496) {
            obj.tier = 5; 
          }
          break;
      }
    }    
  }
  
  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    
    if (botState.getWeapon() == STOPWATCH && botState.getPlayerX() < 496) {
      botState.setCurrentStrategy(null);
      if (stopWatchDelay > 0 && --stopWatchDelay == 180) {
        playerController.useWeapon(gameState);
      } 
      if (stopWatchDelay == 0 && botState.getPlayerX() < 272 && botState.getWeapon() == STOPWATCH
          && botState.getHearts() >= 5) {
        stopWatchDelay = 181;        
      } else if (stopWatchDelay < 179) {
        route(104, 48, false, botState ,gameState);
      }
    } else if (!killedSkeleton) {
      if (botState.getCurrentStrategy() == allStrategies.getSKELETON_WALL()) {
        if (allStrategies.getSKELETON_WALL().done) {
          killedSkeleton = true;
          super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
        }
      } else {
        clearTarget(targetedObject);
        allStrategies.getSKELETON_WALL().init(736, 128);
        botState.setCurrentStrategy(allStrategies.getSKELETON_WALL());
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
    }
  }  

  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    if (botState.getPlayerX() < 224) {
      if (!blockBroken && api.readPPU(BLOCK_170000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = next;
      }
      if (!blockWhipped) {
        gameState.addBlock(160, 80, botState);
      }
    }    

    if (botState.getPlayerX() >= 496) {
      gameState.addDestination(680, 48, botState);
    } else {
      gameState.addDestination(104, 48, botState);
    }
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 496) {
      route(521, 160, botState, gameState);
    } else if (botState.getPlayerX() >= 248 && botState.getPlayerY() < 168) {
      route(256, 96, botState, gameState);
    } else if (!botState.isOnStairs() && botState.getPlayerX() > 152 && botState.getPlayerX() < 208) {
      route(160, 192, botState, gameState);
    } else {
      route(41, 96, botState, gameState);
    }
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 496) {
      if (botState.isOnStairs() || (botState.getPlayerY() <= 96 && botState.getPlayerX() < 648)) {
        route(680, 48, botState, gameState);
      } else {
        route(750, 128, botState, gameState);
      }
    } else if (botState.getPlayerX() >= 248 && botState.getPlayerY() < 168) {
      route(471, 160, botState, gameState);
    } else if (!botState.isOnStairs() && botState.getPlayerX() > 152 && botState.getPlayerX() < 208) {
      route(287, 192, botState, gameState);
    } else if (botState.isOnStairs() && botState.getPlayerX() < 120) {
      route(104, 48, botState, gameState);
    } else {
      route(287, 192, botState, gameState);
    }
  }
  
  @Override
  public void blockWhipped(BotState botState) {
    blockWhipped = true;
  }  
}