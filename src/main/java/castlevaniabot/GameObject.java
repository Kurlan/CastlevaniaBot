package castlevaniabot;

import lombok.Builder;
import lombok.Data;

public class GameObject {
  
  public GameObjectType type;
  public int x;
  public int y;
  public int x1;
  public int y1;
  public int x2;
  public int y2;
  public int supportX;
  public int platformX;
  public int platformY;
  public int distanceX;
  public int distanceY;
  public int distance;
  public int rank;
  public int tier;
  public int subTier;
  public int distTier;
  public boolean left;
  public boolean active;
  public boolean onPlatform;
  public boolean playerFacing;
  
  @Override public String toString() {
    return String.format(
        "[ %s %d %d %d %d %d %d %d %d %d %d %d %b %b %b %b %d %d ]", type, 
        x, y, x1, y1, x2, y2, distanceX, distanceY, distance, platformX, 
            platformY, onPlatform, left, active, playerFacing, tier, subTier);
  }
}