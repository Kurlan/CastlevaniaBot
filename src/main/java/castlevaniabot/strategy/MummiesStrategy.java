package castlevaniabot.strategy;

import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.GameObjectType;
import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.*;
import static java.lang.Math.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class MummiesStrategy extends Strategy {
  
  private GameObject mummy1; // left mummy
  private GameObject mummy2; // right mummy
  private int mummyLastX1;
  private int mummyLastX2;
  private int mummyOffsetX1;
  private int mummyOffsetX2;
  private int weaponDelay;
  private int routeX;
  private int routeY;
  private int moveAway;
  private int moveAwayFromX;
  private boolean weaponedMummy1;

  public MummiesStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    mummy1 = mummy2 = null;
    moveAway = moveAwayFromX = routeX = routeY = weaponDelay = mummyOffsetX1 
        = mummyOffsetX2 = 0;
    weaponedMummy1 = ThreadLocalRandom.current().nextBoolean();
  }
  
  @Override
  public void step() {
    
    mummy1 = null;
    mummy2 = null;
    final GameObject[] objs = b.gameObjects;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      if (obj.type == GameObjectType.MUMMY) {
        if (mummy1 == null) {
          mummy1 = obj;
        } else {
          mummy2 = obj;
          if (mummy1.x > mummy2.x) {
            final GameObject temp = mummy1;
            mummy1 = mummy2;
            mummy2 = temp;
          }
          break;
        }
      }
    }
    if (mummy1 == null) {
      b.substage.bossDefeated();
      return;
    } else {
      mummyOffsetX1 = mummy1.x - mummyLastX1;
      mummyLastX1 = mummy1.x;
    }
    if (mummy2 != null) {
      mummyOffsetX2 = mummy2.x - mummyLastX2;
      mummyLastX2 = mummy2.x;
    }    
    
    if (botState.getPlayerY() > 164) {
      if (b.hearts > 0 && b.weapon == BOOMERANG) {
        stepBoomerangStrategy();
        return;
      } else if (b.SUBSTAGE_0900.blockBroken) {
        stepGroundAssault();
        return;
      }
    } 
    
    if (b.hearts > 0) {
      switch(b.weapon) {
        case BOOMERANG:  stepBoomerangStrategy(); break;
        case HOLY_WATER: stepHolyWaterStrategy(); break;
        default:         stepNoWeaponsStrategy(); break;
      }      
    } else {
      stepNoWeaponsStrategy();
    }
  }
  
  private void stepGroundAssault() {
    
    GameObject closestMummy = mummy1;
    if (mummy2 != null && mummy2.distanceX < mummy1.distanceX) {
      closestMummy = mummy2;
    }
    
    if (moveAway > 0) {
      --moveAway;
      b.substage.moveAwayFromTarget(moveAwayFromX);
      return;
    } 
    
    if (closestMummy.distanceX < 30 && botState.getPlayerX() > 1312 && botState.getPlayerX() < 1488) {
      moveAway = 17 + ThreadLocalRandom.current().nextInt(17);
      moveAwayFromX = closestMummy.x;
      b.substage.moveAwayFromTarget(closestMummy.x);
      return;
    } 
    
    final boolean inRange1 = b.isInStandingWhipRange(mummy1, 
        mummyOffsetX1, 0);
    final boolean inRange2 = mummy2 != null && b.isInStandingWhipRange(mummy2, 
        mummyOffsetX2, 0);
    final GameObject targetMummy;
    if (inRange1 && inRange2) {      
      targetMummy = weaponedMummy1 ? mummy2 : mummy1;      
    } else if (inRange1) {
      targetMummy = mummy1;      
    } else if (inRange2) {
      targetMummy = mummy2;
    } else {
      targetMummy = null;
    }
    if (targetMummy != null && b.face(targetMummy) && !b.weaponing) {
      weaponedMummy1 = !weaponedMummy1;
      b.whipOrWeapon();
    }
  }
  
  private void stepBoomerangStrategy() {
        
    if (botState.getPlayerX() != 1512 || botState.getPlayerY() != 208 || !b.playerLeft) {
      b.substage.routeAndFace(1512, 208, true, false);
    } else if (!b.weaponing) {
      b.whipOrWeapon();
    }
  }
  
  private void stepNoWeaponsStrategy() {
    
    if (mummy1 != null) { // mummy1 is the left-most mummy
      if (mummy1.x < 1356) {
        routeX = 1322;
        routeY = 160;
      } else if (mummy1.x > 1364) {
        routeX = 1338;
        routeY = 176;
      }
    }
    
    if (botState.getPlayerX() != routeX || botState.getPlayerY() != routeY || b.playerLeft) {
      b.substage.routeAndFace(routeX, routeY, false);
    } else {
      b.kneel();
      if (b.kneeling && !b.weaponing) {
        b.whip();
      }
    }
  }
  
  private void stepHolyWaterStrategy() {
    
    if (botState.getPlayerX() != 1290 || botState.getPlayerY() != 144 || b.playerLeft) {
      b.substage.routeAndFace(1290, 144, false);
    } else if (weaponDelay > 0) {
      --weaponDelay;
    } else if (!b.weaponing && ((mummy1 != null && abs(mummy1.x - 1360) < 16) 
        || (mummy2 != null && abs(mummy2.x - 1360) < 16))) {
      weaponDelay = 60;
      b.useWeapon();
    }
  }
}