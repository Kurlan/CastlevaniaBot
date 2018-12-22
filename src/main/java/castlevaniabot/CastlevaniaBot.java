package castlevaniabot;

import castlevaniabot.control.GamePad;
import castlevaniabot.control.PlayerController;
import castlevaniabot.level.Level;
import castlevaniabot.model.gameelements.Coordinates;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.MapElement;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.model.gameelements.Modes;
import castlevaniabot.model.gameelements.TargetedObject;
import castlevaniabot.model.gameelements.TileType;
import castlevaniabot.strategy.AllStrategies;
import castlevaniabot.substage.Substage;
import castlevaniabot.substage.Substage0000;
import castlevaniabot.substage.Substage0100;
import castlevaniabot.substage.Substage0200;
import castlevaniabot.substage.Substage0201;
import castlevaniabot.substage.Substage0300;
import castlevaniabot.substage.Substage0400;
import castlevaniabot.substage.Substage0401;
import castlevaniabot.substage.Substage0500;
import castlevaniabot.substage.Substage0501;
import castlevaniabot.substage.Substage0600;
import castlevaniabot.substage.Substage0601;
import castlevaniabot.substage.Substage0700;
import castlevaniabot.substage.Substage0701;
import castlevaniabot.substage.Substage0800;
import castlevaniabot.substage.Substage0801;
import castlevaniabot.substage.Substage0900;
import castlevaniabot.substage.Substage1000;
import castlevaniabot.substage.Substage1100;
import castlevaniabot.substage.Substage1200;
import castlevaniabot.substage.Substage1300;
import castlevaniabot.substage.Substage1301;
import castlevaniabot.substage.Substage1400;
import castlevaniabot.substage.Substage1401;
import castlevaniabot.substage.Substage1500;
import castlevaniabot.substage.Substage1501;
import castlevaniabot.substage.Substage1600;
import castlevaniabot.substage.Substage1700;
import castlevaniabot.substage.Substage1701;
import castlevaniabot.substage.Substage1800;
import castlevaniabot.substage.Substage1801;
import nintaco.api.API;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.NONE;
import static castlevaniabot.model.gameelements.Addresses.CAMERA_X;
import static castlevaniabot.model.gameelements.Addresses.HEARTS;
import static castlevaniabot.model.gameelements.Addresses.KNEELING;
import static castlevaniabot.model.gameelements.Addresses.MODE;
import static castlevaniabot.model.gameelements.Addresses.ON_STAIRS;
import static castlevaniabot.model.gameelements.Addresses.PAUSED;
import static castlevaniabot.model.gameelements.Addresses.PLAYER_FACING;
import static castlevaniabot.model.gameelements.Addresses.PLAYER_IMAGE;
import static castlevaniabot.model.gameelements.Addresses.PLAYER_X;
import static castlevaniabot.model.gameelements.Addresses.PLAYER_Y;
import static castlevaniabot.model.gameelements.Addresses.PLAYING;
import static castlevaniabot.model.gameelements.Addresses.SHOT;
import static castlevaniabot.model.gameelements.Addresses.STAGE;
import static castlevaniabot.model.gameelements.Addresses.SUBSTAGE;
import static castlevaniabot.model.gameelements.Addresses.WEAPON;
import static castlevaniabot.model.gameelements.Addresses.WEAPONING;
import static castlevaniabot.model.gameelements.Addresses.WHIP_LENGTH;

public class CastlevaniaBot {
  
  private static final int AVOID_X_RESET = -512;

  final Substage0000 SUBSTAGE_0000;
  final Substage0100 SUBSTAGE_0100;
  final Substage0200 SUBSTAGE_0200;
  public final Substage0201 SUBSTAGE_0201;
  final Substage0300 SUBSTAGE_0300;
  final Substage0400 SUBSTAGE_0400;
  final Substage0401 SUBSTAGE_0401;
  final Substage0500 SUBSTAGE_0500;
  final Substage0501 SUBSTAGE_0501;
  final Substage0600 SUBSTAGE_0600;
  final Substage0601 SUBSTAGE_0601;
  final Substage0700 SUBSTAGE_0700;
  final Substage0701 SUBSTAGE_0701;
  final Substage0800 SUBSTAGE_0800;
  final Substage0801 SUBSTAGE_0801;
  public final Substage0900 SUBSTAGE_0900;
  final Substage1000 SUBSTAGE_1000;
  final Substage1100 SUBSTAGE_1100;
  final Substage1200 SUBSTAGE_1200;
  final Substage1300 SUBSTAGE_1300;
  final Substage1301 SUBSTAGE_1301;
  final Substage1400 SUBSTAGE_1400;
  final Substage1401 SUBSTAGE_1401;
  final Substage1500 SUBSTAGE_1500;
  public final Substage1501 SUBSTAGE_1501;
  final Substage1600 SUBSTAGE_1600;
  final Substage1700 SUBSTAGE_1700;
  final Substage1701 SUBSTAGE_1701;
  final Substage1800 SUBSTAGE_1800;
  final Substage1801 SUBSTAGE_1801;

