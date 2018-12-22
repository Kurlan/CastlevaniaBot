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
import static castlevaniabot.model.gameelements.Addresses.BLOCK_120000;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_120001;
import static castlevaniabot.model.gameelements.GameObjectType.BONE_DRAGON_HEAD;
import static castlevaniabot.model.gameelements.GameObjectType.CANDLES;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;

public class Substage1200 extends Substage {
  
  private boolean blockWhipped1;
  private boolean blockBroken1;

  private boolean blockWhipped2;
  private boolean blockBroken2;

  private boolean bossDefeated;
  private boolean gotHighCandle;
  private boolean aboutToGetCrystalBall;
  private MapRoutes next;
  private MapRoutes next2;
  private Strategy frankenstein;

  public Substage1200(final BotState botState, final API api, PlayerController playerController, GameState gameState, Map<String, MapRoutes> allMapRoutes, Strategy frankenstein) {
    super(botState, api, playerController, gameState, allMapRoutes.get("12-00-00"));
    next = allMapRoutes.get("12-00-01");
    next2 = allMapRoutes.get("12-00-02");
    this.frankenstein = frankenstein;
  }

  @Override
  public void init() {
    super.init();    
    gotHighCandle = bossDefeated = aboutToGetCrystalBall = blockWhipped1 
        = blockBroken1 = blockWhipped2 = blockBroken2 = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    
    if (obj.type == FIREBALL) {
      if (obj.distanceX < 80 
          && (obj.y2 >= botState.getPlayerY() - 32 && obj.y1 <= botState.getPlayerY())
              && ((obj.left && obj.x2 >= botState.getPlayerX() - 16)
                  || (!obj.left && obj.x1 <= botState.getPlayerX() + 16))) {
        obj.tier = 7;
      }
    } else if (obj.type == BONE_DRAGON_HEAD) {
      obj.tier = 6;
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
        case BLOCK:
          if (botState.getPlayerX() > 224) {
            switch(roundTile(obj.x)) {
              case 14: obj.subTier = 2; break;
              case 18: obj.subTier = 1; break;
            }
          } else if (roundTile(obj.x) == 6 && botState.getWeapon() != NONE
              && botState.getWeapon() != STOPWATCH) {
            break;
          }
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
          if (botState.getPlayerX() < 768 && botState.getWeapon() != BOOMERANG
              && botState.getWeapon() != HOLY_WATER) {
            obj.tier = 4;
          } else {
            playerController.avoid(obj, botState);
          }
          break;
        case BOOMERANG_WEAPON:       
          if (botState.getPlayerX() < 768 && botState.getWeapon() != HOLY_WATER) {
            obj.tier = 4;
          } else {
            playerController.avoid(obj, botState);
          }
          break;           
        case DAGGER_WEAPON:        
          if (botState.getPlayerX() < 768 && (botState.getWeapon() == NONE || botState.getWeapon() == STOPWATCH)) {
            obj.tier = 4;
          } else {
            playerController.avoid(obj, botState);
          }
          break;            
        case STOPWATCH_WEAPON:
          if (botState.getPlayerX() < 768 && botState.getWeapon() == NONE) {
            obj.tier = 4;
          } else {
            playerController.avoid(obj, botState);
          }
          break;            
        case HOLY_WATER_WEAPON:
        case PORK_CHOP:
          obj.tier = 5;
          break;
        case CRYSTAL_BALL:
          bossDefeated = true;
          obj.tier = 0;
          break;          
      }
    }    
  }
  
  @Override
  public void route(final int targetX, final int targetY,
                    final boolean checkForEnemies) {

    if (bossDefeated) {
      // crystal ball X +/- 20
      if (botState.getPlayerX() == 876 && targetX >= 912 && !botState.isPlayerLeft()) {
        playerController.goRightAndJump(botState);
      } else if (botState.getPlayerX() == 916 && targetX <= 880 && botState.isPlayerLeft()) {
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
    if (botState.getCurrentStrategy() == allStrategies.getFRANKENSTEIN()) {
      if (allStrategies.getFRANKENSTEIN().done) {
        bossDefeated = true;
        super.pickStrategy(targetedObject, allStrategies);
      }
    } else if (!bossDefeated && botState.getPlayerX() > 896) {
      clearTarget(targetedObject);
      allStrategies.getFRANKENSTEIN().init();
      botState.setCurrentStrategy(allStrategies.getFRANKENSTEIN());
    } else if (bossDefeated && !gotHighCandle && gameState.countObjects(CANDLES) == 1) {
      if (botState.getCurrentStrategy() != allStrategies.getWHIP()) {
        clearTarget(targetedObject);
        allStrategies.getWHIP().init(992, 144, true, 0, true, true, 24);
        botState.setCurrentStrategy(allStrategies.getWHIP());
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
    if (botState.getPlayerX() > 176 && botState.getPlayerX() < 336) {
      if (!blockBroken1 && api.readPPU(BLOCK_120000) == 0x00) {
        blockWhipped1 = blockBroken1 = true;
        mapRoutes = next;
      }
      if (!blockWhipped1) {
        gameState.addBlock(304, 160, botState);
      }
    } else if (botState.getPlayerX() > 640 && botState.getPlayerX() < 768) {
      if (!blockBroken2 && api.readPPU(BLOCK_120001) == 0x00) {
        blockWhipped2 = blockBroken2 = true;
        mapRoutes = next2;
      }
      if (!blockWhipped2) {
        gameState.addBlock(736, 160, botState);
      }
    }
    
    if (botState.getCurrentStrategy() != frankenstein && !bossDefeated) {
      gameState.addDestination(944, 176, botState);
    }
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerX() >= 768) {
      route(777, 208);
    } else {
      route(9, 192);
    }
  }
  
  @Override
  public void routeRight() {
    route(1004, 208);
  }
  
  @Override
  public void blockWhipped() {
    if (botState.getPlayerX() > 448) {
      blockWhipped2 = true;
    } else {
      blockWhipped1 = true;
    }
  }

  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }  

  @Override
  public void whipUsed() {
    gotHighCandle = true;
  }
}