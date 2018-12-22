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
import static castlevaniabot.model.gameelements.Addresses.BLOCK_170100;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_170101;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;

public class Substage1701 extends Substage {
  
  private boolean blockWhipped1;
  private boolean blockBroken1;

  private boolean blockWhipped2;
  private boolean blockBroken2;
  
  private boolean usedStopwatch;
  private boolean killedLowerSkeleton;
  private boolean killedUpperSkeleton;
  private MapRoutes next;
  private GameStateRestarter gameStateRestarter;

  public Substage1701(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("17-01-00"));
    this.next = allMapRoutes.get("17-01-01");
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    killedUpperSkeleton = killedLowerSkeleton = usedStopwatch = blockWhipped1 
        = blockBroken1 = blockWhipped2 = blockBroken2 = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    
    if (usedStopwatch) {
      if (obj.type == DESTINATION) {
        obj.tier = 0;
      }
      return;
    }
    
    switch(obj.type) {
      case EAGLE:
        if (obj.distanceX < 64 && obj.y1 <= botState.getPlayerY()
            && obj.y2 >= botState.getPlayerY() - 32) {
          obj.tier = 8;
        }
        break;
      case FLEAMAN:
        if (!botState.isOnStairs() && obj.distanceX < 48 && obj.y1 <= botState.getPlayerY()
            && obj.y2 >= botState.getPlayerY() - 48) {
          obj.tier = 7;
        }
        break;
      case WHITE_SKELETON:
        if (obj.distanceX < 128 && obj.y1 <= botState.getPlayerY() + 16
            && obj.y2 >= botState.getPlayerY() - 64) {
          obj.tier = 6;
        }
        break;
      case DESTINATION:
        obj.tier = 0;
        break;
      case BLOCK:      
        if (botState.getPlayerY() > 112 && botState.getPlayerX() > 364 && botState.getPlayerX() < 496) {
          obj.tier = 6; 
        }
        break;
      case PORK_CHOP:
        obj.tier = 9;
        break;
      default:
        if (obj.distance < HORIZON && (obj.x < 64 || botState.getPlayerX() > 480)
            && obj.y1 <= botState.getPlayerY()) {
          switch(obj.type) {
            case CANDLES:
              switch(roundTile(obj.x)) {
                case 34:
                  if (botState.getWeapon() != STOPWATCH) {
                    obj.tier = 1;
                  }
                  break;
                case 38: // skip this candle
                  break;
                default:
                  obj.tier = 1;
                  break;
              }
              break;
            case MONEY_BAG:
            case SMALL_HEART:
            case LARGE_HEART:        
            case INVISIBLE_POTION:
            case WHIP_UPGRADE:
              obj.tier = 3; break;
            case CROSS:          
            case DOULE_SHOT:
            case TRIPLE_SHOT:
              obj.tier = 4; break;
            case DAGGER_WEAPON:       
            case BOOMERANG_WEAPON:
            case AXE_WEAPON:                         
            case HOLY_WATER_WEAPON:
              if (botState.getWeapon() != STOPWATCH) {
                obj.tier = 5; 
              } else {
                playerController.avoid(obj, botState);
              }
              break;
            case EXTRA_LIFE:
            case STOPWATCH_WEAPON:
              obj.tier = 5; break;
          }
        } 
      break;
    }    
  }
  
  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    if (!usedStopwatch && botState.getPlayerX() <= 480 && botState.getPlayerY() <= 96
        && botState.getWeapon() == STOPWATCH && botState.getHearts() >= 5) {
      playerController.useWeapon(gameState);
      usedStopwatch = true;
    } else if (!killedLowerSkeleton && botState.getPlayerX() >= 496 && botState.getPlayerY() > 128) {
      if (botState.getCurrentStrategy() == allStrategies.getSKELETON_WALL()) {
        if (allStrategies.getSKELETON_WALL().done) {
          killedLowerSkeleton = true;
          super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
        }
      } else {
        clearTarget(targetedObject);
        allStrategies.getSKELETON_WALL().init(726, 192, 136);
        botState.setCurrentStrategy(allStrategies.getSKELETON_WALL());
      }
    } else if (!killedUpperSkeleton && botState.getPlayerX() >= 496 && botState.getPlayerY() <= 128) {
      if (botState.getCurrentStrategy() == allStrategies.getSKELETON_WALL()) {
        if (allStrategies.getSKELETON_WALL().done) {
          killedUpperSkeleton = true;
          super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
        }
      } else {
        clearTarget(targetedObject);
        allStrategies.getSKELETON_WALL().init(704, 128);
        botState.setCurrentStrategy(allStrategies.getSKELETON_WALL());
      } 
    } else {
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
    }
  }

  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 368 && botState.getPlayerX() < 512) {
      final boolean block1 = api.readPPU(BLOCK_170100) == 0x00;
      final boolean block2 = api.readPPU(BLOCK_170101) == 0x00;
      if (!blockBroken1 && block1) {
        blockWhipped1 = blockBroken1 = true;
        if (block1 && block2) {
          mapRoutes = next;
        }
      }
      if (!blockBroken2 && block2) {
        blockWhipped2 = blockBroken2 = true;
        if (block1 && block2) {
          mapRoutes = next;
        }
      }
      if (!blockWhipped1) {
        gameState.addBlock(480, 160, botState);
      }
      if (!blockWhipped2) {
        gameState.addBlock(480, 176, botState);
      }
    }    

    if (botState.getPlayerX() >= 176) {
      gameState.addDestination(320, 223, botState);
    } else {
      gameState.addDestination(0, 128, botState);
    }
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 496) {
      if (botState.getPlayerY() <= 128 || (botState.isOnStairs() && botState.getPlayerY() < 192)) {
        route(448, 96, botState, gameState);
      } else {
        route(521, 192, botState, gameState);
      }
    } else if (botState.getPlayerX() >= 176) {
      if (botState.getPlayerY() <= 96) {
        route(336, 96, botState, gameState);
      } else if (botState.getPlayerX() < 368 && botState.getPlayerY() < 192) {
        route(256, 160, botState, gameState);
      } else if (botState.getPlayerX() >= 368) {
        route(336, 192, botState, gameState);
      } else {
        route(320, 223, botState, gameState);
      }
    } else {
      route(0, 128, botState, gameState);
    }
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 496) {
      if (botState.getPlayerY() <= 128 || (botState.isOnStairs() && botState.getPlayerY() < 192)) {
        route(727, 128, botState, gameState);
      } else {
        route(727, 192, botState, gameState);
      }
    } else if (botState.getPlayerX() >= 176) {
      if (botState.getPlayerY() <= 96) {
        route(727, 128, botState, gameState);
      } else if (botState.getPlayerX() < 368 && botState.getPlayerY() < 192) {
        route(359, 192, botState, gameState);
      } else if (botState.getPlayerX() >= 368) {
        if (blockBroken1 && blockBroken2) {
          route(487, 192, botState, gameState);
        } else {
          route(471, 192, botState, gameState);
        }
      } else {
        route(359, 192, botState, gameState);
      }
    } else {
      route(151, 192, botState, gameState);
    }
  }
  
  @Override
  public void blockWhipped(BotState botState) {
    if (blockWhipped1) {
      blockWhipped2 = true;
    } else {
      blockWhipped1 = true;
    }
  }  
}