package castlevaniabot.strategy;

import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.CastlevaniaBot;

public class SnakeStrategy extends Strategy {
  
  private int lastX;
  private int lastY;  

  public SnakeStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void step() {
    
    final GameObject snake = b.getTargetedObject().getTarget();
    final int offsetX = (snake.x - lastX) << 4;
    final int offsetY = (snake.y - lastY) << 4;
    lastX = snake.x;
    lastY = snake.y;    
    
    if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();
        }
      }
    } 
  }  
}