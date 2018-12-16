package castlevaniabot.strategy;

import castlevaniabot.model.creativeelements.MovingPlatform;
import castlevaniabot.CastlevaniaBot;

import static java.lang.Math.*;

public class JumpMovingPlatformStrategy extends Strategy {
  
  private static final int JUMP_DISTANCE = 40;
  
  private static enum State {
    WALK_TO_POINT_1,
    WAIT_FOR_PLATFORM,
    WALK_TO_PLATFORM_CENTER,
    WAIT_FOR_PLATFORM_TO_MOVE,
    DONE,
  }

  private State state;
  private int playerX1;
  private int playerX2;
  private int playerY;
  private int minX;
  private int maxX;
  private int lastX;
  private boolean approaching;
  
  public JumpMovingPlatformStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  // playerX1 and playerX2 are 16 pixels removed from the chasm edges
  public void init(final int playerX1, final int playerX2, final int playerY) {
    this.playerX1 = playerX1;
    this.playerX2 = playerX2;
    this.playerY = playerY;
    if (playerX1 < playerX2) {
      minX = playerX1;
      maxX = playerX2;
    } else {
      minX = playerX2;
      maxX = playerX1;
    }
    state = State.WALK_TO_POINT_1;
    approaching = false;
    lastX = -512;
  }
  
  @Override
  public void step() {
    
    final MovingPlatform platform = b.getMovingPlatform(minX, maxX);
    if (platform == null) {
      approaching = false;
      lastX = -512;
      return;
    }
        
    final int vx = (lastX >= 0) ? (platform.x1 - lastX) : 0;
    if (vx != 0) {
      approaching = (playerX1 > playerX2) == (vx > 0);
    }
    lastX = platform.x1;
    
    switch(state) {
      case WALK_TO_POINT_1:
        if (b.playerX != playerX1 || b.playerY != playerY) {
          b.substage.route(playerX1, playerY);
        } else {
          state = State.WAIT_FOR_PLATFORM;
        }
        break;
      case WAIT_FOR_PLATFORM:
        if (approaching) {
          if (playerX1 > playerX2) {
            if (abs(platform.x2 - playerX1) < JUMP_DISTANCE) {
              b.pressLeftAndJump();
              state = State.WALK_TO_PLATFORM_CENTER;
            }
          } else {
            if (abs(platform.x1 - playerX1) < JUMP_DISTANCE) {
              b.pressRightAndJump();
              state = State.WALK_TO_PLATFORM_CENTER;
            }
          }
        }
        break;
      case WALK_TO_PLATFORM_CENTER:
        if (b.playerX == platform.x1 + 16) {
          if (b.playerLeft == (playerX1 > playerX2)) {
            state = State.WAIT_FOR_PLATFORM_TO_MOVE;
          } else if (b.playerLeft) {
            b.pressLeft();                // walk past and turn around
          } else {
            b.pressRight();               // walk past and turn around
          }
        } else if (b.playerX < platform.x1 + 16) {
          b.pressRight();
        } else {
          b.pressLeft();
        }
        break;
      case WAIT_FOR_PLATFORM_TO_MOVE:
        if (playerX1 > playerX2) {
          if (abs(platform.x1 - playerX2) < JUMP_DISTANCE) {
            b.pressLeftAndJump();
            state = State.DONE;
          }
        } else {
          if (abs(platform.x2 - playerX2) < JUMP_DISTANCE) {
            b.pressLeftAndJump();
            state = State.DONE;
          }
        }
        break;
    }    
  }  
}