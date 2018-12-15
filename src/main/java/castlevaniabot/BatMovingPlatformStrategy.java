package castlevaniabot;

import static castlevaniabot.GameObjectType.*;

public class BatMovingPlatformStrategy extends Strategy {
  
  private static final int BAT_TIMER_THRESHOLD = 120;
  
  boolean done;
  
  private int lastY0;
  private int lastY1;
  private int lastY2;
  private int lastX; 
  private int lastPlatformX;
  private int lastFishmanX;
  private int lastFishmanY;
  private int batTimer;
  private boolean jumpRequested;
  private boolean left;
  private GameObject lastBat;
  
  public BatMovingPlatformStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override void init() {
    left = jumpRequested = done = false;
    batTimer = BAT_TIMER_THRESHOLD;
    lastBat = null;
  }

  @Override void step() {
    
    if (done) {
      return;
    }
    
    ++batTimer;    
    final GameObject bat = getRedBat();
    if (bat != null) {
      final int offsetX = (bat.x > lastX) ? 16 : -16;
      lastX = bat.x;
      final int offsetY;
      if (bat.y > lastY2) {
        offsetY = 8;
      } else if (bat.y < lastY2) {
        offsetY = -8;
      } else {
        offsetY = 0;
      }
      lastY2 = lastY1;
      lastY1 = lastY0;
      lastY0 = bat.y;
      
      batTimer = BAT_TIMER_THRESHOLD;

      if (b.isInStandingWhipRange(bat, offsetX, offsetY)) {
        if (b.face(bat) && !b.weaponing) {
          b.whip();                            
        }
      } else if (b.isInKneelingWhipRange(bat, offsetX, offsetY)) {
        if (b.face(bat)) {
          b.kneel();
          if (b.kneeling && !b.weaponing) {
            b.whip();   
          }
        }      
      }
    } else if (lastBat != null) {
      batTimer = 0;
    }
    lastBat = bat;    
    
    final MovingPlatform platform = getMovingPlatform();    
    if (platform != null) {      
      final int vx = platform.x1 - lastPlatformX;
      lastPlatformX = platform.x1;
      if (vx < 0) {
        left = true;
      } else if (vx > 0) {
        left = false;
      }
    } 
    
    final GameObject fishman = getFishman();
    if (fishman != null) {
      final int offsetX = (fishman.x - lastFishmanX) << 4;
      final int offsetY = (fishman.y - lastFishmanY) << 4;
      lastFishmanX = fishman.x;
      lastFishmanY = fishman.y;
      
      if (b.isInStandingWhipRange(fishman, offsetX, offsetY)) {
        if (b.face(fishman) && !b.weaponing) {
          b.whip();
        }
      }
    }
    
    if (bat == null && fishman == null && (b.playerLeft || (b.playerX > 400 
        && (b.playerX - (b.tileX << 4)) < 19))) {
      b.pressRight();
    }
    
    if (platform != null) {
      if (b.playerX + 4 >= platform.x1 && b.playerX - 4 <= platform.x2) {
        final int cx = platform.x1 + 24;
        if (b.playerX == cx) {
          if (b.playerLeft) {
            b.pressLeft();
          }
        } else if (b.playerX < cx) {
          b.pressRight();
        } else {
          b.pressLeft();
        }

        if (b.playerX >= 856 && b.playerX < 936) {
          b.kneel();
        }
      } else if (b.canJump && batTimer < BAT_TIMER_THRESHOLD) {
        if (b.playerX > 400) {
          if (left && platform.x1 <= 812) {
            jumpRequested = true;
            batTimer = BAT_TIMER_THRESHOLD;
          }
        } else {
          if ((left && platform.x1 < 296) || (!left && platform.x1 < 246)) {
            jumpRequested = true;
            batTimer = BAT_TIMER_THRESHOLD;
          }
        }
      }
    }
    
    if ((b.playerX >= 352 && b.playerX < 388) 
        || (b.playerX >= 948 && b.playerX < 964)) {
      jumpRequested = true;
    }
    
    if (jumpRequested) {
      if (b.playerLeft) {
        b.pressRight();
      } else {
        b.pressRightAndJump();
        jumpRequested = false;
        if ((b.playerX >= 380 && b.playerX < 388) 
            || (b.playerX >= 956 && b.playerX < 964)) {
          done = true;
        }
      }
    }    
  }
  
  private MovingPlatform getMovingPlatform() {
    return (b.movingPlatformsCount > 0) ? b.movingPlatforms[0] : null;
  }
  
  private GameObject getRedBat() {
    final GameObject[] objs = b.objs;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      if (objs[i].type == RED_BAT) {
        return objs[i];
      }
    }
    return null;
  }
  
  private GameObject getFishman() {
    final GameObject[] objs = b.objs;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      if (objs[i].type == FISHMAN) {
        return objs[i];
      }
    }
    return null;
  }
}