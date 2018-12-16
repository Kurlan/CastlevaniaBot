package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;

import static castlevaniabot.model.gameelements.Addresses.*;
import static castlevaniabot.model.gameelements.GameObjectType.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class Substage1701 extends Substage {
  
  private boolean blockWhipped1;
  private boolean blockBroken1;

  private boolean blockWhipped2;
  private boolean blockBroken2;
  
  private boolean usedStopwatch;
  private boolean killedLowerSkeleton;
  private boolean killedUpperSkeleton;
  
  public Substage1701(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    killedUpperSkeleton = killedLowerSkeleton = usedStopwatch = blockWhipped1 
        = blockBroken1 = blockWhipped2 = blockBroken2 = false;
    mapRoutes = b.allMapRoutes.get("17-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (usedStopwatch) {
      if (obj.type == DESTINATION) {
        obj.tier = 0;
      }
      return;
    }
    
    switch(obj.type) {
      case EAGLE:
        if (obj.distanceX < 64 && obj.y1 <= b.playerY 
            && obj.y2 >= b.playerY - 32) {
          obj.tier = 8;
        }
        break;
      case FLEAMAN:
        if (!b.onStairs && obj.distanceX < 48 && obj.y1 <= b.playerY 
            && obj.y2 >= b.playerY - 48) {
          obj.tier = 7;
        }
        break;
      case WHITE_SKELETON:
        if (obj.distanceX < 128 && obj.y1 <= b.playerY + 16
            && obj.y2 >= b.playerY - 64) {
          obj.tier = 6;
        }
        break;
      case DESTINATION:
        obj.tier = 0;
        break;
      case BLOCK:      
        if (b.playerY > 112 && b.playerX > 364 && b.playerX < 496) {
          obj.tier = 6; 
        }
        break;
      case PORK_CHOP:
        obj.tier = 9;
        break;
      default:
        if (obj.distance < HORIZON && (obj.x < 64 || b.playerX > 480) 
            && obj.y1 <= b.playerY) {
          switch(obj.type) {
            case CANDLES:
              switch(roundTile(obj.x)) {
                case 34:
                  if (b.weapon != STOPWATCH) {
                    obj.tier = 1;
                  }
                  break;
                case 38: // skip this candle
                  break;
                default:
                  obj.tier = 1;
                  break;
              }
              break;
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
            case BOOMERANG_WEAPON:
            case AXE_WEAPON:                         
            case HOLY_WATER_WEAPON:
              if (b.weapon != STOPWATCH) {
                obj.tier = 5; 
              } else {
                b.avoid(obj);
              }
              break;
            case EXTRA_LIFE:
            case STOPWATCH_WEAPON:
              obj.tier = 5; break;
          }
        } 
      break;
    }    
  }
  
  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    if (!usedStopwatch && b.playerX <= 480 && b.playerY <= 96 
        && b.weapon == STOPWATCH && b.hearts >= 5) {
      b.useWeapon();
      usedStopwatch = true;
    } else if (!killedLowerSkeleton && b.playerX >= 496 && b.playerY > 128) {
      if (b.strategy == b.SKELETON_WALL) {
        if (b.SKELETON_WALL.done) {
          killedLowerSkeleton = true;
          super.pickStrategy(targetedObject);
        }
      } else {
        clearTarget(targetedObject);
        b.SKELETON_WALL.init(726, 192, 136);
        b.strategy = b.SKELETON_WALL;
      }
    } else if (!killedUpperSkeleton && b.playerX >= 496 && b.playerY <= 128) {
      if (b.strategy == b.SKELETON_WALL) {
        if (b.SKELETON_WALL.done) {
          killedUpperSkeleton = true;
          super.pickStrategy(targetedObject);
        }
      } else {
        clearTarget(targetedObject);
        b.SKELETON_WALL.init(704, 128);
        b.strategy = b.SKELETON_WALL;
      } 
    } else {
      super.pickStrategy(targetedObject);
    }
  }

  @Override
  public void readGameObjects() {
    if (b.playerX >= 368 && b.playerX < 512) {
      final boolean block1 = api.readPPU(BLOCK_170100) == 0x00;
      final boolean block2 = api.readPPU(BLOCK_170101) == 0x00;
      if (!blockBroken1 && block1) {
        blockWhipped1 = blockBroken1 = true;
        if (block1 && block2) {
          mapRoutes = b.allMapRoutes.get("17-01-01");
        }
      }
      if (!blockBroken2 && block2) {
        blockWhipped2 = blockBroken2 = true;
        if (block1 && block2) {
          mapRoutes = b.allMapRoutes.get("17-01-01");
        }
      }
      if (!blockWhipped1) {
        b.addBlock(480, 160);
      }
      if (!blockWhipped2) {
        b.addBlock(480, 176);
      }
    }    

    if (b.playerX >= 176) {
      b.addDestination(320, 223);
    } else {
      b.addDestination(0, 128);
    }
  }  

  @Override
  public void routeLeft() {
    if (b.playerX >= 496) {
      if (b.playerY <= 128 || (b.onStairs && b.playerY < 192)) {
        route(448, 96);
      } else {
        route(521, 192);
      }
    } else if (b.playerX >= 176) {
      if (b.playerY <= 96) {
        route(336, 96);
      } else if (b.playerX < 368 && b.playerY < 192) {
        route(256, 160);
      } else if (b.playerX >= 368) {
        route(336, 192);
      } else {
        route(320, 223);
      }
    } else {
      route(0, 128);
    }
  }
  
  @Override
  public void routeRight() {
    if (b.playerX >= 496) {      
      if (b.playerY <= 128 || (b.onStairs && b.playerY < 192)) {
        route(727, 128);
      } else {
        route(727, 192);
      }
    } else if (b.playerX >= 176) {
      if (b.playerY <= 96) {
        route(727, 128);
      } else if (b.playerX < 368 && b.playerY < 192) {
        route(359, 192);
      } else if (b.playerX >= 368) {
        if (blockBroken1 && blockBroken2) {
          route(487, 192);
        } else {
          route(471, 192);
        }
      } else {
        route(359, 192);
      }
    } else {
      route(151, 192);
    }
  }
  
  @Override
  public void blockWhipped() {
    if (blockWhipped1) {
      blockWhipped2 = true;
    } else {
      blockWhipped1 = true;
    }
  }  
}