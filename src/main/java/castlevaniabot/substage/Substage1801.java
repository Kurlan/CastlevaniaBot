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
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;

public class Substage1801 extends Substage {
  
  private boolean aboutToGetCrystalBall;
  private boolean walkDownStairs;
  private boolean bossTriggered;
  private boolean bossDefeated;
  private int holyWaterTimeOut;
  private GameStateRestarter gameStateRestarter;
  
  public Substage1801(final API api, PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("18-01-00"));
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    bossTriggered = bossDefeated = aboutToGetCrystalBall = walkDownStairs 
        = false;
    holyWaterTimeOut = 180;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {

    if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 30: // boomerang
              if (botState.getWeapon() != HOLY_WATER) {
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
          if (botState.getWeapon() == NONE || botState.getWeapon() == STOPWATCH) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
          }
          break;        
        case BOOMERANG_WEAPON:
          if (botState.getWeapon() != HOLY_WATER) {
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
        case DRACULA_HEAD:
          if (holyWaterTimeOut == 0 || botState.getWeapon() == HOLY_WATER) {
            bossTriggered = true;
            if (botState.getCurrentStrategy() != botState.getDraculaStrategy()) {
              clearTarget(botState.getTargetedObject());
              botState.getDraculaStrategy().init();
              botState.setCurrentStrategy(botState.getDraculaStrategy());
            }
          } else if (holyWaterTimeOut > 0) {
            --holyWaterTimeOut;
          }
          break;
        case COOKIE_MONSTER_HEAD:
          if (holyWaterTimeOut == 0 || botState.getWeapon() == HOLY_WATER) {
            bossTriggered = true;
            if (botState.getCurrentStrategy() != botState.getCookieMonsterStrategy()) {
              clearTarget(botState.getTargetedObject());
              botState.getCookieMonsterStrategy().init();
              botState.setCurrentStrategy(botState.getCookieMonsterStrategy());
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
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    if (bossDefeated) {
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
    } else if (botState.getCurrentStrategy() == botState.getCookieMonsterStrategy()) {
      if (botState.getCookieMonsterStrategy().isDone()) {
        bossTriggered = bossDefeated = true;
        super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
      } else {
        botState.getCookieMonsterStrategy().step();
      }
    } else if (botState.getCurrentStrategy() == botState.getDraculaStrategy()) {
      botState.getDraculaStrategy().step();
    } else if (botState.getWeapon() == HOLY_WATER) {
      bossTriggered = true;
      clearTarget(targetedObject);
      botState.getDraculaStrategy().init();
      botState.setCurrentStrategy(botState.getDraculaStrategy());
    } else if (walkDownStairs) {
      route(607, 223, botState ,gameState);
    } else if (botState.getPlayerX() <= 144 && (botState.getHearts() < 20 || botState.getWhipLength() != 2)) {
      walkDownStairs = true;
      clearTarget(targetedObject);
      setStrategy(null, botState);
    } else {
      if (botState.getPlayerX() < 128) {
        bossTriggered = true;
      }
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
    } else {
      return super.selectStrategy(target, allStrategies);
    }    
  }  

  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    if (!bossTriggered) {
      gameState.addDestination(9, 192, botState);
    }
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    route(9, 192, botState, gameState);
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    route(607, 223, botState, gameState);
  }
  
  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }  
}