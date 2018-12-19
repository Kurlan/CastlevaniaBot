package castlevaniabot;

import castlevaniabot.control.GamePad;
import castlevaniabot.control.PlayerController;
import castlevaniabot.level.Level;
import castlevaniabot.model.creativeelements.Axe;
import castlevaniabot.model.creativeelements.Sickle;
import castlevaniabot.model.gameelements.Coordinates;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.GameObjectType;
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
import static castlevaniabot.model.creativeelements.Whip.WHIPS;
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
import static castlevaniabot.model.gameelements.GameObjectType.DESTINATION;
import static java.lang.Math.abs;
import static java.lang.Math.min;

public class CastlevaniaBot {
  
  private static final int[][] WHIP_HEIGHT_AND_DELAY = {
    { 13, 21 }, { 19, 19 }, { 25, 17 }, { 31, 14 }, { 36, 4 },
  };
  
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

  Sickle[] sickles0 = new Sickle[64];
  Sickle[] sickles1 = new Sickle[64];
  public int sickleCount0;
  public int sickleCount1;
  
  int draculaHeadX;
  int draculaHeadY;
  public int draculaHeadTime;
  boolean draculaHeadLeft;
  
  int crystalBallX;
  int crystalBallY;
  int crystalBallTime;

  public boolean atTopOfStairs;
  public boolean kneeling;

  public boolean canJump;

  int entryDelay;
  int pauseDelay;

  public Coordinates currentTile;

  private final API api;
  public final Map<String, MapRoutes> allMapRoutes;

  private BotState botState;
  private GameState gameState;

  private TargetedObject targetedObject;

  public final AllStrategies allStrategies;

  private final List<Level> levels;
  private final GamePad gamePad;
  private final PlayerController playerController;

  public CastlevaniaBot(API api, Map<String, MapRoutes> allMapRoutes, GameObject[] gameObjects, List<Level> levels,
                        GamePad gamePad, PlayerController playerController) {
      this.currentTile = Coordinates.builder().x(0).y(0).build();
      this.targetedObject = TargetedObject
            .builder()
            .target(null)
            .targetType(null)
            .coordinates(Coordinates
                    .builder()
                    .x(-512)
                    .y(-512)
                    .build())
            .build();
      this.botState = new BotState();
      this.botState.setWeapon(NONE);
      this.gameState = new GameState();
      gameState.setGameObjects(gameObjects);
      this.allStrategies = new AllStrategies(this,botState, gameState, playerController);
      this.levels = levels;
      this.gamePad = gamePad;
      this.playerController = playerController;


    try {

      for(int i = sickles0.length - 1; i >= 0; --i) {
        sickles0[i] = new Sickle();
        sickles1[i] = new Sickle();
      }
    } catch(final Throwable t) {
      t.printStackTrace(); // Display construction errors to console
    }

    this.api = api;
    this.allMapRoutes = allMapRoutes;

    SUBSTAGE_0000 = new Substage0000(this, botState, api, playerController, gameState);
    SUBSTAGE_0100 = new Substage0100(this, botState, api, playerController, gameState);
    SUBSTAGE_0200 = new Substage0200(this, botState, api, playerController, gameState);
    SUBSTAGE_0201 = new Substage0201(this, botState, api, playerController, gameState);
    SUBSTAGE_0300 = new Substage0300(this, botState, api, playerController, gameState);
    SUBSTAGE_0400 = new Substage0400(this, botState, api, playerController, gameState);
    SUBSTAGE_0401 = new Substage0401(this, botState, api, playerController, gameState);
    SUBSTAGE_0500 = new Substage0500(this, botState, api, playerController, gameState);
    SUBSTAGE_0501 = new Substage0501(this, botState, api, playerController, gameState);
    SUBSTAGE_0600 = new Substage0600(this, botState, api, playerController, gameState);
    SUBSTAGE_0601 = new Substage0601(this, botState, api, playerController, gameState);
    SUBSTAGE_0700 = new Substage0700(this, botState, api, playerController, gameState);
    SUBSTAGE_0701 = new Substage0701(this, botState, api, playerController, gameState);
    SUBSTAGE_0800 = new Substage0800(this, botState, api, playerController, gameState);
    SUBSTAGE_0801 = new Substage0801(this, botState, api, playerController, gameState);
    SUBSTAGE_0900 = new Substage0900(this, botState, api, playerController, gameState);
    SUBSTAGE_1000 = new Substage1000(this, botState, api, playerController, gameState);
    SUBSTAGE_1100 = new Substage1100(this, botState, api, playerController, gameState);
    SUBSTAGE_1200 = new Substage1200(this, botState, api, playerController, gameState);
    SUBSTAGE_1300 = new Substage1300(this, botState, api, playerController, gameState);
    SUBSTAGE_1301 = new Substage1301(this, botState, api, playerController, gameState);
    SUBSTAGE_1400 = new Substage1400(this, botState, api, playerController, gameState);
    SUBSTAGE_1401 = new Substage1401(this, botState, api, playerController, gameState);
    SUBSTAGE_1500 = new Substage1500(this, botState, api, playerController, gameState);
    SUBSTAGE_1501 = new Substage1501(this, botState, api, playerController, gameState);
    SUBSTAGE_1600 = new Substage1600(this, botState, api, playerController, gameState);
    SUBSTAGE_1700 = new Substage1700(this, botState, api, playerController, gameState);
    SUBSTAGE_1701 = new Substage1701(this, botState, api, playerController, gameState);
    SUBSTAGE_1800 = new Substage1800(this, botState, api, playerController, gameState);
    SUBSTAGE_1801 = new Substage1801(this, botState, api, playerController, gameState);
  }

