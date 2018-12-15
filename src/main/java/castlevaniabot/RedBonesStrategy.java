package castlevaniabot;

import static java.lang.Math.*;

public class RedBonesStrategy extends Strategy {

  private boolean playerLeft;
  private int targetX;
  
  public RedBonesStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override void init() {
    targetX = b.target.x;
    playerLeft = b.playerLeft;
  }
  
  @Override void step() {
    final int distanceX = abs(targetX - b.playerX);
    if (distanceX <= 16) {
      // When standing on red bones, continue walking in direction facing.
      if (playerLeft) {
        b.pressLeft();
      } else {
        b.pressRight();
      }
    } else if (distanceX < 32) {
      b.substage.moveAwayFromTarget(targetX);
    } 
  } 
}