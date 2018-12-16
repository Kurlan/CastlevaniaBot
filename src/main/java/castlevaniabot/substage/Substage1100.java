package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FLEAMAN;

public class Substage1100 extends Substage {
  
  public Substage1100(final CastlevaniaBot b, final BotState botState, final API api) {
    super(b, botState, api);
  }

  @Override
  public void init() {
    super.init();
    mapRoutes = b.allMapRoutes.get("11-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == FLEAMAN) {
      if (obj.y2 >= botState.getPlayerY() - 56 && obj.y1 <= botState.getPlayerY()) {
        obj.tier = 5;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case FLAMES: // Fleamen flames can spawn axes. Avoid the flames!
          b.avoid(obj);
          break;
        case WHIP_UPGRADE:
          obj.tier = 1; break;
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          obj.tier = 2; break;
        case AXE_WEAPON:
          if (b.weapon != BOOMERANG && b.weapon != HOLY_WATER) {
            obj.tier = 3;
          } else {
            b.avoid(obj);
          }
          break;
        case BOOMERANG_WEAPON:       
          if (b.weapon != HOLY_WATER) {
            obj.tier = 3;
          } else {
            b.avoid(obj);
          }
          break;           
        case DAGGER_WEAPON:        
          if (b.weapon == NONE || b.weapon == STOPWATCH) {
            obj.tier = 3;
          } else {
            b.avoid(obj);
          }
          break;            
        case STOPWATCH_WEAPON:
          if (b.weapon == NONE) {
            obj.tier = 3;
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
  public void readGameObjects() {
    b.addDestination(1519, 192);
  }  

  @Override
  public void routeLeft() {
    route(9, 192);
  }
  
  @Override
  public void routeRight() {
    route(1519, 192);
  }
}