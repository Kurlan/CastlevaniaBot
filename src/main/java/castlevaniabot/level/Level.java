package castlevaniabot.level;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.Coordinates;

public interface Level {
  void readGameObjects(GameState gameState, BotState botState, Coordinates currentTile, PlayerController playerController);
}
