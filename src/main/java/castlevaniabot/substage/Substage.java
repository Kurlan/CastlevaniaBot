package castlevaniabot.substage;

import castlevaniabot.CastlevaniaBot;
import castlevaniabot.model.creativeelements.Bone;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.strategy.Strategy;
import nintaco.api.API;
import nintaco.api.ApiSource;

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
  
  final API api = ApiSource.getAPI();
  final CastlevaniaBot b;
  
  public MapRoutes mapRoutes;
  int playerDelay;
  
  Substage(final CastlevaniaBot b) {
    this.b = b;
  }
  
  public void init() {
    b.medusaHeadsCount0 = b.medusaHeadsCount1 = b.draculaHeadTime 
        = b.sickleCount0 = b.sickleCount1 = b.redBatsCount0 = b.redBatsCount1 
            = b.redBonesCount0 = b.redBonesCount1 = b.boneCount0 
                = b.boneCount1 = 0;
    b.strategy = null;
  }
  
  public MapRoutes getMapRoutes() {
    return mapRoutes;
  }  
  
  public void routeAndFace(final int targetX, final int targetY, final boolean left) {
    routeAndFace(targetX, targetY, left, true);
  }
  
  // Must provide at least a 1 pixel cushion to potentially turn around.
  // In other words, do not route to the edge of a block or the middle of a 
  // box against a wall.
  public void routeAndFace(final int targetX, final int targetY, final boolean left,
                           final boolean checkForEnemies) {
    
    if (b.playerX == targetX && b.playerY == targetY) {
      if (b.playerLeft != left) {
        if (left) {
          b.pressRight();  // walk past and turn around
        } else {
          b.pressLeft();   // walk past and turn around
        }
      }
    } else {
      route(targetX, targetY, true);
    }
  }  
  
  public void route(final int targetX, final int targetY) {
    route(targetX, targetY, true);
  }
  
  public void route(final int targetX, final int targetY,
                    final boolean checkForEnemies) {
    
    if (targetX < 0 || targetY < 0) {
      return;
    }
    final int tx = targetX >> 4;
    final int ty = targetY >> 4;
    if (ty > 15 || tx >= mapRoutes.width) {
      return;
    }
    
    if (b.currentTile.getX() == tx && b.currentTile.getY() == ty) {
      if (b.playerX < targetX) {
        b.pressRight();
      } else if (b.playerX > targetX) {
        b.pressLeft();
      }      
    } else {  
      final int route = mapRoutes.routes[b.currentTile.getY()][b.currentTile.getX()][ty][tx];
      b.executeOperation(mapRoutes.map, mapRoutes.width, getOperation(route), 
          getStepX(route), getStepY(route), checkForEnemies);
    }
  } 
  
  public void crystalBallAlmostAquired() {
  }
  
  public void candlesWhipped(final GameObject candle) {
  }

  public void blockWhipped() {
  }
  
  public void treasureTriggered() {
  }
  
  public void weaponUsed() {
  }
  
  public void whipUsed() {
  }
  
  public void bossDefeated() {
  }
  
  public void moveToward(final GameObject obj) {
    if (obj.onPlatform) {
      route(obj.supportX, obj.y, false);
    } else {
      route(obj.supportX, obj.platformY << 4, false);
    }
  } 
  
  void moveTowardTarget(final boolean checkForEnemies, GameObject target) {
    if (target.onPlatform) {
      route(target.supportX, target.y, checkForEnemies);
    } else {
      route(target.supportX, target.platformY << 4, checkForEnemies);
    }
  }  
  
  public void moveTowardTarget(GameObject target) {
    if (target.onPlatform) {
      route(target.supportX, target.y, false);
    } else {
      route(target.supportX, target.platformY << 4, false);
    }
  } 
  
  public void moveAwayFromTarget(final int targetX) {
    if (b.playerX < targetX) {
      routeLeft();
    } else {
      routeRight();
    }
  }  
  
  public void moveAwayFromTarget(GameObject target) {
    if (b.playerX < target.x) {
      routeLeft();
    } else {
      routeRight();
    }
  }
  
  void delayPlayer() {
    delayPlayer(30);
  }
  
  void delayPlayer(final int delay) {
    playerDelay = delay;
  }
  
  Strategy selectStrategy(final GameObject target) {
    if (target == null) {
      return null;
    }
    switch(target.type) {
      case AXE:                 return b.AXE;
      case AXE_KNIGHT:          return b.AXE_KNIGHT;
      case BLACK_BAT:           return b.BLACK_BAT;
      case BLOCK:               return b.BLOCK;
      case BONE_DRAGON_HEAD:    return b.BONE_DRAGON;
      case BONE_TOWER:          return b.BONE_TOWER;
      case CANDLES:             return b.CANDLES;
      case CRYSTAL_BALL:        return b.GET_CRYSTAL_BALL;
      case DEATH:               return b.BOOMERANG_DEATH;
      case EAGLE:               return b.EAGLE;
      case FIREBALL:            return b.FIREBALL;
      case FIRE_COLUMN:         return b.FIRE_COLUMN;
      case FISHMAN:             return b.FISHMAN;
      case FLEAMAN:             return b.FLEAMAN;
      case GHOST:               return b.GHOST;
      case GHOUL:               return b.GHOUL;
      case MEDUSA:              return b.MEDUSA;
      case PANTHER:             return b.PANTHER;
      case PHANTOM_BAT:         return b.PHANTOM_BAT;
      case RAVEN:               return b.RAVEN;
      case RED_BAT:             return b.RED_BAT;
      case RED_BONES:           return b.RED_BONES;
      case RED_SKELETON:        return b.RED_SKELETON;
      case RED_SKELETON_RISING: return b.RED_BONES;
      case SICKLE:              return b.SICKLE;
      case SNAKE:               return b.SNAKE;
      case SPEAR_KNIGHT:        return b.SPEAR_KNIGHT;
      case WHITE_SKELETON:      return b.WHITE_SKELETON;
      default:                  return b.GET_ITEM;
    }
  }

  boolean handleBones(TargetedObject targetedObject) {
    
    final Bone bone = b.getHarmfulBone();
    if (bone == null) {
      return false;
    }
    
    if (b.strategy != b.BONE) {
      clearTarget(targetedObject);
      b.BONE.init(bone);
      b.strategy = b.BONE;
    }
    
    return true;
  }  
  
  public void pickStrategy(TargetedObject targetedObject) {
    
    if (playerDelay > 0) {
      --playerDelay;
      if (targetedObject.getTargetType() != null) {
        clearTarget(targetedObject);
        setStrategy(null);
      }
      return;
    }
    
    if (handleBones(targetedObject)) {
      return;
    }
    
    GameObject currentTarget = null;    
    GameObject newTarget = null;
    for(int i = b.objsCount - 1; i >= 0; --i) {      
      final GameObject obj = b.gameObjects[i];
      
      obj.tier = -1;
      obj.subTier = 0;
      evaluteTierAndSubTier(obj);
      
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
        setStrategy(selectStrategy(null));
      }
    } else if (currentTarget == null
        || newTarget.rank - currentTarget.rank > MARGINAL_RANK) {
      setTarget(newTarget, targetedObject);
      setStrategy(selectStrategy(newTarget));    
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
  
  void setStrategy(final Strategy strategy) {
    if (strategy != null) {
      strategy.init();
    }
    b.strategy = strategy;
  }
  
  abstract void evaluteTierAndSubTier(GameObject obj);
  public abstract void routeLeft();
  public abstract void routeRight();
  public abstract void readGameObjects();
}