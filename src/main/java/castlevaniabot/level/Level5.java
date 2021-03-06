package castlevaniabot.level;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.Coordinates;
import castlevaniabot.model.gameelements.GameObjectType;
import nintaco.api.API;

import javax.inject.Inject;

import static castlevaniabot.model.gameelements.Addresses.SPRITES;
import static castlevaniabot.model.gameelements.GameObjectType.AXE;
import static castlevaniabot.model.gameelements.GameObjectType.AXE_KNIGHT;
import static castlevaniabot.model.gameelements.GameObjectType.AXE_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.BOOMERANG_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.CANDLES;
import static castlevaniabot.model.gameelements.GameObjectType.CROSS;
import static castlevaniabot.model.gameelements.GameObjectType.DAGGER_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.DEATH;
import static castlevaniabot.model.gameelements.GameObjectType.DOULE_SHOT;
import static castlevaniabot.model.gameelements.GameObjectType.EXTRA_LIFE;
import static castlevaniabot.model.gameelements.GameObjectType.FIREBALL;
import static castlevaniabot.model.gameelements.GameObjectType.FLEAMAN;
import static castlevaniabot.model.gameelements.GameObjectType.HOLY_WATER_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.INVISIBLE_POTION;
import static castlevaniabot.model.gameelements.GameObjectType.LARGE_HEART;
import static castlevaniabot.model.gameelements.GameObjectType.MONEY_BAG;
import static castlevaniabot.model.gameelements.GameObjectType.PORK_CHOP;
import static castlevaniabot.model.gameelements.GameObjectType.RED_SKELETON;
import static castlevaniabot.model.gameelements.GameObjectType.SMALL_HEART;
import static castlevaniabot.model.gameelements.GameObjectType.STOPWATCH_WEAPON;
import static castlevaniabot.model.gameelements.GameObjectType.TRIPLE_SHOT;
import static castlevaniabot.model.gameelements.GameObjectType.WHIP_UPGRADE;
import static castlevaniabot.model.gameelements.GameObjectType.WHITE_SKELETON;

public class Level5 implements Level {

  private final API api;

  @Inject
  public Level5(API api) {
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
        case 0x01A0: type = AXE_KNIGHT;          break;                     
        case 0x41A4: type = AXE_KNIGHT;          
                     left = false;               break;
        case 0x01D0: type = DEATH;               break;
        case 0x41D4: type = DEATH;
                     left = false;               break;
        
//        case 0x02E8:
//        case 0x02EC: 
//          type = MEDUSA_HEAD;
//          b.addMedusaHead(x, y);
//          break;
//        case 0x42EA:
//        case 0x42EE: 
//          type = MEDUSA_HEAD;
//          left = false;
//          b.addMedusaHead(x, y);
//          break;        
                     
        case 0x01B7: 
          if (gameState.getStageNumber() >= 14) {
            type = AXE;
          } else {
            type = AXE_WEAPON;
          }
          break;                     
        case 0x41B9:             
        case 0x81B7:
        case 0xC1B9: type = AXE;                 break;
        
        case 0x0180:
        case 0x0188: type = WHITE_SKELETON;      break;                                          
        case 0x4182: 
        case 0x418A: type = WHITE_SKELETON;   
                     left = false;               break; 
        case 0x0380:
        case 0x0388: type = RED_SKELETON;        break;        
        case 0x4382: 
        case 0x438A: type = RED_SKELETON;
                     left = false;               break; 
                                          
        case 0x0398: // red skeleton rising (left)
        case 0x439A: // red skeleton rising (right)
        case 0x039C: // red bones (left)    
        case 0x439E: // red bones (right)
          gameState.addRedBones(x, y, gameState, botState);
          continue;
          
        case 0x01E0:
        case 0x03E0:
        case 0x41E2:
        case 0x43E2:
          gameState.addBoneTowerSegment(x, y);
          continue;    
          
        case 0x03F6:
        case 0x83F6:
        case 0x43F8:
        case 0xC3F8:
        case 0x03FA:
        case 0x83FA:
        case 0x43FC:
        case 0xC3FC: 
          gameState.addSickle(x, y);
          continue;
          
        case 0x035C:
        case 0x035A: 
          gameState.addCrystalBall(x, y);
          continue;          
                     
        default:     type = null;                break;
      }      
      if (type != null) {
        gameState.addGameObject(type, x, y, left, active, botState, currentTile, playerController);
      }
    }
    gameState.buildBoneTowers(botState, currentTile, playerController);
    gameState.buildRedBones(gameState, currentTile, botState, playerController);
    gameState.buildSickles(gameState, botState, currentTile, playerController);
    gameState.buildCrystalBall(gameState, botState, currentTile, playerController);
//    b.buildMedusaHeads();
  }
}