package castlevaniabot;

import nintaco.api.*;
import static castlevaniabot.Addresses.*;
import static castlevaniabot.GameObjectType.*;

public class CrusherStrategy extends Strategy {
  
  private static enum State {
    INACTIVE,
    WALK_TO_LEFT_WALL,
    WAIT_FOR_CRUSHER_2,
    WALK_BETWEEN_CRUSHERS_1_AND_2,
    WHIP_CANDLE_1_2,
    WAIT_FOR_CRUSHER_1,
    WALK_BETWEEN_CRUSHERS_0_AND_1,
    WHIP_CANDLE_0_1,
    WAIT_FOR_CRUSHER_0,
    WALK_IN_FRONT_OF_CRUSHER_0,
    WHIP_CANDLE_0,
  }

  private final API api = ApiSource.getAPI(); 
  
  private final int[] ys = new int[3];
  private final boolean[] ascendings = new boolean[3];
  
  private State state;
  private int delay;
  
  public CrusherStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override void init() {
    state = State.INACTIVE;
    delay = 20;
  }
  
  @Override void step() {
    
    readCrushers();
    
    switch(state) {
      case WALK_TO_LEFT_WALL:
        if (b.playerX != 521 || b.playerY != 208) {
          b.substage.route(521, 208);
        } else {
          state = State.WAIT_FOR_CRUSHER_2;
        }
        break;
      case WAIT_FOR_CRUSHER_2:
        if (ascendings[2] && ys[2] == 4) {
          b.pressLeftAndJump();
          state = State.WALK_BETWEEN_CRUSHERS_1_AND_2;
        }
        break;
      case WALK_BETWEEN_CRUSHERS_1_AND_2:
        if (b.playerX == 456 && b.playerY == 176) {
          state = State.WHIP_CANDLE_1_2;
        } else {
          b.substage.route(456, 176);
        }
        break;
      case WHIP_CANDLE_1_2:
        if (delay > 0) {
          if (--delay == 0) {
            state = State.WAIT_FOR_CRUSHER_1;
          }
        } else if (b.playerLeft) {
          b.pressRight();
        } else {
          b.whip();
          delay = 48;
        }
        break;
      case WAIT_FOR_CRUSHER_1:
        if (ascendings[1] && ys[1] == 3) {
          state = State.WALK_BETWEEN_CRUSHERS_0_AND_1;
        }
        break;
      case WALK_BETWEEN_CRUSHERS_0_AND_1:
        if (b.playerX == 392 && b.playerY == 176) {
          state = State.WHIP_CANDLE_0_1;
        } else {
          b.substage.route(392, 176);
        }
        break;
      case WHIP_CANDLE_0_1:
        if (delay > 0) {
          if (--delay == 0) {
            state = State.WAIT_FOR_CRUSHER_0;
          }
        } else if (b.playerLeft) {
          b.pressRight();
        } else {
          b.whip();
          delay = 48;
        }
        break;
      case WAIT_FOR_CRUSHER_0:
        if (ascendings[0] && ys[0] == 3) {
          state = State.WALK_IN_FRONT_OF_CRUSHER_0;
        }
        break;
      case WALK_IN_FRONT_OF_CRUSHER_0:
        if (b.playerX == 328 && b.playerY == 176) {
          state = State.WHIP_CANDLE_0;
        } else {
          b.substage.route(328, 176);
        }
        break;
      case WHIP_CANDLE_0:
        if (delay > 0) {
          if (--delay == 0) {
            init();
          }
        } else if (b.playerLeft) {
          b.pressRight();
        } else {
          b.whip();
          delay = 48;
        }
        break;
    }
  }
  
  boolean isActive() {
    
    if (state == State.INACTIVE && b.playerX >= 512 && b.playerX <= 671
        && b.playerY >= 96) {
      final GameObject[] objs = b.objs;
      outer: {
        for(int i = b.objsCount - 1; i >= 0; --i) {
          final GameObject obj = objs[i];
          if (obj.type != DESTINATION && obj.x2 >= 512 && obj.x1 <= 671 
              && obj.y2 >= 96) {
            break outer;
          }
        }
        if (delay > 0) {
          --delay;
        } else {
          state = State.WALK_TO_LEFT_WALL;
        } 
      }
    }
    
    return state != State.INACTIVE;
  }

  private void readCrushers() {
    for(int i = 2; i >= 0; --i) {
      final int address = CRUSHER + (i << 3);
      for(int j = 0; j < 8; ++j) {
        if (api.readPPU(address + (j << 5)) != 0) {
          if (ys[i] < j) {
            ascendings[i] = false;
          } else if (ys[i] > j) {
            ascendings[i] = true;
          }
          ys[i] = j;
          break;
        } 
      }
    }
  }
}