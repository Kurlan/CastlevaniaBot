package castlevaniabot.model.gameelements;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TargetedObject {
    private GameObject target;
    private GameObjectType targetType;
    private Coordinates coordinates;

    public void reset() {
        this.target = null;
        this.targetType = null;
        this.coordinates.setX(-512);
        this.coordinates.setY(-512);
    }
}
