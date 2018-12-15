package castlevaniabot;

public abstract class Strategy {
  
  final CastlevaniaBot b;
  
  Strategy(final CastlevaniaBot b) {
    this.b = b;
  }
  
  public void init() {
  }
  
  public abstract void step();
}