package castlevaniabot;

import castlevaniabot.level.Level;
import castlevaniabot.level.Level1;
import castlevaniabot.level.Level2;
import castlevaniabot.level.Level3;
import castlevaniabot.level.Level4;
import castlevaniabot.level.Level5;
import castlevaniabot.level.Level6;
import castlevaniabot.strategy.AxeKnightStrategy;
import castlevaniabot.strategy.AxeStrategy;
import castlevaniabot.strategy.BatDualPlatformsStrategy;
import castlevaniabot.strategy.BatMovingPlatformStrategy;
import castlevaniabot.strategy.BlackBatStrategy;
import castlevaniabot.strategy.BlockStrategy;
import castlevaniabot.strategy.BoneDragonStrategy;
import castlevaniabot.strategy.BoneStrategy;
import castlevaniabot.strategy.BoneTowerStrategy;
import castlevaniabot.strategy.BoomerangDeathStrategy;
import castlevaniabot.strategy.CandlesStrategy;
import castlevaniabot.strategy.CookieMonsterStrategy;
import castlevaniabot.strategy.CrusherStrategy;
import castlevaniabot.strategy.DeathHallHolyWaterStrategy;
import castlevaniabot.strategy.DraculaStrategy;
import castlevaniabot.strategy.EagleStrategy;
import castlevaniabot.strategy.FireColumnStrategy;
import castlevaniabot.strategy.FireballStrategy;
import castlevaniabot.strategy.FishmanStrategy;
import castlevaniabot.strategy.FleamanStrategy;
import castlevaniabot.strategy.FrankensteinStrategy;
import castlevaniabot.strategy.GetCrystalBallStrategy;
import castlevaniabot.strategy.GetItemStrategy;
import castlevaniabot.strategy.GhostStrategy;
import castlevaniabot.strategy.GhoulStrategy;
import castlevaniabot.strategy.GiantBatStrategy;
import castlevaniabot.strategy.GotCrystalBallStrategy;
import castlevaniabot.strategy.HolyWaterDeathStrategy;
import castlevaniabot.strategy.JumpMovingPlatformStrategy;
import castlevaniabot.strategy.MedusaHeadStrategy;
import castlevaniabot.strategy.MedusaHeadsPitsStrategy;
import castlevaniabot.strategy.MedusaHeadsWalkStrategy;
import castlevaniabot.strategy.MedusaStrategy;
import castlevaniabot.strategy.MummiesStrategy;
import castlevaniabot.strategy.NoJumpMovingPlatformStrategy;
import castlevaniabot.strategy.PantherStrategy;
import castlevaniabot.strategy.PhantomBatStrategy;
import castlevaniabot.strategy.RavenStrategy;
import castlevaniabot.strategy.RedBatDamageBoostStrategy;
import castlevaniabot.strategy.RedBatStrategy;
import castlevaniabot.strategy.RedBonesStrategy;
import castlevaniabot.strategy.RedSkeletonStrategy;
import castlevaniabot.strategy.SickleStrategy;
import castlevaniabot.strategy.SkeletonWallStrategy;
import castlevaniabot.strategy.SnakeStrategy;
import castlevaniabot.strategy.SpearKnightStrategy;
import castlevaniabot.strategy.Strategy;
import castlevaniabot.strategy.UseWeaponStrategy;
import castlevaniabot.strategy.WaitStrategy;
import castlevaniabot.strategy.WhipStrategy;
import castlevaniabot.strategy.WhiteSkeletonStrategy;
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
import nintaco.api.ApiSource;
import nintaco.api.Colors;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.Addresses.CAMERA_X;
import static castlevaniabot.Addresses.HEARTS;
import static castlevaniabot.Addresses.KNEELING;
import static castlevaniabot.Addresses.MODE;
import static castlevaniabot.Addresses.ON_STAIRS;
import static castlevaniabot.Addresses.PAUSED;
import static castlevaniabot.Addresses.PLAYER_FACING;
import static castlevaniabot.Addresses.PLAYER_IMAGE;
import static castlevaniabot.Addresses.PLAYER_X;
import static castlevaniabot.Addresses.PLAYER_Y;
import static castlevaniabot.Addresses.PLAYING;
import static castlevaniabot.Addresses.SHOT;
import static castlevaniabot.Addresses.STAGE;
import static castlevaniabot.Addresses.SUBSTAGE;
import static castlevaniabot.Addresses.WEAPON;
import static castlevaniabot.Addresses.WEAPONING;
import static castlevaniabot.Addresses.WHIP_LENGTH;
import static castlevaniabot.GameObjectType.DESTINATION;
import static castlevaniabot.Operations.GO_DOWN_STAIRS;
import static castlevaniabot.Operations.GO_UP_STAIRS;
import static castlevaniabot.Operations.WALK_CENTER_LEFT_JUMP;
import static castlevaniabot.Operations.WALK_CENTER_RIGHT_JUMP;
import static castlevaniabot.Operations.WALK_LEFT;
import static castlevaniabot.Operations.WALK_LEFT_EDGE_LEFT_JUMP;
import static castlevaniabot.Operations.WALK_LEFT_EDGE_RIGHT_JUMP;
import static castlevaniabot.Operations.WALK_LEFT_MIDDLE_LEFT_JUMP;
import static castlevaniabot.Operations.WALK_LEFT_MIDDLE_RIGHT_JUMP;
import static castlevaniabot.Operations.WALK_RIGHT;
import static castlevaniabot.Operations.WALK_RIGHT_EDGE_LEFT_JUMP;
import static castlevaniabot.Operations.WALK_RIGHT_EDGE_RIGHT_JUMP;
import static castlevaniabot.Operations.WALK_RIGHT_MIDDLE_LEFT_JUMP;
import static castlevaniabot.Operations.WALK_RIGHT_MIDDLE_RIGHT_JUMP;
import static castlevaniabot.TileType.BACK_PLATFORM;
import static castlevaniabot.TileType.BACK_STAIRS;
import static castlevaniabot.TileType.FORWARD_PLATFORM;
import static castlevaniabot.TileType.FORWARD_STAIRS;
import static castlevaniabot.TileType.isBack;
import static castlevaniabot.TileType.isForward;
import static castlevaniabot.TileType.isStairsPlatform;
import static castlevaniabot.Weapon.HOLY_WATER;
import static castlevaniabot.Weapon.NONE;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static nintaco.api.GamepadButtons.A;
import static nintaco.api.GamepadButtons.B;
import static nintaco.api.GamepadButtons.Down;
import static nintaco.api.GamepadButtons.Left;
import static nintaco.api.GamepadButtons.Right;
import static nintaco.api.GamepadButtons.Start;
import static nintaco.api.GamepadButtons.Up;

public class CastlevaniaBot {
  
  // Parabolic jump path
  public static final int[] ABSOLUTE_JUMP_YS = {
      0,  5, 10, 13, 16, 19, 22, 25, 27, 29, 31, 32, 33, 34, 35, 35, 
                     36, 36, 36, 36, 36, 36, 36, 36, 36, 
     35, 35, 34, 33, 32, 31, 29, 27, 25, 22, 19, 16, 13, 10,  5,  0, };
  
