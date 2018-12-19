package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_070000;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FLEAMAN;
import static castlevaniabot.model.gameelements.GameObjectType.GHOST;
import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

public class Substage0700 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;
  
  public Substage0700(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState) {
    super(b, botState, api, playerController, gameState);
  }

  @Override
  public void init() {
    super.init();
    blockWhipped = blockBroken = false;
    mapRoutes = b.allMapRoutes.get("07-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == GHOST) {
      obj.tier = 8;
    } else if (obj.type == FLEAMAN) {
      obj.tier = 7;
    } else if (obj.type == WHITE_SKELETON) {
      obj.tier = 6;
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES: 
          obj.tier = 1; break;
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
          obj.tier = 5;
          break;
      }
    }    
  }

  @Override
  public void readGameObjects() {
    if (botState.getPlayerX() < 384) {
      if (!blockBroken && api.readPPU(BLOCK_070000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("07-00-01");
      }
      if (!blockWhipped) {
        b.addBlock(288, 160);
      }
    }    
    gameState.addDestination(88, 48, botState);
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerY() < 128) {
      route(88, 48);
    } else {
      route(41, 160);
    }
  }
  
  @Override
  public void routeRight() {
    route(743, 192);
  }

  @Override
  public void blockWhipped() {
    blockWhipped = true;
  }
}