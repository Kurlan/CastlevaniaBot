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
import castlevaniabot.strategy.WaitStrategy;
import nintaco.api.API;

import java.util.Map;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_090000;
import static castlevaniabot.model.gameelements.GameObjectType.BANDAGE;
import static castlevaniabot.model.gameelements.GameObjectType.BONE_TOWER;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.MUMMY;
import static castlevaniabot.model.gameelements.GameObjectType.RAVEN;
import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

public class Substage0900 extends Substage {
  
  private boolean blockWhipped;
  private boolean enteredTomb;
  private boolean bossTriggered;
  private boolean treasureTriggered;
  private boolean bossDefeated;
  private boolean aboutToGetCrystalBall;
  
  public boolean blockBroken;
  private MapRoutes next;
  private GameStateRestarter gameStateRestarter;

  public Substage0900(final API api, PlayerController playerController,  Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("09-00-00"));
    next = allMapRoutes.get("09-00-01");
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    blockWhipped = blockBroken = aboutToGetCrystalBall = bossDefeated 
        = bossTriggered = enteredTomb = treasureTriggered = false;
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
        
    if (obj.type == MUMMY) {
      if (obj.x > 1312 && obj.x < 1502) {
        enteredTomb = treasureTriggered = bossTriggered = true;
      }
    } else if (obj.type == BANDAGE) {
      enteredTomb = treasureTriggered = bossTriggered = true;
    } else if (obj.type == WHITE_SKELETON) {
      obj.tier = 6;
    } else if (obj.type == RAVEN) {
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
      outer: switch(obj.type) {
        case CANDLES: 
          switch(roundTile(obj.x)) {
            case 78: obj.subTier = 2; break;
            case 82:
              if (botState.getWeapon() == BOOMERANG) {
                break outer;
              }
              obj.subTier = 1; 
              break;
            case 94:
              if (botState.getPlayerX() >= 1496) { // right wall inhibits routing
                break outer;
              }
              break;
          }
          obj.tier = 1; break;
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
          if (!bossTriggered && (botState.getWeapon() == NONE || botState.getWeapon() == STOPWATCH)) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
          }
          break;        
        case BOOMERANG_WEAPON:
          if (!bossTriggered && botState.getWeapon() != HOLY_WATER) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
          }
          break;
        case AXE_WEAPON:
          if (!bossTriggered && botState.getWeapon() != HOLY_WATER
              && botState.getWeapon() != BOOMERANG) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
          }
          break;          
        case STOPWATCH_WEAPON:  
          if (!bossTriggered && botState.getWeapon() == NONE) {
            obj.tier = 5;
          } else {
            playerController.avoid(obj, botState);
          }
          break;          
        case HOLY_WATER_WEAPON:
        case PORK_CHOP:
          obj.tier = 5;
          break;
        case CRYSTAL_BALL:
          bossTriggered = enteredTomb = treasureTriggered = bossDefeated = true;
          obj.tier = 0;
          break;
      }
    }    
  }

  @Override
  public void route(final int targetX, final int targetY,
                    final boolean checkForEnemies, BotState botState, GameState gameState) {
    
    if (bossDefeated) {
      if (botState.getPlayerX() == 1388 && targetX >= 1424 && !botState.isPlayerLeft()) {
        playerController.goRightAndJump(botState);
      } else if (botState.getPlayerX() == 1428 && targetX <= 1376 && botState.isPlayerLeft()) {
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
    
    if (bossTriggered && !bossDefeated) {
      if (botState.getCurrentStrategy() != allStrategies.getMUMMIES()) {
        clearTarget(targetedObject);
        allStrategies.getMUMMIES().init();
        botState.setCurrentStrategy(allStrategies.getMUMMIES());
      }
    } else if (enteredTomb && !treasureTriggered) {
      if (botState.getCurrentStrategy() != allStrategies.getWAIT()) {
        clearTarget(targetedObject);
        allStrategies.getWAIT().init(1320, 160, WaitStrategy.WaitType.WALK_LEFT, 200);
        botState.setCurrentStrategy(allStrategies.getWAIT());
      }
    } else if (!enteredTomb && botState.getPlayerX() >= 992 && botState.getPlayerX()< 1327
        && areFireballsOrBoneTowersNotPresent(gameState)) {
      if (botState.getCurrentStrategy() != allStrategies.getMEDUSA_HEADS_WALK()) {
        clearTarget(targetedObject);
        allStrategies.getMEDUSA_HEADS_WALK().init(false);
        botState.setCurrentStrategy(allStrategies.getMEDUSA_HEADS_WALK());
      }
    } else {
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
    }
  }
  
  private boolean areFireballsOrBoneTowersNotPresent(GameState gameState) {
    final GameObject[] objs = gameState.getGameObjects();
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      if (obj.type == FIREBALL || obj.type == BONE_TOWER) {
        return false;
      }
    }
    return true;
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
  public void readGameObjects(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 1280) {
      if (!blockBroken && api.readPPU(BLOCK_090000) == 0x00) {
        enteredTomb = treasureTriggered = blockWhipped = blockBroken = true;
        mapRoutes = next;
      }
      if (!blockWhipped && bossDefeated) {
        gameState.addBlock(1328, 176, botState);
      }
    } 
    if (botState.getPlayerX() > 1408) {
      bossTriggered = enteredTomb = treasureTriggered = true;
    } else if (botState.getPlayerX() >= 1320) {
      enteredTomb = true;
    }
    if (!bossTriggered) {
      gameState.addDestination(1527, 208, botState);
    }
  }  

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    if (botState.getPlayerY() > 164 && (blockBroken || botState.getPlayerX() < 1336)) {
      route(1289, 208, botState, gameState);
    } else {
      route(9, 128, botState, gameState);
    }
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    route(1527, 208, botState, gameState);
  }
  
  @Override
  public void treasureTriggered(BotState botState) {
    treasureTriggered = true;
  }  

  @Override
  public void candlesWhipped(final GameObject candle, BotState botState) {
    if (botState.getWeapon() != NONE && botState.getWeapon() != STOPWATCH
        && roundTile(candle.x) == 82) { // dagger
      delayPlayer();
    }
  }
  
  @Override
  public void bossDefeated() {
    bossTriggered = enteredTomb = treasureTriggered = bossDefeated = true;
  }
  
  @Override
  public void blockWhipped(BotState botState) {
    blockWhipped = true;
  }  
  
  @Override
  public void crystalBallAlmostAquired() {
    aboutToGetCrystalBall = true;
  }  
}