  // Parabolic jump velocities 
  public static final int[] DELTA_JUMP_YS = new int[ABSOLUTE_JUMP_YS.length - 1];
  static {
    for(int i = DELTA_JUMP_YS.length - 1; i >= 0; --i) {
      DELTA_JUMP_YS[i] = ABSOLUTE_JUMP_YS[i + 1] - ABSOLUTE_JUMP_YS[i];
    }
  } 
  
  // The change in height during a jump after a whip delay of 16 frames.
  private static final int[] JUMP_WHIP_OFFSETS = new int[DELTA_JUMP_YS.length];
  static {
    for(int i = JUMP_WHIP_OFFSETS.length - 1; i >= 0; --i) {
      int sum = 0;
      for(int j = 15; j >= 0; --j) {
        final int index = i - j;
        if (index > 0) {
          sum += DELTA_JUMP_YS[i];
        } else {
          sum += 8;
        }
      }
      JUMP_WHIP_OFFSETS[i] = sum;
    }
  }
  
  // For a given height, this is how long to delay whipping after a jump.
  public static final int[] JUMP_WHIP_DELAYS = new int[37];
  static {
    for(int i = 0; i <= 36; ++i) {
      int index = -1;
      for(int j = 16; j < ABSOLUTE_JUMP_YS.length; ++j) {
        if (ABSOLUTE_JUMP_YS[j] == i) {
          index = j;
          break;
        } else if (ABSOLUTE_JUMP_YS[j] < i) {
          if (abs(ABSOLUTE_JUMP_YS[j - 1] - i) < abs(ABSOLUTE_JUMP_YS[j] - i)) {
            index = j - 1;
          } else {
            index = j;
          }
          break;
        }
      }
      JUMP_WHIP_DELAYS[i] = index - 16;
    }
    JUMP_WHIP_DELAYS[36] = 7;
  }  
  
  private static final int[][] WHIP_HEIGHT_AND_DELAY = {
    { 13, 21 }, { 19, 19 }, { 25, 17 }, { 31, 14 }, { 36, 4 },
  }; 

  static final int[][] HITBOX_RADII = {
    {  0,  0 }, // 00
    {  6, 14 }, // 01
    {  6,  6 }, // 02
    {  6, 14 }, // 03
    {  8,  8 }, // 04
    {  6,  6 }, // 05
    {  6,  6 }, // 06
    {  6,  6 }, // 07
    {  6, 14 }, // 08
    {  6,  3 }, // 09
    {  6, 14 }, // 0A
    {  6,  6 }, // 0B
    {  6,  6 }, // 0C
    {  6,  6 }, // 0D
    {  6, 14 }, // 0E
    {  6,  6 }, // 0F
    {  6, 14 }, // 10
    { 10,  6 }, // 11
    { 14, 14 }, // 12
    {  6, 14 }, // 13
    {  6,  6 }, // 14
    {  1,  2 }, // 15
    {  1,  2 }, // 16
    {  6,  6 }, // 17
    { 12, 22 }, // 18
    { 20, 10 }, // 19
    {  8, 22 }, // 1A
    { 14, 14 }, // 1B
    {  8, 22 }, // 1C
    { 16, 24 }, // 1D
    { 20, 10 }, // 1E
    {  6,  6 }, // 1F
    {  2,  2 }, // 20
    {  2,  2 }, // 21
    {  2,  4 }, // 22
    {  2,  2 }, // 23
    {  4,  4 }, // 24
    {  0,  0 }, // 25
    {  2,  2 }, // 26
    {  4,  4 }, // 27
    {  6,  6 }, // 28
    {  6,  6 }, // 29
    {  6,  6 }, // 2A
    {  6,  6 }, // 2B
    {  6,  6 }, // 2C
    {  6,  6 }, // 2D
    {  6,  6 }, // 2E
    {  6,  6 }, // 2F 
    
    {  6,  8 }, // 30 (added for Dracula head)
  };
  
  private static final Whip[][] whips = {
    { new Whip(24, -2, 16, 6), new Whip(24, 7, 16, 6) },
    { new Whip(26, -2, 18, 8), new Whip(26, 7, 18, 8) },
    { new Whip(34, -2, 26, 8), new Whip(34, 7, 26, 8) },
  };
  
  static final int MAX_DISTANCE = 0xFFFF;
  static final int MAX_HEIGHT   = 0xF;
  
  static final int WEAPON_DELAY = 16;
  
  private static final int AVOID_X_RESET = -512;
  private static final int RED_BONES_THRESHOLD = 120;
  
  final Level LEVEL_1 = new Level1(this);
  final Level LEVEL_2 = new Level2(this);
  final Level LEVEL_3 = new Level3(this);
  final Level LEVEL_4 = new Level4(this);
  final Level LEVEL_5 = new Level5(this);
  final Level LEVEL_6 = new Level6(this);
  
  final Substage0000 SUBSTAGE_0000 = new Substage0000(this);
  final Substage0100 SUBSTAGE_0100 = new Substage0100(this);
  final Substage0200 SUBSTAGE_0200 = new Substage0200(this);
  public final Substage0201 SUBSTAGE_0201 = new Substage0201(this);
  final Substage0300 SUBSTAGE_0300 = new Substage0300(this);
  final Substage0400 SUBSTAGE_0400 = new Substage0400(this);
  final Substage0401 SUBSTAGE_0401 = new Substage0401(this);
  final Substage0500 SUBSTAGE_0500 = new Substage0500(this);
  final Substage0501 SUBSTAGE_0501 = new Substage0501(this);
  final Substage0600 SUBSTAGE_0600 = new Substage0600(this);
  final Substage0601 SUBSTAGE_0601 = new Substage0601(this);
  final Substage0700 SUBSTAGE_0700 = new Substage0700(this);
  final Substage0701 SUBSTAGE_0701 = new Substage0701(this);
  final Substage0800 SUBSTAGE_0800 = new Substage0800(this);
  final Substage0801 SUBSTAGE_0801 = new Substage0801(this);
  public final Substage0900 SUBSTAGE_0900 = new Substage0900(this);
  final Substage1000 SUBSTAGE_1000 = new Substage1000(this);
  final Substage1100 SUBSTAGE_1100 = new Substage1100(this);
  final Substage1200 SUBSTAGE_1200 = new Substage1200(this);
  final Substage1300 SUBSTAGE_1300 = new Substage1300(this);
  final Substage1301 SUBSTAGE_1301 = new Substage1301(this);
  final Substage1400 SUBSTAGE_1400 = new Substage1400(this);
  final Substage1401 SUBSTAGE_1401 = new Substage1401(this);
  final Substage1500 SUBSTAGE_1500 = new Substage1500(this);
  public final Substage1501 SUBSTAGE_1501 = new Substage1501(this);
  final Substage1600 SUBSTAGE_1600 = new Substage1600(this);
  final Substage1700 SUBSTAGE_1700 = new Substage1700(this);
  final Substage1701 SUBSTAGE_1701 = new Substage1701(this);
  final Substage1800 SUBSTAGE_1800 = new Substage1800(this);
  final Substage1801 SUBSTAGE_1801 = new Substage1801(this);

