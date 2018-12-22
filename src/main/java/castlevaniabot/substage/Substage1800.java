package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import nintaco.api.API;

import java.util.Map;

public class Substage1800 extends Substage {
  
  public Substage1800(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState, Map<String, MapRoutes> allMapRoutes) {
    super(b, botState, api, playerController, gameState, allMapRoutes.get("18-00-00"));
  }

  @Override
  public void init() {
    super.init();
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    obj.tier = 0;
  }

  @Override
  public void readGameObjects() {
    gameState.addDestination(600, 48, botState);
  }  

  @Override
  public void routeLeft() {
    route(600, 48);
  }
  
  @Override
  public void routeRight() {
    route(750, 128);
  }
}