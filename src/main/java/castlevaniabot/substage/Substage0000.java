package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;
import nintaco.api.*;
import static java.lang.Math.*;
import static castlevaniabot.Addresses.*;
import static castlevaniabot.Weapon.*;

public class Substage0000 extends Substage {
  
  private final API api = ApiSource.getAPI();
  
  private boolean triggeredTreasure;
  
  public Substage0000(final CastlevaniaBot b) {
    super(b);    
  }  

  @Override
  public void init() {
    super.init();    
    mapRoutes = b.allMapRoutes.get("00-00-00");
    triggeredTreasure = false;
    
    // The bot cannot handle Difficult Mode (game loop 2 or above).
    if (api.readCPU(LOOP) != 0x00) { 
      api.writeCPU(LOOP, 0x00);       // Reset to Normal Mode.
      api.writeCPU(WEAPONING, 0x00);  // Clear weaponing state from prior loop.
    }
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    switch(obj.type) {
      case DESTINATION:   
        obj.tier = 0; break;
      case FIRE_COLUMN:
        obj.tier = 1; break;
      case MONEY_BAG:
      case SMALL_HEART:
      case LARGE_HEART:        
      case INVISIBLE_POTION:
      case WHIP_UPGRADE:
        obj.tier = 3; break;
      case CROSS:          
      case DOULE_SHOT:
      case TRIPLE_SHOT:
        obj.tier = 4; break;
      case DAGGER_WEAPON:
        if (b.weapon == NONE || b.weapon == STOPWATCH) {
          obj.tier = 5;
        } else {
          b.avoid(obj);
        }
        break;        
      case BOOMERANG_WEAPON:
        if (b.weapon != HOLY_WATER) {
          obj.tier = 5;
        } else {
          b.avoid(obj);
        }
        break;
      case AXE_WEAPON:
        if (b.weapon != HOLY_WATER && b.weapon != BOOMERANG) {
          obj.tier = 5;
        } else {
          b.avoid(obj);
        }
        break;          
      case STOPWATCH_WEAPON:  
        if (b.weapon == NONE) {
          obj.tier = 5;
        } else {
          b.avoid(obj);
        }
        break;          
      case HOLY_WATER_WEAPON:
      case PORK_CHOP:
      case EXTRA_LIFE:
        obj.tier = 5; break;
    }
  }

  @Override
  public void route(final int targetX, final int targetY) {
    if (targetX > b.playerX) {
      if (targetX > 696 && b.playerX >= 676 && b.playerX < 696) {
        b.pressRightAndJump();
      } else {
        b.pressRight();      
      }
    } else if (targetX < b.playerX) {
      if (targetX < 696 && b.playerX > 696 && b.playerX <= 714) {
        b.pressLeftAndJump();
      } else {     
        b.pressLeft();
      }
    } 
  }
  
  @Override
  public void routeLeft() {
    route(9, 192);
  }
  
  @Override
  public void routeRight() {
    route(760, 192);
  }  

  @Override
  public void readGameObjects() {
    if (triggeredTreasure) {
      b.addDestination(696, 192);
    } else {
      if (abs(b.playerX - 712) < 2) {
        triggeredTreasure = true;
      } else {
        b.addDestination(712, 192);
      }
    }
  } 
}