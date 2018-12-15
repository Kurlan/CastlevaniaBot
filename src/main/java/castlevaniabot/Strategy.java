package castlevaniabot;

public abstract class Strategy {
  
  final CastlevaniaBot b;
  
  Strategy(final CastlevaniaBot b) {
    this.b = b;
  }
  
  void init() {    
  }
  
  abstract void step();
}