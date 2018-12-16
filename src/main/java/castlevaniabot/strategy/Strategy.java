package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;

public abstract class Strategy {
  
  final CastlevaniaBot b;
  final BotState botState;
  
  Strategy(final CastlevaniaBot b) {
    this.b = b;
    this.botState = b.getBotState();
  }
  
  public void init() {
  }
  
  public abstract void step();
}