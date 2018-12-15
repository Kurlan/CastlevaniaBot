package castlevaniabot.strategy;

import castlevaniabot.GameObject;
import castlevaniabot.CastlevaniaBot;

import static castlevaniabot.GameObjectType.*;

public class HolyWaterDeathStrategy extends Strategy {
  
  private boolean deathSpawned;
  
  private int jumpCounter;
  private int jumpDelay;
  
  public boolean done;

  public HolyWaterDeathStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    jumpDelay = jumpCounter = 0;
    done = deathSpawned = false;
  }
  
  @Override
  public void step() {
    
    if (done) {
      return;
    }
    
    GameObject death = null;
    final GameObject[] objs = b.gameObjects;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      if (obj.type == DEATH) {
        death = obj;
        break;
      }
    }
    if (death != null) {
      deathSpawned = true;
    }
    
    if (death == null && deathSpawned) {
      done = true;
      return;
    }
    
    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        b.whip();
      }
    } else if (b.tileX != 11) {
      b.substage.route(191, 160, false);
    } else if (b.playerX != 195) {
      b.pressRight(); 
    } else if (jumpDelay > 0) {
      if (b.canJump && --jumpDelay == 0) {
        jumpCounter = 4;
        b.jump();
      }
    } else if (b.onPlatform) {
      jumpDelay = 2;
      b.useWeapon();
    }
  }  
}