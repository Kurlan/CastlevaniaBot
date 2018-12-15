package castlevaniabot;

public class MovingPlatform {  
  public int y;
  public int x1;
  public int x2;
  
  @Override public String toString() {
    return String.format("[ MOVING_PLATFORM %d %d %d ]", y, x1, x2);
  }
}