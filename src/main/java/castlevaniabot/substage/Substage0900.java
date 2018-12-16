package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.Strategy;
import castlevaniabot.strategy.WaitStrategy;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_090000;
import static castlevaniabot.model.gameelements.GameObjectType.BANDAGE;
import static castlevaniabot.model.gameelements.GameObjectType.BONE_TOWER;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.MUMMY;
import static castlevaniabot.model.gameelements.GameObjectType.RAVEN;
import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

public class Substage0900 extends Substage {
  
  private boolean blockWhipped;
  private boolean enteredTomb;
  private boolean bossTriggered;
  private boolean treasureTriggered;
  private boolean bossDefeated;
  private boolean aboutToGetCrystalBall;
  
  public boolean blockBroken;
  
  public Substage0900(final CastlevaniaBot b, final BotState botState, final API api) {
    super(b, botState, api);
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
      if (obj.distanceX < 80 && obj.y >= botState.getPlayerY() - 16 && obj.y <= botState.getPlayerY()) {
        obj.tier = 8;
      }
    } else if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= botState.getPlayerY() - 32 && obj.y1 <= botState.getPlayerY())
              && ((obj.left && obj.x2 >= botState.getPlayerX() - 16)
                  || (!obj.left && obj.x1 <= botState.getPlayerX() + 16))) {
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
              if (botState.getPlayerX() >= 1496) { // right wall inhibits routing
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
      if (botState.getPlayerX() == 1388 && targetX >= 1424 && !b.playerLeft) {
        b.goRightAndJump();
      } else if (botState.getPlayerX() == 1428 && targetX <= 1376 && b.playerLeft) {
        b.goLeftAndJump();
      } else {
        super.route(targetX, targetY, checkForEnemies);
      }
    } else {
      super.route(targetX, targetY, checkForEnemies);
    }
  }
  
  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    
    if (bossTriggered && !bossDefeated) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getMUMMIES()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getMUMMIES().init();
        botState.setCurrentStrategy(b.getAllStrategies().getMUMMIES());
      }
    } else if (enteredTomb && !treasureTriggered) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getWAIT()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getWAIT().init(1320, 160, WaitStrategy.WaitType.WALK_LEFT, 200);
        botState.setCurrentStrategy(b.getAllStrategies().getWAIT());
      }
    } else if (!enteredTomb && botState.getPlayerX() >= 992 && botState.getPlayerX()< 1327
        && areFireballsOrBoneTowersNotPresent()) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getMEDUSA_HEADS_WALK()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getMEDUSA_HEADS_WALK().init(false);
        botState.setCurrentStrategy(b.getAllStrategies().getMEDUSA_HEADS_WALK());
      }
    } else {
      super.pickStrategy(targetedObject);
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
      return b.getAllStrategies().getGOT_CRYSTAL_BALL();
    } else {
      return super.selectStrategy(target);
    }
  }  

  @Override
  public void readGameObjects() {
    if (botState.getPlayerX() >= 1280) {
      if (!blockBroken && api.readPPU(BLOCK_090000) == 0x00) {
        enteredTomb = treasureTriggered = blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("09-00-01");
      }
      if (!blockWhipped && bossDefeated) {
        b.addBlock(1328, 176);
      }
    } 
    if (botState.getPlayerX() > 1408) {
      bossTriggered = enteredTomb = treasureTriggered = true;
    } else if (botState.getPlayerX() >= 1320) {
      enteredTomb = true;
    }
    if (!bossTriggered) {
      b.addDestination(1527, 208);
    }
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerY() > 164 && (blockBroken || botState.getPlayerX() < 1336)) {
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