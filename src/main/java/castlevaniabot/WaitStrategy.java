package castlevaniabot;

import java.util.concurrent.*;
import static castlevaniabot.WaitStrategy.WaitType.*;

public class WaitStrategy extends Strategy {
  
  public static enum WaitType {
    STAND,
    KNEEL,
    WALK_LEFT,
    WALK_RIGHT,
  }

  private static final int WAIT_DELAY = 150;
  
  private int playerX;
  private int playerY;
  private int delay;
  private WaitType waitType;
  private boolean inPosition;
  
  public WaitStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  public void init(final int playerX, final int playerY) {
    init(playerX, playerY, STAND);
  }
  
  public void init(final int playerX, final int playerY, final WaitType waitType) {
    init(playerX, playerY, waitType, (waitType == KNEEL ? 1 : WAIT_DELAY) 
        + ThreadLocalRandom.current().nextInt(11));
  }
  
  public void init(final int playerX, final int playerY, final WaitType waitType,
                   final int delay) {
    this.playerX = playerX;
    this.playerY = playerY;
    this.waitType = waitType;
    this.delay = delay;
    inPosition = false;
  }

  @Override
  public void step() {
    if (b.playerX == playerX && b.playerY == playerY) {
      inPosition = true;
    }
    if (!inPosition) {
      b.substage.route(playerX, playerY);
    } else if (delay > 0) {
      switch(waitType) {
        case KNEEL:      b.kneel();      break;
        case WALK_LEFT:  b.pressLeft();  break; // walk against wall
        case WALK_RIGHT: b.pressRight(); break; // walk against wall
      }
      if (--delay == 0) {
        b.substage.treasureTriggered();
      }
    }
  }  
}