package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

public class Substage0800 extends Substage {
  
  public Substage0800(final CastlevaniaBot b, final BotState botState, final API api) {
    super(b, botState, api);
  }

  @Override
  public void init() {
    super.init();
    mapRoutes = b.allMapRoutes.get("08-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == WHITE_SKELETON) {
      if (!b.onStairs && botState.getPlayerY() >= 112 && obj.y >= 112) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON && obj.x < 160) {
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
          obj.tier = 5;
          break;
      }
    }    
  }
  
  @Override
  public void readGameObjects() {
    b.addDestination(600, 48);
  }  

  @Override
  public void routeLeft() {
    route(9, 160);
  }
  
  @Override
  public void routeRight() {
    route(727, 160);
  } 
  
  @Override boolean handleBones(TargetedObject targetedObject) {
    return false; // Walk right through falling bones
  }
}