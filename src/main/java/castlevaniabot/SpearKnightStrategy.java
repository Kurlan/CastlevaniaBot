package castlevaniabot;

public class SpearKnightStrategy extends Strategy {
  
  private int lastX;
  private int lastY;
  private boolean usedHolyWater;
  
  public SpearKnightStrategy(final CastlevaniaBot b) {
    super(b);
  }  

  @Override public void init() {
    usedHolyWater = false;
  }

  @Override public void step() {
    
    final GameObject knight = b.target;
    final int offsetX = (knight.x - lastX) << 4;
    final int offsetY = (knight.y - lastY) << 4;
    lastX = knight.x;
    lastY = knight.y;
    
    if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (!b.weaponing && b.faceTarget()) {
        useWeapon();
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY + 32)) {
      if (b.faceTarget() && b.canJump) {
        b.jump();
      }
    } else if (knight.distanceX < 24) {
      b.substage.moveAwayFromTarget();
    } else if (knight.distanceX > 32) {
      b.substage.moveTowardTarget();
    }    
  }
  
  private void useWeapon() {
    if (!usedHolyWater) {
      usedHolyWater = b.grind();
    } else {
      b.whip();
    }
  }
}