package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.WaitStrategy;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_130100;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_130101;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FLEAMAN;
import static castlevaniabot.model.gameelements.GameObjectType.RED_BONES;
import static castlevaniabot.model.gameelements.GameObjectType.RED_SKELETON;
import static castlevaniabot.model.gameelements.GameObjectType.RED_SKELETON_RISING;
import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

public class Substage1301 extends Substage {
  
  private boolean treasureTriggered;
  
  private boolean blockWhipped1;
  private boolean blockBroken1;

  private boolean blockWhipped2;
  private boolean blockBroken2;  
   
  public Substage1301(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController) {
    super(b, botState, api, playerController);
  }

  @Override
  public void init() {
    super.init();
    treasureTriggered = blockWhipped1 = blockBroken1 = blockWhipped2 
        = blockBroken2 = false;
    mapRoutes = b.allMapRoutes.get("13-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == FLEAMAN) {
      if (obj.distanceX < 64 && obj.y1 <= botState.getPlayerY()
          && obj.y2 >= botState.getPlayerY() - 48) {
        obj.tier = 9;
      }
    } else if (obj.type == WHITE_SKELETON) {
      if (obj.distanceX < 128 && obj.y1 <= botState.getPlayerY() + 16
          && obj.y2 >= botState.getPlayerY() - 64) {
        obj.tier = 8;
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
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 62: 
              obj.tier = 3;
              obj.subTier = 1;
              break;
            case 66:
              obj.tier = 3;
              obj.subTier = 2;
              break;
            default:
              if ((obj.x >= 320 || obj.y <= 104) 
                  && (obj.x < 480 || obj.x >= 832)) {
                obj.tier = 1; 
              }
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
          if (b.weapon == NONE || b.weapon == STOPWATCH) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;        
        case BOOMERANG_WEAPON:
          if (b.weapon != HOLY_WATER) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;
        case AXE_WEAPON:
          if (b.weapon != HOLY_WATER && b.weapon != BOOMERANG) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;          
        case STOPWATCH_WEAPON:  
          if (b.weapon == NONE) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
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
    if (!treasureTriggered && botState.getPlayerY() <= 96 && botState.getPlayerX() >= 336
        && botState.getPlayerX() < 416) {
      if (botState.getCurrentStrategy()!= b.getAllStrategies().getWAIT()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getWAIT().init(407, 96, WaitStrategy.WaitType.WALK_RIGHT);
        botState.setCurrentStrategy(b.getAllStrategies().getWAIT());
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }

  @Override
  public void readGameObjects() {
    if (botState.getPlayerX() >= 416 && botState.getPlayerX() < 480) {
      if (!blockBroken1 && api.readPPU(BLOCK_130100) == 0x00) {
        blockWhipped1 = blockBroken1 = true;
        mapRoutes = b.allMapRoutes.get("13-01-01");
      }
      if (!blockWhipped1) {
        b.addBlock(432, 96);
      }
    } else if (botState.getPlayerX() >= 1024) {
      if (!blockBroken2 && api.readPPU(BLOCK_130101) == 0x00) {
        blockWhipped2 = blockBroken2 = true;
        mapRoutes = b.allMapRoutes.get("13-01-02");
      }
      if (!blockWhipped2) {
        b.addBlock(1088, 160);
      }
    }        
    
    b.addDestination(1255, 192);
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerX() < 320 && botState.getPlayerY() > 104) {
      route(9, 192);
    } else if (botState.getPlayerX() < 416 && botState.getPlayerY() <= 104) {
      route(9, 96);
    } else {
      route(361, 192);
    }
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerX() < 320 && botState.getPlayerY() > 104) {
      route(311, 192);
    } else if (botState.getPlayerX() < 416 && botState.getPlayerY() <= 104) {
      route(407, 96);
    } else {
      route(1255, 192);
    }
  }
  
  @Override
  public void blockWhipped() {
    if (botState.getPlayerX() > 448) {
      blockWhipped2 = true;
    } else {
      blockWhipped1 = true;
    }
  }  

  @Override
  public void treasureTriggered() {
    treasureTriggered = true;
  }
}