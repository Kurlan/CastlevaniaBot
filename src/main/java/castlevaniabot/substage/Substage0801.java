package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.WaitStrategy;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.GameObjectType.BONE_TOWER;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.RAVEN;

public class Substage0801 extends Substage {
  
  private boolean treasureTriggered;  
  
  public Substage0801(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState) {
    super(b, botState, api, playerController, gameState);
  }

  @Override
  public void init() {
    super.init();
    treasureTriggered = false;
    mapRoutes = b.allMapRoutes.get("08-01-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == RAVEN) {
      obj.tier = 7;
    } else if (obj.type == BONE_TOWER) {
      if (obj.distanceX < 80 && obj.y >= botState.getPlayerY() - 16 && obj.y <= botState.getPlayerY()) {
        obj.tier = 8;
      }
    } else if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= botState.getPlayerY() - 32 && obj.y1 <= botState.getPlayerY())
              && ((obj.left && obj.x2 >= botState.getPlayerX() - 16)
                  || (!obj.left && obj.x1 <= botState.getPlayerX() + 16))) {
        obj.tier = 9;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          if (b.weapon != HOLY_WATER || botState.getPlayerX() < 664
              || roundTile(obj.x) != 42) {
            // Hit stopwatch candle even if current weapon is holy water to
            // reduce risk of hitting it while attacking raven.
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
    if (!treasureTriggered && botState.getPlayerX() >= 627 && botState.getPlayerX() < 659) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getWAIT()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getWAIT().init(643, 160, WaitStrategy.WaitType.KNEEL);
        botState.setCurrentStrategy(b.getAllStrategies().getWAIT());
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }

  @Override
  public void readGameObjects() {
    b.addDestination(1255, 128);
  }  

  @Override
  public void routeLeft() {
    route(512, 160);
  }
  
  @Override
  public void routeRight() {
    route(1255, 128);
  }
  
  @Override
  public void treasureTriggered() {
    treasureTriggered = true;
  }  

  @Override
  public void candlesWhipped(final GameObject candle) {
    if (b.weapon != NONE && roundTile(candle.x) == 42) { // stopwatch
      delayPlayer();
    }
  }
}