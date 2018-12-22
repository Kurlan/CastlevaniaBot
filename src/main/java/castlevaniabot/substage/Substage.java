package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.creativeelements.Bone;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.strategy.AllStrategies;
import castlevaniabot.strategy.Strategy;
import nintaco.api.API;

import static castlevaniabot.model.gameelements.MapRoutes.getOperation;
import static castlevaniabot.model.gameelements.MapRoutes.getStepX;
import static castlevaniabot.model.gameelements.MapRoutes.getStepY;
import static java.lang.Math.abs;

public abstract class Substage {
  
  static final int MARGINAL_RANK = (2 << 8) | 32; 
  static final int HORIZON = 32;
  
  // Rounds up or down to nearest multiple of 16
  public static int roundTile(final int value) {
    return (value >> 4) + ((value >> 3) & 1);
  }
  
  final API api;
  final PlayerController playerController;
  
  public MapRoutes mapRoutes;
  int playerDelay;
  
  Substage(final API api, PlayerController playerController, MapRoutes mapRoutes) {
    this.api = api;
    this.playerController = playerController;
    this.mapRoutes = mapRoutes;
  }
  
  public void init(BotState botState, GameState gameState) {

  }
  
  public MapRoutes getMapRoutes() {
    return mapRoutes;
  }  
  
  public void routeAndFace(final int targetX, final int targetY, final boolean left, BotState botState, GameState gameState) {
    routeAndFace(targetX, targetY, left, true, botState, gameState);
  }
  
  // Must provide at least a 1 pixel cushion to potentially turn around.
  // In other words, do not route to the edge of a block or the middle of a 
  // box against a wall.
  public void routeAndFace(final int targetX, final int targetY, final boolean left,
                           final boolean checkForEnemies, BotState botState, GameState gameState) {
    
    if (botState.getPlayerX() == targetX && botState.getPlayerY() == targetY) {
      if (botState.isPlayerLeft() != left) {
        if (left) {
          playerController.goRight(botState);  // walk past and turn around
        } else {
          playerController.goLeft(botState);   // walk past and turn around
        }
      }
    } else {
      route(targetX, targetY, true, botState, gameState);
    }
  }  
  
  public void route(final int targetX, final int targetY, BotState botState, GameState gameState) {
    route(targetX, targetY, true, botState, gameState);
  }
  
  public void route(final int targetX, final int targetY,
                    final boolean checkForEnemies, BotState botState, GameState gameState) {
    
    if (targetX < 0 || targetY < 0) {
      return;
    }
    final int tx = targetX >> 4;
    final int ty = targetY >> 4;
    if (ty > 15 || tx >= mapRoutes.width) {
      return;
    }
    
    if (botState.getCurrentTile().getX() == tx && botState.getCurrentTile().getY() == ty) {
      if (botState.getPlayerX() < targetX) {
        playerController.goRight(botState);
      } else if (botState.getPlayerX() > targetX) {
        playerController.goLeft(botState);
      }      
    } else {  
      final int route = mapRoutes.routes[botState.getCurrentTile().getY()][botState.getCurrentTile().getX()][ty][tx];
      playerController.executeOperation(mapRoutes.map, mapRoutes.width, getOperation(route),
          getStepX(route), getStepY(route), checkForEnemies, botState, gameState, botState.getCurrentTile());
    }
  } 
  
  public void crystalBallAlmostAquired() {
  }
  
  public void candlesWhipped(final GameObject candle, BotState botState) {
  }

  public void blockWhipped(BotState botState) {
  }
  
  public void treasureTriggered(BotState botState) {
  }
  
  public void weaponUsed() {
  }
  
  public void whipUsed() {
  }
  
  public void bossDefeated() {
  }
  
  public void moveToward(final GameObject obj, BotState botState, GameState gameState) {
    if (obj.onPlatform) {
      route(obj.supportX, obj.y, false, botState, gameState);
    } else {
      route(obj.supportX, obj.platformY << 4, false, botState, gameState);
    }
  } 
  
  void moveTowardTarget(final boolean checkForEnemies, GameObject target, BotState botState, GameState gameState) {
    if (target.onPlatform) {
      route(target.supportX, target.y, checkForEnemies, botState, gameState);
    } else {
      route(target.supportX, target.platformY << 4, checkForEnemies, botState, gameState);
    }
  }  
  
  public void moveTowardTarget(GameObject target, BotState botState, GameState gameState) {
    if (target.onPlatform) {
      route(target.supportX, target.y, false, botState, gameState);
    } else {
      route(target.supportX, target.platformY << 4, false, botState, gameState);
    }
  } 
  
  public void moveAwayFromTarget(final int targetX, BotState botState, GameState gameState) {
    if (botState.getPlayerX() < targetX) {
      routeLeft(botState, gameState);
    } else {
      routeRight(botState, gameState);
    }
  }  
  
  public void moveAwayFromTarget(GameObject target, BotState botState, GameState gameState) {
    if (botState.getPlayerX() < target.x) {
      routeLeft(botState, gameState);
    } else {
      routeRight(botState, gameState);
    }
  }
  
  void delayPlayer() {
    delayPlayer(30);
  }
  
  void delayPlayer(final int delay) {
    playerDelay = delay;
  }
  
