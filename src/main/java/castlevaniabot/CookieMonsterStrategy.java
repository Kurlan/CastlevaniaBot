package castlevaniabot;

import java.util.concurrent.*;

public class CookieMonsterStrategy extends Strategy {

  private GameObject head;
  private GameObject item;
  private int jumpCounter;  
  private int playerX;
  private boolean playerLeft;
  
  boolean done;
  
  public CookieMonsterStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override void init() {
    item = head = null;
    jumpCounter = playerX = 0;
    done = playerLeft = false;
  }

  @Override void step() {
    
    if (done) {
      return;
    }
    
    updateObjects();
    
    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        b.whipOrWeapon();
      }
    }
    
    if (head == null) {
      done = true;
    } else {      
      if (item != null) {
        playerX = item.x;
        playerLeft = item.x < b.playerX;
      } else if (b.playerX < head.x) {
        playerX = head.x - 48;
        playerLeft = false;
      } else {
        playerX = head.x + 48;
        playerLeft = true;
      }
      
      if (head.distanceX > 24) {
        if (playerX < 10) {
          playerX = 10;
          playerLeft = false;
        } else if (playerX > 238) {
          playerX = 238;
          playerLeft = true;
        }
      } else {
        if (playerX < 10) {
          playerX = head.x + 48;
          playerLeft = true;
        } else if (playerX > 238) {
          playerX = head.x - 48;
          playerLeft = false;
        }
      }
      
      if (b.playerX != playerX || b.playerLeft != playerLeft) {
        b.substage.routeAndFace(playerX, 192, playerLeft, false);
      } else if (b.canJump) {
        b.jump();
        jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);        
      }
    } 
  } 
  
  private void updateObjects() {
    item = head = null;
    final GameObject[] objs = b.objs;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      switch(obj.type) {
        case COOKIE_MONSTER_HEAD:
          head = obj;
          break;
        case DOULE_SHOT:
        case TRIPLE_SHOT:
        case LARGE_HEART:
        case PORK_CHOP:
        case HOLY_WATER_WEAPON:
          item = obj;
          break;
        case CRYSTAL_BALL:
          done = true;
          break;
      }
    }
  }
}