  public TargetedObject getTargetedObject() {
    return this.targetedObject;
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
    targetedObject = TargetedObject
            .builder()
            .target(null)
            .targetType(null)
            .coordinates(Coordinates
                    .builder()
                    .x(-512)
                    .y(-512)
                    .build())
            .build();
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
    kneeling = api.readCPU(KNEELING) == 0x0A;
    botState.setPlayerY(api.readCPU(PLAYER_Y) + (kneeling ? 12 : 16));
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
      entryDelay = ThreadLocalRandom.current().nextInt(17);
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
      currentTile.setX(botState.getPlayerX() >> 4);
      currentTile.setY(botState.getPlayerY() >> 4);
      final MapRoutes mapRoutes = gameState.getCurrentSubstage().getMapRoutes();
      final MapElement[][] map = mapRoutes.map;
      if (!TileType.isStairs(map[currentTile.getY()][currentTile.getX()].tileType)) {
        if (currentTile.getX() < mapRoutes.width - 1
            && TileType.isStairs(map[currentTile.getY()][currentTile.getX() + 1].tileType)) {
          currentTile.setX(currentTile.getX() + 1);
        } else if (currentTile.getX() > 0
            && TileType.isStairs(map[currentTile.getY()][currentTile.getX() - 1].tileType)) {
          currentTile.setX(currentTile.getX() - 1);
        }
      }
    } else if (playerController.isOnPlatform(botState.getPlayerX(), botState.getPlayerY(), gameState.getCurrentSubstage().getMapRoutes())) {
      botState.setOnPlatform(true);
      botState.setOverHangingLeft(false);
      botState.setOverHangingRight(false);
      currentTile.setX(botState.getPlayerX() >> 4);
      currentTile.setY(botState.getPlayerY() >> 4);
    } else if (playerController.isOnPlatform(botState.getPlayerX() - 4, botState.getPlayerY(), gameState.getCurrentSubstage().getMapRoutes())) {
      botState.setOnPlatform(true);
      botState.setOverHangingLeft(false);
      botState.setOverHangingRight(true);
      currentTile.setX((botState.getPlayerX() - 4) >> 4);
      currentTile.setY(botState.getPlayerY() >> 4);
    } else if (playerController.isOnPlatform(botState.getPlayerX() + 4, botState.getPlayerY(), gameState.getCurrentSubstage().getMapRoutes())) {
      botState.setOnPlatform(true);
      botState.setOverHangingLeft(true);
      botState.setOverHangingRight(false);
      currentTile.setX((botState.getPlayerX() + 4) >> 4);
      currentTile.setY(botState.getPlayerY() >> 4);
    } else {
      botState.setOverHangingLeft(false);
      botState.setOverHangingRight(false);
      botState.setOnPlatform(false);
    }

    botState.setAtBottomOfStairs(playerController.isAtBottomOfStairs(botState, gameState, currentTile));
    atTopOfStairs = playerController.isAtTopOfStairs(botState, gameState, currentTile);
    canJump = !gameState.isWeaponing() && !botState.isOnStairs() && !kneeling && botState.isOnPlatform()
        && botState.getJumpDelay() == 0;

