package castlevaniabot.level;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.Coordinates;
import castlevaniabot.model.gameelements.GameObjectType;
import nintaco.api.API;

import javax.inject.Inject;

import static castlevaniabot.model.gameelements.Addresses.SPRITES;
import static castlevaniabot.model.gameelements.GameObjectType.AXE_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.BLACK_BAT;
import static castlevaniabot.model.gameelements.GameObjectType.BOOMERANG_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.CANDLES;
import static castlevaniabot.model.gameelements.GameObjectType.CROSS;
import static castlevaniabot.model.gameelements.GameObjectType.DAGGER_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.DOULE_SHOT;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.GHOST;
import static castlevaniabot.model.gameelements.GameObjectType.HOLY_WATER_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.INVISIBLE_POTION;
import static castlevaniabot.model.gameelements.GameObjectType.LARGE_HEART;
import static castlevaniabot.model.gameelements.GameObjectType.MEDUSA;
import static castlevaniabot.model.gameelements.GameObjectType.MEDUSA_HEAD;
import static castlevaniabot.model.gameelements.GameObjectType.MONEY_BAG;
import static castlevaniabot.model.gameelements.GameObjectType.PORK_CHOP;
import static castlevaniabot.model.gameelements.GameObjectType.SMALL_HEART;
import static castlevaniabot.model.gameelements.GameObjectType.SNAKE;
import static castlevaniabot.model.gameelements.GameObjectType.SPEAR_KNIGHT;
import static castlevaniabot.model.gameelements.GameObjectType.STOPWATCH_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.TRIPLE_SHOT;
import static castlevaniabot.model.gameelements.GameObjectType.WHIP_UPGRADE;

public class Level2 implements Level {

  private final API api;

  @Inject
  public Level2(API api) {
    this.api = api;
  }
  
  @Override public void readGameObjects(CastlevaniaBot b, GameState gameState, BotState botState, Coordinates currentTile, PlayerController playerController) {
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
        case 0x00A0:
        case 0x01A0:
        case 0x02A0:
        case 0x03A0:
        case 0x00A4:
        case 0x01A4:
        case 0x02A4:
        case 0x03A4:
        case 0x0060:
        case 0x0160:
        case 0x0260:
        case 0x0360: type = MONEY_BAG;         break;        
        case 0xC2BD: type = DAGGER_WEAPON;     break;
        case 0x01B7: type = AXE_WEAPON;        break;
        case 0x02A1: type = HOLY_WATER_WEAPON; break;
        case 0x0289: type = STOPWATCH_WEAPON;  break;
        case 0x0252: type = BOOMERANG_WEAPON; break;        
        case 0x0340:
        case 0x0350: type = CANDLES;           break;
        case 0x02AD: type = CROSS;             break;
        case 0x0364: type = PORK_CHOP;         break;
        case 0x03AC:
        case 0x0042: type = INVISIBLE_POTION;  break;
        case 0x02A5: type = DOULE_SHOT;        break;
        case 0x03A9: type = TRIPLE_SHOT;       break;
        case 0x0368: type = FIREBALL;          
                     left = false;             break;
        case 0x4368: type = FIREBALL;          break;        
        case 0x4192:
        case 0x4196:
        case 0x419A: type = BLACK_BAT;
                     left = false;             break;
        case 0x019C: type = BLACK_BAT;         
                     active = false;           break;
        case 0x0180: type = SPEAR_KNIGHT;      break;
        case 0x4182: type = SPEAR_KNIGHT;      
                     left = false;             break;
        case 0x02E8:
        case 0x02EC: type = MEDUSA_HEAD;       break;
        case 0x42EA:
        case 0x42EE: type = MEDUSA_HEAD;
                     left = false;             break;
        case 0x01B0: type = GHOST;             break;
        case 0x41B2:
        case 0x41B4: type = GHOST;             
                     left = false;             break;
        case 0x01D0:
        case 0x02D8: type = MEDUSA;            break;
        case 0x42F0:
        case 0x42F6: type = SNAKE;
                     left = false;             break;
        case 0x02F2:
        case 0x02F4: type = SNAKE;             break;
        
        case 0x0072:
        case 0x4072:
          b.getGameState().addMovingPlatformSegment(x, y);
          continue;
          
        case 0x01E0:
        case 0x03E0:
        case 0x41E2:
        case 0x43E2:
          gameState.addBoneTowerSegment(x, y);
          continue;
          
        case 0x035C:
        case 0x035A: 
          b.addCrystalBall(x, y);
          continue;          
          
        default:     type = null;              break;
      }      
      if (type != null) {
        gameState.addGameObject(type, x, y, left, active, botState, currentTile, playerController);
      }
    }
    b.getGameState().buildMovingPlatforms();
    gameState.buildBoneTowers(botState, currentTile, playerController);
    b.buildCrystalBall();
  }
}