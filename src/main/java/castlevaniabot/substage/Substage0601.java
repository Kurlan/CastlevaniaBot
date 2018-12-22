package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.AllStrategies;
import castlevaniabot.strategy.MedusaStrategy;
import castlevaniabot.strategy.Strategy;
import nintaco.api.API;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.GameObjectType.CRYSTAL_BALL;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.MEDUSA;
import static castlevaniabot.model.gameelements.GameObjectType.SNAKE;

public class Substage0601 extends Substage {

  private final MedusaStrategy medusaStrategy;
  private int walkDelay;
  private boolean reachedBoss;
  private boolean aboutToGetCrystalBall;  
   
  public Substage0601(final BotState botState, final API api, final PlayerController playerController, GameState gameState, Map<String, MapRoutes> allMapRoutes, MedusaStrategy medusaStrategy) {
    super(botState, api, playerController, gameState, allMapRoutes.get("06-01-00"));
    this.medusaStrategy = medusaStrategy;
  }

  @Override
  public void init() {
    super.init();
    walkDelay = 0;
    aboutToGetCrystalBall = reachedBoss = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.x >= 256 && obj.x < 592) {
      return;
    }
      
    if (obj.type == SNAKE) {
      if (!medusaStrategy.isTimeFrozen()) {
        if (obj.distanceX < 64) {
          if (obj.left) {
            if (obj.x2 > botState.getPlayerX() - 16) {
              obj.tier = 7;
            }
          } else {
            if (obj.x1 < botState.getPlayerX() + 16) {
              obj.tier = 7;
            }
          }
        }
      }
    } else if (obj.type == MEDUSA) {
      obj.tier = 6;
    } else if (obj.type == DESTINATION || obj.type == CRYSTAL_BALL) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES: 
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
        case BOOMERANG_WEAPON:
        case DAGGER_WEAPON:        
        case STOPWATCH_WEAPON:
          if (botState.getPlayerX() >= 256) {
            if (botState.getWeapon() != HOLY_WATER) {
              obj.tier = 4;
            } else {
              playerController.avoid(obj, botState);
            }
          }
          break;          
        case HOLY_WATER_WEAPON:
          if (botState.getPlayerX() >= 256) {
            obj.tier = 5;
          }
          break;
        case PORK_CHOP:
          obj.tier = 5;
          break;
      }
    }    
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies) {

    if (!reachedBoss && botState.getPlayerX() <= 40) {
      reachedBoss = true;
      playerController.goRight(botState);
    }

    if (walkDelay > 0) {
      if (--walkDelay == 0) {
        playerController.goLeft(botState);
      }
    } else if (botState.getPlayerX() == 493) {
      botState.setCurrentStrategy(null);
      walkDelay = 150 + ThreadLocalRandom.current().nextInt(11);
    } else if (botState.getPlayerX() >= 256 && botState.getPlayerX() < 608) {
      if (botState.getCurrentStrategy() != allStrategies.getMEDUSA_HEADS_WALK()) {
        clearTarget(targetedObject);
        allStrategies.getMEDUSA_HEADS_WALK().init(true);
        botState.setCurrentStrategy(allStrategies.getMEDUSA_HEADS_WALK());
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies);
    }
  }
  
  @Override
  Strategy selectStrategy(final GameObject target, AllStrategies allStrategies) {
    if (target == null && aboutToGetCrystalBall) {
      return allStrategies.getGOT_CRYSTAL_BALL();
    } else {
      return super.selectStrategy(target, allStrategies);
    }
  }  
  
  @Override
  public void readGameObjects() {
    if (!reachedBoss) {
      gameState.addDestination(40, 176, botState);
    }
  }  

  @Override
  public void routeLeft() {
    route(25, 176);
  }
  
  @Override
  public void routeRight() {
    route(727, 176);
  }
  
  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }  
}