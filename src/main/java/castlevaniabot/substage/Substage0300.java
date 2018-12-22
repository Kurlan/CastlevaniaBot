package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.strategy.AllStrategies;
import castlevaniabot.strategy.Strategy;
import nintaco.api.API;

import java.util.Map;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_030000;
import static castlevaniabot.model.gameelements.GameObjectType.CRYSTAL_BALL;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.GHOUL;
import static castlevaniabot.model.gameelements.GameObjectType.PHANTOM_BAT;

public class Substage0300 extends Substage {
  
  private boolean blockWhipped;
  private boolean blockBroken;
  private boolean aboutToGetCrystalBall;
 
  public Substage0300(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState, Map<String, MapRoutes> allMapRoutes) {
    super(b, botState, api, playerController, gameState, allMapRoutes.get("03-00-00"));
  }

  @Override
  public void init() {
    super.init();
    aboutToGetCrystalBall = blockWhipped = blockBroken = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= botState.getPlayerY() - 32 && obj.y1 <= botState.getPlayerY())
              && ((obj.left && obj.x2 >= botState.getPlayerX() - 16)
                  || (!obj.left && obj.x1 <= botState.getPlayerX() + 16))) {
        obj.tier = 7;
      }
    } else if (obj.type == DESTINATION || obj.type == PHANTOM_BAT 
        || obj.type == CRYSTAL_BALL) {
      obj.tier = 0;
    } else if (obj.type == GHOUL) {
      if (obj.distanceX < 80 && obj.y <= botState.getPlayerY() + 8
          && obj.y >= botState.getPlayerY() - 56
              && (obj.left ^ (botState.getPlayerX() > obj.x))) {
        obj.tier = 6;
      }
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          if (roundTile(obj.x) != 22 || botState.getWeapon() != HOLY_WATER) {
            obj.tier = 1;
            if ((obj.y < 160) ^ (botState.getPlayerY() >= 160)) {
              obj.subTier = 1;
            }
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
        case STOPWATCH_WEAPON:
        case AXE_WEAPON:
          if (botState.getPlayerX() < 512) {
            if (botState.getWeapon() != HOLY_WATER) {
              obj.tier = 5;
            } else {
              playerController.avoid(obj, botState);
            }
          }
          break;          
        case HOLY_WATER_WEAPON: obj.tier = 5; break;
      }        
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
    if (botState.getPlayerX() >= 640) {
      if (!blockBroken && api.readPPU(BLOCK_030000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("03-00-01");
      }
      if (!blockWhipped) {
        b.addBlock(688, 176);
      }
    } 
    if (!blockWhipped) {
      gameState.addDestination(751, 208, botState);
    }
  }
  
  @Override
  public void routeLeft() {
    if (botState.getPlayerY() <= 160 && botState.getPlayerX() < 320) {
      route(9, 112);
    } else {
      route(9, 208);
    }
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerY() <= 160 && botState.getPlayerX() > 704) {
      route(751, 144);
    } else {
      route(751, 208);
    }
  }  
  
  @Override
  public void blockWhipped() {
    blockWhipped = true;
  }  
  
  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }
}