  public final AxeStrategy AXE = new AxeStrategy(this);
  public final AxeKnightStrategy AXE_KNIGHT = new AxeKnightStrategy(this);
  public final BatMovingPlatformStrategy BAT_MOVING_PLATFORM
      = new BatMovingPlatformStrategy(this);
  public final BatDualPlatformsStrategy BAT_DUAL_PLATFORMS
      = new BatDualPlatformsStrategy(this);
  public final BoneDragonStrategy BONE_DRAGON = new BoneDragonStrategy(this);
  public final BlackBatStrategy BLACK_BAT = new BlackBatStrategy(this);
  public final BlockStrategy BLOCK = new BlockStrategy(this);
  public final BoomerangDeathStrategy BOOMERANG_DEATH
      = new BoomerangDeathStrategy(this);
  public final BoneStrategy BONE = new BoneStrategy(this);
  public final BoneTowerStrategy BONE_TOWER = new BoneTowerStrategy(this);
  public final CandlesStrategy CANDLES = new CandlesStrategy(this);
  public final CookieMonsterStrategy COOKIE_MONSTER
      = new CookieMonsterStrategy(this);
  public final CrusherStrategy CRUSHER
      = new CrusherStrategy(this);
  public final DeathHallHolyWaterStrategy DEATH_HALL_HOLY_WATER
      = new DeathHallHolyWaterStrategy(this);
  public final DraculaStrategy DRACULA = new DraculaStrategy(this);
  public final EagleStrategy EAGLE = new EagleStrategy(this);
  public final FireballStrategy FIREBALL = new FireballStrategy(this);
  public final FireColumnStrategy FIRE_COLUMN = new FireColumnStrategy(this);
  public final FishmanStrategy FISHMAN = new FishmanStrategy(this);
  public final FleamanStrategy FLEAMAN = new FleamanStrategy(this);
  public final FrankensteinStrategy FRANKENSTEIN
      = new FrankensteinStrategy(this);
  public final GetCrystalBallStrategy GET_CRYSTAL_BALL
      = new GetCrystalBallStrategy(this);
  public final GetItemStrategy GET_ITEM = new GetItemStrategy(this);
  public final GhostStrategy GHOST = new GhostStrategy(this);
  public final GhoulStrategy GHOUL = new GhoulStrategy(this);
  public final GiantBatStrategy GIANT_BAT = new GiantBatStrategy(this);
  public final GotCrystalBallStrategy GOT_CRYSTAL_BALL
      = new GotCrystalBallStrategy(this);
  public final HolyWaterDeathStrategy HOLY_WATER_DEATH
      = new HolyWaterDeathStrategy(this);
  public final JumpMovingPlatformStrategy JUMP_MOVING_PLATFORM
      = new JumpMovingPlatformStrategy(this);
  public final MedusaStrategy MEDUSA = new MedusaStrategy(this);
  public final MedusaHeadStrategy MEDUSA_HEAD = new MedusaHeadStrategy(this);
  public final MedusaHeadsPitsStrategy MEDUSA_HEADS_PITS
      = new MedusaHeadsPitsStrategy(this);
  public final MedusaHeadsWalkStrategy MEDUSA_HEADS_WALK
      = new MedusaHeadsWalkStrategy(this);
  public final MummiesStrategy MUMMIES = new MummiesStrategy(this);
  public final NoJumpMovingPlatformStrategy NO_JUMP_MOVING_PLATFORM
      = new NoJumpMovingPlatformStrategy(this);
  public final PantherStrategy PANTHER = new PantherStrategy(this);
  public final PhantomBatStrategy PHANTOM_BAT = new PhantomBatStrategy(this);
  public final RavenStrategy RAVEN = new RavenStrategy(this);
  public final RedBatDamageBoostStrategy RED_BAT_DAMAGE_BOOST
      = new RedBatDamageBoostStrategy(this);
  public final RedBatStrategy RED_BAT = new RedBatStrategy(this);
  public final RedBonesStrategy RED_BONES = new RedBonesStrategy(this);
  public final RedSkeletonStrategy RED_SKELETON
      = new RedSkeletonStrategy(this);
  public final SickleStrategy SICKLE = new SickleStrategy(this);
  public final SkeletonWallStrategy SKELETON_WALL
      = new SkeletonWallStrategy(this);
  public final SnakeStrategy SNAKE = new SnakeStrategy(this);
  public final SpearKnightStrategy SPEAR_KNIGHT
      = new SpearKnightStrategy(this);
  public final UseWeaponStrategy USE_WEAPON = new UseWeaponStrategy(this);
  public final WaitStrategy WAIT = new WaitStrategy(this);
  public final WhiteSkeletonStrategy WHITE_SKELETON
      = new WhiteSkeletonStrategy(this);
  public final WhipStrategy WHIP = new WhipStrategy(this);
  
  private final API api = ApiSource.getAPI();

  Level level;
  public Substage substage;
  public Strategy strategy;
  public GameObject target;
  public GameObjectType targetType;
  public int targetX = -512;
  public int targetY = -512;
  
  public final GameObject[] gameObjects = new GameObject[128];
  public int objsCount;
  
  public final MovingPlatform[] movingPlatforms = new MovingPlatform[16];
  public int movingPlatformsCount;
  
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
  
  public final Map<String, MapRoutes> allMapRoutes = new HashMap<>();
  
  boolean playing;
  public boolean onStairs;
  public boolean onPlatform;
  boolean overHangingLeft;
  boolean overHangingRight;
  boolean atBottomOfStairs;
  public boolean atTopOfStairs;
  public boolean playerLeft;
  public boolean kneeling;
  boolean paused;
  public boolean weaponing;
  public boolean canJump;
  int mode;
  public int stageNumber;
  int substageNumber;
  public int playerX;
  public int playerY;
  public int tileX;
  public int tileY;
  int cameraX;
  public int whipLength;
  public int hearts;
  public int shot;
  int jumpDelay;
  int weaponDelay;
  int entryDelay;
  int pauseDelay;
  public int weapon = NONE;
  
  private int avoidX;
  
  public CastlevaniaBot() {
    try {
      for(int i = gameObjects.length - 1; i >= 0; --i) {
        gameObjects[i] = new GameObject();
      }
      for(int i = movingPlatforms.length - 1; i >= 0; --i) {
        movingPlatforms[i] = new MovingPlatform();
      }
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
      loadMaps();
    } catch(final Throwable t) {
      t.printStackTrace(); // Display construction errors to console
    }

    api.addFrameListener(this::renderFinished);
    api.addStatusListener(this::statusChanged);
    api.addActivateListener(this::apiEnabled);
    api.addDeactivateListener(this::apiDisabled);
    api.addStopListener(this::dispose);
    api.run();
  }
  
  private void loadMaps() throws Throwable {    
    try(final BufferedReader br = new BufferedReader(new InputStreamReader(
        CastlevaniaBot.class.getClassLoader().getResourceAsStream("maps/files.txt")))) {
      String name = null;
      while((name = br.readLine()) != null) {
        name = name.trim();
        if (name.length() > 0) {
          try(final DataInputStream in = new DataInputStream(
              new BufferedInputStream(CastlevaniaBot.class.getClassLoader().getResourceAsStream("maps/" + name)))) {
            loadMap(name, in);
          }
        }
      }
    }
  }
  
