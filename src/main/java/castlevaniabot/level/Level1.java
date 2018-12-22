package castlevaniabot.level;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.Coordinates;
import castlevaniabot.model.gameelements.GameObjectType;
import nintaco.api.API;

import javax.inject.Inject;

import static castlevaniabot.model.gameelements.Addresses.SPRITES;
import static castlevaniabot.model.gameelements.GameObjectType.AXE_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.CANDLES;
import static castlevaniabot.model.gameelements.GameObjectType.CROSS;
import static castlevaniabot.model.gameelements.GameObjectType.DAGGER_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.DOULE_SHOT;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.FIRE_COLUMN;
import static castlevaniabot.model.gameelements.GameObjectType.FISHMAN;
import static castlevaniabot.model.gameelements.GameObjectType.GHOUL;
import static castlevaniabot.model.gameelements.GameObjectType.HOLY_WATER_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.INVISIBLE_POTION;
import static castlevaniabot.model.gameelements.GameObjectType.LARGE_HEART;
import static castlevaniabot.model.gameelements.GameObjectType.MONEY_BAG;
import static castlevaniabot.model.gameelements.GameObjectType.PANTHER;
import static castlevaniabot.model.gameelements.GameObjectType.PHANTOM_BAT;
import static castlevaniabot.model.gameelements.GameObjectType.PORK_CHOP;
import static castlevaniabot.model.gameelements.GameObjectType.RED_BAT;
import static castlevaniabot.model.gameelements.GameObjectType.SMALL_HEART;
import static castlevaniabot.model.gameelements.GameObjectType.STOPWATCH_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.TRIPLE_SHOT;
import static castlevaniabot.model.gameelements.GameObjectType.WHIP_UPGRADE;

public class Level1 implements Level {
  
  private final API api;

  @Inject
  public Level1(API api) {
    this.api = api;
  }
  
  @Override public void readGameObjects(GameState gameState, BotState botState, Coordinates currentTile, PlayerController playerController) {
    gameState.setBoneTowerSegmentsCount(0);
    gameState.setObjsCount(0);
    gameState.setMovingPlatformsCount(0);
    for(int i = 63; i >= 0; --i) {
      final int sprite = api.readCPU32(SPRITES | (i << 2));      
      final int y = sprite & 0xFF;
      if (y <= 0x1F || y >= 0xEF) {
        continue;
      }      
      final int x = (sprite >> 24) & 0xFF;
      final int at = (sprite >> 8) & 0xFFFF;
      boolean left = true;
      boolean active = true;
      final GameObjectType type;
      switch(at) {
        case 0x03C1: type = WHIP_UPGRADE;      break;
        case 0x039F: type = SMALL_HEART;       break;
        case 0x03BF: type = LARGE_HEART;       break;
        case 0x0060:
        case 0x0160:
        case 0x0260:
        case 0x0360: type = MONEY_BAG;         break;
        case 0xC2BD: type = DAGGER_WEAPON;     break;
        case 0x01B7: type = AXE_WEAPON;        break;
        case 0x02A1: type = HOLY_WATER_WEAPON; break;
        case 0x0289: type = STOPWATCH_WEAPON;  break;
        case 0x029B: type = FIRE_COLUMN;       break;
        case 0x0340:
        case 0x0350: type = CANDLES;           break;
        case 0x02AD: type = CROSS;             break;
        case 0x0180: type = GHOUL;             break;
        case 0x4182: type = GHOUL; 
                     left = false;             break;
        case 0x03B0: type = PANTHER;
                     active = false;           break;
        case 0x43B4: type = PANTHER;
                     left = false;
                     active = false;           break;
        case 0x03C0:
        case 0x03C8:                     
        case 0x03B8: type = PANTHER;           break;
        case 0x43BE:
        case 0x43CE:
        case 0x43C6: type = PANTHER;
                     left = false;             break;
        case 0x0364: type = PORK_CHOP;         break;
        case 0x03AC:
        case 0x03A0: type = FISHMAN;           break;
        case 0x43AE:
        case 0x43A2: type = FISHMAN;
                     left = false;             break;
        case 0x0368: type = FIREBALL;          
                     left = false;             break;
        case 0x4368: type = FIREBALL;          break;
        case 0x0042: type = INVISIBLE_POTION;  break;
        case 0x02A5: type = DOULE_SHOT;        break;
        case 0x03A9: type = TRIPLE_SHOT;       break;
        case 0x03D8:
        case 0x03D2: type = PHANTOM_BAT;       break;
        
        case 0x0390:
        case 0x0398:
        case 0x0394: 
          type = RED_BAT;
          gameState.addRedBat(x, y);
          break;
        case 0x4392:
        case 0x4396:
        case 0x439A: 
          type = RED_BAT;
          left = false;
          gameState.addRedBat(x, y);
          break;
          
        case 0x035C:
        case 0x035A: 
          gameState.addCrystalBall(x, y);
          continue;          
        
        default:     type = null;              break;
      }      
      if (type != null) {
        gameState.addGameObject(type, x, y, left, active, botState, currentTile, playerController);
      }
    }
    gameState.buildRedBats();
    gameState.buildCrystalBall(gameState, botState, currentTile, playerController);
  }
}
