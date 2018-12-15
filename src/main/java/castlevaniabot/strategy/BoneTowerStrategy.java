package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;

import java.util.concurrent.*;
import static castlevaniabot.Weapon.*;

public class BoneTowerStrategy extends Strategy {
  
  private int moveAway;
  private boolean usedHolyWater;
  
  public BoneTowerStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override
  public void init() {
    usedHolyWater = false;
  }

  @Override
  public void step() {
    
    final GameObject tower = b.target;
    
    if (b.weaponing) {
      return;
    }
    
    if (moveAway > 0) {
      --moveAway;
      b.substage.moveAwayFromTarget();
    } else if (b.isTargetInStandingWhipRange()) {
      if (b.faceTarget()) {
        if (usedHolyWater || b.weapon != HOLY_WATER || b.hearts == 0 
            || tower.distanceX > 48) {
          b.whip();
        } else {
          usedHolyWater = true;
          b.whipOrWeapon();
        }
      }
    } else if (tower.distanceX < 24) {
      moveAway = 30 + ThreadLocalRandom.current().nextInt(11);
      b.substage.moveAwayFromTarget();
    } else {
      b.substage.moveTowardTarget();
    }
  }  
}