package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.GameObjectType;
import castlevaniabot.model.gameelements.TargetedObject;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.FLEAMAN;

public class Substage1400 extends Substage {
  
  private static enum State {
    KILL_FLEAMAN,
    WHIP_LOWER_AXE,
    DESPAWN_LOWER_KNIGHT,
    WHIP_CANDLES,
    WALK_TO_STAIRS,
    WAIT_FOR_NO_UPPER_AXE,
    WAIT_FOR_UPPER_AXE,
    RUN_FOR_IT,
    GO_UP_STAIRS,
  }
  
  private GameObject fleaman;
  private GameObject lowerAxe;
  private GameObject upperAxe;
  private GameObject lowerKnight;
  private GameObject upperKnight;  
  private GameObject candles;
 
  private int kneelDelay;
  private int lastUpperAxeX;
  private boolean lastFleamanExists;
  private boolean lastLowerAxeExists;
  private boolean lastUpperAxeExists;
  private boolean upperAxeLeft;
  private State state;
  
  public Substage1400(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState) {
    super(b, botState, api, playerController, gameState);
  }

  @Override
  public void init() {
    super.init();
    state = State.KILL_FLEAMAN;
    candles = fleaman = lowerAxe = upperAxe = lowerKnight = upperKnight = null;
    lastFleamanExists = lastLowerAxeExists = lastUpperAxeExists = upperAxeLeft 
        = false;
    lastUpperAxeX = 0;    
    mapRoutes = b.allMapRoutes.get("14-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) { 
    
    if (obj.type == GameObjectType.AXE) {
      if (obj.distanceX < 64 && obj.y1 <= botState.getPlayerY()
          && obj.y2 >= botState.getPlayerY() - 32) {
        obj.tier = 7;
      }
    } else if (obj.type == FLEAMAN) {
      if (obj.y1 <= botState.getPlayerY() && obj.y2 >= botState.getPlayerY() - 48) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      
      if (obj.y > 128) {
        if (botState.getPlayerY() < 152) {
          return;
        }        
      } else if (botState.getPlayerY() > 128) {
        return;
      }
      
      switch(obj.type) {
        case CANDLES:
          if (roundTile(obj.x) != 66 && ((obj.y > 128 && (lowerKnight == null 
              || lowerKnight.distanceX >= 80)) || (obj != candles 
                  || upperAxe == null))) {
            obj.tier = 1; 
          }  
          break;
        case BLOCK: 
          obj.tier = 2; break;
        case MONEY_BAG:
        case SMALL_HEART:
        case LARGE_HEART:        
        case INVISIBLE_POTION:
        case WHIP_UPGRADE:
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
          obj.tier = 5; break;
      }
    }    
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    
    switch(state) {
      case KILL_FLEAMAN:
        if (fleaman == null && lowerKnight == null && upperKnight != null) {
          setState(State.WHIP_CANDLES, targetedObject);
        } else if (botState.getPlayerY() <= 128) {
          if (fleaman == null || fleaman.y <= botState.getPlayerY() + 8) {
            setState(State.WHIP_CANDLES, targetedObject);
          } else {
            super.pickStrategy(targetedObject);
          }
        } else if (fleaman == null 
            && (lastFleamanExists || lowerKnight != null)) {
          setState(State.WHIP_LOWER_AXE, targetedObject);
        } else {
          super.pickStrategy(targetedObject);
        }
        break;
      case WHIP_LOWER_AXE:
        if ((lastLowerAxeExists && lowerAxe == null && botState.getPlayerX() >= 1072
            && botState.getPlayerX() < 1168) || (lowerAxe == null && botState.isOnStairs())) {
          setState(State.DESPAWN_LOWER_KNIGHT, targetedObject);
        } else {
          super.pickStrategy(targetedObject);
        }
        break;
      case DESPAWN_LOWER_KNIGHT:
        if (lowerAxe != null && lowerAxe.y2 >= botState.getPlayerY() - 32
            && lowerAxe.y1 <= botState.getPlayerY() - 8) {
          setState(State.WHIP_LOWER_AXE, targetedObject);
          super.pickStrategy(targetedObject);
        } else if (lowerKnight == null) {
          setState(State.WHIP_CANDLES, targetedObject);
        } else if (lowerKnight.distanceX > 32) {
          route(1247, 128);
        }
        break;
      case WHIP_CANDLES:
        if (botState.getPlayerY() > 128) {
          route(1247, 128);
        } else if (candles == null) {
          setState(State.WALK_TO_STAIRS, targetedObject);
        } else if (upperAxe == null 
            || (upperAxeLeft && upperAxe.x2 < botState.getPlayerX() - 8)) {
          super.pickStrategy(targetedObject);
        }
        break;
      case WALK_TO_STAIRS:          
        if (botState.getPlayerX() == 1152 && botState.getPlayerY() == 128) {
          setState(State.WAIT_FOR_NO_UPPER_AXE, targetedObject);
        } else {
          route(1152, 128);
        }
        break;
      case WAIT_FOR_NO_UPPER_AXE:
        if (!lastUpperAxeExists && upperAxe == null) {
          setState(State.WAIT_FOR_UPPER_AXE, targetedObject);
        }
        break;
      case WAIT_FOR_UPPER_AXE:
        if (upperAxe != null && !upperAxeLeft && upperAxe.x >= 1128 
            && upperAxe.y <= 80) {
          setState(State.RUN_FOR_IT, targetedObject);
        }
        break;
      case RUN_FOR_IT:
        if (kneelDelay > 0) {
          --kneelDelay; 
          b.kneel();
        } else if (botState.getPlayerX() < 1112 && upperAxe != null
            && upperAxe.distanceX < 24) {
          if (upperAxe.y < botState.getPlayerY() - 16) {
            kneelDelay = 16; // remain kneeling even after the axe vanishes
                             // just in case another axe is thrown
            b.kneel();
          } else if (b.canJump) {
            playerController.jump(botState);
          }
        } else {
          if (botState.getPlayerY() == 128) {
            route(1144, 112);
          } else if (botState.getPlayerX() == 1056 && botState.getPlayerY() == 96 && (upperAxe == null
              || (!upperAxeLeft && upperAxe.x > botState.getPlayerX() + 12))) {
            setState(State.GO_UP_STAIRS, targetedObject);
            route(1096, 48);
          } else {
            route(1056, 96);
          }
        }
        break;
      case GO_UP_STAIRS:
        route(1096, 48);
        break;
    }
  }
  
  private void setState(final State state, TargetedObject targetedObject) {
    this.state = state;
    clearTarget(targetedObject);
    setStrategy(null);
  }

  @Override
  public void readGameObjects() {
    lastFleamanExists = fleaman != null;
    lastLowerAxeExists = lowerAxe != null;
    lastUpperAxeExists = upperAxe != null;
    candles = fleaman = lowerAxe = upperAxe = lowerKnight = upperKnight = null;
    final GameObject[] objs = gameState.getGameObjects();
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      switch(obj.type) {
        case AXE:
          if (obj.y < 128) {
            upperAxe = obj;
          } else {
            lowerAxe = obj;
          }
          break;
        case AXE_KNIGHT:
          if (obj.y < 128) {
            upperKnight = obj;
          } else {
            lowerKnight = obj;
          }
          break;
        case FLEAMAN:
          fleaman = obj;
          break;
        case CANDLES:
          if (roundTile(obj.x) == 74) {
            candles = obj;
          }
          break;
      }
    }
    if (upperAxe != null) {
      if (upperAxe.x < lastUpperAxeX) {
        upperAxeLeft = true;
      } else if (upperAxe.x > lastUpperAxeX) {
        upperAxeLeft = false;
      }
      lastUpperAxeX = upperAxe.x;
    }
    
    if ((botState.getPlayerY() == 192 && botState.getPlayerX() < 1080) || (upperKnight == null
        && (botState.getPlayerY() <= 128 || (lowerKnight == null && fleaman == null)))) {
      b.addDestination(1096, 48);
    }
  }  

  @Override
  public void routeLeft() {
    if (botState.getPlayerY() < 136) {
      route(1033, 96);
    } else {
      route(1033, 192);
    }
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerY() < 136) {
      route(1247, 128);
    } else {
      route(1263, 192);
    }
  }
}