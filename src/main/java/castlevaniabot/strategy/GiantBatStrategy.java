package castlevaniabot.strategy;

import castlevaniabot.GameObject;
import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.*;

public class GiantBatStrategy extends Strategy {
  
  private int jumpCounter;
  private int lastX;
  private int lastY; 
  private boolean whipped;

  public GiantBatStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override
  public void init() {
    jumpCounter = 0;
    whipped = false;
  }

  @Override
  public void step() {
    
    final GameObject bat = b.target;
    final int offsetX = (bat.x - lastX) << 4;
    final int offsetY = (bat.y - lastY) << 4;
    lastX = bat.x;
    lastY = bat.y;
    
    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        b.whip();
        whipped = true;
      }
    } else if (bat.y1 > b.playerY 
        && bat.y1 + offsetY <= b.playerY 
        && bat.y2 + offsetY >= b.playerY - 32 
        && bat.x2 + offsetX >= b.playerX - 8 
        && bat.x1 + offsetX <= b.playerX + 8
        && b.canJump) {
      
      final int landX = b.playerX - 37;
      if (landX >= 0 
          && b.substage.mapRoutes.map[b.tileY][landX >> 4].height == 0) {
        b.pressLeftAndJump();
        return;
      }
    } else if (!whipped
        && bat.x2 < b.playerX - 24
        && bat.y2 < b.playerY - 48
        && bat.x2 > b.playerX - b.getWhipRadius() - 16
        && b.canJump) {
      
      final int landX = b.playerX - 37;
      if (landX >= 0 
          && b.substage.mapRoutes.map[b.tileY][landX >> 4].height == 0) {
        b.pressLeftAndJump();
        jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);        
        return;
      }
    }

    b.substage.route(41, 128, false);
  }  
}