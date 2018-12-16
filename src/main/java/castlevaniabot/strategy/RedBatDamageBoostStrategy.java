package castlevaniabot.strategy;

import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.CastlevaniaBot;

import static castlevaniabot.model.gameelements.GameObjectType.*;

public class RedBatDamageBoostStrategy extends Strategy {
  
  private boolean batSpawned;
  
  public RedBatDamageBoostStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override public void init() {
    batSpawned = false;
  }

  @Override public void step() {
    
    if (batSpawned) {
      if (b.playerLeft) {
        b.pressRightAndJump();
        b.SUBSTAGE_0201.redBatDamageBoostDone();
      } else {
        b.pressLeft();
      }
    } else {
      final GameObject bat = b.getType(RED_BAT);
      if (b.playerX != 195 || b.playerY != 144 || b.playerLeft) {
        if (bat != null) {
          b.target = bat;
          b.RED_BAT.step();
        } else if (b.playerY != 144 || b.playerX < 191) {
          b.substage.route(191, 144);
        } else if (b.playerX < 195) {
          b.pressRight();
        }
      } else if (bat != null) {
        if (bat.left && bat.x >= b.playerX - 16) {
          if (bat.x < 272) {
            batSpawned = true;
            b.pressLeft();
          }
        } else {
          b.target = bat;
          b.RED_BAT.step();
        }
      }
    }
  } 
}