    gameState.getCurrentLevel().readGameObjects(this, gameState, botState, currentTile, playerController);
    _substage.readGameObjects();
  }
  
  public void addSickle(final int x, final int y) {
    final Sickle sickle = sickles1[sickleCount1++];
    sickle.x = x;
    sickle.y = y;
    sickle.time = 4;
  }
  
  public void buildSickles() {
    
    for(int i = sickleCount1 - 1; i >= 0; --i) {
      final Sickle s1 = sickles1[i];
      for(int j = sickleCount0 - 1; j >= 0; --j) {
        final Sickle s0 = sickles0[j];
        if (abs(s1.x - s0.x) <= 4 && abs(s1.y - s0.y) <= 4) {
          s0.time = -1;
        }
      }
    }
    
    for(int i = sickleCount0 - 1; i >= 0; --i) {
      final Sickle s0 = sickles0[i];
      if (--s0.time > 0) {
        final Sickle s1 = sickles1[sickleCount1++];
        s1.x = s0.x;
        s1.y = s0.y;
        s1.time = s0.time;
      }
    }
    
    for(int i = sickleCount1 - 1; i >= 0; --i) {
      final Sickle s = sickles1[i];      
      gameState.addGameObject(GameObjectType.SICKLE, s.x, s.y, false, true, botState, currentTile, playerController);
    }
    
    final Sickle[] temp = sickles0;
    sickles0 = sickles1;
    sickles1 = temp;
    sickleCount0 = sickleCount1;
    sickleCount1 = 0;    
  }

  public void addDraculaHead(final int x, final int y, final boolean left) {
    draculaHeadX = x;
    draculaHeadY = y;
    draculaHeadLeft = left;
    draculaHeadTime = 3;    
  }  
  
  public void buildDraculaHead() {
    if (draculaHeadTime > 0) {
      --draculaHeadTime;
      gameState.addGameObject(GameObjectType.DRACULA_HEAD, draculaHeadX, draculaHeadY,
          draculaHeadLeft, true, botState, currentTile, playerController);
    }
  }
  
  public void addCrystalBall(final int x, final int y) {
    crystalBallX = x;
    crystalBallY = y;
    crystalBallTime = 3;    
  }  
  
  public void buildCrystalBall() {
    if (crystalBallTime > 0) {
      --crystalBallTime;
      gameState.addGameObject(GameObjectType.CRYSTAL_BALL, crystalBallX, crystalBallY,
          false, true, botState, currentTile, playerController);
    }
  }  
  
  public void addDestination(int x, int y) {
    final MapRoutes mapRoutes = gameState.getCurrentSubstage().getMapRoutes();
    
    if (x < 0 || y < 0 || x >= mapRoutes.pixelsWidth 
        || y >= mapRoutes.pixelsHeight) {
      return;
    }
    
    final GameObject obj = gameState.getGameObjects()[gameState.getObjsCount()];
    obj.type = DESTINATION;
    obj.supportX = obj.x = x;
    obj.y = y;  
    obj.platformX = x >> 4;
    obj.platformY = y >> 4;      
    obj.onPlatform = true;    
    obj.distanceX = abs(x - botState.getPlayerX());
    obj.distanceY = abs(y - botState.getPlayerY());
    obj.left = false;
    obj.active = false;
    obj.playerFacing = botState.isPlayerLeft() ^ (botState.getPlayerX() < x);
    obj.distance = mapRoutes.getDistance(obj, currentTile);
    obj.x1 = x - 8;
    obj.x2 = x + 8;
    obj.y1 = obj.y2 = y;
    obj.distTier = ((0xFFF - min(0xFFF, obj.distance)) << 8) 
        | (0xFF - min(0xFF, obj.distanceX));

    gameState.setObjsCount(gameState.getObjsCount() + 1);
  }
  
  public void addBlock(int x, int y) {
    final MapRoutes mapRoutes = gameState.getCurrentSubstage().getMapRoutes();
    final MapElement[][] map = mapRoutes.map;
    
    x += 8;
    y += 15;

    if (x < 0 || y < 0 || x >= mapRoutes.pixelsWidth 
        || y >= mapRoutes.pixelsHeight) {
      return;
    }
    
    final GameObject obj = gameState.getGameObjects()[gameState.getObjsCount()];
    obj.type = GameObjectType.BLOCK;
    obj.supportX = obj.x = x;
    obj.y = y;  
    obj.distanceX = abs(x - botState.getPlayerX());
    obj.distanceY = abs(y - botState.getPlayerY());
    obj.left = false;
    obj.active = false;
    obj.playerFacing = botState.isPlayerLeft() ^ (botState.getPlayerX() < x);
    obj.x1 = x - 8;
    obj.x2 = x + 8;
    obj.y1 = y - 15;
    obj.y2 = y;
    
    obj.onPlatform = false;
    final int cx = x >> 4;
    final int cy = y >> 4;      
    obj.distance = GameState.MAX_DISTANCE;
    for(int i = (botState.getWhipLength() == 2 ? 2 : 1); i > 0; --i) {
      final int px = cx - i;
      if (px >= 0) {
        final int height = map[cy][px].height;          
        if (height >= 1 && height <= 4) {
          final int py = cy + height;
          final int dist = mapRoutes.getDistance(px, py, currentTile);
          if (dist < GameState.MAX_DISTANCE) {
            if (dist < obj.distance) {                
              obj.distance = dist;
              obj.platformX = px;
              obj.platformY = py;
            }
            break;
          }
        }
      }
    }
    for(int i = (botState.getWhipLength() == 2 ? 2 : 1); i > 0; --i) {
      final int px = cx + i;
      if (px < mapRoutes.width) {
        final int height = map[cy][px].height;          
        if (height >= 1 && height <= 4) {
          final int py = cy + height;
          final int dist = mapRoutes.getDistance(px, py, currentTile);
          if (dist < GameState.MAX_DISTANCE) {
            if (dist < obj.distance) {                
              obj.distance = dist;
              obj.platformX = px;
              obj.platformY = py;
            }
            break;
          }
        }
      }
    }
    
    obj.distTier = ((0xFFF - min(0xFFF, obj.distance)) << 8) 
        | (0xFF - min(0xFF, obj.distanceX));

    gameState.setObjsCount(gameState.getObjsCount() + 1);
  }


  public boolean isInStandingWhipRange(final GameObject obj, final int xOffset,
                                       final int yOffset) {
    return WHIPS[botState.getWhipLength()][0].inRange(obj, xOffset, yOffset, botState);
  } 
  
  public boolean isInKneelingWhipRange(final GameObject obj, final int xOffset,
                                       final int yOffset) {
    return WHIPS[botState.getWhipLength()][1].inRange( obj, xOffset, yOffset, botState);
  }

  public boolean isInStandingWhipRange(final GameObject obj) {
    return WHIPS[botState.getWhipLength()][0].inRange(obj, botState);
  } 
  
  public boolean isInKneelingWhipRange(final GameObject obj) {
    return WHIPS[botState.getWhipLength()][1].inRange(obj, botState);
  }
  
  public boolean isTargetInStandingWhipRange() {
    return WHIPS[botState.getWhipLength()][0].inRange(targetedObject.getTarget(), botState);
  } 
  
  public boolean isTargetInKneelingWhipRange() {
    return WHIPS[botState.getWhipLength()][1].inRange(targetedObject.getTarget(), botState);
  } 
  
  // Returns the whip delay after jumping or -1 if not in range.
  public int isTargetInJumpingWhipRange(final int xOffset, final int yOffset) {
    for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {      
      if (WHIPS[botState.getWhipLength()][0].inRange(targetedObject.getTarget(), xOffset,
          yOffset + WHIP_HEIGHT_AND_DELAY[i][0], botState)) {
        return WHIP_HEIGHT_AND_DELAY[i][1];
      }
    }
    return -1;
  }  
  
  public boolean isTargetInStandingWhipRange(final int xOffset, final int yOffset) {
    return WHIPS[botState.getWhipLength()][0].inRange(targetedObject.getTarget(), xOffset, yOffset, botState);
  } 
  
  public boolean isTargetInKneelingWhipRange(final int xOffset, final int yOffset) {
    return WHIPS[botState.getWhipLength()][1].inRange(targetedObject.getTarget(), xOffset, yOffset, botState);
  }
  public int countObjects(final GameObjectType type) {
    int count = 0;
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type == type) {
        ++count;
      }
    }    
    return count;
  }
  
  public GameObject getType(final GameObjectType type) {
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type == type) {
        return obj;
      }
    }    
    return null;
  }
  
  public boolean isUnderLedge() {
    
    if (currentTile.getY() < 4) {
      return false;
    }
    
    final MapElement[][] map = gameState.getCurrentSubstage().mapRoutes.map;
    if (map[currentTile.getY() - 4][currentTile.getX()].height == 0 || map[currentTile.getY() - 3][currentTile.getX()].height == 0){
      return true;
    }
    
    if (botState.isPlayerLeft()) {
      return (currentTile.getX() > 0) && (map[currentTile.getY() - 4][currentTile.getX() - 1].height == 0
              || map[currentTile.getY() - 3][currentTile.getX() - 1].height == 0);
    } else {
      return (currentTile.getX() < gameState.getCurrentSubstage().mapRoutes.width - 1)
          && (map[currentTile.getY() - 4][currentTile.getX() + 1].height == 0
              || map[currentTile.getY() - 3][currentTile.getX() + 1].height == 0);
    }
  }  
  
  public boolean isTypePresent(final GameObjectType type) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type == type) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean isObjectBelow(final int y) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.y2 >= y) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean isTypeRight(final GameObjectType type, final int x) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type == type && obj.x2 >= x) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean isTypeInBounds(final GameObjectType type, final int x1, final int y1,
                                final int x2, final int y2) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type == type && obj.x1 <= x2 && obj.x2 >= x1 && obj.y2 >= y1 
          && obj.y1 <= y2) {
        return true;
      }
    }
    
    return false;
  }
  
  public void avoid(final GameObject obj) {
    if ((!obj.onPlatform || obj.y >= botState.getPlayerY() - 48)
        && (botState.getAvoidX() < 0 || obj.distanceX < abs(botState.getPlayerX() - botState.getAvoidX()))) {
      botState.setAvoidX(obj.x);
    }
  }
  
  // Can player axe target when standing on specified currentTile?
  public boolean canHitTargetWithAxe(final int platformX, final int platformY) {
    return canHitWithAxe(platformX, platformY, targetedObject.getTarget());
  }
  
  // Can player axe specified GameObject when standing on specified currentTile?
  boolean canHitWithAxe(final int platformX, final int platformY, 
      final GameObject obj) {
    
    final int ty = platformY << 4;
    final int dx = botState.getPlayerX() < obj.x ? 2 : -2;
    int x = (platformX << 4) + 8;
    for(int i = Axe.YS.length - 1; i >= 0; --i, x += dx) {
      final int y = ty - Axe.YS[i];
      if (x >= obj.x1 && x <= obj.x2 && y >= obj.y1 && y <= obj.y2) {
        return true;
      } else if (dx > 0) {
        if (x > obj.x2) {
          return false;
        }         
      } else {
        if (x < obj.x1) {
          return false;
        }
      }
    }
    
    return false;
  }
  
  // Can player axe specified GameObject when standing on specified currentTile?
  public boolean canHitWithAxe(final int platformX, final int platformY,
                               final int offsetX, final int offsetY, final GameObject obj) {
    
    final int ty = platformY << 4;
    final int dx = botState.getPlayerX() < (obj.x + offsetX) ? 2 : -2;
    int x = (platformX << 4) + 8;
    for(int i = Axe.YS.length - 1; i >= 0; --i, x += dx) {
      final int y = ty - Axe.YS[i];
      if (x >= obj.x1 + offsetX && x <= obj.x2 + offsetX 
          && y >= obj.y1 + offsetY && y <= obj.y2 + offsetY) {
        return true;
      } else if (dx > 0) {
        if (x > obj.x2 + offsetX) {
          return false;
        }         
      } else {
        if (x < obj.x1 + offsetX) {
          return false;
        }
      }
    }
    
    return false;
  }
  
  public int getWhipRadius() {
    return WHIPS[botState.getWhipLength()][0].getRadius();
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
      if (pauseDelay > 0) {
        --pauseDelay;
      } else {
        pauseDelay = 60;
        gamePad.pressStart();
      }
      return;
    } else {
      pauseDelay = 0;
    }

    if (!gameState.isPlaying() || gameState.getCurrentLevel() == null || gameState.getCurrentSubstage() == null) {
      botState.setCurrentStrategy(null);
      targetedObject = TargetedObject
              .builder()
              .target(null)
              .targetType(null)
              .coordinates(Coordinates
                      .builder()
                      .x(-512)
                      .y(-512)
                      .build())
              .build();
      return;
    } 

    if (botState.getJumpDelay() > 0) {
      botState.setJumpDelay(botState.getJumpDelay() - 1);
    }

    if (gameState.getWeaponDelay() > 0) {
      gameState.setWeaponDelay(gameState.getWeaponDelay() - 1);;
    }

    botState.setAvoidX(AVOID_X_RESET);
    gameState.getCurrentSubstage().pickStrategy(targetedObject);
    if (botState.getCurrentStrategy() != null) {
      if (entryDelay > 0) {
        --entryDelay;
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