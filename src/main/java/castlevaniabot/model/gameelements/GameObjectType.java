package castlevaniabot.model.gameelements;

import static castlevaniabot.CastlevaniaBot.*;

public enum GameObjectType {
  
  WHIP_UPGRADE,
  SMALL_HEART(0x2F, 8),
  LARGE_HEART,
  MONEY_BAG,
  CROSS,
  PORK_CHOP,
  DOULE_SHOT,
  TRIPLE_SHOT,
  INVISIBLE_POTION,
  CRYSTAL_BALL,
  EXTRA_LIFE,
  
  DAGGER_WEAPON,
  AXE_WEAPON,
  HOLY_WATER_WEAPON,
  BOOMERANG_WEAPON,
  STOPWATCH_WEAPON,
  
  FIRE_COLUMN(0x28),
  CANDLES(0x28, 8),
  
  FIREBALL(0x26, 8, 16, true),
  SNAKE(0x09, 16, 16, 8, 12, true),
  BONE_DIAGONAL(0x24, 16, 16, true),
  BONE_VERTICAL(0x24,  8, 16, true),
  BANDAGE(0x22, 16, 8, 8, 12, true),
  RED_BONES(0x2F, true),
  AXE(0x27, true),
  SICKLE(0x0B, true),
  
  AXE_KNIGHT(0x0A, 24, 32, true),
  BLACK_BAT(0x07, true),
  RED_BAT(0x02, true),
  BONE_DRAGON_HEAD(0x14, true),
  BONE_TOWER(0x0E, 16, 32),  // stationary enemy (can safely jump toward it)
  EAGLE(0x12, 16, 32, true), // actually 32x32, but 16x32 required for detection
  FLEAMAN(0x0C, true),
  GHOST(0x0D, true),
  FISHMAN(0x03, 16, 32, true),
  GHOUL(0x01, 16, 32, true),
  MEDUSA_HEAD(0x06, true),
  PANTHER(0x11, 32, 16, true),
  RAVEN(0x0F, true),
  RED_SKELETON(0x13, 16, 32, true),
  RED_SKELETON_RISING(0x13, 16, 16, 8, 0, true),
  WHITE_SKELETON(0x10, 16, 32, true),
  SPEAR_KNIGHT(0x08, 16, 32, true),
  
  PHANTOM_BAT(0x19, 28, 24, true),
  MEDUSA(0x1B, 32, 32, true),  
  MUMMY(0x1A, 16, 48), // stationary while inactive (can safely jump toward it)
  FRANKENSTEIN(0x1C, 16, 48, true),
  DEATH(0x18, 24, 48, true),
  DRACULA_HEAD(0x30, 8, 16, true),
  COOKIE_MONSTER_HEAD(0x04, true),
  
  DESTINATION,
  BLOCK,
  FLAMES(0x2F, 8, 16); // appear when an enemy is destroyed
  
  public final int width;
  public final int height;
  public final int xOffset;
  public final int yOffset;
  public final int hitbox;
  public final int xRadius;
  public final int yRadius;
  
  // This is used to detect enemies within the vicinity of a jump landing spot.
  // It's okay to set this to false for stationary enemies, like bone towers.
  public final boolean enemy;  
  
  private GameObjectType() {
    this(0x2F, 16, 16, false);
  }
  
  private GameObjectType(final int hitbox) {
    this(hitbox, 16, 16, false);
  }
  
  private GameObjectType(final int hitbox, final boolean enemy) {
    this(hitbox, 16, 16, enemy);
  }  

  private GameObjectType(final int hitbox, final int width) {
    this(hitbox, width, 16, false);
  }
  
  private GameObjectType(final int hitbox, final int width, 
      final boolean enemy) {
    this(hitbox, width, 16, enemy);
  }  
  
  private GameObjectType(final int hitbox, final int width, 
      final int height) {
    this(hitbox, width, height, false);
  }  
  
  private GameObjectType(final int hitbox, final int width, final int height, 
      final boolean enemy) {
    this(hitbox, width, height, width >> 1, height >> 1, enemy);
  }
  
  private GameObjectType(final int hitbox, final int width, final int height, 
      final int xOffset, final int yOffset, final boolean enemy) {
    this.hitbox = hitbox;
    this.width = width;
    this.height = height;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.enemy = enemy;
    this.xRadius = HITBOX_RADII[hitbox][0];
    this.yRadius = HITBOX_RADII[hitbox][1];
  }  
}