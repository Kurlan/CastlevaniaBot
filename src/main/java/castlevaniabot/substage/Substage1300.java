package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FLEAMAN;
import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

public class Substage1300 extends Substage {
  
  private boolean waited;
  
  public Substage1300(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController) {
    super(b, botState, api, playerController);
  }

  @Override
  public void init() {
    super.init();
    waited = false;
    mapRoutes = b.allMapRoutes.get("13-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == FLEAMAN) {
      if (obj.distanceX < 128 && obj.y1 <= botState.getPlayerY()
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
          if (obj.y > 132 && botState.getWhipLength() != 2) {
            obj.tier = 6;
          } else if (roundTile(obj.x) != 30 || botState.getPlayerX() < 480) {
            obj.tier = 1; 
          }
          break;
        case BLOCK: 
          obj.tier = 2; break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:        
        case INVISIBLE_POTION:
        case WHIP_UPGRADE:
          if (obj.y > 132 && botState.getWhipLength() != 2) {
            obj.tier = 7;
          } else {
            obj.tier = 3; 
          }
          break;
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
          if (obj.y > 132 && botState.getWhipLength() != 2) {
            obj.tier = 8;
          } else {
            obj.tier = 5; 
          }
          break;
      }
    }    
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    
    if (botState.getCurrentStrategy() == b.getAllStrategies().getWAIT()) {
      final GameObject skeleton = b.getType(WHITE_SKELETON);
      if (waited || (skeleton != null && (skeleton.x < botState.getPlayerX() - 48
          || skeleton.y > 132))) {
        super.pickStrategy(targetedObject);
      }
    } else if (botState.getPlayerX() >= 368 && botState.getPlayerY() > 160 && !b.isObjectBelow(132)) {
      final GameObject skeleton = b.getType(WHITE_SKELETON);
      if (skeleton != null && skeleton.y <= 132 
          && botState.getPlayerX() < skeleton.x) {
        clearTarget(targetedObject);
        b.getAllStrategies().getWAIT().init(493, 192);
        botState.setCurrentStrategy(b.getAllStrategies().getWAIT());
        waited = false;
      } else {
        super.pickStrategy(targetedObject);
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }

  @Override
  public void readGameObjects() {
    b.addDestination(88, 48);
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerY() < 144) {
      route(9, 96);
    } else {
      route(9, 192);
    }    
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerY() < 144) {
      route(503, 128);
    } else {
      route(503, 192);
    }
  }
  
  @Override
  public void treasureTriggered() {
    waited = true;
  }
}