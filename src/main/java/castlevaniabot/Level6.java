package castlevaniabot;

import nintaco.api.*;
import static castlevaniabot.Addresses.*;
import static castlevaniabot.GameObjectType.*;

public class Level6 implements Level {
  
  private final API api = ApiSource.getAPI();
  private final CastlevaniaBot b;
  
  public Level6(final CastlevaniaBot b) {
    this.b = b;
  }
  
  @Override public void readGameObjects() {
    b.boneTowerSegmentsCount = b.movingPlatformsCount = b.objsCount = 0;
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
        case 0x03C1: type = WHIP_UPGRADE;        break;
        case 0x039F: type = SMALL_HEART;         break;
        case 0x03BF: type = LARGE_HEART;         break;
        case 0x00B1:
        case 0x01B1: 
        case 0x02B1: 
        case 0x03B1: type = EXTRA_LIFE;          break;
        case 0x0060:
        case 0x0160:
        case 0x0260:
        case 0x0360: type = MONEY_BAG;           break; 
        case 0x01B7: type = AXE_WEAPON;          break;
        case 0xC2BD: type = DAGGER_WEAPON;       break;
        case 0x02A1: type = HOLY_WATER_WEAPON;   break;
        case 0x0289: type = STOPWATCH_WEAPON;    break;
        case 0x0252: type = BOOMERANG_WEAPON;    break;        
        case 0x0340:
        case 0x0350: type = CANDLES;             break;
        case 0x02AD: type = CROSS;               break;
        case 0x0364: type = PORK_CHOP;           break;
        case 0x0042: type = INVISIBLE_POTION;    break;
        case 0x02A5: type = DOULE_SHOT;          break;
        case 0x03A9: type = TRIPLE_SHOT;         break;
        case 0x0368: type = FIREBALL;          
                     left = false;               break;
        case 0x4368: type = FIREBALL;            break;        
        case 0x0378: 
        case 0x037C: type = FLEAMAN;             break;
        case 0x437A:
        case 0x437E: type = FLEAMAN;
                     left = false;               break;
        case 0x0180:
        case 0x0188: type = WHITE_SKELETON;      break;                                          
        case 0x4182: 
        case 0x418A: type = WHITE_SKELETON;   
                     left = false;               break;
        case 0x03D8:
        case 0x03D2: type = PHANTOM_BAT;         break;
        case 0x41B4:
        case 0x41C4: type = EAGLE;
                     left = false;               break;
        case 0x01B2:
        case 0x01C2: type = EAGLE;               break;
        case 0x0282: 
        case 0x02A4: type = COOKIE_MONSTER_HEAD; break;
        case 0x4284: 
        case 0x42A6: type = COOKIE_MONSTER_HEAD;
                     left = false;               break;
        
        case 0x01FC: 
          b.addDraculaHead(x, y, true);
          continue;
        case 0x41FC: 
          b.addDraculaHead(x, y, false);
          continue;
          
        case 0x035C:
        case 0x035A: 
          b.addCrystalBall(x, y);
          continue;
                     
        default:     type = null;                break;
      }      
      if (type != null) {
        b.addGameObject(type, x, y, left, active);
      }
    }
    b.buildDraculaHead();
    b.buildCrystalBall();
  }
}