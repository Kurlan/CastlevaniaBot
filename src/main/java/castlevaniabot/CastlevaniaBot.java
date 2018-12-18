package castlevaniabot;

import castlevaniabot.control.GamePad;
import castlevaniabot.control.PlayerController;
import castlevaniabot.level.Level;
import castlevaniabot.model.creativeelements.Axe;
import castlevaniabot.model.creativeelements.Bone;
import castlevaniabot.model.creativeelements.BoneTowerSegment;
import castlevaniabot.model.creativeelements.MedusaHead;
import castlevaniabot.model.creativeelements.MovingPlatform;
import castlevaniabot.model.creativeelements.RedBat;
import castlevaniabot.model.creativeelements.RedBones;
import castlevaniabot.model.creativeelements.Sickle;
import castlevaniabot.model.creativeelements.Whip;
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
import nintaco.api.Colors;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.control.PlayerController.WEAPON_DELAY;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
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
import static castlevaniabot.model.gameelements.TileType.BACK_STAIRS;
import static castlevaniabot.model.gameelements.TileType.FORWARD_STAIRS;
import static castlevaniabot.model.gameelements.TileType.isBack;
import static castlevaniabot.model.gameelements.TileType.isForward;
import static castlevaniabot.model.gameelements.TileType.isStairsPlatform;
import static java.lang.Math.abs;
import static java.lang.Math.min;

public class CastlevaniaBot {
  
  private static final int[][] WHIP_HEIGHT_AND_DELAY = {
    { 13, 21 }, { 19, 19 }, { 25, 17 }, { 31, 14 }, { 36, 4 },
  };
  
  static final int MAX_DISTANCE = 0xFFFF;
  static final int MAX_HEIGHT   = 0xF;
  
  private static final int AVOID_X_RESET = -512;
  private static final int RED_BONES_THRESHOLD = 120;

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
  
  final BoneTowerSegment[] boneTowerSegments = new BoneTowerSegment[16];
  public int boneTowerSegmentsCount;
  
  Bone[] bones0 = new Bone[64];
  Bone[] bones1 = new Bone[64];
  public int boneCount0;
  public int boneCount1;
  
  public RedBones[] redBones0 = new RedBones[64];
  RedBones[] redBones1 = new RedBones[64];
  public int redBonesCount0;
  public int redBonesCount1;
  
  RedBat[] redBats0 = new RedBat[64];
  RedBat[] redBats1 = new RedBat[64];
  public int redBatsCount0;
  public int redBatsCount1;
  
  MedusaHead[] medusaHeads0 = new MedusaHead[64];
  MedusaHead[] medusaHeads1 = new MedusaHead[64];
  public int medusaHeadsCount0;
  public int medusaHeadsCount1;

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

  boolean atBottomOfStairs;
  public boolean atTopOfStairs;
  public boolean kneeling;

  public boolean canJump;

  public int hearts;
  public int shot;

  int entryDelay;
  int pauseDelay;
  public int weapon = NONE;

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
      this.gameState = new GameState();
      gameState.setGameObjects(gameObjects);
      this.allStrategies = new AllStrategies(this,botState, gameState, playerController);
      this.levels = levels;
      this.gamePad = gamePad;
      this.playerController = playerController;

    try {
      for(int i = boneTowerSegments.length - 1; i >= 0; --i) {
        boneTowerSegments[i] = new BoneTowerSegment();
      }
      for(int i = bones0.length - 1; i >= 0; --i) {
        bones0[i] = new Bone();
        bones1[i] = new Bone();
      }
      for(int i = redBones0.length - 1; i >= 0; --i) {
        redBones0[i] = new RedBones();
        redBones1[i] = new RedBones();
      }
      for(int i = redBats0.length - 1; i >= 0; --i) {
        redBats0[i] = new RedBat();
        redBats1[i] = new RedBat();
      }
      for(int i = medusaHeads0.length - 1; i >= 0; --i) {
        medusaHeads0[i] = new MedusaHead();
        medusaHeads1[i] = new MedusaHead();
      }      
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
    weapon = api.readCPU(WEAPON);
    botState.setWhipLength(api.readCPU(WHIP_LENGTH));
    hearts = api.readCPU(HEARTS);
    shot = api.readCPU(SHOT) + 1;
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

    atBottomOfStairs = isAtBottomOfStairs();
    atTopOfStairs = isAtTopOfStairs();
    canJump = !gameState.isWeaponing() && !botState.isOnStairs() && !kneeling && botState.isOnPlatform()
        && botState.getJumpDelay() == 0;

    gameState.getCurrentLevel().readGameObjects(this, gameState);
    _substage.readGameObjects();
  }
  
