package castlevaniabot.model.gameelements;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Coordinates {
    private int x;
    private int y;
}
