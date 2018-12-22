package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import lombok.Value;

@Value
public class AllStrategies {
    private final AxeStrategy AXE;
    private final AxeKnightStrategy AXE_KNIGHT;
    private final BatMovingPlatformStrategy BAT_MOVING_PLATFORM;
    private final BatDualPlatformsStrategy BAT_DUAL_PLATFORMS;
    private final BoneDragonStrategy BONE_DRAGON;
    private final BlackBatStrategy BLACK_BAT;
    private final BlockStrategy BLOCK;
    private final BoomerangDeathStrategy BOOMERANG_DEATH;
    private final BoneStrategy BONE;
    private final BoneTowerStrategy BONE_TOWER;
    private final CandlesStrategy CANDLES;
    private final CookieMonsterStrategy COOKIE_MONSTER;
    private final CrusherStrategy CRUSHER;
    private final DeathHallHolyWaterStrategy DEATH_HALL_HOLY_WATER;
    private final DraculaStrategy DRACULA;
    private final EagleStrategy EAGLE;
    private final FireballStrategy FIREBALL;
    private final FireColumnStrategy FIRE_COLUMN;
    private final FishmanStrategy FISHMAN;
    private final FleamanStrategy FLEAMAN;
    private final FrankensteinStrategy FRANKENSTEIN;
    private final GetCrystalBallStrategy GET_CRYSTAL_BALL;
    private final GetItemStrategy GET_ITEM;
    private final GhostStrategy GHOST;
    private final GhoulStrategy GHOUL;
    private final GiantBatStrategy GIANT_BAT;
    private final GotCrystalBallStrategy GOT_CRYSTAL_BALL;
    private final HolyWaterDeathStrategy HOLY_WATER_DEATH;
    private final JumpMovingPlatformStrategy JUMP_MOVING_PLATFORM;
    private final MedusaStrategy MEDUSA;
    private final MedusaHeadStrategy MEDUSA_HEAD;
    private final MedusaHeadsPitsStrategy MEDUSA_HEADS_PITS;
    private final MedusaHeadsWalkStrategy MEDUSA_HEADS_WALK;
    private final MummiesStrategy MUMMIES;
    private final NoJumpMovingPlatformStrategy NO_JUMP_MOVING_PLATFORM;
    private final PantherStrategy PANTHER;
    private final PhantomBatStrategy PHANTOM_BAT;
    private final RavenStrategy RAVEN;
    private final RedBatDamageBoostStrategy RED_BAT_DAMAGE_BOOST;
    private final RedBatStrategy RED_BAT;
    private final RedBonesStrategy RED_BONES;
    private final RedSkeletonStrategy RED_SKELETON;
    private final SickleStrategy SICKLE;
    private final SkeletonWallStrategy SKELETON_WALL;
    private final SnakeStrategy SNAKE;
    private final SpearKnightStrategy SPEAR_KNIGHT;
    private final UseWeaponStrategy USE_WEAPON;
    private final WaitStrategy WAIT;
    private final WhiteSkeletonStrategy WHITE_SKELETON;
    private final WhipStrategy WHIP;

    public AllStrategies(BotState botState, GameState gameState, PlayerController playerController) {
        AXE = new AxeStrategy(botState, gameState, playerController);
        AXE_KNIGHT = new AxeKnightStrategy(botState, gameState, playerController);
        BAT_MOVING_PLATFORM = new BatMovingPlatformStrategy(botState, gameState, playerController);
        BAT_DUAL_PLATFORMS = new BatDualPlatformsStrategy(botState, gameState, playerController);
        BONE_DRAGON = new BoneDragonStrategy(botState, gameState, playerController);
        BLACK_BAT = new BlackBatStrategy(botState, gameState, playerController);
        BLOCK = new BlockStrategy(botState, gameState, playerController);
        BOOMERANG_DEATH = new BoomerangDeathStrategy(botState, gameState, playerController);
        BONE = new BoneStrategy(botState, gameState);
        BONE_TOWER = new BoneTowerStrategy(botState, gameState, playerController);
        CANDLES = new CandlesStrategy(botState, gameState, playerController);
        COOKIE_MONSTER = new CookieMonsterStrategy(botState, gameState, playerController);
        CRUSHER = new CrusherStrategy(botState, gameState, playerController);
        DEATH_HALL_HOLY_WATER = new DeathHallHolyWaterStrategy(botState, gameState, playerController);
        DRACULA = new DraculaStrategy(botState, gameState, playerController, COOKIE_MONSTER);
        EAGLE = new EagleStrategy( botState, gameState, playerController);
        FIREBALL = new FireballStrategy( botState, gameState, playerController);
        FIRE_COLUMN = new FireColumnStrategy( botState, gameState, playerController);
        FISHMAN = new FishmanStrategy( botState, gameState, playerController);
        FLEAMAN = new FleamanStrategy( botState, gameState, playerController);
        FRANKENSTEIN = new FrankensteinStrategy( botState, gameState, playerController);
        GET_CRYSTAL_BALL = new GetCrystalBallStrategy( botState, gameState, playerController);
        GET_ITEM = new GetItemStrategy( botState, gameState, playerController);
        GHOST = new GhostStrategy( botState, gameState, playerController);
        GHOUL = new GhoulStrategy( botState, gameState, playerController);
        GIANT_BAT = new GiantBatStrategy( botState, gameState, playerController);
        GOT_CRYSTAL_BALL = new GotCrystalBallStrategy(botState, gameState, playerController);
        HOLY_WATER_DEATH = new HolyWaterDeathStrategy(botState, gameState, playerController);
        JUMP_MOVING_PLATFORM = new JumpMovingPlatformStrategy(botState, gameState, playerController);
        MEDUSA = new MedusaStrategy(botState, gameState, playerController);
        MEDUSA_HEAD = new MedusaHeadStrategy(botState, gameState, playerController);
        MEDUSA_HEADS_PITS = new MedusaHeadsPitsStrategy(botState, gameState, playerController);
        MEDUSA_HEADS_WALK = new MedusaHeadsWalkStrategy(botState, gameState);
        MUMMIES = new MummiesStrategy(botState, gameState, playerController);
        NO_JUMP_MOVING_PLATFORM = new NoJumpMovingPlatformStrategy(botState, gameState, playerController);
        PANTHER = new PantherStrategy(botState, gameState, playerController);
        PHANTOM_BAT = new PhantomBatStrategy(botState, gameState, playerController);
        RAVEN = new RavenStrategy(botState, gameState, playerController);
        RED_BAT = new RedBatStrategy(botState, gameState, playerController);
        RED_BAT_DAMAGE_BOOST = new RedBatDamageBoostStrategy(botState, gameState, playerController, RED_BAT);
        RED_BONES = new RedBonesStrategy(botState, gameState, playerController);
        RED_SKELETON = new RedSkeletonStrategy(botState, gameState, playerController);
        SICKLE = new SickleStrategy(botState, gameState, playerController);
        SKELETON_WALL = new SkeletonWallStrategy(botState, gameState, playerController);
        SNAKE = new SnakeStrategy(botState, gameState, playerController);
        SPEAR_KNIGHT = new SpearKnightStrategy(botState, gameState, playerController);
        USE_WEAPON = new UseWeaponStrategy(botState, gameState, playerController);
        WAIT = new WaitStrategy(botState, gameState, playerController);
        WHITE_SKELETON = new WhiteSkeletonStrategy(botState, gameState, playerController);
        WHIP = new WhipStrategy(botState, gameState, playerController);
    }
}
