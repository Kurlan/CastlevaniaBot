package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.operation.GameStateRestarter;
import nintaco.api.API;

import java.util.Map;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FLEAMAN;

public class Substage1100 extends Substage {

  private GameStateRestarter gameStateRestarter;
  
  public Substage1100(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("11-00-00"));
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    
    if (obj.type == FLEAMAN) {
      if (obj.y2 >= botState.getPlayerY() - 56 && obj.y1 <= botState.getPlayerY()) {
        obj.tier = 5;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case FLAMES: // Fleamen flames can spawn axes. Avoid the flames!
          playerController.avoid(obj, botState);
          break;
        case WHIP_UPGRADE:
          obj.tier = 1; break;
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          obj.tier = 2; break;
        case AXE_WEAPON:
          if (botState.getWeapon() != BOOMERANG && botState.getWeapon() != HOLY_WATER) {
            obj.tier = 3;
          } else {
            playerController.avoid(obj, botState);
          }
          break;
        case BOOMERANG_WEAPON:       
          if (botState.getWeapon() != HOLY_WATER) {
            obj.tier = 3;
          } else {
            playerController.avoid(obj, botState);
          }
          break;           
        case DAGGER_WEAPON:        
          if (botState.getWeapon() == NONE || botState.getWeapon() == STOPWATCH) {
            obj.tier = 3;
          } else {
            playerController.avoid(obj, botState);
          }
          break;            
        case STOPWATCH_WEAPON:
          if (botState.getWeapon() == NONE) {
            obj.tier = 3;
          } else {
            playerController.avoid(obj, botState);
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
  public void readGameObjects(BotState botState, GameState gameState) {
    gameState.addDestination(1519, 192, botState);
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    route(9, 192, botState, gameState);
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    route(1519, 192, botState, gameState);
  }
}