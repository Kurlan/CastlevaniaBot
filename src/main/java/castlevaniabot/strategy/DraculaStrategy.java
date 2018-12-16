package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

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
  
  @Override
  public void init() {
    lastHead = head = fireball = null;
    jumpCounter = playerX = 0;
    playerLeft = false;
  }

  @Override
  public void step() {
    
    updateObjects();
    
    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        b.whip();
      }
    }
    
    if (fireball != null && b.canJump 
        && fireball.left == (botState.getPlayerX() < fireball.x)
            && fireball.distanceX < 48) {
      b.jump();
      jumpCounter = 2 + ThreadLocalRandom.current().nextInt(7);
      return;
    }
    
    if (head != null) {
      
      if (lastHead == null) {
        // head just spawned
        if (botState.getPlayerX() < head.x) {
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
      
      if (botState.getPlayerX() != playerX || b.playerLeft != playerLeft) {
        b.substage.routeAndFace(playerX, 192, playerLeft, false);
      }
    } 
    lastHead = head;
  } 
  
  private void updateObjects() {
    head = fireball = null;
    final GameObject[] objs = b.gameObjects;
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
          b.getAllStrategies().getCOOKIE_MONSTER().init();
          b.getBotState().setCurrentStrategy(b.getAllStrategies().getCOOKIE_MONSTER());
          break;
        case CRYSTAL_BALL:
          b.getAllStrategies().getCOOKIE_MONSTER().done = true;
          b.getBotState().setCurrentStrategy(b.getAllStrategies().getCOOKIE_MONSTER());
          break;
      }
    }
  }
}