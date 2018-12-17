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

    public AllStrategies(CastlevaniaBot castlevaniaBot, BotState botState, GameState gameState, PlayerController playerController) {
        AXE = new AxeStrategy(castlevaniaBot, botState, gameState);
        AXE_KNIGHT = new AxeKnightStrategy(castlevaniaBot, botState, gameState);
        BAT_MOVING_PLATFORM = new BatMovingPlatformStrategy(castlevaniaBot, botState, gameState, playerController);
        BAT_DUAL_PLATFORMS = new BatDualPlatformsStrategy(castlevaniaBot, botState, gameState, playerController);
        BONE_DRAGON = new BoneDragonStrategy(castlevaniaBot, botState, gameState);
        BLACK_BAT = new BlackBatStrategy(castlevaniaBot, botState, gameState);
        BLOCK = new BlockStrategy(castlevaniaBot, botState, gameState);
        BOOMERANG_DEATH = new BoomerangDeathStrategy(castlevaniaBot, botState, gameState, playerController);
        BONE = new BoneStrategy(castlevaniaBot, botState, gameState);
        BONE_TOWER = new BoneTowerStrategy(castlevaniaBot, botState, gameState);
        CANDLES = new CandlesStrategy(castlevaniaBot, botState, gameState);
        COOKIE_MONSTER = new CookieMonsterStrategy(castlevaniaBot, botState, gameState);
        CRUSHER = new CrusherStrategy(castlevaniaBot, botState, gameState, playerController);
        DEATH_HALL_HOLY_WATER = new DeathHallHolyWaterStrategy(castlevaniaBot, botState, gameState);
        DRACULA = new DraculaStrategy(castlevaniaBot, botState, gameState);
        EAGLE = new EagleStrategy(castlevaniaBot, botState, gameState);
        FIREBALL = new FireballStrategy(castlevaniaBot, botState, gameState);
        FIRE_COLUMN = new FireColumnStrategy(castlevaniaBot, botState, gameState);
        FISHMAN = new FishmanStrategy(castlevaniaBot, botState, gameState);
        FLEAMAN = new FleamanStrategy(castlevaniaBot, botState, gameState);
        FRANKENSTEIN = new FrankensteinStrategy(castlevaniaBot, botState, gameState);
        GET_CRYSTAL_BALL = new GetCrystalBallStrategy(castlevaniaBot, botState, gameState);
        GET_ITEM = new GetItemStrategy(castlevaniaBot, botState, gameState);
        GHOST = new GhostStrategy(castlevaniaBot, botState, gameState);
        GHOUL = new GhoulStrategy(castlevaniaBot, botState, gameState);
        GIANT_BAT = new GiantBatStrategy(castlevaniaBot, botState, gameState);
        GOT_CRYSTAL_BALL = new GotCrystalBallStrategy(castlevaniaBot, botState, gameState);
        HOLY_WATER_DEATH = new HolyWaterDeathStrategy(castlevaniaBot, botState, gameState, playerController);
        JUMP_MOVING_PLATFORM = new JumpMovingPlatformStrategy(castlevaniaBot, botState, gameState, playerController);
        MEDUSA = new MedusaStrategy(castlevaniaBot, botState, gameState);
        MEDUSA_HEAD = new MedusaHeadStrategy(castlevaniaBot, botState, gameState);
        MEDUSA_HEADS_PITS = new MedusaHeadsPitsStrategy(castlevaniaBot, botState, gameState, playerController);
        MEDUSA_HEADS_WALK = new MedusaHeadsWalkStrategy(castlevaniaBot, botState, gameState);
        MUMMIES = new MummiesStrategy(castlevaniaBot, botState, gameState);
        NO_JUMP_MOVING_PLATFORM = new NoJumpMovingPlatformStrategy(castlevaniaBot, botState, gameState, playerController);
        PANTHER = new PantherStrategy(castlevaniaBot, botState, gameState);
        PHANTOM_BAT = new PhantomBatStrategy(castlevaniaBot, botState, gameState);
        RAVEN = new RavenStrategy(castlevaniaBot, botState, gameState);
        RED_BAT_DAMAGE_BOOST = new RedBatDamageBoostStrategy(castlevaniaBot, botState, gameState, playerController);
        RED_BAT = new RedBatStrategy(castlevaniaBot, botState, gameState);
        RED_BONES = new RedBonesStrategy(castlevaniaBot, botState, gameState, playerController);
        RED_SKELETON = new RedSkeletonStrategy(castlevaniaBot, botState, gameState);
        SICKLE = new SickleStrategy(castlevaniaBot, botState, gameState, playerController);
        SKELETON_WALL = new SkeletonWallStrategy(castlevaniaBot, botState, gameState);
        SNAKE = new SnakeStrategy(castlevaniaBot, botState, gameState);
        SPEAR_KNIGHT = new SpearKnightStrategy(castlevaniaBot, botState, gameState);
        USE_WEAPON = new UseWeaponStrategy(castlevaniaBot, botState, gameState);
        WAIT = new WaitStrategy(castlevaniaBot, botState, gameState, playerController);
        WHITE_SKELETON = new WhiteSkeletonStrategy(castlevaniaBot, botState, gameState);
        WHIP = new WhipStrategy(castlevaniaBot, botState, gameState);
    }
}
