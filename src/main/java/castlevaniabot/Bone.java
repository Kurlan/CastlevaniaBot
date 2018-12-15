package castlevaniabot;

public class Bone {
  int x;
  int y;
  int x1;
  int y1;
  int x2;
  int y2;
  int vx;
  int vy;
  boolean left;
  
  @Override public String toString() {
    return String.format("< BONE %d %d %d %d %b >%n", x, y, vx, vy, left);
  }
}