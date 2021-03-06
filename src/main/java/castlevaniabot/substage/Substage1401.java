package castlevaniabot.substage;

import castlevaniabot.BotState;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.GameObjectType;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.operation.GameStateRestarter;
import castlevaniabot.strategy.AllStrategies;
import nintaco.api.API;

import java.util.Map;

import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.creativeelements.Weapon.STOPWATCH;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_140100;
import static castlevaniabot.model.gameelements.Addresses.BLOCK_140101;
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static castlevaniabot.model.gameelements.GameObjectType.RED_BONES;
import static castlevaniabot.model.gameelements.GameObjectType.RED_SKELETON;
import static castlevaniabot.model.gameelements.GameObjectType.RED_SKELETON_RISING;

public class Substage1401 extends Substage {
  
  private boolean treasureTriggered1;
  private boolean treasureTriggered2;
  private boolean treasureTriggered3;
  
  private boolean blockWhipped1;
  private boolean blockBroken1;

  private boolean blockWhipped2;
  private boolean blockBroken2;
  private MapRoutes next1;
  private MapRoutes next2;
  private GameStateRestarter gameStateRestarter;

  public Substage1401(final API api, final PlayerController playerController, Map<String, MapRoutes> allMapRoutes, GameStateRestarter gameStateRestarter) {
    super(api, playerController, allMapRoutes.get("14-01-00"));
    this.next1 = allMapRoutes.get("14-01-01");
    this.next2 = allMapRoutes.get("14-01-01");
    this.gameStateRestarter = gameStateRestarter;
  }

  @Override
  public void init(BotState botState, GameState gameState) {
    gameStateRestarter.restartSubstage(gameState, botState);
    blockWhipped1 = blockBroken1 = blockWhipped2 = blockBroken2 
        = treasureTriggered1 = false;
    treasureTriggered3 = treasureTriggered2 = botState.getWeapon() != HOLY_WATER;

  }
  
