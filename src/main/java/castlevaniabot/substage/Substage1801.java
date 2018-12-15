package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameObject;
import castlevaniabot.Strategy;
import castlevaniabot.substage.Substage;

import static castlevaniabot.GameObjectType.*;
import static castlevaniabot.Weapon.*;

public class Substage1801 extends Substage {
  
  private boolean aboutToGetCrystalBall;
  private boolean walkDownStairs;
  private boolean bossTriggered;
  private boolean bossDefeated;
  private int holyWaterTimeOut;
  
  public Substage1801(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void init() {
    super.init();
    bossTriggered = bossDefeated = aboutToGetCrystalBall = walkDownStairs 
        = false;
    mapRoutes = b.allMapRoutes.get("18-01-00");
    holyWaterTimeOut = 180;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {

    if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 30: // boomerang
              if (b.weapon != HOLY_WATER) {
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
        case DRACULA_HEAD:
          if (holyWaterTimeOut == 0 || b.weapon == HOLY_WATER) {
            bossTriggered = true;
            if (b.strategy != b.DRACULA) {
              clearTarget();
              b.DRACULA.init();
              b.strategy = b.DRACULA;
            }
          } else if (holyWaterTimeOut > 0) {
            --holyWaterTimeOut;
          }
          break;
        case COOKIE_MONSTER_HEAD:
          if (holyWaterTimeOut == 0 || b.weapon == HOLY_WATER) {
            bossTriggered = true;
            if (b.strategy != b.COOKIE_MONSTER) {
              clearTarget();
              b.COOKIE_MONSTER.init();
              b.strategy = b.COOKIE_MONSTER;
            }
          } else if (holyWaterTimeOut > 0) {
            --holyWaterTimeOut;
          }
          break;
        case CRYSTAL_BALL:
          bossTriggered = bossDefeated = true;
          obj.tier = 0;
          break;  
      }
    }    
  }
  
  @Override
  public void pickStrategy() {
    if (bossDefeated) {
      super.pickStrategy();
    } else if (b.strategy == b.COOKIE_MONSTER) {
      if (b.COOKIE_MONSTER.done) {
        bossTriggered = bossDefeated = true;
        super.pickStrategy();
      } else {
        b.COOKIE_MONSTER.step();
      }
    } else if (b.strategy == b.DRACULA) {
      b.DRACULA.step();
    } else if (b.weapon == HOLY_WATER) {
      bossTriggered = true;
      clearTarget();
      b.DRACULA.init();
      b.strategy = b.DRACULA;
    } else if (walkDownStairs) {
      route(607, 223);
    } else if (b.playerX <= 144 && (b.hearts < 20 || b.whipLength != 2)) {
      walkDownStairs = true;
      clearTarget();
      setStrategy(null);
    } else {
      if (b.playerX < 128) {
        bossTriggered = true;
      }
      super.pickStrategy();
    }
  }
  
  @Override
  Strategy selectStrategy(final GameObject target) {
    if (target == null) {
      if (aboutToGetCrystalBall) {
        return b.GOT_CRYSTAL_BALL;
      } else {
        return super.selectStrategy(target);
      }
    } else {
      return super.selectStrategy(target);
    }    
  }  

  @Override
  public void readGameObjects() {
    if (!bossTriggered) {
      b.addDestination(9, 192);
    }
  }  

  @Override
  public void routeLeft() {
    route(9, 192);
  }
  
  @Override
  public void routeRight() {
    route(607, 223);
  }
  
  @Override void crystalBallAlmostAquired() {    
    aboutToGetCrystalBall = true;
  }  
}