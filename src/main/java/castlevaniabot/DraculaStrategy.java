package castlevaniabot;

import java.util.concurrent.*;

public class DraculaStrategy extends Strategy {

  private GameObject head;
  private GameObject lastHead;
  private GameObject fireball;
  private int jumpCounter;  
  private int playerX;
  private boolean playerLeft;
  
  public DraculaStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override void init() {
    lastHead = head = fireball = null;
    jumpCounter = playerX = 0;
    playerLeft = false;
  }

  @Override void step() {
    
    updateObjects();
    
    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        b.whip();
      }
    }
    
    if (fireball != null && b.canJump 
        && fireball.left == (b.playerX < fireball.x) 
            && fireball.distanceX < 48) {
      b.jump();
      jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
      return;
    }
    
    if (head != null) {
      
      if (lastHead == null) {
        // head just spawned
        if (b.playerX < head.x) {
          playerX = head.x - 32;
          playerLeft = false;
        } else {
          playerX = head.x + 32;
          playerLeft = true;
        }
        if (playerX < 10) {
          playerX = head.x + 32;
          playerLeft = true;
        } else if (playerX > 238) {
          playerX = head.x - 32;
          playerLeft = false;
        }
      }
      
      if (b.playerX != playerX || b.playerLeft != playerLeft) {
        b.substage.routeAndFace(playerX, 192, playerLeft, false);
      }
    } 
    lastHead = head;
  } 
  
  private void updateObjects() {
    head = fireball = null;
    final GameObject[] objs = b.objs;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      switch(obj.type) {
        case DRACULA_HEAD:
          head = obj;
          break;
        case FIREBALL:
          fireball = obj;
          break;
        case COOKIE_MONSTER_HEAD:          
          b.COOKIE_MONSTER.init();
          b.strategy = b.COOKIE_MONSTER;
          break;
        case CRYSTAL_BALL:
          b.COOKIE_MONSTER.done = true;
          b.strategy = b.COOKIE_MONSTER;
          break;
      }
    }
  }
}