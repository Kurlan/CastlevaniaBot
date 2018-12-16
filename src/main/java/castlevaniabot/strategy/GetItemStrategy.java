package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.*;
import static castlevaniabot.model.gameelements.GameObjectType.*;

public class GetItemStrategy extends Strategy {

  private int error;

  public GetItemStrategy(final CastlevaniaBot b) {
    super(b);
  }  
      
  @Override public void init() {
    error = (b.target.type == DESTINATION) ? 0 
        : (ThreadLocalRandom.current().nextInt(7) - 3);
  }  

  @Override public void step() {

    final int y = b.target.platformY << 4;
    int x = b.target.supportX;
    if (b.target.y <= y) {
      final int t = (x & 0xF) + error;
      if ((t & 0xF) == t) {
        x += error;
      }
    }
    
    if (b.target.type != DESTINATION && b.playerX == x && b.playerY == y) {
      if (b.target.y > y) {
        b.kneel();
      } else if (b.canJump && b.target.y < b.playerY - 32) {
        b.jump();
      }
    } else {
      b.substage.route(x, y);
    }
  }  
}