package castlevaniabot;

import java.util.concurrent.*;

public class MedusaHeadsPitsStrategy extends Strategy {

  private static enum State {
    WALK_TO_EDGE,
    RUN_FOR_IT,
    WAIT_FOR_HEAD,
    DONE,
  }
  
  private State state;
  private boolean damageBoost;
  private int delay;
  
  public MedusaHeadsPitsStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override void init() {
    state = State.WALK_TO_EDGE;
    damageBoost = ThreadLocalRandom.current().nextBoolean();
    delay = 48;
  }

  @Override void step() {
    switch(state) {
      case WALK_TO_EDGE:
        if (b.tileX != 7 || b.tileY != 12) {
          b.substage.route(127, 192);
        } else if (b.playerX < 131) {
          b.pressRight();
        } else {
          b.pressRightAndJump();
          state = State.RUN_FOR_IT;
        }
        break;
      case RUN_FOR_IT:
        if (damageBoost && b.playerX == 232) {
          state = State.WAIT_FOR_HEAD;
        } else if (b.playerX == 328 && b.playerY == 176) {
          state = State.DONE;
        } else {
          b.substage.route(328, 176);
        }
        break;
      case WAIT_FOR_HEAD:
        if (--delay == 0) {
          b.jump();
          state = State.DONE;
        }
        break;
    }
  }
}