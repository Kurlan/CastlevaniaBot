package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.*;

public class GotCrystalBallStrategy extends Strategy {

  private int jumpDelay;
  
  public GotCrystalBallStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override public void init() {
    jumpDelay = 0;
  }

  @Override public void step() {
    if (jumpDelay > 0) {
      if (--jumpDelay == 0) {
        b.whip();
      }
    } else if (b.canJump) {
      b.jump();
      jumpDelay = 16 + ThreadLocalRandom.current().nextInt(11);
    }
  }  
}