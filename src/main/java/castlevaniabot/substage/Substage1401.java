package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.GameObjectType;

import static castlevaniabot.model.gameelements.Addresses.*;
import static castlevaniabot.model.gameelements.GameObjectType.*;
import static castlevaniabot.model.creativeelements.Weapon.*;

public class Substage1401 extends Substage {
  
  private boolean treasureTriggered1;
  private boolean treasureTriggered2;
  private boolean treasureTriggered3;
  
  private boolean blockWhipped1;
  private boolean blockBroken1;

  private boolean blockWhipped2;
  private boolean blockBroken2;   
  
  public Substage1401(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    blockWhipped1 = blockBroken1 = blockWhipped2 = blockBroken2 
        = treasureTriggered1 = false;
    treasureTriggered3 = treasureTriggered2 = b.weapon != HOLY_WATER;
    mapRoutes = b.allMapRoutes.get("14-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == GameObjectType.AXE) {
      if (obj.distanceX < 64 && obj.y1 <= b.playerY 
          && obj.y2 >= b.playerY - 32) {
        obj.tier = 9;
      }
    } else if (obj.type == GameObjectType.AXE_KNIGHT) {
      if (obj.distanceX < 64 && obj.y1 <= b.playerY + 16
          && obj.y2 >= b.playerY - 64) {
        obj.tier = 9;
      }
    } else if (obj.type == RED_SKELETON) {
      if (obj.distanceX < 64 && obj.distanceY <= 4) {
        obj.tier = 7;
      }
    } else if (obj.type == RED_BONES || obj.type == RED_SKELETON_RISING) {      
      if (obj.distanceY <= 4 && (obj.distanceX <= 32 
          || (obj.playerFacing && obj.distanceX < 48))) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 18:
              if (b.weapon == HOLY_WATER && b.hearts > 0 && !b.onStairs 
                  && b.playerY >= 128 && b.playerX >= 288 && b.playerX < 400) {
                obj.tier = 1;
              }
              break;
            case 22:
              if (b.weapon == HOLY_WATER && b.hearts > 0) {
                obj.tier = 1;
              }
              break;
            default:
              obj.tier = 1;
              break;
          }          
          break;
        case BLOCK: 
          obj.tier = 2; break;
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
  }

  @Override
  public void pickStrategy() {
    if (b.strategy == b.WAIT) {
      if (b.playerX >= 832) {
        if (treasureTriggered1) {
          super.pickStrategy();
        }
      } else {
        if (treasureTriggered3) {
          super.pickStrategy();
        }
      }
    } else if (!treasureTriggered3 && !b.onStairs && b.playerX >= 288 
        && b.playerX < 480 && b.playerY > 128
            && !b.isEnemyInBounds(288, 128, 480, 208)) {
      clearTarget();
      b.WAIT.init(297, 192);
      b.strategy = b.WAIT;
    } else if (!treasureTriggered1 && b.playerX >= 928 && b.playerX < 1024 
        && b.playerY > 112 && !b.isEnemyInBounds(816, 112, 1024, 208)) {
      clearTarget();
      b.WAIT.init(984, 192);
      b.strategy = b.WAIT;
    } else if (b.playerY == 192 && b.playerX <= 33) {
      b.pressLeft();
    } else {
      super.pickStrategy();
    }
  }

  @Override
  public void readGameObjects() {
    if (b.playerX >= 672 && b.playerX < 784 && b.playerY <= 128) {
      final boolean block1 = api.readPPU(BLOCK_140100) == 0x00;
      final boolean block2 = api.readPPU(BLOCK_140101) == 0x00;
      if (!blockBroken1 && block1) {
        blockWhipped1 = blockBroken1 = true;
        if (block1 && block2) {
          mapRoutes = b.allMapRoutes.get("14-01-01");
        }
      }
      if (!blockBroken2 && block2) {
        blockWhipped2 = blockBroken2 = true;
        if (block1 && block2) {
          mapRoutes = b.allMapRoutes.get("14-01-01");
        }
      }
      if (!blockWhipped1) {
        b.addBlock(768, 64);
      }
      if (!blockWhipped2) {
        b.addBlock(768, 80);
      }
    }    
    if (treasureTriggered2) {
      b.addDestination(32, 192);
    } else {
      b.addDestination(560, 128);
      if (b.playerX < 208) {
        treasureTriggered2 = true;
      } else if (b.playerX == 560 && b.playerY == 128) {
        if (b.kneeling) {
          treasureTriggered2 = true;
        } else {
          b.kneel();
        }
      }      
    }   
  }  

  @Override
  public void route(final int targetX, final int targetY,
                    final boolean checkForEnemies) {
    if (b.onStairs && b.playerY >= 156 && b.playerX < 256) {
      if (!b.isEnemyInBounds(120, 156, 184, 208)) {
        super.route(targetX, targetY, checkForEnemies);
      }
    } else if (b.playerX >= 1200 && b.playerY > 96 && b.playerY < 144) {
      if (!b.isEnemyInBounds(1192, 0, 1279, 104)) {
        super.route(targetX, targetY, checkForEnemies);
      }
    } else {
      super.route(targetX, targetY, checkForEnemies);
    }
  }

  @Override
  public void routeLeft() {
    if (b.playerX >= 1056 && b.playerY >= 144) {
      route(1065, 192);
    } else if (b.playerX >= 800 && b.playerY <= 96) {
      route(809, 96);
    } else if (b.playerX >= 288 && b.playerX < 1024 && b.playerY >= 112) {
      route(297, 192);
    } else if (b.playerY <= 96 && b.playerX < 768) {
      route(41, 96);
    } else {
      route(32, 192);
    }
  }
  
  @Override
  public void routeRight() {
    if (b.playerX >= 1056 && b.playerY >= 144) {
      route(1262, 192);
    } else if (b.playerX >= 800 && b.playerY <= 96) {
      route(1262, 96);
    } else if (b.playerX >= 288 && b.playerX < 1024 && b.playerY >= 112) {
      route(1015, 192);
    } else if (b.playerY <= 96 && b.playerX < 768) {
      route(759, 96);
    } else {
      route(247, 192);
    }
  }

  @Override
  public void treasureTriggered() {
    if (b.playerX >= 832) {
      treasureTriggered1 = true;
    } else {
      treasureTriggered3 = true;
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