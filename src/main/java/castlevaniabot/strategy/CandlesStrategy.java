package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.*;
import static nintaco.util.MathUtil.clamp;
import static castlevaniabot.CastlevaniaBot.*;

public class CandlesStrategy extends Strategy {
  
  private int jumpCounter;
  private int done;
  private int delayJump;
  private boolean usedHolyWater;  

  public CandlesStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override public void init() {
    jumpCounter = 0;
    done = 0;
    usedHolyWater = false;
    delayJump = ThreadLocalRandom.current().nextInt(3);
  }

  @Override public void step() {
    if (done > 0) {
      --done;
      return;
    }
    if (b.weaponing) {
      return;
    }
    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        useWeapon();
      }
    } else {
      final int playerY = b.getTargetedObject().getTarget().platformY << 4;
      int playerX = b.getTargetedObject().getTarget().platformX << 4;
      final boolean playerLeft = playerX > b.getTargetedObject().getTarget().x;
      playerX += playerLeft ? 1 : 14;      
      if (b.playerX == playerX && b.playerY == playerY 
          && b.playerLeft == playerLeft) {
        final int height = b.playerY - b.getTargetedObject().getTarget().y;
        if (height < 16) {
          b.kneel();
          if (b.kneeling) {
            b.whip();
            done = 64;
          }
        } else if (height > 16) {
          if (b.canJump) {
            if (delayJump > 0) {
              --delayJump;
            } else {
              jumpCounter = JUMP_WHIP_DELAYS[clamp(height - 12, 0, 36)];
              if (b.whipLength == 0) {
                jumpCounter -= 6;
              }              
              b.jump();
            }
          }
        } else {
          useWeapon();
        }
      } else {
        b.substage.routeAndFace(playerX, playerY, playerLeft);
      }
    }
  }  
  
  private void useWeapon() {
    if (!usedHolyWater && b.getTargetedObject().getTarget().active) { // active indicates grindable
      usedHolyWater = b.grind();
    } else {
      b.whip();
    }
    done = 64;
    b.substage.candlesWhipped(b.getTargetedObject().getTarget());
  }
}