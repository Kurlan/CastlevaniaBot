package castlevaniabot.strategy;

import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.CastlevaniaBot;

import java.util.concurrent.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class WhiteSkeletonStrategy extends Strategy {
  
  private static final int HOLY_WATER_RESET = 240;
  
  private int jumpCounter;  
  private int lastX;
  private int lastY;
  private int holyWaterDelay;
  private boolean drawingTowardHolyWater;

  public WhiteSkeletonStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override
  public void init() {
    jumpCounter = holyWaterDelay = 0;
    drawingTowardHolyWater = false;
  }
  
  @Override
  public void step() {
    
    final GameObject skeleton = b.getTargetedObject().getTarget();
    final int offsetX = (skeleton.x - lastX) << 4;
    final int offsetY = (skeleton.y - lastY) << 4;
    lastX = skeleton.x;
    lastY = skeleton.y;
    
    if (skeleton.distanceX > 112) {
      holyWaterDelay = 0;
      drawingTowardHolyWater = false;
    }
    if (holyWaterDelay > 0 && --holyWaterDelay == 0) {
      drawingTowardHolyWater = false;
    }    

    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        holyWaterDelay = HOLY_WATER_RESET;
        drawingTowardHolyWater = true;
        b.useWeapon();
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (!b.weaponing && b.faceTarget()) {
        if (holyWaterDelay > 0) {
          b.whip();
        } else if (b.grind()) { 
          holyWaterDelay = HOLY_WATER_RESET;
        }
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
      if (b.faceTarget() && b.canJump) {
        b.jump();
      }
    } else if (drawingTowardHolyWater) {
      b.substage.moveAwayFromTarget(b.getTargetedObject().getTarget());
    } else if (!b.onStairs && holyWaterDelay == 0 && b.weapon == HOLY_WATER 
        && b.hearts > 0 && skeleton.distanceX < 96 
            && skeleton.distanceY <= 36) {
      if (!b.weaponing && b.faceTarget() && b.canJump) {
        if (b.isUnderLedge()) {
          holyWaterDelay = HOLY_WATER_RESET;
          drawingTowardHolyWater = true;
          b.useWeapon();
        } else {
          jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
          b.jump();
        }
      }
    } else {
      b.substage.moveTowardTarget(b.getTargetedObject().getTarget());
    }   
  }
}