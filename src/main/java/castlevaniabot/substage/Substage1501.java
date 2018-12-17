package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.Strategy;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.GameObjectType.MEDUSA_HEAD;

public class Substage1501 extends Substage {
  
  private boolean bossTriggered;
  private boolean aboutToGetCrystalBall; 
  private boolean bossDefeated;
  private boolean whippedCandles;
  
  public Substage1501(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController) {
    super(b, botState, api, playerController);
  }

  @Override
  public void init() {
    super.init();
    whippedCandles = bossDefeated = aboutToGetCrystalBall = bossTriggered 
        = false;
    mapRoutes = b.allMapRoutes.get("15-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    switch(obj.type) {
      case DEATH:
        obj.tier = 6;
        break;
      case SICKLE:
        if (obj.distanceX < 80) {          
          obj.tier = 7;
          if (b.isInStandingWhipRange(obj)) {
            obj.subTier = 4;
          } else if (b.isInKneelingWhipRange(obj)) {
            obj.subTier = 3;
          } else if (obj.y2 <= botState.getPlayerY() && obj.y1 >= botState.getPlayerY() - 32) {
            obj.subTier = 2;
          } else if (obj.y1 <= botState.getPlayerY() && obj.y2 >= botState.getPlayerY() - 32) {
            obj.subTier = 1;
          }
        }
        break;
      
      // Interestingly, the best medusa heads strategy appears to be to ignore
      // them completely.  All the medusa heads logic has been commented out.
        
//      case MEDUSA_HEAD:
//        if (obj.distanceX < 24) {
//          obj.tier = 8;
//        }
//        break;
        
      case AXE:
        if (obj.distanceX < 128 && obj.y1 <= botState.getPlayerY()
            && obj.y2 >= botState.getPlayerY() - 32) {
          obj.tier = 7;
        }        
        break;
      case AXE_KNIGHT:
        if (!botState.isOnStairs() && obj.distanceX < 128 && obj.y1 <= botState.getPlayerY() + 16
            && obj.y2 >= botState.getPlayerY() - 64) {
          obj.tier = 6;
        }        
        break;
      case DESTINATION:
        obj.tier = 0;
        break;
      default:
        if (obj.distance < HORIZON) {
          switch(obj.type) {
            case CANDLES:
              if (bossDefeated) {
                switch(roundTile(obj.x)) {
                  case 6:
                    if (botState.getPlayerX() < obj.x - 24) {
                      obj.tier = 1;
                    }
                    break;
                  case 14:
                    obj.platformX = 14;
                    obj.tier = 1;
                    break;
                  default:
                    obj.tier = 1;
                    break;
                }
              } else if (!bossTriggered 
                  && (botState.getPlayerX() < 256 || (obj.x < 480 && obj.x < botState.getPlayerX()))) {
                obj.tier = 1; 
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
              obj.tier = 5; break;
            case DAGGER_WEAPON:
              if (b.weapon == NONE || b.weapon == STOPWATCH) {
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
            case CRYSTAL_BALL:
              bossDefeated = true;
              obj.tier = 0;
              break;         
          }
        }        
        break;
    }    
  }
  
  @Override
  public void route(final int targetX, final int targetY,
                    final boolean checkForEnemies) {

    if (bossDefeated) {
      // crystal ball X +/- 20
      if (botState.getPlayerX() == 108 && targetX >= 144 && !b.playerLeft) {
        b.goRightAndJump();
      } else if (botState.getPlayerX() == 148 && targetX <= 112 && b.playerLeft) {
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
    
    if (botState.getPlayerX() == 800 && b.weapon == BOOMERANG && b.hearts > 0
        && !b.getGameState().isWeaponing()) {
      b.useWeapon(); // hit candles with boomerang
    } 
    
    if (botState.getCurrentStrategy() == b.getAllStrategies().getWHIP()) {
      if (whippedCandles) {
        super.pickStrategy(targetedObject);
      }
    } else if (bossDefeated) {
      if (!whippedCandles && botState.getPlayerX() >= 224) {
        if (botState.getCurrentStrategy() != b.getAllStrategies().getWHIP()) {
          clearTarget(targetedObject);
          b.getAllStrategies().getWHIP().init(238, 128, true, 0, true, false, 36);
          botState.setCurrentStrategy(b.getAllStrategies().getWHIP());
        }
      } else {
        super.pickStrategy(targetedObject);
      }
    } else if (botState.getCurrentStrategy() == b.getAllStrategies().getHOLY_WATER_DEATH() && b.getAllStrategies().getHOLY_WATER_DEATH().done) {
      bossDefeated = true;
      super.pickStrategy(targetedObject);
    } else if (bossTriggered) {
      if (b.weapon == HOLY_WATER && b.hearts > 0) {
        b.getAllStrategies().getHOLY_WATER_DEATH().step();
      } else {
        super.pickStrategy(targetedObject);
      }
    } else if (botState.getPlayerX() < 128) {
      bossTriggered = true;
      if (b.weapon == HOLY_WATER && b.hearts > 0) {
        clearTarget(targetedObject);
        b.getAllStrategies().getHOLY_WATER_DEATH().init();
        botState.setCurrentStrategy(b.getAllStrategies().getHOLY_WATER_DEATH());
      }
    } else if (botState.getCurrentStrategy() == b.getAllStrategies().getDEATH_HALL_HOLY_WATER()) {
      if (b.weapon == HOLY_WATER && b.hearts > 0) {
        botState.getCurrentStrategy().step();
      } else {
        super.pickStrategy(targetedObject);
      }      
    } else if (b.weapon == HOLY_WATER && b.hearts > 0) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getDEATH_HALL_HOLY_WATER()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getDEATH_HALL_HOLY_WATER().init();
        botState.setCurrentStrategy(b.getAllStrategies().getDEATH_HALL_HOLY_WATER());
      } else {
        super.pickStrategy(targetedObject);
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }
  
  @Override
  Strategy selectStrategy(final GameObject target) {
    if (target == null) {
      if (aboutToGetCrystalBall) {
        return b.getAllStrategies().getGOT_CRYSTAL_BALL();
      } else {
        return super.selectStrategy(target);
      }
    } else if (target.type == MEDUSA_HEAD) {
      return b.getAllStrategies().getMEDUSA_HEAD();
    } else {
      return super.selectStrategy(target);
    }    
  }  

  @Override
  public void readGameObjects() {
    if (!bossDefeated) {
      if (bossTriggered && botState.getCurrentStrategy() != b.getAllStrategies().getHOLY_WATER_DEATH()) {
        b.addDestination(80, 160);
      } else {
        b.addDestination(9, 128);
      }
    }
  }  

  @Override
  public void routeLeft() {
    route(9, 128);
  }
  
  @Override
  public void routeRight() {
    route(1006, 192);
  }
  
  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }  

  @Override
  public void whipUsed() {
    whippedCandles = true;
  }
}