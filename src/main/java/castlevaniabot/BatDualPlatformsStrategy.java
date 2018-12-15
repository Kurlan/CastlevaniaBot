package castlevaniabot;

import static castlevaniabot.GameObjectType.*;

public class BatDualPlatformsStrategy extends Strategy {

  boolean done;
  
  private GameObject bat;
  private int lastY0;           
  private int lastY1;
  private int lastY2;
  private int lastX;
  
  private MovingPlatform platform;
  private int lastPlatformX;
  private boolean left;
  
  private GameObject fishman;
  private int lastFishmanX;
  private int lastFishmanY;

  private int justWhipped;  
  private boolean hangRequested;
  private boolean walkRequested;
  
  public BatDualPlatformsStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override void init() {
    justWhipped = 0;
    hangRequested = walkRequested = left = done = false;
  }

  @Override void step() {
    
    if (done) {
      return;
    } else if (b.playerX >= 1280) {
      done = true;
    }
    
    updateObjects();
    
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

      if (b.isInStandingWhipRange(bat, offsetX, offsetY)) {
        if (b.face(bat) && !b.weaponing) {
          b.whip();                            
          justWhipped = 4;
        }
      } else if (b.isInKneelingWhipRange(bat, offsetX, offsetY)) {
        if (b.face(bat)) {
          b.kneel();
          if (b.kneeling && !b.weaponing) {
            b.whip();   
            justWhipped = 4;
          }
        }      
      }
    }
        
    if (hangRequested) {
      if (b.playerX < 995) {
        b.pressRight();
      } else {
        hangRequested = false;
        walkRequested = true;
      }      
    } 
    
    if (walkRequested) {
      if (platform == null || (!left && platform.x1 > 1008) 
          || (b.playerX + 4 >= platform.x1 && b.playerX - 4 <= platform.x2)) {
        walkRequested = false;
      } else if (platform.x1 < 1000) {
        b.pressRight();
      } 
    } else if (justWhipped > 0 && --justWhipped == 0 && bat == null) {
      hangRequested = true;
    }
    
    if (platform != null && b.playerX + 4 >= platform.x1 
        && b.playerX - 4 <= platform.x2) {
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
      
      if (b.playerX >= 1016 && b.playerX < 1064) {
        b.kneel();
      } else if ((platform.x1 >= 1087 && platform.x1 <= 1089) 
          || (platform.x1 >= 1231 && platform.x1 <= 1233)) {
        b.pressRightAndJump();
      } 
    }    
  }
  
  private void updateObjects() {
    
    bat = fishman = null;
    final GameObject[] objs = b.objs;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      if (obj.type == RED_BAT) {
        bat = obj;
      } else if (obj.type == FISHMAN) {
        fishman = obj;
      }
    }
    
    platform = null;
    final MovingPlatform[] movingPlatforms = b.movingPlatforms;
    for(int i = b.movingPlatformsCount - 1; i >= 0; --i) {
      final MovingPlatform p = movingPlatforms[i];
      if (p.x2 >= b.playerX && (platform == null 
          || (p.x2 - b.playerX < platform.x2 - b.playerX))) {
        platform = p;
      }
    }
    if (platform != null) {
      final int vx = platform.x1 - lastPlatformX;
      lastPlatformX = platform.x1;
      if (vx > 0) {
        left = false;
      } else if (vx < 0) {
        left = true;
      }      
    }
  }
}