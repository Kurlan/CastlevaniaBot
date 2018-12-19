package castlevaniabot.level;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.Coordinates;

public interface Level {
  void readGameObjects(CastlevaniaBot b, GameState gameState, BotState botState, Coordinates currentTile, PlayerController playerController);
}