  private void loadMap(final String name, final DataInputStream in) 
      throws Throwable {    
    
    final int WIDTH = in.readInt();
    final int HEIGHT = in.readInt();

    final MapElement[][] map = new MapElement[HEIGHT][WIDTH];
    for(int y = HEIGHT - 1; y >= 0; --y) {
      for(int x = WIDTH - 1; x >= 0; --x) {
        map[y][x] = new MapElement(in.read());
      }
    }

    final int ROUTES = in.readInt();
    final int[][][][] routes = new int[HEIGHT][WIDTH][HEIGHT][WIDTH];
    for(int startY = HEIGHT - 1; startY >= 0; --startY) {
      for(int startX = WIDTH - 1; startX >= 0; --startX) {
        for(int endY = HEIGHT - 1; endY >= 0; --endY) {
          for(int endX = WIDTH - 1; endX >= 0; --endX) {
            routes[startY][startX][endY][endX] = (endX << 8) | (endY << 4);
            if (startX != endX || startY != endY) {
              routes[startY][startX][endY][endX] |= 0xFFFF0000;
            } 
          }
        }
      }
    }
    for(int i = ROUTES - 1; i >= 0; --i) {
      final int startX = in.read();
      final int endX = in.read();
      final int endYstartY = in.read();
      final int startY = endYstartY & 0x0F;
      final int endY = (endYstartY >> 4) & 0x0F;        
      routes[startY][startX][endY][endX] = in.readInt();        
    }
    allMapRoutes.put(name, new MapRoutes(this, name, WIDTH, HEIGHT, map, 
        routes));
  }
  
  private void apiEnabled() {
    System.out.println("API enabled");
  }
  
  private void apiDisabled() {
    System.out.println("API disabled");
    level = null;
    substage = null;
    strategy = null;
    target = null;
    targetType = null;
    targetX = targetY = -512;
  }
  
  private void dispose() {
    System.out.println("API stopped");
  }
  
  private void statusChanged(final String message) {
    System.out.format("Status message: %s%n", message);
  }
  
