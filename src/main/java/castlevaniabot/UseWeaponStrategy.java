package castlevaniabot;

import java.util.concurrent.*;

public class UseWeaponStrategy extends Strategy {
  
  private int jumpCounter;
  private int done;
  private int playerX;
  private int playerY;
  private boolean jump;
  private boolean faceLeft;
  private int delayAfterUse;

  public UseWeaponStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  void init(final int playerX, final int playerY, final boolean jump, 
      final boolean faceLeft) {
    init(playerX, playerY, jump, faceLeft, 170);
  }
  
  void init(final int playerX, final int playerY, final boolean jump, 
      final boolean faceLeft, final int delayAfterUse) {
    jumpCounter = done = 0;
    this.playerX = playerX;
    this.playerY = playerY;
    this.jump = jump;
    this.faceLeft = faceLeft;
    this.delayAfterUse = delayAfterUse 
        + ThreadLocalRandom.current().nextInt(11);;
  }

  @Override void step() {
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
        useWeapon();
      }
    } else if (b.playerX != playerX || b.playerY != playerY) {
      b.substage.route(playerX, playerY);
    } else if (faceLeft != b.playerLeft) { 
      // walk past and turn around
      if (faceLeft) {
        b.substage.routeRight();
      } else {
        b.substage.routeLeft();
      }
    } else if (jump && b.canJump) { 
      jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
      b.jump();            
    } else {
      useWeapon();
    }
  }  
  
  private void useWeapon() {
    b.useWeapon();
    done = delayAfterUse;
  }
}