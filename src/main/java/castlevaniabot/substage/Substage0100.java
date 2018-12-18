package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.TargetedObject;
import nintaco.api.API;

import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_010000;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.GHOUL;
import static castlevaniabot.model.gameelements.GameObjectType.PANTHER;

public class Substage0100 extends Substage {

  private boolean blockWhipped;
  private boolean blockBroken;
  
  public Substage0100(final CastlevaniaBot b, final BotState botState, final API api, PlayerController playerController, GameState gameState) {
    super(b, botState, api, playerController, gameState);
  }

  @Override
  public void init() {
    super.init();
    blockWhipped = blockBroken = false;
    mapRoutes = b.allMapRoutes.get("01-00-00");
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj) {
    if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.type == GHOUL) {
      if (obj.distanceX < 80 && obj.distanceY < 16 
          && (obj.left ^ (botState.getPlayerX() > obj.x))) {
        obj.tier = 6;
      }
    } else if (obj.type == PANTHER) {
      if (obj.active) {
        if (obj.distanceY < 16) {
          obj.tier = 6;
        }
      } 
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES: 
          obj.tier = 1;
          switch (roundTile(obj.x)) { 
            case 58: obj.subTier = 1; break;
            case 62: obj.subTier = 2; break;              
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
        case STOPWATCH_WEAPON:  
          if (botState.getWeapon() != HOLY_WATER) {
            obj.tier = 5;
          } else {
            b.avoid(obj);
          }
          break;          
        case HOLY_WATER_WEAPON: obj.tier = 5; break;
      }        
    }    
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject) {
    if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0 && b.currentTile.getY() == 7
        && b.currentTile.getX() >= 52 && b.currentTile.getX() <= 56 && isPantherResting()) {
      if (botState.getCurrentStrategy() != b.getAllStrategies().getUSE_WEAPON()) {
        clearTarget(targetedObject);
        b.getAllStrategies().getUSE_WEAPON().init(879, 112, true, false);
        botState.setCurrentStrategy(b.getAllStrategies().getUSE_WEAPON());
      }
    } else {
      super.pickStrategy(targetedObject);
    }
  }
  
  private boolean isPantherResting() {
    final GameObject[] objs = gameState.getGameObjects();
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      if (!obj.active && obj.type == PANTHER && obj.x > 912) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public void readGameObjects() {
    if (botState.getPlayerY() >= 160 && botState.getPlayerX() >= 944 && botState.getPlayerX()< 1136) {
      if (!blockBroken && api.readPPU(BLOCK_010000) == 0x00) {
        blockWhipped = blockBroken = true;
        mapRoutes = b.allMapRoutes.get("01-00-01");
      }
      if (!blockWhipped) {
        b.addBlock(1008, 144);
      }
    }
    if (botState.getPlayerX() < 768) {
      b.addDestination(992, 144);  // central platform
    } else {
      b.addDestination(1520, 112); // exit door
    }
  }
  
  @Override
  public void routeLeft() {
    route(9, 208);
  }
  
  @Override
  public void routeRight() {
    if (botState.getPlayerX() >= 1392 && botState.getPlayerY() <= 112) {
      route(1511, 112);
    } else {
      route(1511, 208);
    }
  }  
  
  @Override
  public void blockWhipped() {
    blockWhipped = true;
  }  
}