package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.Strategy;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_120000;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_120001;
import static castlevaniabot.model.gameelements.GameObjectType.BONE_DRAGON_HEAD;
import static castlevaniabot.model.gameelements.GameObjectType.CANDLES;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;

public class Substage1200 extends Substage {
  
  private boolean blockWhipped1;
  private boolean blockBroken1;

  private boolean blockWhipped2;
  private boolean blockBroken2;

  private boolean bossDefeated;
  private boolean gotHighCandle;
  private boolean aboutToGetCrystalBall;  
  
  public Substage1200(final CastlevaniaBot b, final BotState botState, final API api) {
    super(b, botState, api);
  }

  @Override
  public void init() {
    super.init();    
    gotHighCandle = bossDefeated = aboutToGetCrystalBall = blockWhipped1 
        = blockBroken1 = blockWhipped2 = blockBroken2 = false;
    mapRoutes = b.allMapRoutes.get("12-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= b.playerY - 32 && obj.y1 <= b.playerY)
              && ((obj.left && obj.x2 >= b.playerX - 16) 
                  || (!obj.left && obj.x1 <= b.playerX + 16))) {
        obj.tier = 7;
      }
    } else if (obj.type == BONE_DRAGON_HEAD) {
      obj.tier = 6;
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
        case BLOCK:
          if (b.playerX > 224) {
            switch(roundTile(obj.x)) {
              case 14: obj.subTier = 2; break;
              case 18: obj.subTier = 1; break;
            }
          } else if (roundTile(obj.x) == 6 && b.weapon != NONE 
              && b.weapon != STOPWATCH) {
            break;
          }
          obj.tier = 1; break;        
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:
        case WHIP_UPGRADE:
        case INVISIBLE_POTION:
          obj.tier = 2; break;
        case CROSS:          
        case DOULE_SHOT:
        case TRIPLE_SHOT:
          obj.tier = 3; break;        
        case AXE_WEAPON:
          if (b.playerX < 768 && b.weapon != BOOMERANG 
              && b.weapon != HOLY_WATER) {
            obj.tier = 4;
          } else {
            b.avoid(obj);
          }
          break;
        case BOOMERANG_WEAPON:       
          if (b.playerX < 768 && b.weapon != HOLY_WATER) {
            obj.tier = 4;
          } else {
            b.avoid(obj);
          }
          break;           
        case DAGGER_WEAPON:        
          if (b.playerX < 768 && (b.weapon == NONE || b.weapon == STOPWATCH)) {
            obj.tier = 4;
          } else {
            b.avoid(obj);
          }
          break;            
        case STOPWATCH_WEAPON:
          if (b.playerX < 768 && b.weapon == NONE) {
            obj.tier = 4;
          } else {
            b.avoid(obj);
          }
          break;            
        case HOLY_WATER_WEAPON:
        case PORK_CHOP:
          obj.tier = 5;
          break;
        case CRYSTAL_BALL:
          bossDefeated = true;
          obj.tier = 0;
          break;          
      }
    }    
  }
  
  @Override
  public void route(final int targetX, final int targetY,
                    final boolean checkForEnemies) {

    if (bossDefeated) {
      // crystal ball X +/- 20
      if (b.playerX == 876 && targetX >= 912 && !b.playerLeft) {
        b.pressRightAndJump();
      } else if (b.playerX == 916 && targetX <= 880 && b.playerLeft) {
        b.pressLeftAndJump();
      } else {
        super.route(targetX, targetY, checkForEnemies);
      }
    } else {
      super.route(targetX, targetY, checkForEnemies);
    }
  }  
  
  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    if (botState.getCurrentStrategy() == b.getAllStrategies().getFRANKENSTEIN()) {
      if (b.getAllStrategies().getFRANKENSTEIN().done) {
        bossDefeated = true;
        super.pickStrategy(targetedObject);
      }
    } else if (!bossDefeated && b.playerX > 896) {
      clearTarget(targetedObject);
      b.getAllStrategies().getFRANKENSTEIN().init();
      botState.setCurrentStrategy(b.getAllStrategies().getFRANKENSTEIN());
    } else if (bossDefeated && !gotHighCandle && b.countObjects(CANDLES) == 1) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getWHIP()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getWHIP().init(992, 144, true, 0, true, true, 24);
        botState.setCurrentStrategy(b.getAllStrategies().getWHIP());
      }
    } else {
      super.pickStrategy(targetedObject);
    }
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
    if (b.playerX > 176 && b.playerX < 336) {
      if (!blockBroken1 && api.readPPU(BLOCK_120000) == 0x00) {
        blockWhipped1 = blockBroken1 = true;
        mapRoutes = b.allMapRoutes.get("12-00-01");
      }
      if (!blockWhipped1) {
        b.addBlock(304, 160);
      }
    } else if (b.playerX > 640 && b.playerX < 768) {
      if (!blockBroken2 && api.readPPU(BLOCK_120001) == 0x00) {
        blockWhipped2 = blockBroken2 = true;
        mapRoutes = b.allMapRoutes.get("12-00-02");
      }
      if (!blockWhipped2) {
        b.addBlock(736, 160);
      }
    }
    
    if (botState.getCurrentStrategy() != b.getAllStrategies().getFRANKENSTEIN() && !bossDefeated) {
      b.addDestination(944, 176);
    }
  }  

  @Override
  public void routeLeft() {
    if (b.playerX >= 768) {
      route(777, 208);
    } else {
      route(9, 192);
    }
  }
  
  @Override
  public void routeRight() {
    route(1004, 208);
  }
  
  @Override
  public void blockWhipped() {
    if (b.playerX > 448) {
      blockWhipped2 = true;
    } else {
      blockWhipped1 = true;
    }
  }

  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }  

  @Override
  public void whipUsed() {
    gotHighCandle = true;
  }
}