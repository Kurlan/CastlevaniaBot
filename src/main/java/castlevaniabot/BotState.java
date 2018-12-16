package castlevaniabot;

import castlevaniabot.strategy.Strategy;
import lombok.Data;

@Data
public class BotState {
    private int playerX;
    private int playerY;
    private Strategy currentStrategy;
}
