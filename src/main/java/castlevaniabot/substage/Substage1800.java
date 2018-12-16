package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import nintaco.api.API;

public class Substage1800 extends Substage {
  
  public Substage1800(final CastlevaniaBot b, final BotState botState, final API api) {
    super(b, botState, api);
  }

  @Override
  public void init() {
    super.init();
    mapRoutes = b.allMapRoutes.get("18-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    obj.tier = 0;
  }

  @Override
  public void readGameObjects() {
    b.addDestination(600, 48);
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