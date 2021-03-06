package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.operation.GameStateRestarter;
import nintaco.api.API;

import java.util.Map;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_020000;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.FISHMAN;

public class Substage0200 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;  
  private boolean triggeredTreasure;
  private MapRoutes next;
  private GameStateRestarter gameStateRestarter;
  
  public Substage0200(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("02-00-00"));
    this.gameStateRestarter = gameStateRestarter;
    next = allMapRoutes.get("02-00-01");
  }
  
  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    blockWhipped = blockBroken = triggeredTreasure = false;
  }  
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= botState.getPlayerY() - 32 && obj.y1 <= botState.getPlayerY())
              && ((obj.left && obj.x2 >= botState.getPlayerX() - 16)
                  || (!obj.left && obj.x1 <= botState.getPlayerX() + 16))) {
        obj.tier = 7;
      }
    } else if (obj.type == FISHMAN) {
      if (obj.distance > 64) {
        obj.distance = obj.distanceX >> 4;
      }
      if (obj.distanceX < 64 && obj.y <= botState.getPlayerY() + 4) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case BLOCK:
          obj.tier = 1; break;
        case CANDLES:
          if (roundTile(obj.x) != 18 || botState.getWeapon() != HOLY_WATER) {
            obj.tier = 2;
          }
          break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:
        case WHIP_UPGRADE:
        case INVISIBLE_POTION:
          obj.tier = 3; break;
        case CROSS:
        case PORK_CHOP:
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
          obj.tier = 5; break;
      }
    }    
  }

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    route(9, 128, botState, gameState);
  }

  @Override
  public void routeRight(BotState botState, GameState gameState) {
    route(495, 160, botState, gameState);
  }

  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 384) {
      if (!blockBroken && api.readPPU(BLOCK_020000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = next;
      }
      if (!blockWhipped) {
        gameState.addBlock(464, 128, botState);
      }
    }   
    if (blockBroken && !triggeredTreasure) {
      if (botState.getPlayerX() == 464 && botState.getPlayerY() == 192) {
        if (botState.isKneeling()) {
          triggeredTreasure = true;
        } else {
          playerController.kneel();
        }
      } else {
        gameState.addDestination(464, 192, botState);
      }
    } else {
      gameState.addDestination(368, 48, botState);
    }  
  }
  
  @Override
  public void blockWhipped(BotState botState) {
    blockWhipped = true;
  }
}