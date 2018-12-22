package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.AllStrategies;
import castlevaniabot.strategy.WaitStrategy;
import nintaco.api.API;

import java.util.Map;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.GameObjectType.CANDLES;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.GHOST;
import static castlevaniabot.model.gameelements.GameObjectType.RAVEN;
import static castlevaniabot.model.gameelements.GameObjectType.SMALL_HEART;
import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

public class Substage0701 extends Substage {
  
  private boolean treasureTriggered;
  
  public Substage0701(final BotState botState, final API api, PlayerController playerController, GameState gameState, Map<String, MapRoutes> allMapRoutes) {
    super(botState, api, playerController, gameState, allMapRoutes.get("07-01-00"));
  }

  @Override
  public void init() {
    super.init();
    treasureTriggered = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == GHOST) {
      obj.tier = 8;
    } else if (obj.type == WHITE_SKELETON) {
      obj.tier = 6;
    } else if (obj.type == RAVEN) {
      obj.tier = 7;
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES: 
          if (botState.getWeapon() != HOLY_WATER || roundTile(obj.x) != 38) {
            // Keep holy water by avoiding axe candle
            obj.tier = 1;             
          }
          break;
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
          obj.tier = 5;
          break;
      }
    }    
  }
  
  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies) {
    if (!treasureTriggered && botState.getPlayerX() >= 480 && botState.getPlayerX() < 544
        && !gameState.isTypeInBounds(CANDLES, 528, 176, 560, 208)
            && !gameState.isTypeInBounds(SMALL_HEART, 528, 176, 560, 208)) {
      if (botState.getCurrentStrategy() != allStrategies.getWAIT()) {
        clearTarget(targetedObject);
        allStrategies.getWAIT().init(528, 208, WaitStrategy.WaitType.KNEEL);
        botState.setCurrentStrategy(allStrategies.getWAIT());
      }
    } else if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0
        && botState.getPlayerY() == 128 && botState.getPlayerX() >= 544 && botState.getPlayerX() < 576
            && gameState.isTypeRight(WHITE_SKELETON, 576)) {
      if (botState.getCurrentStrategy() != allStrategies.getUSE_WEAPON()) {
        clearTarget(targetedObject);
        allStrategies.getUSE_WEAPON().init(560, 128, false, false);
        botState.setCurrentStrategy(allStrategies.getUSE_WEAPON());
      }
    } else if (botState.getPlayerY() == 128 && botState.getPlayerX() >= 544 && botState.getPlayerX() < 576
        && gameState.getBoneCount0() > 0) {
      if (botState.getCurrentStrategy() != allStrategies.getWAIT()) {
        clearTarget(targetedObject);
        allStrategies.getWAIT().init(560, 128, WaitStrategy.WaitType.STAND, 30);
        botState.setCurrentStrategy(allStrategies.getWAIT());
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies);
    }
  }

  @Override
  public void readGameObjects() {
    gameState.addDestination(743, 160, botState);
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerX() < 160) {
      route(41, 192);
    } else {
      route(169, 192);
    }
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerX() < 160) {
      route(159, 128);
    } else if (botState.getPlayerX() < 544) {
      route(543, 208);
    } else {
      route(743, 160);
    }
  }
  
  @Override
  public void treasureTriggered() {
    treasureTriggered = true;
  }  
}