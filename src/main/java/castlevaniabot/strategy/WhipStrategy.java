package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.*;
import static nintaco.util.MathUtil.*;
import static castlevaniabot.CastlevaniaBot.*;

public class WhipStrategy extends Strategy {
  
  private int jumpCounter;
  private int done;
  private int playerX;
  private int playerY;
  private int delayAfterUse;
  private int delayJump;
  private int targetHeight;
  private boolean jump;
  private boolean playerLeft;
  private boolean jumpToward;

  public WhipStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  void init(final int playerX, final int playerY, final boolean playerLeft) {
    init(playerX, playerY, playerLeft, 0, false, false, 0);
  }
  
  public void init(final int playerX, final int playerY, final boolean playerLeft,
                   final int delayAfterUse) {
    init(playerX, playerY, playerLeft, delayAfterUse, false, false, 0);
  }  
  
  public void init(final int playerX, final int playerY, final boolean playerLeft,
                   final int delayAfterUse, final boolean jump,
                   final boolean jumpToward, final int targetHeight) {
    
    jumpCounter = done = 0;
    delayJump = ThreadLocalRandom.current().nextInt(3);
    
    this.playerX = playerX;
    this.playerY = playerY;    
    this.playerLeft = playerLeft;
    this.jump = jump;
    this.jumpToward = jumpToward;
    this.targetHeight = targetHeight;
    this.delayAfterUse = (delayAfterUse == 0) ? 0 : delayAfterUse 
        + ThreadLocalRandom.current().nextInt(11);
  }

  @Override
  public void step() {
    if (done > 0) {
      if (--done == 0) {
        b.substage.weaponUsed();
      }
      return;
    }
    if (b.weaponing) {
      return;
    }
    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        useWhip();
      }
    } else if (b.playerX != playerX || b.playerY != playerY 
        || b.playerLeft != playerLeft) {
      b.substage.routeAndFace(playerX, playerY, playerLeft);
    } else if (jump && b.canJump) { 
      if (delayJump > 0) {
        --delayJump;
      } else {
        jumpCounter = JUMP_WHIP_DELAYS[clamp(targetHeight, 0, 36)];
        if (b.whipLength == 0) {
          jumpCounter -= 6;
        }
        if (jumpToward) {
          if (playerLeft) {
            b.pressLeftAndJump();
          } else {
            b.pressRightAndJump();
          }
        } else {
          b.jump();
        }
      }            
    } else {
      useWhip();
    }
  }  
  
  private void useWhip() {
    b.whip();
    if (delayAfterUse == 0) {
      b.substage.whipUsed();
    } else {
      done = delayAfterUse;
    }
  }
}