  private final API api;
  public final Map<String, MapRoutes> allMapRoutes;

  private BotState botState;
  private GameState gameState;

  public final AllStrategies allStrategies;

  private final List<Level> levels;
  private final GamePad gamePad;
  private final PlayerController playerController;

  public CastlevaniaBot(API api, Map<String, MapRoutes> allMapRoutes, GameObject[] gameObjects, List<Level> levels,
                        GamePad gamePad, PlayerController playerController) {
      this.botState = new BotState();
      this.botState.setWeapon(NONE);
      this.botState.setTargetedObject(
              TargetedObject.builder()
                .target(null)
                .targetType(null)
                .coordinates(Coordinates
                        .builder()
                        .x(-512)
                        .y(-512)
                        .build())
              .build());
      this.botState.setCurrentTile(Coordinates.builder().x(0).y(0).build());
      this.gameState = new GameState();
      gameState.setGameObjects(gameObjects);
      this.allStrategies = new AllStrategies(this,botState, gameState, playerController);
      this.levels = levels;
      this.gamePad = gamePad;
      this.playerController = playerController;

    try {

    } catch(final Throwable t) {
      t.printStackTrace(); // Display construction errors to console
    }

    this.api = api;
    this.allMapRoutes = allMapRoutes;

    SUBSTAGE_0000 = new Substage0000(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0100 = new Substage0100(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0200 = new Substage0200(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0201 = new Substage0201(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0300 = new Substage0300(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0400 = new Substage0400(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0401 = new Substage0401(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0500 = new Substage0500(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0501 = new Substage0501(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0600 = new Substage0600(botState, api, playerController, gameState, allMapRoutes, allStrategies.getCRUSHER());
    SUBSTAGE_0601 = new Substage0601(botState, api, playerController, gameState, allMapRoutes, allStrategies.getMEDUSA());
    SUBSTAGE_0700 = new Substage0700(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0701 = new Substage0701(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0800 = new Substage0800(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0801 = new Substage0801(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_0900 = new Substage0900(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1000 = new Substage1000(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1100 = new Substage1100(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1200 = new Substage1200(botState, api, playerController, gameState, allMapRoutes, allStrategies.getFRANKENSTEIN());
    SUBSTAGE_1300 = new Substage1300(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1301 = new Substage1301(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1400 = new Substage1400(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1401 = new Substage1401(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1500 = new Substage1500(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1501 = new Substage1501(botState, api, playerController, gameState, allMapRoutes, allStrategies.getHOLY_WATER_DEATH());
    SUBSTAGE_1600 = new Substage1600(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1700 = new Substage1700(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1701 = new Substage1701(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1800 = new Substage1800(botState, api, playerController, gameState, allMapRoutes);
    SUBSTAGE_1801 = new Substage1801(botState, api, playerController, gameState, allMapRoutes, allStrategies.getDRACULA(), allStrategies.getCOOKIE_MONSTER());
  }

  public AllStrategies getAllStrategies() {
    return this.allStrategies;
  }

  public GameState getGameState() {
    return this.gameState;
  }

  public BotState getBotState() {
    return this.botState;
  }

  public void apiDisabled() {
    System.out.println("API disabled");
    gameState.setCurrentLevel(null);
    gameState.setCurrentSubstage(null);
    botState.setCurrentStrategy(null);
    botState.getTargetedObject().reset();
  }

  private void readState() {

    gameState.setMode(api.readCPU(MODE));
    final int play = api.readCPU(PLAYING);
    gameState.setPlaying((gameState.getMode() == Modes.PLAYING || gameState.getMode() == Modes.CRYSTAL_BALL)
        && (play == 0x06 || play == 0x01 || play == 0x00)
            && api.readCPU(PLAYER_IMAGE) != 0x1C);
    if (!gameState.isPlaying()) {
      return;
    }
    gameState.setStageNumber(api.readCPU(STAGE));
    gameState.setSubstageNumber(api.readCPU(SUBSTAGE));
    botState.setKneeling(api.readCPU(KNEELING) == 0x0A);
    botState.setPlayerY(api.readCPU(PLAYER_Y) + (botState.isKneeling() ? 12 : 16));
    botState.setPlayerX(api.readCPU16(PLAYER_X));
    botState.setPlayerLeft(api.readCPU(PLAYER_FACING) == 0x01);
    gameState.setCameraX(api.readCPU16(CAMERA_X));
    botState.setWeapon(api.readCPU(WEAPON));
    botState.setWhipLength(api.readCPU(WHIP_LENGTH));
    botState.setHearts(api.readCPU(HEARTS));
    botState.setShot(api.readCPU(SHOT) + 1);
    botState.setOnStairs(api.readCPU(ON_STAIRS) == 0x00);
    gameState.setPaused(api.readCPU(PAUSED) == 0x01);

    if (gameState.getWeaponDelay() == 0) {
      final int _weaponing = api.readCPU(WEAPONING);
      gameState.setWeaponing(_weaponing != 0xFC && _weaponing != 0x00);
    } else {
      gameState.setWeaponing(true);
    }

   switch(gameState.getStageNumber()) {
      case  0:
      case  1:
      case  2:
      case  3: gameState.setCurrentLevel(levels.get(0)); break;
      case  4:
      case  5:
      case  6: gameState.setCurrentLevel(levels.get(1)); break;
      case  7:
      case  8:
      case  9: gameState.setCurrentLevel(levels.get(2)); break;
      case 10:
      case 11:
      case 12: gameState.setCurrentLevel(levels.get(3)); break;
      case 13:
      case 14:
      case 15: gameState.setCurrentLevel(levels.get(4)); break;
      case 16:
      case 17:
      case 18: gameState.setCurrentLevel(levels.get(5)); break;
    }

    Substage _substage = null;
    switch((gameState.getStageNumber() << 8) | gameState.getSubstageNumber()) {
      case 0x0000: _substage = SUBSTAGE_0000; break;
      case 0x0100: _substage = SUBSTAGE_0100; break;
      case 0x0200: _substage = SUBSTAGE_0200; break;
      case 0x0201: _substage = SUBSTAGE_0201; break;
      case 0x0300: _substage = SUBSTAGE_0300; break;
      case 0x0400: _substage = SUBSTAGE_0400; break;
      case 0x0401: _substage = SUBSTAGE_0401; break;
      case 0x0500: _substage = SUBSTAGE_0500; break;
      case 0x0501: _substage = SUBSTAGE_0501; break;
      case 0x0600: _substage = SUBSTAGE_0600; break;
      case 0x0601: _substage = SUBSTAGE_0601; break;
      case 0x0700: _substage = SUBSTAGE_0700; break;
      case 0x0701: _substage = SUBSTAGE_0701; break;
      case 0x0800: _substage = SUBSTAGE_0800; break;
      case 0x0801: _substage = SUBSTAGE_0801; break;
      case 0x0900: _substage = SUBSTAGE_0900; break;
      case 0x0A00: _substage = SUBSTAGE_1000; break;
      case 0x0B00: _substage = SUBSTAGE_1100; break;
      case 0x0C00: _substage = SUBSTAGE_1200; break;
      case 0x0D00: _substage = SUBSTAGE_1300; break;
      case 0x0D01: _substage = SUBSTAGE_1301; break;
      case 0x0E00: _substage = SUBSTAGE_1400; break;
      case 0x0E01: _substage = SUBSTAGE_1401; break;
      case 0x0F00: _substage = SUBSTAGE_1500; break;
      case 0x0F01: _substage = SUBSTAGE_1501; break;
      case 0x1000: _substage = SUBSTAGE_1600; break;
      case 0x1100: _substage = SUBSTAGE_1700; break;
      case 0x1101: _substage = SUBSTAGE_1701; break;
      case 0x1200: _substage = SUBSTAGE_1800; break;
      case 0x1201: _substage = SUBSTAGE_1801; break;
    }
    if (_substage != null && _substage != gameState.getCurrentSubstage()) {
      gameState.setEntryDelay(ThreadLocalRandom.current().nextInt(17));
      _substage.init();
    }
    gameState.setCurrentSubstage(_substage);

    if (gameState.getCurrentLevel() == null || _substage == null) {
      return;
    }

    if (botState.isOnStairs()) {
      botState.setOverHangingLeft(false);
      botState.setOverHangingRight(false);
      botState.setOnPlatform(false);
      botState.getCurrentTile().setX(botState.getPlayerX() >> 4);
      botState.getCurrentTile().setY(botState.getPlayerY() >> 4);
      final MapRoutes mapRoutes = gameState.getCurrentSubstage().getMapRoutes();
      final MapElement[][] map = mapRoutes.map;
      if (!TileType.isStairs(map[botState.getCurrentTile().getY()][botState.getCurrentTile().getX()].tileType)) {
        if (botState.getCurrentTile().getX() < mapRoutes.width - 1
            && TileType.isStairs(map[botState.getCurrentTile().getY()][botState.getCurrentTile().getX() + 1].tileType)) {
          botState.getCurrentTile().setX(botState.getCurrentTile().getX() + 1);
        } else if (botState.getCurrentTile().getX() > 0
            && TileType.isStairs(map[botState.getCurrentTile().getY()][botState.getCurrentTile().getX() - 1].tileType)) {
          botState.getCurrentTile().setX(botState.getCurrentTile().getX() - 1);
        }
      }
    } else if (playerController.isOnPlatform(botState.getPlayerX(), botState.getPlayerY(), gameState.getCurrentSubstage().getMapRoutes())) {
      botState.setOnPlatform(true);
      botState.setOverHangingLeft(false);
      botState.setOverHangingRight(false);
      botState.getCurrentTile().setX(botState.getPlayerX() >> 4);
      botState.getCurrentTile().setY(botState.getPlayerY() >> 4);
    } else if (playerController.isOnPlatform(botState.getPlayerX() - 4, botState.getPlayerY(), gameState.getCurrentSubstage().getMapRoutes())) {
      botState.setOnPlatform(true);
      botState.setOverHangingLeft(false);
      botState.setOverHangingRight(true);
      botState.getCurrentTile().setX((botState.getPlayerX() - 4) >> 4);
      botState.getCurrentTile().setY(botState.getPlayerY() >> 4);
    } else if (playerController.isOnPlatform(botState.getPlayerX() + 4, botState.getPlayerY(), gameState.getCurrentSubstage().getMapRoutes())) {
      botState.setOnPlatform(true);
      botState.setOverHangingLeft(true);
      botState.setOverHangingRight(false);
      botState.getCurrentTile().setX((botState.getPlayerX() + 4) >> 4);
      botState.getCurrentTile().setY(botState.getPlayerY() >> 4);
    } else {
      botState.setOverHangingLeft(false);
      botState.setOverHangingRight(false);
      botState.setOnPlatform(false);
    }

    botState.setAtBottomOfStairs(playerController.isAtBottomOfStairs(botState, gameState, botState.getCurrentTile()));
    botState.setAtTopOfStairs(playerController.isAtTopOfStairs(botState, gameState, botState.getCurrentTile()));
    botState.setCanJump(!gameState.isWeaponing() && !botState.isOnStairs() && !botState.isKneeling() && botState.isOnPlatform()
        && botState.getJumpDelay() == 0);

    gameState.getCurrentLevel().readGameObjects(this, gameState, botState, botState.getCurrentTile(), playerController);
    _substage.readGameObjects();
  }
  
  public void renderFinished() {
   
    readState();

    final boolean halted;
    switch(gameState.getMode()) {
      case Modes.TITLE_SCREEN:
      case Modes.DEMO:
      case Modes.GAME_OVER:
        halted = true;
        break;
      case Modes.PLAYING:
        halted = gameState.isPaused();
        break;
      default:
        halted = false;
        break;
    }
    if (halted) {
      if (gameState.getPauseDelay() > 0) {
        gameState.setPauseDelay(gameState.getPauseDelay() - 1);
      } else {
        gameState.setPauseDelay(60);
        gamePad.pressStart();
      }
      return;
    } else {
      gameState.setPauseDelay(0);
    }

    if (!gameState.isPlaying() || gameState.getCurrentLevel() == null || gameState.getCurrentSubstage() == null) {
      botState.setCurrentStrategy(null);
      botState.getTargetedObject().reset();
      return;
    } 

    if (botState.getJumpDelay() > 0) {
      botState.setJumpDelay(botState.getJumpDelay() - 1);
    }

    if (gameState.getWeaponDelay() > 0) {
      gameState.setWeaponDelay(gameState.getWeaponDelay() - 1);;
    }

    botState.setAvoidX(AVOID_X_RESET);
    gameState.getCurrentSubstage().pickStrategy(botState.getTargetedObject(), allStrategies);
    if (botState.getCurrentStrategy() != null) {
      if (gameState.getEntryDelay() > 0) {
        gameState.setEntryDelay(gameState.getEntryDelay() - 1);
      } else {
        botState.getCurrentStrategy().step();
      }
    }  
    
//    paintGameObjects();
//    printGameObjects();
  }

    public GamePad getGamepad() {
        return this.gamePad;
    }
}