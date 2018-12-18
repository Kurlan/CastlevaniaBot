package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import nintaco.api.API;

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
  
  public Substage1700(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState) {
    super(b, botState, api, playerController, gameState);
  }

  @Override
  public void init() {
    super.init();
    stopWatchDelay = 0;
    killedSkeleton = blockWhipped = blockBroken = false;
    mapRoutes = b.allMapRoutes.get("17-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
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
              b.avoid(obj);
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
  public void pickStrategy(TargetedObject targetedObject) {
    
    if (botState.getWeapon() == STOPWATCH && botState.getPlayerX() < 496) {
      botState.setCurrentStrategy(null);
      if (stopWatchDelay > 0 && --stopWatchDelay == 180) {
        playerController.useWeapon(gameState);
      } 
      if (stopWatchDelay == 0 && botState.getPlayerX() < 272 && botState.getWeapon() == STOPWATCH
          && botState.getHearts() >= 5) {
        stopWatchDelay = 181;        
      } else if (stopWatchDelay < 179) {
        route(104, 48, false);
      }
    } else if (!killedSkeleton) {
      if (botState.getCurrentStrategy() == b.getAllStrategies().getSKELETON_WALL()) {
        if (b.getAllStrategies().getSKELETON_WALL().done) {
          killedSkeleton = true;
          super.pickStrategy(targetedObject);
        }
      } else {
        clearTarget(targetedObject);
        b.getAllStrategies().getSKELETON_WALL().init(736, 128);
        botState.setCurrentStrategy(b.getAllStrategies().getSKELETON_WALL());
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }  

  @Override
  public void readGameObjects() {
    if (botState.getPlayerX() < 224) {
      if (!blockBroken && api.readPPU(BLOCK_170000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("17-00-01");
      }
      if (!blockWhipped) {
        b.addBlock(160, 80);
      }
    }    

    if (botState.getPlayerX() >= 496) {
      b.addDestination(680, 48);
    } else {
      b.addDestination(104, 48);
    }
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerX() >= 496) {
      route(521, 160);
    } else if (botState.getPlayerX() >= 248 && botState.getPlayerY() < 168) {
      route(256, 96);
    } else if (!botState.isOnStairs() && botState.getPlayerX() > 152 && botState.getPlayerX() < 208) {
      route(160, 192);
    } else {
      route(41, 96);
    }
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerX() >= 496) {
      if (botState.isOnStairs() || (botState.getPlayerY() <= 96 && botState.getPlayerX() < 648)) {
        route(680, 48);
      } else {
        route(750, 128);
      }
    } else if (botState.getPlayerX() >= 248 && botState.getPlayerY() < 168) {
      route(471, 160);
    } else if (!botState.isOnStairs() && botState.getPlayerX() > 152 && botState.getPlayerX() < 208) {
      route(287, 192);
    } else if (botState.isOnStairs() && botState.getPlayerX() < 120) {
      route(104, 48);
    } else {
      route(287, 192);
    }
  }
  
  @Override
  public void blockWhipped() {
    blockWhipped = true;
  }  
}