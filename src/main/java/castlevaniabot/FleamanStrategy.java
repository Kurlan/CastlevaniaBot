package castlevaniabot;

import java.util.concurrent.*;

public class FleamanStrategy extends Strategy {
  
  private int lastX;
  private int lastY;
  private int move;
  private boolean left;

  public FleamanStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override
  public void init() {
    lastX = lastY = move = 0;
  }

  @Override
  public void step() {
    
    final GameObject fleaman = b.target;
    final int vx;
    final int vy;
    if (lastX != 0) {
      vx = (fleaman.x - lastX) << 4;
      vy = (fleaman.y - lastY) << 4;
    } else {
      vx = vy = 0;
    }
    lastX = fleaman.x;
    lastY = fleaman.y;
    
    if (move > 0) {
      --move;
      if (left) {
        b.substage.routeLeft();
      } else {
        b.substage.routeRight();
      }
    } else if (b.isTargetInStandingWhipRange(vx, vy)) {
      if (!b.weaponing && b.faceTarget()) {
        b.whip();
      }
    } else if (b.isTargetInKneelingWhipRange(vx, vy)) {
      b.kneel();
      if (b.kneeling && !b.weaponing && b.faceTarget()) {
        b.whip();
      }
    } else if ((vy < 0 && fleaman.y < b.playerY - 16 && fleaman.distanceX < 56) 
        || (fleaman.x1 + vx <= b.playerX + 8 
            && fleaman.x2 + vx >= b.playerX - 8 
            && fleaman.y2 + vy >= b.playerY - 32 
            && fleaman.y1 + vy <= b.playerY)) {
      move = 23 + ThreadLocalRandom.current().nextInt(17);
      left = vx > 0;
    }
  }  
}