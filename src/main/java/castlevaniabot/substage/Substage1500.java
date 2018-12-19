package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_150000;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_150001;
import static castlevaniabot.model.gameelements.GameObjectType.BONE_TOWER;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.RED_BONES;
import static castlevaniabot.model.gameelements.GameObjectType.RED_SKELETON;
import static castlevaniabot.model.gameelements.GameObjectType.RED_SKELETON_RISING;

public class Substage1500 extends Substage {
  
  private boolean blockWhipped1;
  private boolean blockBroken1;

  private boolean blockWhipped2;
  private boolean blockBroken2;   
  
  public Substage1500(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState) {
    super(b, botState, api, playerController, gameState);
  }

  @Override
  public void init() {
    super.init();
    blockWhipped1 = blockBroken1 = blockWhipped2 = blockBroken2 = false;
    mapRoutes = b.allMapRoutes.get("15-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == BONE_TOWER) {
      if (obj.distanceX < 160 && obj.y >= botState.getPlayerY() - 16
          && obj.y <= botState.getPlayerY()) {
        obj.tier = 8;
      }
    } else if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= botState.getPlayerY() - 32 && obj.y1 <= botState.getPlayerY())
              && ((obj.left && obj.x2 >= botState.getPlayerX() - 16)
                  || (!obj.left && obj.x1 <= botState.getPlayerX() + 16))) {
        obj.tier = 9;
      }
    } else if (obj.type == RED_SKELETON) {
      if (obj.distanceX < 64 && obj.distanceY <= 4) {
        obj.tier = 7;
      }
    } else if (obj.type == RED_BONES || obj.type == RED_SKELETON_RISING) {      
      if (obj.distanceY <= 4 && (obj.distanceX <= 32 
          || (obj.playerFacing && obj.distanceX < 48))) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      
      if (botState.getPlayerY() <= 96 && (botState.isOnStairs() || obj.y > 96)) {
        return;
      }
      
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 38: // boomerang
              if (botState.getWeapon() != HOLY_WATER) {
                obj.tier = 1;
              }
              break;
            case 62:
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
        case WHIP_UPGRADE:
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
        case EXTRA_LIFE:
          obj.tier = 5; break;
      }
    }    
  }
  
  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    if (botState.isOnStairs() && botState.getPlayerY() <= 160 && botState.getPlayerX() < 672
        && b.isTypeInBounds(RED_SKELETON, 584, 0, 624, 112)) {
      if (botState.getCurrentStrategy() != null) {
        clearTarget(targetedObject);
        setStrategy(null);
      }
      if (botState.getPlayerY() < 128) {
        b.getGamepad().pressDown();
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }

  @Override
  public void readGameObjects() {
    if (botState.getPlayerX() >= 848 && botState.getPlayerY() <= 96) {
      final boolean block1 = api.readPPU(BLOCK_150000) == 0x00;
      final boolean block2 = api.readPPU(BLOCK_150001) == 0x00;
      if (!blockBroken1 && block1) {
        blockWhipped1 = blockBroken1 = true;
        if (block1 && block2) {
          mapRoutes = b.allMapRoutes.get("15-00-01");
        }
      }
      if (!blockBroken2 && block2) {
        blockWhipped2 = blockBroken2 = true;
        if (block1 && block2) {
          mapRoutes = b.allMapRoutes.get("15-00-01");
        }
      }
      if (!blockWhipped1) {
        b.addBlock(992, 64);
      }
      if (!blockWhipped2) {
        b.addBlock(992, 80);
      }
    }    

    b.addDestination(936, 48);
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerY() >= 128) {
      route(553, 192);
    } else {
      route(521, 96);
    }
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerY() >= 128) {
      if (botState.isOnStairs()) {
        route(992, 80);
      } else {
        route(1006, 192);
      }
    } else {
      route(983, 96);
    }
  }
  
  @Override
  public void blockWhipped() {
    if (blockWhipped1) {
      blockWhipped2 = true;
    } else {
      blockWhipped1 = true;
    }
  }  
}