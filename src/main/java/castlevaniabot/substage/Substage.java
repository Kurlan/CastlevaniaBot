package castlevaniabot.substage;

import castlevaniabot.*;
import nintaco.api.*;
import static java.lang.Math.*;
import static castlevaniabot.MapRoutes.*;

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
  
  void routeAndFace(final int targetX, final int targetY, final boolean left) {
    routeAndFace(targetX, targetY, left, true);
  }
  
  // Must provide at least a 1 pixel cushion to potentially turn around.
  // In other words, do not route to the edge of a block or the middle of a 
  // box against a wall.
  void routeAndFace(final int targetX, final int targetY, final boolean left,
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
  
  void route(final int targetX, final int targetY, 
      final boolean checkForEnemies) {
    
    if (targetX < 0 || targetY < 0) {
      return;
    }
    final int tx = targetX >> 4;
    final int ty = targetY >> 4;
    if (ty > 15 || tx >= mapRoutes.width) {
      return;
    }
    
    if (b.tileX == tx && b.tileY == ty) {
      if (b.playerX < targetX) {
        b.pressRight();
      } else if (b.playerX > targetX) {
        b.pressLeft();
      }      
    } else {  
      final int route = mapRoutes.routes[b.tileY][b.tileX][ty][tx];
      b.executeOperation(mapRoutes.map, mapRoutes.width, getOperation(route), 
          getStepX(route), getStepY(route), checkForEnemies);
    }
  } 
  
  void crystalBallAlmostAquired() {    
  }
  
  void candlesWhipped(final GameObject candle) {
  }

  void blockWhipped() {    
  }
  
  public void treasureTriggered() {
  }
  
  void weaponUsed() {    
  }
  
  void whipUsed() {    
  }
  
  void bossDefeated() {    
  }
  
  public void moveToward(final GameObject obj) {
    if (obj.onPlatform) {
      route(obj.supportX, obj.y, false);
    } else {
      route(obj.supportX, obj.platformY << 4, false);
    }
  } 
  
  void moveTowardTarget(final boolean checkForEnemies) {
    if (b.target.onPlatform) {
      route(b.target.supportX, b.target.y, checkForEnemies);
    } else {
      route(b.target.supportX, b.target.platformY << 4, checkForEnemies);
    }
  }  
  
  public void moveTowardTarget() {
    if (b.target.onPlatform) {
      route(b.target.supportX, b.target.y, false);
    } else {
      route(b.target.supportX, b.target.platformY << 4, false);
    }
  } 
  
  void moveAwayFromTarget(final int targetX) {
    if (b.playerX < targetX) {
      routeLeft();
    } else {
      routeRight();
    }
  }  
  
  void moveAwayFromTarget() {
    if (b.playerX < b.target.x) {
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

  boolean handleBones() {
    
    final Bone bone = b.getHarmfulBone();
    if (bone == null) {
      return false;
    }
    
    if (b.strategy != b.BONE) {
      clearTarget();
      b.BONE.init(bone);
      b.strategy = b.BONE;
    }
    
    return true;
  }  
  
  public void pickStrategy() {
    
    if (playerDelay > 0) {
      --playerDelay;
      if (b.targetType != null) {
        clearTarget();
        setStrategy(null);
      }
      return;
    }
    
    if (handleBones()) {
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
            && obj.type == b.targetType 
            && abs(obj.x - b.targetX) <= 8 
            && abs(obj.y - b.targetY) <= 8) {
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
      if (b.targetType != null) {
        clearTarget();
        setStrategy(selectStrategy(null));
      }
    } else if (currentTarget == null
        || newTarget.rank - currentTarget.rank > MARGINAL_RANK) {
      setTarget(newTarget);
      setStrategy(selectStrategy(newTarget));    
    } else {
      setTarget(currentTarget);
    }   
  } 
  
  void clearTarget() {
    setTarget(null);
  }
  
  void setTarget(final GameObject obj) {
    if (obj == null) {
      b.target = null;
      b.targetType = null;
      b.targetX = b.targetY = -512;
    } else {
      b.target = obj;
      b.targetType = obj.type;
      b.targetX = obj.x;
      b.targetY = obj.y;
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