  @Override void evaluteTierAndSubTier(final GameObject obj, BotState botState, GameState gameState) {
    
    if (obj.type == GameObjectType.AXE) {
      if (obj.distanceX < 64 && obj.y1 <= botState.getPlayerY()
          && obj.y2 >= botState.getPlayerY() - 32) {
        obj.tier = 9;
      }
    } else if (obj.type == GameObjectType.AXE_KNIGHT) {
      if (obj.distanceX < 64 && obj.y1 <= botState.getPlayerY() + 16
          && obj.y2 >= botState.getPlayerY() - 64) {
        obj.tier = 9;
      }
    } else if (obj.type == RED_SKELETON) {
      if (obj.distanceX < 64 && obj.distanceY <= 4) {
        obj.tier = 7;
      }
    } else if (obj.type == RED_BONES || obj.type == RED_SKELETON_RISING) {      
      if (obj.distanceY <= 4 && (obj.distanceX <= 32 
          || (obj.playerFacing && obj.distanceX < 48))) {
        obj.tier = 6;
      }
    } else if (obj.type == DESTINATION) {
      obj.tier = 0;
    } else if (obj.distance < HORIZON) {
      switch(obj.type) {
        case CANDLES:
          switch(roundTile(obj.x)) {
            case 18:
              if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0 && !botState.isOnStairs()
                  && botState.getPlayerY() >= 128 && botState.getPlayerX() >= 288 && botState.getPlayerX() < 400) {
                obj.tier = 1;
              }
              break;
            case 22:
              if (botState.getWeapon() == HOLY_WATER && botState.getHearts() > 0) {
                obj.tier = 1;
              }
              break;
            default:
              obj.tier = 1;
              break;
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
  }

  @Override
  public void pickStrategy(TargetedObject targetedObject, AllStrategies allStrategies, BotState botState, GameState gameState) {
    if (botState.getCurrentStrategy() == allStrategies.getWAIT()) {
      if (botState.getPlayerX() >= 832) {
        if (treasureTriggered1) {
          super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
        }
      } else {
        if (treasureTriggered3) {
          super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
        }
      }
    } else if (!treasureTriggered3 && !botState.isOnStairs()&& botState.getPlayerX() >= 288
        && botState.getPlayerX() < 480 && botState.getPlayerY() > 128
            && !playerController.isEnemyInBounds(288, 128, 480, 208, gameState)) {
      clearTarget(targetedObject);
      allStrategies.getWAIT().init(297, 192);
      botState.setCurrentStrategy(allStrategies.getWAIT());
    } else if (!treasureTriggered1 && botState.getPlayerX() >= 928 && botState.getPlayerX() < 1024
        && botState.getPlayerY() > 112 && !playerController.isEnemyInBounds(816, 112, 1024, 208, gameState)) {
      clearTarget(targetedObject);
      allStrategies.getWAIT().init(984, 192);
      botState.setCurrentStrategy(allStrategies.getWAIT());
    } else if (botState.getPlayerY() == 192 && botState.getPlayerX() <= 33) {
      playerController.goLeft(botState);
    } else {
      super.pickStrategy(targetedObject, allStrategies, botState ,gameState);
    }
  }

  @Override
  public void readGameObjects(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 672 && botState.getPlayerX() < 784 && botState.getPlayerY() <= 128) {
      final boolean block1 = api.readPPU(BLOCK_140100) == 0x00;
      final boolean block2 = api.readPPU(BLOCK_140101) == 0x00;
      if (!blockBroken1 && block1) {
        blockWhipped1 = blockBroken1 = true;
        if (block1 && block2) {
          mapRoutes = next1;
        }
      }
      if (!blockBroken2 && block2) {
        blockWhipped2 = blockBroken2 = true;
        if (block1 && block2) {
          mapRoutes = next2;
        }
      }
      if (!blockWhipped1) {
        gameState.addBlock(768, 64, botState);
      }
      if (!blockWhipped2) {
        gameState.addBlock(768, 80, botState);
      }
    }    
    if (treasureTriggered2) {
      gameState.addDestination(32, 192, botState);
    } else {
      gameState.addDestination(560, 128, botState);
      if (botState.getPlayerX() < 208) {
        treasureTriggered2 = true;
      } else if (botState.getPlayerX() == 560 && botState.getPlayerY() == 128) {
        if (botState.isKneeling()) {
          treasureTriggered2 = true;
        } else {
          playerController.kneel();
        }
      }      
    }   
  }  

  @Override
  public void route(final int targetX, final int targetY,
                    final boolean checkForEnemies, BotState botState, GameState gameState) {
    if (botState.isOnStairs() && botState.getPlayerY() >= 156 && botState.getPlayerX() < 256) {
      if (!playerController.isEnemyInBounds(120, 156, 184, 208, gameState)) {
        super.route(targetX, targetY, checkForEnemies, botState ,gameState);
      }
    } else if (botState.getPlayerX() >= 1200 && botState.getPlayerY() > 96 && botState.getPlayerY() < 144) {
      if (!playerController.isEnemyInBounds(1192, 0, 1279, 104, gameState)) {
        super.route(targetX, targetY, checkForEnemies, botState ,gameState);
      }
    } else {
      super.route(targetX, targetY, checkForEnemies, botState ,gameState);
    }
  }

  @Override
  public void routeLeft(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 1056 && botState.getPlayerY() >= 144) {
      route(1065, 192, botState, gameState);
    } else if (botState.getPlayerX() >= 800 && botState.getPlayerY() <= 96) {
      route(809, 96, botState, gameState);
    } else if (botState.getPlayerX() >= 288 && botState.getPlayerX() < 1024 && botState.getPlayerY() >= 112) {
      route(297, 192, botState, gameState);
    } else if (botState.getPlayerY() <= 96 && botState.getPlayerX() < 768) {
      route(41, 96, botState, gameState);
    } else {
      route(32, 192, botState, gameState);
    }
  }
  
  @Override
  public void routeRight(BotState botState, GameState gameState) {
    if (botState.getPlayerX() >= 1056 && botState.getPlayerY() >= 144) {
      route(1262, 192, botState, gameState);
    } else if (botState.getPlayerX() >= 800 && botState.getPlayerY() <= 96) {
      route(1262, 96, botState, gameState);
    } else if (botState.getPlayerX() >= 288 && botState.getPlayerX() < 1024 && botState.getPlayerY() >= 112) {
      route(1015, 192, botState, gameState);
    } else if (botState.getPlayerY() <= 96 && botState.getPlayerX() < 768) {
      route(759, 96, botState, gameState);
    } else {
      route(247, 192, botState, gameState);
    }
  }

  @Override
  public void treasureTriggered(BotState botState) {
    if (botState.getPlayerX() >= 832) {
      treasureTriggered1 = true;
    } else {
      treasureTriggered3 = true;
    }
  }
  
  @Override
  public void blockWhipped(BotState botState) {
    if (blockWhipped1) {
      blockWhipped2 = true;
    } else {
      blockWhipped1 = true;
    }
  }  
}