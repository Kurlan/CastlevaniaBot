package castlevaniabot;

import castlevaniabot.strategy.Strategy;
import lombok.Data;

@Data
public class BotState {
    public Strategy currentStrategy;
}
