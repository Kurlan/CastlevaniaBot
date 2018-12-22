package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.operation.GameStateRestarter;
import nintaco.api.API;

import java.util.Map;

public class Substage1800 extends Substage {

  private GameStateRestarter gameStateRestarter;

  public Substage1800(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("18-00-00"));
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    obj.tier = 0;
  }

  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    gameState.addDestination(600, 48, botState);
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    route(600, 48, botState, gameState);
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    route(750, 128, botState, gameState);
  }
}