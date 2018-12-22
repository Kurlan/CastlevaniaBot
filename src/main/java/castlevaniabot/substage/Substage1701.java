package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.AllStrategies;
import nintaco.api.API;

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
  
  public Substage1701(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState) {
    super(b, botState, api, playerController, gameState);
  }

  @Override
  public void init() {
    super.init();
    killedUpperSkeleton = killedLowerSkeleton = usedStopwatch = blockWhipped1 
        = blockBroken1 = blockWhipped2 = blockBroken2 = false;
    mapRoutes = b.allMapRoutes.get("17-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
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
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies) {
    if (!usedStopwatch && botState.getPlayerX() <= 480 && botState.getPlayerY() <= 96
        && botState.getWeapon() == STOPWATCH && botState.getHearts() >= 5) {
      playerController.useWeapon(gameState);
      usedStopwatch = true;
    } else if (!killedLowerSkeleton && botState.getPlayerX() >= 496 && botState.getPlayerY() > 128) {
      if (botState.getCurrentStrategy() == b.getAllStrategies().getSKELETON_WALL()) {
        if (b.getAllStrategies().getSKELETON_WALL().done) {
          killedLowerSkeleton = true;
          super.pickStrategy(targetedObject, allStrategies);
        }
      } else {
        clearTarget(targetedObject);
        b.getAllStrategies().getSKELETON_WALL().init(726, 192, 136);
        botState.setCurrentStrategy(b.getAllStrategies().getSKELETON_WALL());
      }
    } else if (!killedUpperSkeleton && botState.getPlayerX() >= 496 && botState.getPlayerY() <= 128) {
      if (botState.getCurrentStrategy() == b.getAllStrategies().getSKELETON_WALL()) {
        if (b.getAllStrategies().getSKELETON_WALL().done) {
          killedUpperSkeleton = true;
          super.pickStrategy(targetedObject, allStrategies);
        }
      } else {
        clearTarget(targetedObject);
        b.getAllStrategies().getSKELETON_WALL().init(704, 128);
        botState.setCurrentStrategy(b.getAllStrategies().getSKELETON_WALL());
      } 
    } else {
      super.pickStrategy(targetedObject, allStrategies);
    }
  }

  @Override
  public void readGameObjects() {
    if (botState.getPlayerX() >= 368 && botState.getPlayerX() < 512) {
      final boolean block1 = api.readPPU(BLOCK_170100) == 0x00;
      final boolean block2 = api.readPPU(BLOCK_170101) == 0x00;
      if (!blockBroken1 && block1) {
        blockWhipped1 = blockBroken1 = true;
        if (block1 && block2) {
          mapRoutes = b.allMapRoutes.get("17-01-01");
        }
      }
      if (!blockBroken2 && block2) {
        blockWhipped2 = blockBroken2 = true;
        if (block1 && block2) {
          mapRoutes = b.allMapRoutes.get("17-01-01");
        }
      }
      if (!blockWhipped1) {
        b.addBlock(480, 160);
      }
      if (!blockWhipped2) {
        b.addBlock(480, 176);
      }
    }    

    if (botState.getPlayerX() >= 176) {
      gameState.addDestination(320, 223, botState);
    } else {
      gameState.addDestination(0, 128, botState);
    }
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerX() >= 496) {
      if (botState.getPlayerY() <= 128 || (botState.isOnStairs() && botState.getPlayerY() < 192)) {
        route(448, 96);
      } else {
        route(521, 192);
      }
    } else if (botState.getPlayerX() >= 176) {
      if (botState.getPlayerY() <= 96) {
        route(336, 96);
      } else if (botState.getPlayerX() < 368 && botState.getPlayerY() < 192) {
        route(256, 160);
      } else if (botState.getPlayerX() >= 368) {
        route(336, 192);
      } else {
        route(320, 223);
      }
    } else {
      route(0, 128);
    }
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerX() >= 496) {
      if (botState.getPlayerY() <= 128 || (botState.isOnStairs() && botState.getPlayerY() < 192)) {
        route(727, 128);
      } else {
        route(727, 192);
      }
    } else if (botState.getPlayerX() >= 176) {
      if (botState.getPlayerY() <= 96) {
        route(727, 128);
      } else if (botState.getPlayerX() < 368 && botState.getPlayerY() < 192) {
        route(359, 192);
      } else if (botState.getPlayerX() >= 368) {
        if (blockBroken1 && blockBroken2) {
          route(487, 192);
        } else {
          route(471, 192);
        }
      } else {
        route(359, 192);
      }
    } else {
      route(151, 192);
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