  private void readState() {
    
    mode = api.readCPU(MODE);
    final int play = api.readCPU(PLAYING);
    playing = (mode == Modes.PLAYING || mode == Modes.CRYSTAL_BALL)
        && (play == 0x06 || play == 0x01 || play == 0x00) 
            && api.readCPU(PLAYER_IMAGE) != 0x1C;
    if (!playing) {
      return;
    }
    stageNumber = api.readCPU(STAGE);
    substageNumber = api.readCPU(SUBSTAGE);    
    kneeling = api.readCPU(KNEELING) == 0x0A;    
    playerY = api.readCPU(PLAYER_Y) + (kneeling ? 12 : 16);
    playerX = api.readCPU16(PLAYER_X);
    playerLeft = api.readCPU(PLAYER_FACING) == 0x01;
    cameraX = api.readCPU16(CAMERA_X);
    weapon = api.readCPU(WEAPON);
    whipLength = api.readCPU(WHIP_LENGTH);
    hearts = api.readCPU(HEARTS);
    shot = api.readCPU(SHOT) + 1;
    onStairs = api.readCPU(ON_STAIRS) == 0x00;
    paused = api.readCPU(PAUSED) == 0x01;
    
    if (weaponDelay == 0) {
      final int _weaponing = api.readCPU(WEAPONING);
      weaponing = _weaponing != 0xFC && _weaponing != 0x00;
    } else {
      weaponing = true;
    }
    
   switch(stageNumber) {
      case  0:
      case  1:
      case  2:
      case  3: level = LEVEL_1; break;
      case  4:
      case  5:
      case  6: level = LEVEL_2; break;
      case  7:
      case  8:
      case  9: level = LEVEL_3; break;
      case 10:
      case 11:
      case 12: level = LEVEL_4; break;
      case 13:
      case 14:
      case 15: level = LEVEL_5; break;
      case 16:
      case 17:
      case 18: level = LEVEL_6; break;
    }
    
    Substage _substage = null;
    switch((stageNumber << 8) | substageNumber) {
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
    if (_substage != null && _substage != substage) {
      entryDelay = ThreadLocalRandom.current().nextInt(17);
      _substage.init();
    }
    substage = _substage;
    
    if (level == null || _substage == null) {
      return;
    }
    
    if (onStairs) {
      overHangingLeft = overHangingRight = onPlatform = false;
      tileX = playerX >> 4;
      tileY = playerY >> 4;
      final MapRoutes mapRoutes = substage.getMapRoutes();
      final MapElement[][] map = mapRoutes.map;
      if (!TileType.isStairs(map[tileY][tileX].tileType)) {
        if (tileX < mapRoutes.width - 1 
            && TileType.isStairs(map[tileY][tileX + 1].tileType)) {
          ++tileX;
        } else if (tileX > 0 
            && TileType.isStairs(map[tileY][tileX - 1].tileType)) {
          --tileX;
        } 
      }
    } else if (isOnPlatform(playerX, playerY)) {
      onPlatform = true;
      overHangingLeft = overHangingRight = false;
      tileX = playerX >> 4;
      tileY = playerY >> 4;
    } else if (isOnPlatform(playerX - 4, playerY)) {
      overHangingRight = onPlatform = true;
      overHangingLeft = false;
      tileX = (playerX - 4) >> 4;
      tileY = playerY >> 4;
    } else if (isOnPlatform(playerX + 4, playerY)) {
      overHangingLeft = onPlatform = true;
      overHangingRight = false;
      tileX = (playerX + 4) >> 4;
      tileY = playerY >> 4;
    } else {
      overHangingLeft = overHangingRight = onPlatform = false;
    }
    
    atBottomOfStairs = isAtBottomOfStairs();
    atTopOfStairs = isAtTopOfStairs();
    canJump = !weaponing && !onStairs && !kneeling && onPlatform 
        && jumpDelay == 0;
    
    level.readGameObjects();
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
    
    head.x = x + 8 + cameraX;
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
    
    bat.x = x + 8 + cameraX;
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
    
    bones.x = x + 8 + cameraX;
    bones.y = y + 16;
    bones.time = abs(playerX - bones.x) > 96 ? RED_BONES_THRESHOLD : 0;
  }
  
  public void buildRedBones() {
    
    for(int i = redBonesCount1 - 1; i >= 0; --i) {
      final RedBones b1 = redBones1[i];
      for(int j = redBonesCount0 - 1; j >= 0; --j) {
        final RedBones b0 = redBones0[j];
        if (abs(b1.x - b0.x) <= 4 && abs(b1.y - b0.y) <= 4) {
          b1.time = b0.time + 1;
          if (b1.time >= RED_BONES_THRESHOLD) {
            addGameObject(GameObjectType.RED_BONES, b1.x - 8 - cameraX, 
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
    
    x += type.xOffset + cameraX;    
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
      if (bone.vy > 0 && bone.y1 <= playerY && bone.x2 >= playerX - 32 
          && bone.x1 <= playerX + 32) {
        return bone;
      }
    }
    return null;
  }
  
  public void addMovingPlatformSegment(final int x, final int y) {
    for(int i = movingPlatformsCount - 1; i >= 0; --i) {
      final MovingPlatform m = movingPlatforms[i];
      if (m.y == y && abs(m.x1 - x) <= 24) {
        m.x1 = min(m.x1, x);
        m.x2 = max(m.x2, x);
        return;
      } 
    }
    final MovingPlatform m = movingPlatforms[movingPlatformsCount++];
    m.y = y;
    m.x1 = x;
    m.x2 = x;
  }
  
  public void buildMovingPlatforms() {
    for(int i = movingPlatformsCount - 1; i >= 0; --i) {
      final MovingPlatform m = movingPlatforms[i];
      m.x2 += 7;
      final int width = m.x2 - m.x1 + 1;
      if (width < 32) {
        if (m.x2 < 128) {
          m.x1 = m.x2 - 31;
        } else {
          m.x2 = m.x1 + 31;
        }
      }
      m.x1 += cameraX;
      m.x2 += cameraX;
    }
  }
  
  public MovingPlatform getMovingPlatform(final int minX, final int maxX) {
    for(int i = movingPlatformsCount - 1; i >= 0; --i) {
      final MovingPlatform platform = movingPlatforms[i];
      if (platform.x1 >= minX && platform.x2 <= maxX) {
        return platform;
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
    final MapRoutes mapRoutes = substage.getMapRoutes();
    
    if (x < 0 || y < 0 || x >= mapRoutes.pixelsWidth 
        || y >= mapRoutes.pixelsHeight) {
      return;
    }
    
    final GameObject obj = gameObjects[objsCount];
    obj.type = DESTINATION;
    obj.supportX = obj.x = x;
    obj.y = y;  
    obj.platformX = x >> 4;
    obj.platformY = y >> 4;      
    obj.onPlatform = true;    
    obj.distanceX = abs(x - playerX);
    obj.distanceY = abs(y - playerY);    
    obj.left = false;
    obj.active = false;
    obj.playerFacing = playerLeft ^ (playerX < x);
    obj.distance = mapRoutes.getDistance(obj);
    obj.x1 = x - 8;
    obj.x2 = x + 8;
    obj.y1 = obj.y2 = y;
    obj.distTier = ((0xFFF - min(0xFFF, obj.distance)) << 8) 
        | (0xFF - min(0xFF, obj.distanceX));    
    
    ++objsCount;
  }
  
  public void addBlock(int x, int y) {
    final MapRoutes mapRoutes = substage.getMapRoutes();
    final MapElement[][] map = mapRoutes.map;
    
    x += 8;
    y += 15;

    if (x < 0 || y < 0 || x >= mapRoutes.pixelsWidth 
        || y >= mapRoutes.pixelsHeight) {
      return;
    }
    
    final GameObject obj = gameObjects[objsCount];
    obj.type = GameObjectType.BLOCK;
    obj.supportX = obj.x = x;
    obj.y = y;  
    obj.distanceX = abs(x - playerX);
    obj.distanceY = abs(y - playerY);
    obj.left = false;
    obj.active = false;
    obj.playerFacing = playerLeft ^ (playerX < x);
    obj.x1 = x - 8;
    obj.x2 = x + 8;
    obj.y1 = y - 15;
    obj.y2 = y;
    
    obj.onPlatform = false;
    final int cx = x >> 4;
    final int cy = y >> 4;      
    obj.distance = MAX_DISTANCE;
    for(int i = (whipLength == 2 ? 2 : 1); i > 0; --i) {
      final int px = cx - i;
      if (px >= 0) {
        final int height = map[cy][px].height;          
        if (height >= 1 && height <= 4) {
          final int py = cy + height;
          final int dist = mapRoutes.getDistance(px, py);
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
    for(int i = (whipLength == 2 ? 2 : 1); i > 0; --i) {
      final int px = cx + i;
      if (px < mapRoutes.width) {
        final int height = map[cy][px].height;          
        if (height >= 1 && height <= 4) {
          final int py = cy + height;
          final int dist = mapRoutes.getDistance(px, py);
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
    
    ++objsCount;    
  }
  
  public void addGameObject(final GameObjectType type, int x, int y,
                            final boolean left, final boolean active) {
    
    final MapRoutes mapRoutes = substage.getMapRoutes();
    final MapElement[][] map = mapRoutes.map;
    
    x += type.xOffset + cameraX;
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
    
    final GameObject obj = gameObjects[objsCount];
    obj.type = type;
    obj.x = x;
    obj.y = y;
    obj.distanceX = abs(x - playerX);
    obj.distanceY = abs(y - playerY);
    obj.left = left;
    obj.active = active;
    obj.playerFacing = playerLeft ^ (playerX < x);
    
    obj.x1 = x - type.xRadius;
    obj.x2 = x + type.xRadius;
    obj.y1 = Y - type.yRadius;
    obj.y2 = Y + type.yRadius;    
    
    if (type == GameObjectType.CANDLES) {
      obj.onPlatform = false;
      obj.distance = MAX_DISTANCE;      
      final int cy = (y >> 4) - 1;
      final int[] whipDistances = Whip.WHIP_DISTANCES[whipLength == 2 ? 1 : 0];
      
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
          final int dist = mapRoutes.getDistance(pxLeft, py);
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
          final int dist = mapRoutes.getDistance(pxRight, py);
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
            final int dist = mapRoutes.getDistance(pxLeft, py);
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
            final int dist = mapRoutes.getDistance(pxRight, py);
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
      obj.onPlatform = isOnOrInPlatform(mapRoutes, x, y);
      if (!obj.onPlatform) {
        if (obj.onPlatform = isOnOrInPlatform(mapRoutes, x - 4, y)) {
          obj.supportX = x - 4;
          obj.platformX = (x - 4) >> 4;
          obj.platformY = y >> 4;
        } else if (obj.onPlatform = isOnOrInPlatform(mapRoutes, x + 4, y)) {
          obj.supportX = x + 4;
          obj.platformX = (x + 4) >> 4;
          obj.platformY = y >> 4;
        } 
      }
      if (obj.onPlatform) {
        obj.distance = mapRoutes.getDistance(obj);
      } else {
        final int height = map[obj.platformY][obj.platformX].height;
        if (height == MAX_HEIGHT) {
          obj.distance = MAX_DISTANCE;        
        } else {
          obj.platformY += height;
          obj.distance = mapRoutes.getDistance(obj);
        }
      }
    }
    
    obj.distTier = ((0xFFF - min(0xFFF, obj.distance)) << 8) 
        | (0xFF - min(0xFF, obj.distanceX)); 
    
    ++objsCount;
  }
  
  // Returns the whip delay after jumping or -1 if not in range.
  int isInJumpingWhipRange(final GameObject obj) {
    for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {      
      if (whips[whipLength][0].inRange(this, obj, 0, 
          WHIP_HEIGHT_AND_DELAY[i][0])) {
        return WHIP_HEIGHT_AND_DELAY[i][1];
      }
    }
    return -1;
  }

  public boolean isInStandingWhipRange(final GameObject obj, final int xOffset,
                                       final int yOffset) {
    return whips[whipLength][0].inRange(this, obj, xOffset, yOffset);     
  } 
  
  public boolean isInKneelingWhipRange(final GameObject obj, final int xOffset,
                                       final int yOffset) {
    return whips[whipLength][1].inRange(this, obj, xOffset, yOffset);
  }  
  
  // Returns the whip delay after jumping or -1 if not in range.
  int isInJumpingWhipRange(final GameObject obj, final int xOffset, 
      final int yOffset) {
    for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {      
      if (whips[whipLength][0].inRange(this, obj, xOffset, 
          yOffset + WHIP_HEIGHT_AND_DELAY[i][0])) {
        return WHIP_HEIGHT_AND_DELAY[i][1];
      }
    }
    return -1;
  }

  public boolean isInStandingWhipRange(final GameObject obj) {
    return whips[whipLength][0].inRange(this, obj);     
  } 
  
  public boolean isInKneelingWhipRange(final GameObject obj) {
    return whips[whipLength][1].inRange(this, obj);
  }  
  
  // Returns the whip delay after jumping or -1 if not in range.
  int isTargetInJumpingWhipRange() {
    for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {      
      if (whips[whipLength][0].inRange(this, target, 0, 
          WHIP_HEIGHT_AND_DELAY[i][0])) {
        return WHIP_HEIGHT_AND_DELAY[i][1];
      }
    }
    return -1;
  }
  
  public boolean isTargetInStandingWhipRange() {
    return whips[whipLength][0].inRange(this, target);     
  } 
  
  public boolean isTargetInKneelingWhipRange() {
    return whips[whipLength][1].inRange(this, target);
  } 
  
  // Returns the whip delay after jumping or -1 if not in range.
  public int isTargetInJumpingWhipRange(final int xOffset, final int yOffset) {
    for(int i = WHIP_HEIGHT_AND_DELAY.length - 1; i >= 0; --i) {      
      if (whips[whipLength][0].inRange(this, target, xOffset, 
          yOffset + WHIP_HEIGHT_AND_DELAY[i][0])) {
        return WHIP_HEIGHT_AND_DELAY[i][1];
      }
    }
    return -1;
  }  
  
  public boolean isTargetInStandingWhipRange(final int xOffset, final int yOffset) {
    return whips[whipLength][0].inRange(this, target, xOffset, yOffset);     
  } 
  
  public boolean isTargetInKneelingWhipRange(final int xOffset, final int yOffset) {
    return whips[whipLength][1].inRange(this, target, xOffset, yOffset);
  }
  
  public int countObjects(final GameObjectType type) {
    int count = 0;
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type == type) {
        ++count;
      }
    }    
    return count;
  }
  
  public GameObject getType(final GameObjectType type) {
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type == type) {
        return obj;
      }
    }    
    return null;
  }
  
  public boolean isUnderLedge() {
    
    if (tileY < 4) {
      return false;
    }
    
    final MapElement[][] map = substage.mapRoutes.map;
    if (map[tileY - 4][tileX].height == 0 || map[tileY - 3][tileX].height == 0){
      return true;
    }
    
    if (playerLeft) {
      return (tileX > 0) && (map[tileY - 4][tileX - 1].height == 0 
              || map[tileY - 3][tileX - 1].height == 0);
    } else {
      return (tileX < substage.mapRoutes.width - 1) 
          && (map[tileY - 4][tileX + 1].height == 0 
              || map[tileY - 3][tileX + 1].height == 0);
    }
  }  
  
  public boolean isTypePresent(final GameObjectType type) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type == type) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean isObjectBelow(final int y) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.y2 >= y) {
        return true;
      }
    }
    
    return false;
  }

  boolean isObjectAbove(final int y) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.y1 <= y) {
        return true;
      }
    }
    
    return false;
  }  
  
  boolean isEnemyBelow(final int y) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type.enemy && obj.y2 >= y) {
        return true;
      }
    }
    
