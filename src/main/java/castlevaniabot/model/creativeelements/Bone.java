package castlevaniabot.model.creativeelements;

public class Bone {
  public int x;
  public int y;
  public int x1;
  public int y1;
  public int x2;
  public int y2;
  public int vx;
  public int vy;
  public boolean left;
  
  @Override public String toString() {
    return String.format("< BONE %d %d %d %d %b >%n", x, y, vx, vy, left);
  }
}