  public void addBoneTowerSegment(final int x, final int y) {
    for(int i = boneTowerSegmentsCount - 1; i >= 0; --i) {
      final BoneTowerSegment s = boneTowerSegments[i];
      if (x == s.x) {
        if (y == s.y + 16) {
          return;
        } else if (y == s.y - 16) {
          s.y -= 16;
          return;
        }
      }
    }
    final BoneTowerSegment s = boneTowerSegments[boneTowerSegmentsCount++];
    s.x = x;
    s.y = y;
  }
  
  public void buildBoneTowers() {
    for(int i = boneTowerSegmentsCount - 1; i >= 0; --i) {
      final BoneTowerSegment s = boneTowerSegments[i];
      addGameObject(GameObjectType.BONE_TOWER, s.x, s.y, false, true);
    }
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
      addGameObject(GameObjectType.SICKLE, s.x, s.y, false, true);
    }
    
    final Sickle[] temp = sickles0;
    sickles0 = sickles1;
    sickles1 = temp;
    sickleCount0 = sickleCount1;
    sickleCount1 = 0;    
  }
  
  void addMedusaHead(final int x, final int y) {
    
    final MedusaHead head = medusaHeads1[medusaHeadsCount1++];
    
    head.x = x + 8 + gameState.getCameraX();
    head.y_32 = head.y_16 = head.y0 = head.y = y + 16;
    head.s = head.t = 0;
    head.sameYs = 1;
    head.left = true;
  }

  void buildMedusaHeads() {
    
    for(int i = medusaHeadsCount1 - 1; i >= 0; --i) {
      final MedusaHead h1 = medusaHeads1[i];
      for(int j = medusaHeadsCount0 - 1; j >= 0; --j) {
        final MedusaHead h0 = medusaHeads0[j];
        if (abs(h1.x - h0.x) <= 8 && abs(h1.y - h0.y) <= 8) {

          h1.left = h1.x < h0.x;
          
          h1.t = h0.t + 1;          
          if (h1.t >= MedusaHead.WAVE.length) {
            h1.t = 0;
          }

          h1.y_16 = h0.y_16;
          h1.y_32 = h0.y_32;                    
          h1.s = h0.s + 1;
          if ((h1.s & 0xF) == 0) {
            h1.y_32 = h1.y_16;
            h1.y_16 = h1.y;
          }
                
          h1.x0 = h0.x0;
          h1.y0 = h0.y0;              
          if (h1.y == h0.y) {
            h1.sameYs = h0.sameYs + 1;
            if (h1.sameYs >= 3) {
              if (h1.s > 32) {
                if (h1.y < h1.y_32) {
                  h1.t = 3;
                  h1.x0 = h1.left ? (h1.x + 4) : (h1.x - 4);
                  h1.y0 = h1.y + 32;
                } else {
                  h1.t = 53;
                  h1.x0 = h1.left ? (h1.x + 66) : (h1.x - 66);
                  h1.y0 = h1.y - 32;
                }
              } else {
                h1.t = 3;
                h1.x0 = h1.left ? (h1.x + 4) : (h1.x - 4);
                h1.y0 = h1.y + 32;
              }
            }
          }
          break;
        }
      }
    }
    final MedusaHead[] temp = medusaHeads0;
    medusaHeads0 = medusaHeads1;
    medusaHeads1 = temp;
    medusaHeadsCount0 = medusaHeadsCount1;
    medusaHeadsCount1 = 0;
  } 
  
  public MedusaHead getMedusaHead(final GameObject head) {
    switch(medusaHeadsCount0) {
      case 0:
        return null;
      case 1:
        return medusaHeads0[0];
      default: 
        for(int i = medusaHeadsCount0 - 1; i >= 0; --i) {
          final MedusaHead medusaHead = medusaHeads0[i];
          if (head.x == medusaHead.x && head.y == medusaHead.y) {
            return medusaHead;
          }
        }
    }
    return null;
  }  
  
  public void addRedBat(final int x, final int y) {
    
    final RedBat bat = redBats1[redBatsCount1++];
    
    bat.x = x + 8 + gameState.getCameraX();
    bat.y_32 = bat.y_16 = bat.y0 = bat.y = y + 16;
    bat.s = bat.t = 0;
    bat.sameYs = 1;
    bat.left = true;
  }  
  
  public void buildRedBats() {
    
    for(int i = redBatsCount1 - 1; i >= 0; --i) {
      final RedBat b1 = redBats1[i];
      for(int j = redBatsCount0 - 1; j >= 0; --j) {
        final RedBat b0 = redBats0[j];
        if (abs(b1.x - b0.x) <= 8 && abs(b1.y - b0.y) <= 8) {

          b1.left = b1.x < b0.x;
          
          b1.t = b0.t + 1;          
          if (b1.t >= RedBat.WAVE.length) {
            b1.t = 0;
          }

          b1.y_16 = b0.y_16;
          b1.y_32 = b0.y_32;                    
          b1.s = b0.s + 1;
          if ((b1.s & 0xF) == 0) {
            b1.y_32 = b1.y_16;
            b1.y_16 = b1.y;
          }
                
          b1.x0 = b0.x0;
          b1.y0 = b0.y0;              
          if (b1.y == b0.y) {
            b1.sameYs = b0.sameYs + 1;
            if (b1.sameYs >= 5) {
              if (b1.s > 32) {
                if (b1.y < b1.y_32) {
                  b1.t = 11;
                  b1.x0 = b1.left ? (b1.x + 12) : (b1.x - 12);
                  b1.y0 = b1.y + 7;
                } else {
                  b1.t = 61;
                  b1.x0 = b1.left ? (b1.x + 69) : (b1.x - 69);
                  b1.y0 = b1.y - 7;
                }
              } else {
                b1.t = 11;
                b1.x0 = b1.left ? (b1.x + 12) : (b1.x - 12);
                b1.y0 = b1.y + 7;
              }
            }
          }
          break;
        }
      }
    }
    final RedBat[] temp = redBats0;
    redBats0 = redBats1;
    redBats1 = temp;
    redBatsCount0 = redBatsCount1;
    redBatsCount1 = 0;
  }  
  
  public RedBat getRedBat(final GameObject bat) {
    switch(redBatsCount0) {
      case 0:
        return null;
      case 1:
        return redBats0[0];
      default: 
        for(int i = redBatsCount0 - 1; i >= 0; --i) {
          final RedBat redBat = redBats0[i];
          if (bat.x == redBat.x && bat.y == redBat.y) {
            return redBat;
          }
        }
    }
    return null;
  }
  
  public void addRedBones(final int x, final int y) {
    
    final RedBones bones = redBones1[redBonesCount1++];
    
    bones.x = x + 8 + gameState.getCameraX();
    bones.y = y + 16;
    bones.time = abs(botState.getPlayerX() - bones.x) > 96 ? RED_BONES_THRESHOLD : 0;
  }
  
  public void buildRedBones() {
    
    for(int i = redBonesCount1 - 1; i >= 0; --i) {
      final RedBones b1 = redBones1[i];
      for(int j = redBonesCount0 - 1; j >= 0; --j) {
        final RedBones b0 = redBones0[j];
        if (abs(b1.x - b0.x) <= 4 && abs(b1.y - b0.y) <= 4) {
          b1.time = b0.time + 1;
          if (b1.time >= RED_BONES_THRESHOLD) {
            addGameObject(GameObjectType.RED_BONES, b1.x - 8 - gameState.getCameraX(),
                b1.y - 16, false, true);
          }
          break;
        }
      }
    }
    final RedBones[] temp = redBones0;
    redBones0 = redBones1;
    redBones1 = temp;
    redBonesCount0 = redBonesCount1;
    redBonesCount1 = 0;
  }
  
  public void addBone(final GameObjectType type, int x, int y) {
    
    final Bone bone = bones1[boneCount1++];
    
    x += type.xOffset + gameState.getCameraX();
    y += type.yOffset;
        
    bone.x1 = x - type.xRadius;
    bone.x2 = x + type.xRadius;
    bone.y1 = y - type.yRadius;
    bone.y2 = y + type.yRadius;

    bone.x = x;
    bone.y = y;
  }
  
  public void buildBones() {
    for(int i = boneCount1 - 1; i >= 0; --i) {
      final Bone b1 = bones1[i];
      b1.vx = b1.vy = 0;
      for(int j = boneCount0 - 1; j >= 0; --j) {
        final Bone b0 = bones0[j];
        if (abs(b1.x1 - b0.x1) <= 8 && abs(b1.y1 - b0.y1) <= 8) {
          b1.vx = b1.x1 - b0.x1;
          b1.vy = b1.y1 - b0.y1;
          if (b1.vx < 0) {
            b1.left = true;
          } else if (b1.vx > 0) {
            b1.left = false;
          }
          break;
        }
      }
    }
    final Bone[] temp = bones0;
    bones0 = bones1;
    bones1 = temp;
    boneCount0 = boneCount1;
    boneCount1 = 0;
  }
  
  public Bone getHarmfulBone() {
    for(int i = boneCount0 - 1; i >= 0; --i) {
      final Bone bone = bones0[i];
      if (bone.vy > 0 && bone.y1 <= botState.getPlayerY() && bone.x2 >= botState.getPlayerX() - 32
          && bone.x1 <= botState.getPlayerX() + 32) {
        return bone;
      }
    }
    return null;
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
      addGameObject(GameObjectType.DRACULA_HEAD, draculaHeadX, draculaHeadY, 
          draculaHeadLeft, true);
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
      addGameObject(GameObjectType.CRYSTAL_BALL, crystalBallX, crystalBallY, 
          false, true);
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
    obj.distance = MAX_DISTANCE;
    for(int i = (botState.getWhipLength() == 2 ? 2 : 1); i > 0; --i) {
      final int px = cx - i;
      if (px >= 0) {
        final int height = map[cy][px].height;          
        if (height >= 1 && height <= 4) {
          final int py = cy + height;
          final int dist = mapRoutes.getDistance(px, py, currentTile);
          if (dist < MAX_DISTANCE) {
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
          if (dist < MAX_DISTANCE) {
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
  
  public void addGameObject(final GameObjectType type, int x, int y,
                            final boolean left, final boolean active) {
    
    final MapRoutes mapRoutes = gameState.getCurrentSubstage().getMapRoutes();
    final MapElement[][] map = mapRoutes.map;
    
    x += type.xOffset + gameState.getCameraX();
    if (x < 0) {
      return;
    } else if (x >= mapRoutes.pixelsWidth) {
      return;
    }
    
    final int Y = y + type.yOffset;
    y += type.height;
    if (y < 0) {
      return;
    } else if (y >= mapRoutes.pixelsHeight) {
      return;
    }
    
    final GameObject obj = gameState.getGameObjects()[gameState.getObjsCount()];
    obj.type = type;
    obj.x = x;
    obj.y = y;
    obj.distanceX = abs(x - botState.getPlayerX());
    obj.distanceY = abs(y - botState.getPlayerY());
    obj.left = left;
    obj.active = active;
    obj.playerFacing = botState.isPlayerLeft() ^ (botState.getPlayerX() < x);
    
    obj.x1 = x - type.xRadius;
    obj.x2 = x + type.xRadius;
    obj.y1 = Y - type.yRadius;
    obj.y2 = Y + type.yRadius;    
    
    if (type == GameObjectType.CANDLES) {
      obj.onPlatform = false;
      obj.distance = MAX_DISTANCE;      
      final int cy = (y >> 4) - 1;
      final int[] whipDistances = Whip.WHIP_DISTANCES[botState.getWhipLength() == 2 ? 1 : 0];
      
      for(int i = whipDistances.length - 1; i >= 0; --i) {        
        final int sx = obj.x - whipDistances[i];
        final int pxLeft;
        final int pxRight;
        if ((sx & 0xF) >= 8) {
          pxLeft = sx >> 4;
          pxRight = pxLeft + 1;
        } else {
          pxRight = sx >> 4;
          pxLeft = pxRight - 1;
        }
        if (pxLeft < 0 || pxRight < 0) {
          continue;
        }
        final int hLeft = map[cy][pxLeft].height;
        final int hRight = map[cy][pxRight].height;
        if (hLeft <= hRight && hLeft >= 1 && hLeft <= 4) {
          final int py = cy + hLeft;
          final int dist = mapRoutes.getDistance(pxLeft, py, currentTile);
          if (dist < MAX_DISTANCE) {
            if (dist < obj.distance) {
              obj.distance = dist;
              obj.platformX = pxLeft;
              obj.platformY = py;
              obj.active = whipDistances[i] > 16; // Grind with holy water
            }
            break;
          }
        }
      }
      for(int i = whipDistances.length - 1; i >= 0; --i) {        
        final int sx = obj.x + whipDistances[i];
        final int pxLeft;
        final int pxRight;
        if ((sx & 0xF) >= 8) {
          pxLeft = sx >> 4;
          pxRight = pxLeft + 1;
        } else {
          pxRight = sx >> 4;
          pxLeft = pxRight - 1;
        }
        if (pxLeft >= mapRoutes.width || pxRight >= mapRoutes.width) {
          continue;
        }
        final int hLeft = map[cy][pxLeft].height;
        final int hRight = map[cy][pxRight].height;
        if (hRight <= hLeft && hRight >= 1 && hRight <= 4) {
          final int py = cy + hRight;
          final int dist = mapRoutes.getDistance(pxRight, py, currentTile);
          if (dist < MAX_DISTANCE) {
            if (dist < obj.distance) {
              obj.distance = dist;
              obj.platformX = pxRight;
              obj.platformY = py;
              obj.active = whipDistances[i] > 16; // Grind with holy water
            }
            break;
          }
        }
      }
      if (obj.distance == MAX_DISTANCE) {
        final int dy = cy + 1;
        for(int i = whipDistances.length - 1; i >= 0; --i) {        
          final int sx = obj.x - whipDistances[i];
          final int pxLeft;
          final int pxRight;
          if ((sx & 0xF) >= 8) {
            pxLeft = sx >> 4;
            pxRight = pxLeft + 1;
          } else {
            pxRight = sx >> 4;
            pxLeft = pxRight - 1;
          }
          if (pxLeft < 0 || pxRight < 0) {
            continue;
          }
          final int hLeft = map[dy][pxLeft].height;
          final int hRight = map[dy][pxRight].height;
          if (hLeft <= hRight && hLeft >= 1 && hLeft <= 3) {
            final int py = dy + hLeft;
            final int dist = mapRoutes.getDistance(pxLeft, py, currentTile);
            if (dist < MAX_DISTANCE) {
              if (dist < obj.distance) {
                obj.distance = dist;
                obj.platformX = pxLeft;
                obj.platformY = py;
                obj.active = false;        // Whip only
              }
              break;
            }
          }
        }
        for(int i = whipDistances.length - 1; i >= 0; --i) {        
          final int sx = obj.x + whipDistances[i];
          final int pxLeft;
          final int pxRight;
          if ((sx & 0xF) >= 8) {
            pxLeft = sx >> 4;
            pxRight = pxLeft + 1;
          } else {
            pxRight = sx >> 4;
            pxLeft = pxRight - 1;
          }
          if (pxLeft >= mapRoutes.width || pxRight >= mapRoutes.width) {
            continue;
          }
          final int hLeft = map[dy][pxLeft].height;
          final int hRight = map[dy][pxRight].height;
          if (hRight <= hLeft && hRight >= 1 && hRight <= 3) {
            final int py = dy + hRight;
            final int dist = mapRoutes.getDistance(pxRight, py, currentTile);
            if (dist < MAX_DISTANCE) {
              if (dist < obj.distance) {
                obj.distance = dist;
                obj.platformX = pxRight;
                obj.platformY = py;
                obj.active = false;        // Whip only
              }
              break;
            }
          }
        }        
      }
    } else {     
      obj.supportX = x;
      obj.platformX = x >> 4;
      obj.platformY = y >> 4;      
      obj.onPlatform = playerController.isOnOrInPlatform(mapRoutes, x, y, currentTile);
      if (!obj.onPlatform) {
        if (obj.onPlatform == playerController.isOnOrInPlatform(mapRoutes, x - 4, y, currentTile)) {
          obj.supportX = x - 4;
          obj.platformX = (x - 4) >> 4;
          obj.platformY = y >> 4;
        } else if (obj.onPlatform = playerController.isOnOrInPlatform(mapRoutes, x + 4, y, currentTile)) {
          obj.supportX = x + 4;
          obj.platformX = (x + 4) >> 4;
          obj.platformY = y >> 4;
        } 
      }
      if (obj.onPlatform) {
        obj.distance = mapRoutes.getDistance(obj, currentTile);
      } else {
        final int height = map[obj.platformY][obj.platformX].height;
        if (height == MAX_HEIGHT) {
          obj.distance = MAX_DISTANCE;        
        } else {
          obj.platformY += height;
          obj.distance = mapRoutes.getDistance(obj, currentTile);
        }
      }
    }
    
    obj.distTier = ((0xFFF - min(0xFFF, obj.distance)) << 8) 
        | (0xFF - min(0xFF, obj.distanceX));

    gameState.setObjsCount(gameState.getObjsCount() + 1);
  }
  
  // Returns the whip delay after jumping or -1 if not in range.
  int isInJumpingWhipRange(final GameObject obj) {
    for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {      
      if (WHIPS[botState.getWhipLength()][0].inRange(obj, 0,
          WHIP_HEIGHT_AND_DELAY[i][0], botState)) {
        return WHIP_HEIGHT_AND_DELAY[i][1];
      }
    }
    return -1;
  }

  public boolean isInStandingWhipRange(final GameObject obj, final int xOffset,
                                       final int yOffset) {
    return WHIPS[botState.getWhipLength()][0].inRange(obj, xOffset, yOffset, botState);
  } 
  
  public boolean isInKneelingWhipRange(final GameObject obj, final int xOffset,
                                       final int yOffset) {
    return WHIPS[botState.getWhipLength()][1].inRange( obj, xOffset, yOffset, botState);
  }  
  
  // Returns the whip delay after jumping or -1 if not in range.
  int isInJumpingWhipRange(final GameObject obj, final int xOffset, 
      final int yOffset) {
    for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {      
      if (WHIPS[botState.getWhipLength()][0].inRange(obj, xOffset,
          yOffset + WHIP_HEIGHT_AND_DELAY[i][0], botState)) {
        return WHIP_HEIGHT_AND_DELAY[i][1];
      }
    }
    return -1;
  }

  public boolean isInStandingWhipRange(final GameObject obj) {
    return WHIPS[botState.getWhipLength()][0].inRange(obj, botState);
  } 
  
  public boolean isInKneelingWhipRange(final GameObject obj) {
    return WHIPS[botState.getWhipLength()][1].inRange(obj, botState);
  }  
  
  // Returns the whip delay after jumping or -1 if not in range.
  int isTargetInJumpingWhipRange() {
    for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {      
      if (WHIPS[botState.getWhipLength()][0].inRange(targetedObject.getTarget(), 0,
          WHIP_HEIGHT_AND_DELAY[i][0], botState)) {
        return WHIP_HEIGHT_AND_DELAY[i][1];
      }
    }
    return -1;
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

  boolean isObjectAbove(final int y) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.y1 <= y) {
        return true;
      }
    }
    
    return false;
  }  
  
  boolean isEnemyBelow(final int y) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type.enemy && obj.y2 >= y) {
        return true;
      }
    }
    
    return false;
  }

  boolean isEnemyAbove(final int y) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type.enemy && obj.y1 <= y) {
        return true;
      }
    }
    
    return false;
  }  
  
  boolean isTypeAbove(final GameObjectType type, final int y) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type == type && obj.y1 <= y) {
        return true;
      }
    }
    
    return false;
  }

  boolean isTypeBelow(final GameObjectType type, final int y) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type == type && obj.y2 >= y) {
        return true;
      }
    }
    
    return false;
  }

  boolean isTypeLeft(final GameObjectType type, final int x) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type == type && obj.x1 <= x) {
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
  
  boolean isTypeInRange(final GameObjectType type, final int x1, final int x2) {
    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type == type && obj.x1 <= x2 && obj.x2 >= x1) {
        return true;
      }
    }
    
    return false;
  }  
  
  int countTypeInBounds(final GameObjectType type, final int x1, final int y1, 
      final int x2, final int y2) {
    
    int count = 0;
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      if (obj.type == type && obj.x1 <= x2 && obj.x2 >= x1 && obj.y2 >= y1 
          && obj.y1 <= y2) {
        ++count;
      }
    }    
    return count;
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
  
  boolean isObjectInBounds(final GameObject obj, final int x1, final int y1, 
      final int x2, final int y2) {    
    return obj.x1 <= x2 && obj.x2 >= x1 && obj.y2 >= y1 && obj.y1 <= y2;
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
  
  // Use holy water if possible to grind for double and triple shots, else whip.
  public boolean grind() {
    if (!gameState.isWeaponing()) {
      gameState.setWeaponDelay(WEAPON_DELAY);
      if (!atBottomOfStairs && weapon == HOLY_WATER && hearts > 5 && shot < 3) {
        gamePad.pressUp();
        gamePad.pressB();
        return true;
      } else {
        gamePad.pressB();
        return false;
      }
    } else {
      return false;
    }
  }
  
  public void whipOrWeapon() {
    if (!gameState.isWeaponing()) {
      gameState.setWeaponDelay(WEAPON_DELAY);
      if (!atBottomOfStairs) {
        gamePad.pressUp();
      }
      gamePad.pressB();
    }
  }
  
  public boolean face(final GameObject obj) {
    if (obj.playerFacing) {
      return true;
    } else if (botState.isOnStairs() && botState.getPlayerY() >= 56 && botState.getPlayerY() <= 200) {
      gamePad.pressDown();
    } else if (botState.getPlayerX() < obj.x) {
      playerController.goRight(botState);
    } else {
      playerController.goLeft(botState);
    }
    return false;
  }
  
  boolean faceFlying(final GameObject obj) {
    if (obj.playerFacing) {
      return true;
    } else if (botState.isOnStairs() && botState.getPlayerY() >= 56 && botState.getPlayerY() <= 200) {
        gamePad.pressDown();
    } else {
      gameState.getCurrentSubstage().moveToward(obj);
    }
    return false;
  }  
  
  public boolean faceTarget() {
    if (targetedObject.getTarget().playerFacing) {
      return true;
    } else if (botState.isOnStairs() && botState.getPlayerY() >= 56 && botState.getPlayerY() <= 200) {
        gamePad.pressDown();
    } else {
      gameState.getCurrentSubstage().moveTowardTarget(targetedObject.getTarget());
    }
    return false;
  }
  
  public boolean faceFlyingTarget() {
    if (targetedObject.getTarget().playerFacing) {
      return true;
    } else if (botState.isOnStairs() && botState.getPlayerY() >= 56 && botState.getPlayerY() <= 200) {
        gamePad.pressDown();
    } else if (botState.getPlayerX() < targetedObject.getTarget().x) {
      playerController.goRight(botState);
    } else {
      playerController.goLeft(botState);
    }
    return false;
  }
  
  public int getWhipRadius() {
    return WHIPS[botState.getWhipLength()][0].getRadius();
  }
  
  private boolean isAtBottomOfStairs() {
    
    if (botState.isOnStairs() || !botState.isOnPlatform()) {
      return false;
    }
    
    final MapElement[][] map = gameState.getCurrentSubstage().mapRoutes.map;
    final int tileType = map[currentTile.getY() - 1][currentTile.getX()].tileType;
    return (tileType == BACK_STAIRS || (currentTile.getX() < gameState.getCurrentSubstage().mapRoutes.width - 1
        && map[currentTile.getY() - 1][currentTile.getX() + 1].tileType == FORWARD_STAIRS))
            || (tileType == FORWARD_STAIRS || (currentTile.getX() > 0
                && map[currentTile.getY() - 1][currentTile.getX() - 1].tileType == BACK_STAIRS));
  }
  
  private boolean isAtTopOfStairs() {
    
    if (botState.isOnStairs() || !botState.isOnPlatform()) {
      return false;
    }
    
    final MapElement[][] map = gameState.getCurrentSubstage().mapRoutes.map;
    final int tileType = map[currentTile.getY()][currentTile.getX()].tileType;
    return isStairsPlatform(tileType) || (currentTile.getX() < gameState.getCurrentSubstage().mapRoutes.width - 1
            && isBack(map[currentTile.getY()][currentTile.getX() + 1].tileType))
        || (currentTile.getX() > 0 && isForward(map[currentTile.getY()][currentTile.getX() - 1].tileType));
  }
  
  boolean isPlayerInRange(final int x1, final int x2) {
    return botState.getPlayerX() >= x1 && botState.getPlayerX() <= x2;
  }
  
  private void paintGameObjects() {
    api.setColor(Colors.YELLOW);
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      api.drawRect(obj.x1 - gameState.getCameraX(), obj.y1, obj.x2 - obj.x1 + 1,
          obj.y2 - obj.y1 + 1);
    }    
    for(int i = gameState.getObjsCount() - 1; i >= 0; --i) {
      final GameObject obj = gameState.getGameObjects()[i];
      api.drawRect(obj.x1 - gameState.getCameraX(), obj.y1, obj.x2 - obj.x1 + 1,
          obj.y2 - obj.y1 + 1);
    }
    api.setColor(Colors.CYAN);
    for(int i = redBonesCount0 - 1; i >= 0; --i) {
      final RedBones bones = redBones0[i]; 
      api.drawRect(bones.x - 8 - gameState.getCameraX(), bones.y - 16, 16, 16);
    }
    if (targetedObject.getTarget() != null) {
      api.setColor(Colors.RED);
      api.drawRect(targetedObject.getTarget().x1 - gameState.getCameraX(), targetedObject.getTarget().y1, targetedObject.getTarget().x2 - targetedObject.getTarget().x1 + 1,
              targetedObject.getTarget().y2 - targetedObject.getTarget().y1 + 1);
    }
    api.setColor(Colors.GREEN);

    for(int i = getGameState().getMovingPlatformsCount() - 1; i >= 0; --i) {
      final MovingPlatform p = getGameState().getMovingPlatforms()[i];
      api.drawRect(p.x1 - gameState.getCameraX(), p.y, p.x2 - p.x1 + 1, 8);
    }
  }
  
  private void printGameObject(final GameObjectType type) {
    for(int i = 0; i < gameState.getObjsCount(); ++i) {
      if (gameState.getGameObjects()[i].type == type) {
        System.out.print(gameState.getGameObjects()[i] + " ");
      }
    }
    System.out.println();
  }
  
  private void printGameObjects() {
    for(int i = redBonesCount0 - 1; i >= 0; --i) {
      System.out.print(redBones0[i] + " ");
    }
    for(int i = 0; i < gameState.getObjsCount(); ++i) {
      System.out.print(gameState.getGameObjects()[i] + " ");
    }
    if (targetedObject.getTarget() != null) {
      System.out.format("* %s", targetedObject.getTarget());
    }
    System.out.println();
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