    return false;
  }

  boolean isEnemyAbove(final int y) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type.enemy && obj.y1 <= y) {
        return true;
      }
    }
    
    return false;
  }  
  
  boolean isTypeAbove(final GameObjectType type, final int y) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type == type && obj.y1 <= y) {
        return true;
      }
    }
    
    return false;
  }

  boolean isTypeBelow(final GameObjectType type, final int y) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type == type && obj.y2 >= y) {
        return true;
      }
    }
    
    return false;
  }

  boolean isTypeLeft(final GameObjectType type, final int x) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type == type && obj.x1 <= x) {
        return true;
      }
    }
    
    return false;
  }  
  
  public boolean isTypeRight(final GameObjectType type, final int x) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type == type && obj.x2 >= x) {
        return true;
      }
    }
    
    return false;
  }  
  
  boolean isTypeInRange(final GameObjectType type, final int x1, final int x2) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type == type && obj.x1 <= x2 && obj.x2 >= x1) {
        return true;
      }
    }
    
    return false;
  }  
  
  int countTypeInBounds(final GameObjectType type, final int x1, final int y1, 
      final int x2, final int y2) {
    
    int count = 0;
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      if (obj.type == type && obj.x1 <= x2 && obj.x2 >= x1 && obj.y2 >= y1 
          && obj.y1 <= y2) {
        ++count;
      }
    }    
    return count;
  }  
  
  public boolean isTypeInBounds(final GameObjectType type, final int x1, final int y1,
                                final int x2, final int y2) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
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
  
  public boolean isEnemyInBounds(final int x1, final int y1, final int x2,
                                 final int y2) {
    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      final GameObjectType type = obj.type;
      if (type.enemy && obj.x1 <= x2 && obj.x2 >= x1 && obj.y2 >= y1 
          && obj.y1 <= y2) {
        return true;
      }
    }

    return false;
  }
  
  public void executeOperation(final MapElement[][] map, final int width,
                               final int operation, final int stepX, final int stepY,
                               final boolean checkForEnemies) {
    
    switch(operation) {
      
      case WALK_LEFT: 
        walk(Left, stepX, stepY, checkForEnemies);
        break;
      case WALK_RIGHT:
        walk(Right, stepX, stepY, checkForEnemies);
        break;
        
      case WALK_CENTER_LEFT_JUMP:
        walkAndJump(map, width, 8, Left, stepX, stepY, checkForEnemies);
        break;
      case WALK_RIGHT_MIDDLE_LEFT_JUMP:
        walkAndJump(map, width, 13, Left, stepX, stepY, checkForEnemies);
        break;
      case WALK_LEFT_MIDDLE_LEFT_JUMP:
        walkAndJump(map, width, 2, Left, stepX, stepY, checkForEnemies);
        break;        
      case WALK_RIGHT_EDGE_LEFT_JUMP:
        walkAndJump(map, width, 19, Left, stepX, stepY, checkForEnemies);
        break;
      case WALK_LEFT_EDGE_LEFT_JUMP:
        walkAndJump(map, width, -4, Left, stepX, stepY, checkForEnemies);
        break;
        
      case WALK_CENTER_RIGHT_JUMP:
        walkAndJump(map, width, 8, Right, stepX, stepY, checkForEnemies);
        break;
      case WALK_RIGHT_MIDDLE_RIGHT_JUMP:
        walkAndJump(map, width, 13, Right, stepX, stepY, checkForEnemies);
        break;
      case WALK_LEFT_MIDDLE_RIGHT_JUMP:
        walkAndJump(map, width, 2, Right, stepX, stepY, checkForEnemies);
        break;          
      case WALK_RIGHT_EDGE_RIGHT_JUMP:
        walkAndJump(map, width, 19, Right, stepX, stepY, checkForEnemies);
        break;
      case WALK_LEFT_EDGE_RIGHT_JUMP:
        walkAndJump(map, width, -4, Right, stepX, stepY, checkForEnemies);
        break;
        
      case GO_UP_STAIRS:
        goUpStairs(map, width);
        break;
      case GO_DOWN_STAIRS:
        goDownStairs(map, width);
        break;
    }    
  }
  
  public void avoid(final GameObject obj) {
    if ((!obj.onPlatform || obj.y >= playerY - 48) 
        && (avoidX < 0 || obj.distanceX < abs(playerX - avoidX))) {
      avoidX = obj.x;
    }
  } 
  
  public void jump() {
    if (jumpDelay == 0) {
      jumpDelay = JUMP_WHIP_OFFSETS.length - 1;
      api.writeGamepad(0, A, true);
    }
  }
  
  public void kneel() {
    api.writeGamepad(0, Down, true);
  }
  
  public void whip() {
    if (!weaponing) {
      weaponDelay = WEAPON_DELAY;
      api.writeGamepad(0, B, true);
    }
  } 
  
  int getJumpWhipOffset() {
    return (onPlatform && jumpDelay == 0) ? 0 : JUMP_WHIP_OFFSETS[jumpDelay];
  }
  
  // Can player axe target when standing on specified tile?
  public boolean canHitTargetWithAxe(final int platformX, final int platformY) {
    return canHitWithAxe(platformX, platformY, target);
  }
  
  // Can player axe specified GameObject when standing on specified tile?
  boolean canHitWithAxe(final int platformX, final int platformY, 
      final GameObject obj) {
    
    final int ty = platformY << 4;
    final int dx = playerX < obj.x ? 2 : -2;
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
  
  // Can player axe specified GameObject when standing on specified tile?
  public boolean canHitWithAxe(final int platformX, final int platformY,
                               final int offsetX, final int offsetY, final GameObject obj) {
    
    final int ty = platformY << 4;
    final int dx = playerX < (obj.x + offsetX) ? 2 : -2;
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
    if (!weaponing) {
      weaponDelay = WEAPON_DELAY;
      if (!atBottomOfStairs && weapon == HOLY_WATER && hearts > 5 && shot < 3) {
        api.writeGamepad(0, Up, true);
        api.writeGamepad(0, B, true);
        return true;
      } else {
        api.writeGamepad(0, B, true);
        return false;
      }
    } else {
      return false;
    }
  }
  
  public void whipOrWeapon() {
    if (!weaponing) {
      weaponDelay = WEAPON_DELAY;
      if (!atBottomOfStairs) {
        api.writeGamepad(0, Up, true);
      }
      api.writeGamepad(0, B, true);
    }
  }
  
  public void useWeapon() {
    if (!weaponing) {
      weaponDelay = WEAPON_DELAY;
      api.writeGamepad(0, Up, true);
      api.writeGamepad(0, B, true);
    }
  }
  
  public boolean face(final GameObject obj) {
    if (obj.playerFacing) {
      return true;
    } else if (onStairs && playerY >= 56 && playerY <= 200) {
      pressDown();
    } else if (playerX < obj.x) {
      pressRight();
    } else {
      pressLeft();
    }
    return false;
  }
  
  boolean faceFlying(final GameObject obj) {
    if (obj.playerFacing) {
      return true;
    } else if (onStairs && playerY >= 56 && playerY <= 200) {
      pressDown();
    } else {
      substage.moveToward(obj);
    }
    return false;
  }  
  
  public boolean faceTarget() {
    if (target.playerFacing) {
      return true;
    } else if (onStairs && playerY >= 56 && playerY <= 200) {
      pressDown();
    } else {
      substage.moveTowardTarget();
    }
    return false;
  }
  
  public boolean faceFlyingTarget() {
    if (target.playerFacing) {
      return true;
    } else if (onStairs && playerY >= 56 && playerY <= 200) {
      pressDown();
    } else if (playerX < target.x) {
      pressRight();
    } else {
      pressLeft();
    }
    return false;
  }
  
  public int getWhipRadius() {
    return whips[whipLength][0].getRadius();
  }
  
  private boolean isAtBottomOfStairs() {
    
    if (onStairs || !onPlatform) {
      return false;
    }
    
    final MapElement[][] map = substage.mapRoutes.map;
    final int tileType = map[tileY - 1][tileX].tileType;
    return (tileType == BACK_STAIRS || (tileX < substage.mapRoutes.width - 1 
        && map[tileY - 1][tileX + 1].tileType == FORWARD_STAIRS)) 
            || (tileType == FORWARD_STAIRS || (tileX > 0 
                && map[tileY - 1][tileX - 1].tileType == BACK_STAIRS));
  }  
  
  private void goUpStairs(final MapElement[][] map, final int width) {
    if (onStairs) {
      api.writeGamepad(0, Up, true);
    } else if (overHangingLeft) {
      pressRight();
    } else if (overHangingRight) {
      pressLeft();
    } else if (onPlatform) {      
      final int x = playerX & 0x0F;
      final int tileType = map[tileY - 1][tileX].tileType;
      if (tileType == BACK_STAIRS || (tileX < width - 1 
          && map[tileY - 1][tileX + 1].tileType == FORWARD_STAIRS)) {
        if (x < 15) {
          pressRight();
        } else {
          api.writeGamepad(0, Up, true);
        }
      } else if (tileType == FORWARD_STAIRS || (tileX > 0 
          && map[tileY - 1][tileX - 1].tileType == BACK_STAIRS)) {
        if (x > 0) {
          pressLeft();
        } else {
          api.writeGamepad(0, Up, true);
        } 
      }
    }
  }
  
  private boolean isAtTopOfStairs() {
    
    if (onStairs || !onPlatform) {
      return false;
    }
    
    final MapElement[][] map = substage.mapRoutes.map;
    final int tileType = map[tileY][tileX].tileType;
    return isStairsPlatform(tileType) || (tileX < substage.mapRoutes.width - 1 
            && isBack(map[tileY][tileX + 1].tileType)) 
        || (tileX > 0 && isForward(map[tileY][tileX - 1].tileType));
  }
  
  private void goDownStairs(final MapElement[][] map, final int width) {
    if (onStairs) {
      api.writeGamepad(0, Down, true);
    } else if (onPlatform) {
      final int x = playerX & 0x0F;
      final int tileType = map[tileY][tileX].tileType;
      if (tileType == FORWARD_PLATFORM || (tileX < width - 1 
          && isBack(map[tileY][tileX + 1].tileType))) {
        if (x < 15) {
          pressRight();
        } else {
          api.writeGamepad(0, Down, true);
        }
      } else if (tileType == BACK_PLATFORM || (tileX > 0 
          && isForward(map[tileY][tileX - 1].tileType))) {
        if (x > 0) {
          pressLeft();
        } else {
          api.writeGamepad(0, Down, true);
        }        
      }
    }
  }
  
  void pressUp() {
    api.writeGamepad(0, Up, true);
  }
  
  public void pressDown() {
    api.writeGamepad(0, Down, true);
  }
  
  public void pressLeft() {
    if (playerX < avoidX || playerX >= avoidX + 16) {
      api.writeGamepad(0, Left, true);
    }
  }
  
  public void pressRight() {
    if (playerX <= avoidX - 16 || playerX > avoidX) {
      api.writeGamepad(0, Right, true);
    }
  }
  
  public void pressLeftAndJump() {
    if (jumpDelay == 0 && (playerX < avoidX || playerX >= avoidX + 58)) {
      jumpDelay = 2; // Low number enables jumps against walls.
      api.writeGamepad(0, Left, true);
      api.writeGamepad(0, A, true);
    }
  }
  
  public void pressRightAndJump() {
    if (jumpDelay == 0 && (playerX <= avoidX - 58 || playerX > avoidX)) {
      jumpDelay = 2; // Low number enables jumps against walls.
      api.writeGamepad(0, Right, true);
      api.writeGamepad(0, A, true);
    }
  }  
  
  void press(final int direction) {
    if (direction == Left) {
      pressLeft();
    } else {
      pressRight();
    }
  }
  
  void pressAndJump(final int direction) {
    if (direction == Left) {
      pressLeftAndJump();
    } else {
      pressRightAndJump();
    }
  }  
  
  private void walk(final int direction, final int stepX, final int stepY, 
      final boolean checkForEnemies) {
    if (onStairs) {
      api.writeGamepad(0, Up, true);
    } else if (checkForEnemies && stepY > tileY) {
      final int x = playerX & 0xF;
      if (overHangingLeft && direction == Left && x < 13) {
        if (!isEnemyInBounds((stepX << 4) - 24, playerY - 32, playerX + 24, 
            stepY << 4)) {
          pressLeft();
        }
      } else if (overHangingRight && direction == Right && x > 2) {
        if (!isEnemyInBounds(playerX - 24, playerY - 32, (stepX << 4) + 40, 
            stepY << 4)) {
          pressRight();
        }
      } else {
        press(direction);
      }
    } else {
      press(direction);
    }
  }
  
  private void walkAndJump(final MapElement[][] map, final int width,
      int offsetX, final int direction, final int stepX, final int stepY, 
          final boolean checkForEnemies) {
    
    switch (offsetX) {
      case 8:
        if (tileX == 0 || map[tileY - 1][tileX - 1].height == 0
            || map[tileY - 2][tileX - 1].height == 0) {
          offsetX = 10;
        } else if (tileX == width - 1 || map[tileY - 1][tileX + 1].height == 0
            || map[tileY - 2][tileX + 1].height == 0) {
          offsetX = 6;
        } break;
      case 19:
        if (direction == Left) {
          offsetX = 18;
        }
        break;
      case -4:
        if (direction == Right) {
          offsetX = -3;
        }
        break;
    }
    
    final int x = playerX - (tileX << 4);
    if (x == offsetX) {
      if (playerLeft ^ (direction == Right)) {
        if (jumpDelay == 0) {
          if (checkForEnemies) {
            if (direction == Left) {
              if (!isEnemyInBounds((stepX << 4) - 48, playerY - 64, 
                  playerX + 24, stepY << 4)) {
                pressLeftAndJump();
              }
            } else {
              if (!isEnemyInBounds(playerX - 24, playerY - 64, 
                  (stepX << 4) + 64, stepY << 4)) {
                pressRightAndJump();
              }
            }
          } else {
            pressAndJump(direction);          
          }
        }
      } else if (direction == Left) {
        pressRight();                   // walk past and turn around
      } else {
        pressLeft();                    // walk past and turn around
      }
    } else if (x > offsetX) {
      pressLeft();
    } else {
      pressRight();
    }    
  }
  
  boolean isPlayerInRange(final int x1, final int x2) {
    return playerX >= x1 && playerX <= x2;
  }
  
  boolean isOnOrInPlatform(final MapRoutes mapRoutes, int x, int y) {
    if (x < 0 || y < 0) {
      return false;
    }
    y >>= 4;
    if (y >= 0x0F) {
      return false;
    }
    x >>= 4;    
    if (x >= mapRoutes.width) {
      return false;
    }
    return mapRoutes.getDistance(x, y) < 32;
  }
  
  // (x, y) are absolute coordinates, not tile coordinates
  boolean isOnPlatform(int x, int y) {
    if (x < 0 || y < 0 || (y & 0x0E) != 0) {
      return false;
    }
    y >>= 4;
    if (y >= 0x0F) {
      return false;
    }
    final MapRoutes mapRoutes = substage.getMapRoutes();
    x >>= 4;    
    if (x >= mapRoutes.width) {
      return false;
    }
    return TileType.isPlatform(mapRoutes.map[y][x].tileType);
  }
  
  private void paintGameObjects() {
    api.setColor(Colors.YELLOW);
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      api.drawRect(obj.x1 - cameraX, obj.y1, obj.x2 - obj.x1 + 1, 
          obj.y2 - obj.y1 + 1);
    }    
    for(int i = objsCount - 1; i >= 0; --i) {
      final GameObject obj = gameObjects[i];
      api.drawRect(obj.x1 - cameraX, obj.y1, obj.x2 - obj.x1 + 1, 
          obj.y2 - obj.y1 + 1);
    }
    api.setColor(Colors.CYAN);
    for(int i = redBonesCount0 - 1; i >= 0; --i) {
      final RedBones bones = redBones0[i]; 
      api.drawRect(bones.x - 8 - cameraX, bones.y - 16, 16, 16);
    }
    if (target != null) {
      api.setColor(Colors.RED);
      api.drawRect(target.x1 - cameraX, target.y1, target.x2 - target.x1 + 1, 
          target.y2 - target.y1 + 1);
    }
    api.setColor(Colors.GREEN);
    for(int i = movingPlatformsCount - 1; i >= 0; --i) {
      final MovingPlatform p = movingPlatforms[i];
      api.drawRect(p.x1 - cameraX, p.y, p.x2 - p.x1 + 1, 8);
    }
  }
  
  private void printGameObject(final GameObjectType type) {
    for(int i = 0; i < objsCount; ++i) {
      if (gameObjects[i].type == type) {
        System.out.print(gameObjects[i] + " ");
      }
    }
    System.out.println();
  }
  
  private void printGameObjects() {
    for(int i = redBonesCount0 - 1; i >= 0; --i) {
      System.out.print(redBones0[i] + " ");
    }
    for(int i = 0; i < objsCount; ++i) {
      System.out.print(gameObjects[i] + " ");
    }
    if (target != null) {
      System.out.format("* %s", target);
    }
    System.out.println();
  }
  
  private void renderFinished() {  
   
    readState();

    final boolean halted;
    switch(mode) {
      case Modes.TITLE_SCREEN:
      case Modes.DEMO:
      case Modes.GAME_OVER:
        halted = true;
        break;
      case Modes.PLAYING:
        halted = paused;
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
        api.writeGamepad(0, Start, true);
      }
      return;
    } else {
      pauseDelay = 0;
    }

    if (!playing || level == null || substage == null) {
      target = null;
      strategy = null;
      targetType = null;
      targetX = targetY = -512;
      return;
    } 

    if (jumpDelay > 0) {
      --jumpDelay;
    }

    if (weaponDelay > 0) {
      --weaponDelay;
    }

    avoidX = AVOID_X_RESET;
    substage.pickStrategy();
    if (strategy != null) {
      if (entryDelay > 0) {
        --entryDelay;
      } else {
        strategy.step();
      }
    }  
    
//    paintGameObjects();
//    printGameObjects();
  }
  
  public static void main(final String... args) {
    ApiSource.initRemoteAPI("localhost", 9999);
    new CastlevaniaBot();
  }
}
