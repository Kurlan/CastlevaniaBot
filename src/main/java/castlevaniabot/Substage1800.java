package castlevaniabot;

public class Substage1800 extends Substage {
  
  public Substage1800(final CastlevaniaBot b) {
    super(b);
  }

  @Override void init() {
    super.init();
    mapRoutes = b.allMapRoutes.get("18-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) { 
    obj.tier = 0;
  }

  @Override void readGameObjects() {
    b.addDestination(600, 48);
  }  

  @Override void routeLeft() {
    route(600, 48);
  }
  
  @Override void routeRight() {
    route(750, 128);
  }
}