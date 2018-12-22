package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
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
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;

public class Substage1801 extends Substage {
  
  private boolean aboutToGetCrystalBall;
  private boolean walkDownStairs;
  private boolean bossTriggered;
  private boolean bossDefeated;
  private int holyWaterTimeOut;
  
  public Substage1801(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState, Map<String, MapRoutes> allMapRoutes) {
    super(b, botState, api, playerController, gameState, allMapRoutes.get("18-01-00"));
  }

  @Override
  public void init() {
    super.init();
    bossTriggered = bossDefeated = aboutToGetCrystalBall = walkDownStairs 
        = false;
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
            if (botState.getCurrentStrategy() != b.getAllStrategies().getDRACULA()) {
              clearTarget(botState.getTargetedObject());
              b.getAllStrategies().getDRACULA().init();
              botState.setCurrentStrategy(b.getAllStrategies().getDRACULA());
            }
          } else if (holyWaterTimeOut > 0) {
            --holyWaterTimeOut;
          }
          break;
        case COOKIE_MONSTER_HEAD:
          if (holyWaterTimeOut == 0 || botState.getWeapon() == HOLY_WATER) {
            bossTriggered = true;
            if (botState.getCurrentStrategy() != b.getAllStrategies().getCOOKIE_MONSTER()) {
              clearTarget(botState.getTargetedObject());
              b.getAllStrategies().getCOOKIE_MONSTER().init();
              botState.setCurrentStrategy(b.getAllStrategies().getCOOKIE_MONSTER());
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
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies) {
    if (bossDefeated) {
      super.pickStrategy(targetedObject, allStrategies);
    } else if (botState.getCurrentStrategy() == b.getAllStrategies().getCOOKIE_MONSTER()) {
      if (b.getAllStrategies().getCOOKIE_MONSTER().done) {
        bossTriggered = bossDefeated = true;
        super.pickStrategy(targetedObject, allStrategies);
      } else {
        b.getAllStrategies().getCOOKIE_MONSTER().step();
      }
    } else if (botState.getCurrentStrategy() == b.getAllStrategies().getDRACULA()) {
      b.getAllStrategies().getDRACULA().step();
    } else if (botState.getWeapon() == HOLY_WATER) {
      bossTriggered = true;
      clearTarget(targetedObject);
      b.getAllStrategies().getDRACULA().init();
      botState.setCurrentStrategy(b.getAllStrategies().getDRACULA());
    } else if (walkDownStairs) {
      route(607, 223);
    } else if (botState.getPlayerX() <= 144 && (botState.getHearts() < 20 || botState.getWhipLength() != 2)) {
      walkDownStairs = true;
      clearTarget(targetedObject);
      setStrategy(null);
    } else {
      if (botState.getPlayerX() < 128) {
        bossTriggered = true;
      }
      super.pickStrategy(targetedObject, allStrategies);
    }
  }
  
  @Override
  Strategy selectStrategy(final GameObject target, AllStrategies allStrategies) {
    if (target == null) {
      if (aboutToGetCrystalBall) {
        return b.getAllStrategies().getGOT_CRYSTAL_BALL();
      } else {
        return super.selectStrategy(target, allStrategies);
      }
    } else {
      return super.selectStrategy(target, allStrategies);
    }    
  }  

  @Override
  public void readGameObjects() {
    if (!bossTriggered) {
      gameState.addDestination(9, 192, botState);
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
  
  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }  
}