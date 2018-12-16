package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_040100;
import static castlevaniabot.model.gameelements.GameObjectType.BLACK_BAT;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.SPEAR_KNIGHT;

public class Substage0401 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;
  
  public Substage0401(final CastlevaniaBot b, final BotState botState, final API api) {
    super(b, botState, api);
  }

  @Override
  public void init() {
    super.init();
    blockWhipped = blockBroken = false;
    mapRoutes = b.allMapRoutes.get("04-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    if (obj.type == BLACK_BAT) {
      if (obj.active && obj.distanceX < 96 && obj.y + 88 >= botState.getPlayerY()
          && obj.y - 40 <= botState.getPlayerY() && ((obj.left && obj.x >= botState.getPlayerX() - 40)
              || (!obj.left && obj.x <= botState.getPlayerX() + 40))) {
        obj.tier = 6;
      }
    } else if (obj.type == SPEAR_KNIGHT) {
      if (obj.distanceX < 64 && obj.y >= botState.getPlayerY() - 32
          && obj.y - 32 <= botState.getPlayerY() && !isInKnightPit(obj)) {
        obj.tier = 5;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON && !((isInKnightPit(obj) 
        && isKnightInPit()) || isInPlatformPit(obj))) {
      switch(obj.type) {
        case BLOCK: 
          if (botState.getPlayerX() >= 248 && !isKnightInPit()) {
            obj.tier = 1;
          }
          break;
        case CANDLES:
          obj.tier = 1; break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:
        case WHIP_UPGRADE:
        case INVISIBLE_POTION:
          obj.tier = 2; break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          obj.tier = 3; break;
        case AXE_WEAPON:
        case BOOMERANG_WEAPON:
        case DAGGER_WEAPON:        
        case STOPWATCH_WEAPON:
          if (b.weapon != HOLY_WATER) {
            obj.tier = 4;
          } else {
            b.avoid(obj);
          }
          break;          
        case HOLY_WATER_WEAPON:
        case PORK_CHOP:
          obj.tier = 4;
          break;
      }
    }    
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    if (botState.getPlayerX() >= 32 && botState.getPlayerX() <= 104) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getNO_JUMP_MOVING_PLATFORM()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getNO_JUMP_MOVING_PLATFORM().init(96, 31, 112);
        botState.setCurrentStrategy(b.getAllStrategies().getNO_JUMP_MOVING_PLATFORM());
      }
    } else if (b.weapon == HOLY_WATER && b.hearts > 0 && b.currentTile.getY() == 7
        && b.currentTile.getX() >= 15 && b.currentTile.getX() <= 17 && isKnightInPit()) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getUSE_WEAPON()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getUSE_WEAPON().init(264, 112, false, false);
        botState.setCurrentStrategy(b.getAllStrategies().getUSE_WEAPON());
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }
  
  private boolean isInPlatformPit(final GameObject obj) {
    return obj.x <= 96 && obj.y > 64;
  }
  
  private boolean isInKnightPit(final GameObject obj) {
    return obj.y >= 144 && obj.x >= 320 && obj.x <= 352;
  }
  
  private boolean isKnightInPit() {
    final GameObject[] objs = b.gameObjects;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      if (obj.type == SPEAR_KNIGHT && isInKnightPit(obj)) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public void readGameObjects() {
    if (!blockBroken && botState.getPlayerX() >= 256 && api.readPPU(BLOCK_040100) == 0x00) {
      blockWhipped = blockBroken = true;
      mapRoutes = b.allMapRoutes.get("04-01-01");
    } 
    if (!blockWhipped) {      
      b.addBlock(352, 112);      
    }    
    if (botState.getPlayerX() < 32) {
      b.addDestination(25, 112);
    } else if (botState.getPlayerX() >= 104) {
      b.addDestination(96, 112);      
    }
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerX() < 32) {
      route(25, 112);
    } else {
      route(64, 144);
    }
  }
  
  @Override
  public void routeRight() {
    route(471, 144);
  }

  @Override
  public void blockWhipped() {
    blockWhipped = true;
  }
}