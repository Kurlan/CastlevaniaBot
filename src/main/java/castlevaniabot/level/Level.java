package castlevaniabot.level;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;

public interface Level {
  void readGameObjects(CastlevaniaBot b, GameState gameState);
}
