package castlevaniabot.model.gameelements;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TargetedObject {
    private GameObject target;
    private GameObjectType targetType;
    private Coordinates coordinates;
}
