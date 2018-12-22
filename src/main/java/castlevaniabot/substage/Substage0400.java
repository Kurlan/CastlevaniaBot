package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import nintaco.api.API;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_040000;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_040001;
import static castlevaniabot.model.gameelements.GameObjectType.BLACK_BAT;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.SPEAR_KNIGHT;

public class Substage0400 extends Substage {
  
  private int blocksWhipped;
  private boolean blocksBroken;
  private boolean triggedTreasure;
  private boolean collectTreasure;
  private MapRoutes next;

  public Substage0400(final BotState botState, final API api, PlayerController playerController, GameState gameState,  Map<String, MapRoutes> allMapRoutes) {
    super(botState, api, playerController, gameState, allMapRoutes.get("04-00-00"));
    next = allMapRoutes.get("04-00-01");
  }

  @Override
  public void init() {
    super.init();
    blocksWhipped = 0;
    triggedTreasure = blocksBroken = false;
    collectTreasure = ThreadLocalRandom.current().nextBoolean();
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    if (obj.type == BLACK_BAT) {
      if (obj.active && obj.distanceX < 96 && obj.y + 88 >= botState.getPlayerY()
          && obj.y - 40 <= botState.getPlayerY() && ((obj.left && obj.x >= botState.getPlayerX() - 40)
              || (!obj.left && obj.x <= botState.getPlayerX() + 40))) {
        obj.tier = 7;
      }
    } else if (obj.type == SPEAR_KNIGHT) {
      if (obj.distanceX < 64 && obj.y >= botState.getPlayerY() - 32
          && obj.y - 32 <= botState.getPlayerY()) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case BLOCK: 
          obj.tier = 1; break;
        case CANDLES:
          if (roundTile(obj.x) != 21 || botState.getWeapon() != HOLY_WATER) {
            obj.tier = 2;
          }
          break;
        case MONEY_BAG:
          if (collectTreasure) {
            obj.tier = 3;
          }
          break;
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
          obj.tier = 5;
          break;
      }
    }    
  }
  
  @Override
  public void readGameObjects() {
    if (!blocksBroken && api.readPPU(BLOCK_040000) == 0x00 
        && api.readPPU(BLOCK_040001) == 0x00) {
      blocksWhipped = 2;
      blocksBroken = true;
      mapRoutes = next;
    } 
    if (blocksWhipped < 2) {
      gameState.addBlock(480, 112, botState);
      gameState.addBlock(480, 128, botState);
    }
    
    if (botState.getPlayerX() >= 476) {
      triggedTreasure = true;
    }
    if (blocksBroken && !triggedTreasure) {
      gameState.addDestination(476, 144, botState);
    } else {
      gameState.addDestination(392, 48, botState);
    }
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerY() >= 176) {
      route(281, 208);
    } else {
      route(281, 96);
    }
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerY() >= 176) {
      route(471, 208);
    } else {
      route(471, 144);
    }
  }

  @Override
  public void blockWhipped() {
    ++blocksWhipped;
  }
}