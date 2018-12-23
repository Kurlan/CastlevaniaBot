package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.operation.GameStateRestarter;
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
  private GameStateRestarter gameStateRestarter;
  
  public Substage1501(final API api, PlayerController playerController,  Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("15-01-00"));
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    whippedCandles = bossDefeated = aboutToGetCrystalBall = bossTriggered 
        = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    
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
                    final boolean checkForEnemies, BotState botState, GameState gameState) {

    if (bossDefeated) {
      // crystal ball X +/- 20
      if (botState.getPlayerX() == 108 && targetX >= 144 && !botState.isPlayerLeft()) {
        playerController.goRightAndJump(botState);
      } else if (botState.getPlayerX() == 148 && targetX <= 112 && botState.isPlayerLeft()) {
        playerController.goLeftAndJump(botState);
      } else {
        super.route(targetX, targetY, checkForEnemies, botState ,gameState);
      }
    } else {      
      super.route(targetX, targetY, checkForEnemies, botState ,gameState);
    }
  }  
  
  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    
    if (botState.getPlayerX() == 800 && botState.getWeapon() == BOOMERANG && botState.getHearts() > 0
        && !gameState.isWeaponing()) {
      playerController.useWeapon(gameState); // hit candles with boomerang
    } 
    
    if (botState.getCurrentStrategy() == allStrategies.getWHIP()) {
      if (whippedCandles) {
        super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
      }
    } else if (bossDefeated) {
      if (!whippedCandles && botState.getPlayerX() >= 224) {
        if (botState.getCurrentStrategy() != allStrategies.getWHIP()) {
          clearTarget(targetedObject);
          allStrategies.getWHIP().init(238, 128, true, 0, true, false, 36);
          botState.setCurrentStrategy(allStrategies.getWHIP());
        }
      } else {
        super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
      }
    } else if (botState.getCurrentStrategy() == allStrategies.getHOLY_WATER_DEATH() && allStrategies.getHOLY_WATER_DEATH().done) {
      bossDefeated = true;
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
    } else if (bossTriggered) {
      if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0) {
        allStrategies.getHOLY_WATER_DEATH().step();
      } else {
        super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
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
        super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
      }      
    } else if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0) {
      if (botState.getCurrentStrategy() != allStrategies.getDEATH_HALL_HOLY_WATER()) {
        clearTarget(targetedObject);
        allStrategies.getDEATH_HALL_HOLY_WATER().init();
        botState.setCurrentStrategy(allStrategies.getDEATH_HALL_HOLY_WATER());
      } else {
        super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
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
  public void readGameObjects(BotState botState, GameState gameState) {
    if (!bossDefeated) {
      if (bossTriggered && botState.getCurrentStrategy() != botState.getHolyWaterDeathStrategy()) {
        gameState.addDestination(80, 160, botState);
      } else {
        gameState.addDestination(9, 128, botState);
      }
    }
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    route(9, 128, botState, gameState);
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    route(1006, 192, botState, gameState);
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