  Strategy selectStrategy(final GameObject target, final AllStrategies allStrategies) {
    if (target == null) {
      return null;
    }
    switch(target.type) {
      case AXE:                 return allStrategies.getAXE();
      case AXE_KNIGHT:          return allStrategies.getAXE_KNIGHT();
      case BLACK_BAT:           return allStrategies.getBLACK_BAT();
      case BLOCK:               return allStrategies.getBLOCK();
      case BONE_DRAGON_HEAD:    return allStrategies.getBONE_DRAGON();
      case BONE_TOWER:          return allStrategies.getBONE_TOWER();
      case CANDLES:             return allStrategies.getCANDLES();
      case CRYSTAL_BALL:        return allStrategies.getGET_CRYSTAL_BALL();
      case DEATH:               return allStrategies.getBOOMERANG_DEATH();
      case EAGLE:               return allStrategies.getEAGLE();
      case FIREBALL:            return allStrategies.getFIREBALL();
      case FIRE_COLUMN:         return allStrategies.getFIRE_COLUMN();
      case FISHMAN:             return allStrategies.getFISHMAN();
      case FLEAMAN:             return allStrategies.getFLEAMAN();
      case GHOST:               return allStrategies.getGHOST();
      case GHOUL:               return allStrategies.getGHOUL();
      case MEDUSA:              return allStrategies.getMEDUSA();
      case PANTHER:             return allStrategies.getPANTHER();
      case PHANTOM_BAT:         return allStrategies.getPHANTOM_BAT();
      case RAVEN:               return allStrategies.getRAVEN();
      case RED_BAT:             return allStrategies.getRED_BAT();
      case RED_BONES:           return allStrategies.getRED_BONES();
      case RED_SKELETON:        return allStrategies.getRED_SKELETON();
      case RED_SKELETON_RISING: return allStrategies.getRED_BONES();
      case SICKLE:              return allStrategies.getSICKLE();
      case SNAKE:               return allStrategies.getSNAKE();
      case SPEAR_KNIGHT:        return allStrategies.getSPEAR_KNIGHT();
      case WHITE_SKELETON:      return allStrategies.getWHITE_SKELETON();
      default:                  return allStrategies.getGET_ITEM();
    }
  }

  boolean handleBones(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    
    final Bone bone = gameState.getHarmfulBone(botState);
    if (bone == null) {
      return false;
    }
    
    if (botState.getCurrentStrategy() != allStrategies.getBONE()) {
      clearTarget(targetedObject);
      allStrategies.getBONE().init(bone);
      botState.setCurrentStrategy(allStrategies.getBONE());
    }
    
    return true;
  }  
  
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    
    if (playerDelay > 0) {
      --playerDelay;
      if (targetedObject.getTargetType() != null) {
        clearTarget(targetedObject);
        setStrategy(null, botState);
      }
      return;
    }
    
    if (handleBones(targetedObject, allStrategies, botState, gameState)) {
      return;
    }
    
    GameObject currentTarget = null;    
    GameObject newTarget = null;
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      
      obj.tier = -1;
      obj.subTier = 0;
      evaluteTierAndSubTier(obj, botState, gameState);
      
      if (obj.tier >= 0) {
        obj.rank = (obj.tier << 24) | (obj.subTier << 20) | obj.distTier;
        if (currentTarget == null
            && obj.type == targetedObject.getTargetType()
            && abs(obj.x - targetedObject.getCoordinates().getX()) <= 8
            && abs(obj.y - targetedObject.getCoordinates().getY()) <= 8) {
          currentTarget = obj;
        }        
        if (newTarget == null || obj.rank > newTarget.rank) {
          newTarget = obj;
        }
      } else {
        obj.rank = 0;
      }
    }
           
    if (newTarget == null) { // currentTarget must also be null
      if (targetedObject.getTargetType() != null) {
        clearTarget(targetedObject);
        setStrategy(selectStrategy(null, allStrategies), botState);
      }
    } else if (currentTarget == null
        || newTarget.rank - currentTarget.rank > MARGINAL_RANK) {
      setTarget(newTarget, targetedObject);
      setStrategy(selectStrategy(newTarget, allStrategies), botState);
    } else {
      setTarget(currentTarget, targetedObject);
    }   
  } 
  
  void clearTarget(TargetedObject targetedObject) {
    setTarget(null, targetedObject);
  }
  
  void setTarget(final GameObject obj, TargetedObject targetedObject) {
    if (obj == null) {
      targetedObject.setTarget(null);
      targetedObject.setTargetType(null);
      targetedObject.getCoordinates().setX(-512);
      targetedObject.getCoordinates().setY(-512);
    } else {
      targetedObject.setTarget(obj);
      targetedObject.setTargetType(obj.type);
      targetedObject.getCoordinates().setX(obj.x);
      targetedObject.getCoordinates().setY(obj.y);
    }
  }
  
  void setStrategy(final Strategy strategy, BotState botState) {
    if (strategy != null) {
      strategy.init();
    }
    botState.setCurrentStrategy(strategy);
  }
  
  abstract void evaluteTierAndSubTier(GameObject obj, BotState botState, GameState gameState);
  public abstract void routeLeft(BotState botState, GameState gameState);
  public abstract void routeRight(BotState botState, GameState gameState);
  public abstract void readGameObjects(BotState botState, GameState gameState);
}