package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.operation.GameStateRestarter;
import nintaco.api.API;

import java.util.Map;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.Addresses.LOOP;
import static castlevaniabot.model.gameelements.Addresses.WEAPONING;
import static java.lang.Math.abs;

public class Substage0000 extends Substage {
  
  private boolean triggeredTreasure;
  private final GameStateRestarter gameStateRestarter;
  
  public Substage0000(final API api, final PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("00-00-00"));
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    triggeredTreasure = false;
    
    // The bot cannot handle Difficult Mode (gameelements loop 2 or above).
    if (api.readCPU(LOOP) != 0x00) { 
      api.writeCPU(LOOP, 0x00);       // Reset to Normal Mode.
      api.writeCPU(WEAPONING, 0x00);  // Clear weaponing state from prior loop.
    }
  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    switch(obj.type) {
      case DESTINATION:   
        obj.tier = 0; break;
      case FIRE_COLUMN:
        obj.tier = 1; break;
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
        if (botState.getWeapon() == NONE || botState.getWeapon() == STOPWATCH) {
          obj.tier = 5;
        } else {
          playerController.avoid(obj, botState);
        }
        break;        
      case BOOMERANG_WEAPON:
        if (botState.getWeapon() != HOLY_WATER) {
          obj.tier = 5;
        } else {
          playerController.avoid(obj, botState);
        }
        break;
      case AXE_WEAPON:
        if (botState.getWeapon() != HOLY_WATER && botState.getWeapon() != BOOMERANG) {
          obj.tier = 5;
        } else {
          playerController.avoid(obj, botState);
        }
        break;          
      case STOPWATCH_WEAPON:  
        if (botState.getWeapon() == NONE) {
          obj.tier = 5;
        } else {
          playerController.avoid(obj, botState);
        }
        break;          
      case HOLY_WATER_WEAPON:
      case PORK_CHOP:
      case EXTRA_LIFE:
        obj.tier = 5; break;
    }
  }

  @Override
  public void route(final int targetX, final int targetY, BotState botState, GameState gameState) {
    if (targetX > botState.getPlayerX()) {
      if (targetX > 696 && botState.getPlayerX() >= 676 && botState.getPlayerX() < 696) {
        playerController.goRightAndJump(botState);
      } else {
        playerController.goRight(botState);
      }
    } else if (targetX < botState.getPlayerX()) {
      if (targetX < 696 && botState.getPlayerX() > 696 && botState.getPlayerX() <= 714) {
        playerController.goLeftAndJump(botState);
      } else {
        playerController.goLeft(botState);
      }
    } 
  }
  
  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    route(9, 192, botState, gameState);
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    route(760, 192, botState, gameState);
  }  

  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    if (triggeredTreasure) {
      gameState.addDestination(696, 192, botState);
    } else {
      if (abs(botState.getPlayerX() - 712) < 2) {
        triggeredTreasure = true;
      } else {
        gameState.addDestination(712, 192, botState);
      }
    }
  } 
}