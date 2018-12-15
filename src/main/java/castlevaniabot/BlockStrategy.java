package castlevaniabot;

import java.util.concurrent.*;

public class BlockStrategy extends Strategy {

  private int jumpCounter;
  private int delayJump;
  private int delayWhip;

  public BlockStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override public void init() {
    jumpCounter = 0;
    delayJump = ThreadLocalRandom.current().nextInt(3);
    delayWhip = 1 + ThreadLocalRandom.current().nextInt(3);
  }

  @Override public void step() {
    if (b.weaponing) {
      return;
    } 
    
    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        b.whip();
      }
    } else if (b.onPlatform && b.target.playerFacing 
        && b.target.y >= b.playerY - 24 && b.target.y <= b.playerY
            && b.isTargetInStandingWhipRange()) {
      whipBlock();
    } else {
      final int playerX = (b.target.platformX << 4) + 8;
      final int playerY = b.target.platformY << 4;      
      final boolean playerLeft = playerX > b.target.x;
      if (b.playerX == playerX && b.playerY == playerY 
          && b.playerLeft == playerLeft) {
        if (b.playerY - b.target.y > 32) {
          if (b.canJump) {
            if (delayJump > 0) {
              --delayJump;
            } else {
              jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
              b.jump();
            }
          }
        } else {
          whipBlock();
        }
      } else {
        b.substage.routeAndFace(playerX, playerY, playerLeft);
      }
    }
  }  
  
  private void whipBlock() {
    if (delayWhip > 0) {
      --delayWhip;
    } else {
      b.whip();
      b.substage.blockWhipped();
    }
  }
}