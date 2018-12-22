package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.AllStrategies;
import castlevaniabot.strategy.Strategy;
import nintaco.api.API;

import java.util.Map;

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
  private Strategy holyWaterDeath;
  
  public Substage1501(BotState botState, final API api, PlayerController playerController, GameState gameState, Map<String, MapRoutes> allMapRoutes, Strategy holyWaterDeath) {
    super( botState, api, playerController, gameState, allMapRoutes.get("15-01-00"));
    this.holyWaterDeath = holyWaterDeath;
  }

  @Override
  public void init() {
    super.init();
    whippedCandles = bossDefeated = aboutToGetCrystalBall = bossTriggered 
        = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    switch(obj.type) {
      case DEATH:
        obj.tier = 6;
        break;
      case SICKLE:
        if (obj.distanceX < 80) {          
          obj.tier = 7;
          if (playerController.isInStandingWhipRange(obj, botState)) {
            obj.subTier = 4;
          } else if (playerController.isInKneelingWhipRange(obj, botState)) {
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
              if (botState.getWeapon() == NONE || botState.getWeapon() == STOPWATCH) {
                obj.tier = 5;
              } else {
                playerController.avoid(obj, botState);
              }
              break;        
            case AXE_WEAPON:
              if (botState.getWeapon() != HOLY_WATER && botState.getWeapon() != BOOMERANG) {
                obj.tier = 5;
              } else {
                playerController.avoid(obj, botState);
              }
              break;          
            case STOPWATCH_WEAPON:  
              if (botState.getWeapon() == NONE) {
                obj.tier = 5;
              } else {
                playerController.avoid(obj, botState);
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
      if (botState.getPlayerX() == 108 && targetX >= 144 && !botState.isPlayerLeft()) {
        playerController.goRightAndJump(botState);
      } else if (botState.getPlayerX() == 148 && targetX <= 112 && botState.isPlayerLeft()) {
        playerController.goLeftAndJump(botState);
      } else {
        super.route(targetX, targetY, checkForEnemies);
      }
    } else {      
      super.route(targetX, targetY, checkForEnemies);
    }
  }  
  
  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies) {
    
    if (botState.getPlayerX() == 800 && botState.getWeapon() == BOOMERANG && botState.getHearts() > 0
        && !gameState.isWeaponing()) {
      playerController.useWeapon(gameState); // hit candles with boomerang
    } 
    
    if (botState.getCurrentStrategy() == allStrategies.getWHIP()) {
      if (whippedCandles) {
        super.pickStrategy(targetedObject, allStrategies);
      }
    } else if (bossDefeated) {
      if (!whippedCandles && botState.getPlayerX() >= 224) {
        if (botState.getCurrentStrategy() != allStrategies.getWHIP()) {
          clearTarget(targetedObject);
          allStrategies.getWHIP().init(238, 128, true, 0, true, false, 36);
          botState.setCurrentStrategy(allStrategies.getWHIP());
        }
      } else {
        super.pickStrategy(targetedObject, allStrategies);
      }
    } else if (botState.getCurrentStrategy() == allStrategies.getHOLY_WATER_DEATH() && allStrategies.getHOLY_WATER_DEATH().done) {
      bossDefeated = true;
      super.pickStrategy(targetedObject, allStrategies);
    } else if (bossTriggered) {
      if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0) {
        allStrategies.getHOLY_WATER_DEATH().step();
      } else {
        super.pickStrategy(targetedObject, allStrategies);
      }
    } else if (botState.getPlayerX() < 128) {
      bossTriggered = true;
      if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0) {
        clearTarget(targetedObject);
        allStrategies.getHOLY_WATER_DEATH().init();
        botState.setCurrentStrategy(allStrategies.getHOLY_WATER_DEATH());
      }
    } else if (botState.getCurrentStrategy() == allStrategies.getDEATH_HALL_HOLY_WATER()) {
      if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0) {
        botState.getCurrentStrategy().step();
      } else {
        super.pickStrategy(targetedObject, allStrategies);
      }      
    } else if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0) {
      if (botState.getCurrentStrategy() != allStrategies.getDEATH_HALL_HOLY_WATER()) {
        clearTarget(targetedObject);
        allStrategies.getDEATH_HALL_HOLY_WATER().init();
        botState.setCurrentStrategy(allStrategies.getDEATH_HALL_HOLY_WATER());
      } else {
        super.pickStrategy(targetedObject, allStrategies);
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies);
    }
  }
  
  @Override
  Strategy selectStrategy(final GameObject target, AllStrategies allStrategies) {
    if (target == null) {
      if (aboutToGetCrystalBall) {
        return allStrategies.getGOT_CRYSTAL_BALL();
      } else {
        return super.selectStrategy(target, allStrategies);
      }
    } else if (target.type == MEDUSA_HEAD) {
      return allStrategies.getMEDUSA_HEAD();
    } else {
      return super.selectStrategy(target, allStrategies);
    }    
  }  

  @Override
  public void readGameObjects() {
    if (!bossDefeated) {
      if (bossTriggered && botState.getCurrentStrategy() != holyWaterDeath) {
        gameState.addDestination(80, 160, botState);
      } else {
        gameState.addDestination(9, 128, botState);
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