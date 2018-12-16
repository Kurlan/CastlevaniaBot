package castlevaniabot.strategy;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;

import static castlevaniabot.model.gameelements.GameObjectType.*;

public class DeathHallHolyWaterStrategy extends Strategy {
  
  private static final int HOLY_WATER_RESET = 180;
  
  private int jumpCounter; 
  private int holyWaterDelay;  

  public DeathHallHolyWaterStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override
  public void init() {
    jumpCounter = holyWaterDelay = 0;
  }

  @Override
  public void step() {
    
    if (holyWaterDelay > 0) {
      --holyWaterDelay;
    }
    
    if (jumpCounter > 0) {
      if (--jumpCounter == 0) {
        holyWaterDelay = HOLY_WATER_RESET;        
        b.useWeapon();
      }
      return;
    } else if (holyWaterDelay == 0) {    
      final GameObject[] objs = b.gameObjects;
      for(int i = b.objsCount - 1; i >= 0; --i) {
        final GameObject obj = objs[i];
        if (obj.type == AXE_KNIGHT && obj.x < botState.getPlayerX() && obj.distanceX < 64) {
          jumpCounter = 9;
          b.pressLeftAndJump();
          return;
        }
      }
    }
    
    b.substage.route(9, 128, false);
  }  
}