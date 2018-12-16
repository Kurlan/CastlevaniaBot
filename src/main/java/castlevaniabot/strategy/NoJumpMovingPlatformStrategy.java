package castlevaniabot.strategy;

import castlevaniabot.model.creativeelements.MovingPlatform;
import castlevaniabot.CastlevaniaBot;

import static java.lang.Math.*;

public class NoJumpMovingPlatformStrategy extends Strategy {

  private static enum State {
    WALK_TO_POINT_1,
    WAIT_FOR_PLATFORM,
    WALK_ONTO_PLATFORM,
    WAIT_FOR_PLATFORM_TO_MOVE,
    WALK_OFF_OF_PLATFORM,
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
  
  public NoJumpMovingPlatformStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  // playerX1 and playerX2 are the tips of the chasam edge blocks
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
            if (abs(platform.x2 - playerX1) < 3) {
              state = State.WALK_ONTO_PLATFORM;
            }
          } else {
            if (abs(platform.x1 - playerX1) < 3) {
              state = State.WALK_ONTO_PLATFORM;
            }
          }
        }
        break;
      case WALK_ONTO_PLATFORM:
        if (abs(platform.x1 + 16 - b.playerX) < 2) {
          state = State.WAIT_FOR_PLATFORM_TO_MOVE;
        }
        if (playerX1 > playerX2) {
          b.pressLeft();
        } else {
          b.pressRight();
        }
        break;
      case WAIT_FOR_PLATFORM_TO_MOVE:
        if (playerX1 > playerX2) {
          if (abs(platform.x1 - playerX2) < 16) {
            state = State.WALK_OFF_OF_PLATFORM;
          }
        } else {
          if (abs(platform.x2 - playerX2) < 16) {
            state = State.WALK_OFF_OF_PLATFORM;
          }
        }
        break;
      case WALK_OFF_OF_PLATFORM:
        if (b.playerX == playerX2 && b.playerY == playerY) {
          state = State.DONE;
        } else if (playerX1 > playerX2) {
          b.pressLeft();
        } else {
          b.pressRight();
        }
        break;
    }    
  } 
}