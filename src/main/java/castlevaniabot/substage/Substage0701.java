package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.WaitStrategy;
import nintaco.api.API;

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
  
  public Substage0701(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController) {
    super(b, botState, api, playerController);
  }

  @Override
  public void init() {
    super.init();
    treasureTriggered = false;
    mapRoutes = b.allMapRoutes.get("07-01-00");
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
          if (b.weapon != HOLY_WATER || roundTile(obj.x) != 38) {
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
          obj.tier = 5;
          break;
      }
    }    
  }
  
  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    if (!treasureTriggered && botState.getPlayerX() >= 480 && botState.getPlayerX() < 544
        && !b.isTypeInBounds(CANDLES, 528, 176, 560, 208)
            && !b.isTypeInBounds(SMALL_HEART, 528, 176, 560, 208)) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getWAIT()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getWAIT().init(528, 208, WaitStrategy.WaitType.KNEEL);
        botState.setCurrentStrategy(b.getAllStrategies().getWAIT());
      }
    } else if (b.weapon == HOLY_WATER && b.hearts > 0
        && botState.getPlayerY() == 128 && botState.getPlayerX() >= 544 && botState.getPlayerX() < 576
            && b.isTypeRight(WHITE_SKELETON, 576)) {      
      if (botState.getCurrentStrategy() != b.getAllStrategies().getUSE_WEAPON()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getUSE_WEAPON().init(560, 128, false, false);
        botState.setCurrentStrategy(b.getAllStrategies().getUSE_WEAPON());
      }
    } else if (botState.getPlayerY() == 128 && botState.getPlayerX() >= 544 && botState.getPlayerX() < 576
        && b.boneCount0 > 0) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getWAIT()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getWAIT().init(560, 128, WaitStrategy.WaitType.STAND, 30);
        botState.setCurrentStrategy(b.getAllStrategies().getWAIT());
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }

  @Override
  public void readGameObjects() {
    b.addDestination(743, 160);
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