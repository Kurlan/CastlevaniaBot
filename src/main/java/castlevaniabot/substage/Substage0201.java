package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import nintaco.api.API;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_020100;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_020101;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.RED_BAT;

public class Substage0201 extends Substage {
  
  private int blocksWhipped;
  private boolean blocksBroken;
  private boolean useRedBatDamageBoost;
  
  public Substage0201(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController) {
    super(b, botState, api, playerController);
  }
  
  @Override public void init() {
    super.init();
    blocksWhipped = 0;
    blocksBroken = false;
    useRedBatDamageBoost = ThreadLocalRandom.current().nextBoolean();
    mapRoutes = b.allMapRoutes.get("02-01-00");
  }  
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    if (obj.type == RED_BAT) {
      if (obj.distanceX < 104 && obj.y + 88 >= botState.getPlayerY()
          && obj.y - 40 <= botState.getPlayerY() && ((obj.left && obj.x >= botState.getPlayerX() - 40)
              || (!obj.left && obj.x <= botState.getPlayerX() + 40))) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON && (b.currentTile.getX() < 16 ^ (obj.x >= 272))) {
      switch(obj.type) {
        case BLOCK:
         obj.tier = 1; break;
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 22:
              if (botState.getPlayerY() >= 160) {
                obj.tier = 2; 
                obj.subTier = 3;
              }
              break;
            case 19: obj.tier = 2; obj.subTier = 2; break;
            case 26: obj.tier = 2; obj.subTier = 1; break;
            default: obj.tier = 2; break;
          }
          break;
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
        case STOPWATCH_WEAPON:  
          if (b.weapon != HOLY_WATER) {
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
    
    if (useRedBatDamageBoost && botState.getPlayerY() == 144 && botState.getPlayerX() >= 128
        && botState.getPlayerX() < 208 && botState.getPlayerY() < 200) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getRED_BAT_DAMAGE_BOOST()) {
        clearTarget(targetedObject);
        botState.setCurrentStrategy(b.getAllStrategies().getRED_BAT_DAMAGE_BOOST());
        botState.getCurrentStrategy().init();
      }
      return;
    }
    
    super.pickStrategy(targetedObject);
  }  
  
  @Override
  public void readGameObjects() {
    if (b.currentTile.getX() < 16) {
      b.addDestination(72, 224);
      if (b.currentTile.getX() >= 8 && b.currentTile.getY() >= 10) {
        if (!blocksBroken && api.readPPU(BLOCK_020100) == 0x00 
            && api.readPPU(BLOCK_020101) == 0x00) {
          blocksWhipped = 2;
          blocksBroken = true;
          mapRoutes = b.allMapRoutes.get("02-01-01");
        } 
        if (blocksWhipped < 2) {
          b.addBlock(256, 176);
          b.addBlock(256, 192);
        }
      }
    } else {
      b.addDestination(496, 112);
    }
  }
  
  @Override
  public void routeLeft() {
    if (botState.getPlayerX() < 256) {
      if (botState.getPlayerY() < 160) {
        route(8, 112);
      } else {
        route(24, 208);
      }
    } else {
      if (botState.getPlayerY() < 160) {
        route(264, 128);
      } else {
        route(328, 208);
      }      
    }
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerX() < 256) {
      route(248, 208);
    } else {
      if (botState.getPlayerY() < 160) {
        route(504, 112);
      } else {
        route(488, 208);
      }      
    }
  }  
  
  @Override
  public void blockWhipped() {
    ++blocksWhipped;
  }
  
  public void redBatDamageBoostDone() {
    useRedBatDamageBoost = false;
  }
}