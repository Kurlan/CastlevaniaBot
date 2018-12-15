package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;
import castlevaniabot.strategy.Strategy;
import castlevaniabot.strategy.WaitStrategy;

import static castlevaniabot.Addresses.*;
import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage0900 extends Substage {
  
  private boolean blockWhipped;
  private boolean enteredTomb;
  private boolean bossTriggered;
  private boolean treasureTriggered;
  private boolean bossDefeated;
  private boolean aboutToGetCrystalBall;
  
  public boolean blockBroken;
  
  public Substage0900(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    blockWhipped = blockBroken = aboutToGetCrystalBall = bossDefeated 
        = bossTriggered = enteredTomb = treasureTriggered = false;
    mapRoutes = b.allMapRoutes.get("09-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
        
    if (obj.type == MUMMY) {
      if (obj.x > 1312 && obj.x < 1502) {
        enteredTomb = treasureTriggered = bossTriggered = true;
      }
    } else if (obj.type == BANDAGE) {
      enteredTomb = treasureTriggered = bossTriggered = true;
    } else if (obj.type == WHITE_SKELETON) {
      obj.tier = 6;
    } else if (obj.type == RAVEN) {
      obj.tier = 7;
    } else if (obj.type == BONE_TOWER) {
      if (obj.distanceX < 80 && obj.y >= b.playerY - 16 && obj.y <= b.playerY) {
        obj.tier = 8;
      }
    } else if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= b.playerY - 32 && obj.y1 <= b.playerY)
              && ((obj.left && obj.x2 >= b.playerX - 16) 
                  || (!obj.left && obj.x1 <= b.playerX + 16))) {
        obj.tier = 9;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      outer: switch(obj.type) {
        case CANDLES: 
          switch(roundTile(obj.x)) {
            case 78: obj.subTier = 2; break;
            case 82:
              if (b.weapon == BOOMERANG) {
                break outer;
              }
              obj.subTier = 1; 
              break;
            case 94:
              if (b.playerX >= 1496) { // right wall inhibits routing
                break outer;
              }
              break;
          }
          obj.tier = 1; break;
        case BLOCK: 
          obj.tier = 2; break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:
        case WHIP_UPGRADE:
        case INVISIBLE_POTION:  
          obj.tier = 3; break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          obj.tier = 4; break;
        case DAGGER_WEAPON:
          if (!bossTriggered && (b.weapon == NONE || b.weapon == STOPWATCH)) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;        
        case BOOMERANG_WEAPON:
          if (!bossTriggered && b.weapon != HOLY_WATER) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;
        case AXE_WEAPON:
          if (!bossTriggered && b.weapon != HOLY_WATER 
              && b.weapon != BOOMERANG) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;          
        case STOPWATCH_WEAPON:  
          if (!bossTriggered && b.weapon == NONE) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;          
        case HOLY_WATER_WEAPON:
        case PORK_CHOP:
          obj.tier = 5;
          break;
        case CRYSTAL_BALL:
          bossTriggered = enteredTomb = treasureTriggered = bossDefeated = true;
          obj.tier = 0;
          break;
      }
    }    
  }

  @Override
  public void route(final int targetX, final int targetY,
                    final boolean checkForEnemies) {
    
    if (bossDefeated) {
      if (b.playerX == 1388 && targetX >= 1424 && !b.playerLeft) {
        b.pressRightAndJump();
      } else if (b.playerX == 1428 && targetX <= 1376 && b.playerLeft) {
        b.pressLeftAndJump();
      } else {
        super.route(targetX, targetY, checkForEnemies);
      }
    } else {
      super.route(targetX, targetY, checkForEnemies);
    }
  }
  
  @Override
  public void pickStrategy() {
    
    if (bossTriggered && !bossDefeated) {
      if (b.strategy != b.MUMMIES) {
        clearTarget();
        b.MUMMIES.init();
        b.strategy = b.MUMMIES;
      }
    } else if (enteredTomb && !treasureTriggered) {
      if (b.strategy != b.WAIT) {
        clearTarget();
        b.WAIT.init(1320, 160, WaitStrategy.WaitType.WALK_LEFT, 200);
        b.strategy = b.WAIT;
      }
    } else if (!enteredTomb && b.playerX >= 992 && b.playerX < 1327 
        && areFireballsOrBoneTowersNotPresent()) {
      if (b.strategy != b.MEDUSA_HEADS_WALK) {
        clearTarget();
        b.MEDUSA_HEADS_WALK.init(false);
        b.strategy = b.MEDUSA_HEADS_WALK;
      }
    } else {
      super.pickStrategy();
    }
  }
  
  private boolean areFireballsOrBoneTowersNotPresent() {
    final GameObject[] objs = b.gameObjects;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      if (obj.type == FIREBALL || obj.type == BONE_TOWER) {
        return false;
      }
    }
    return true;
  }

  @Override
  Strategy selectStrategy(final GameObject target) {
    if (target == null && aboutToGetCrystalBall) {
      return b.GOT_CRYSTAL_BALL;
    } else {
      return super.selectStrategy(target);
    }
  }  

  @Override
  public void readGameObjects() {
    if (b.playerX >= 1280) {
      if (!blockBroken && api.readPPU(BLOCK_090000) == 0x00) {
        enteredTomb = treasureTriggered = blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("09-00-01");
      }
      if (!blockWhipped && bossDefeated) {
        b.addBlock(1328, 176);
      }
    } 
    if (b.playerX > 1408) {
      bossTriggered = enteredTomb = treasureTriggered = true;
    } else if (b.playerX >= 1320) {
      enteredTomb = true;
    }
    if (!bossTriggered) {
      b.addDestination(1527, 208);
    }
  }  

  @Override
  public void routeLeft() {
    if (b.playerY > 164 && (blockBroken || b.playerX < 1336)) {
      route(1289, 208);
    } else {
      route(9, 128);
    }
  }
  
  @Override
  public void routeRight() {
    route(1527, 208);
  }
  
  @Override
  public void treasureTriggered() {
    treasureTriggered = true;
  }  

  @Override
  public void candlesWhipped(final GameObject candle) {
    if (b.weapon != NONE && b.weapon != STOPWATCH 
        && roundTile(candle.x) == 82) { // dagger
      delayPlayer();
    }
  }
  
  @Override
  public void bossDefeated() {
    bossTriggered = enteredTomb = treasureTriggered = bossDefeated = true;
  }
  
  @Override
  public void blockWhipped() {
    blockWhipped = true;
  }  
  
  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }  
}