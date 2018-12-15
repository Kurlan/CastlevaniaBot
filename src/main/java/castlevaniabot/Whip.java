package castlevaniabot;

public class Whip {

  public static final int[][] WHIP_DISTANCES = { { 16, 32 }, { 16, 48, 32 } };

  private final int left;
  private final int right;
  private final int top;
  private final int bottom;
  
  public Whip(final int cx, int cy, final int rx, final int ry) {
    cy -= 16;
    left = cx - rx;
    right = cx + rx;
    top = cy - ry;
    bottom = cy + ry;
  }
  
  public boolean inRange(final CastlevaniaBot b, final GameObject obj) {
    return inRange(b.playerX, b.playerY, obj.x1, obj.x2, obj.y1, obj.y2);
  }
  
  public boolean inRange(final CastlevaniaBot b, final GameObject obj,
                         final int offsetX, final int offsetY) {
    return inRange(b.playerX, b.playerY, obj.x1 + offsetX, obj.x2 + offsetX, 
        obj.y1 + offsetY, obj.y2 + offsetY);
  }
  
  // Returns true if the specified region is within left or right whipping range
  // as opposed to the whipping range directly in front of the player.
  // After this test, the player must face the game object prior to whipping.
  boolean inRange(final int playerX, final int playerY, 
      final int left, final int right, final int top, final int bottom) {
    
    // Check if object is within the correct height bounds.
    if (top >= playerY + this.bottom || bottom <= playerY + this.top) {
      return false;
    }
    
    // Check if object is within left-side bounds or right-side bounds.
    return (left < (playerX + this.right) && right > (playerX + this.left)) 
        || (left < (playerX - this.left) && right > (playerX - this.right));
  }
  
  public int getRadius() {
    return right;
  }
}