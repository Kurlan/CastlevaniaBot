package castlevaniabot;

public class Sickle {
  
  public int x;
  public int y;
  public int time;
  
  @Override public String toString() {
    return String.format("< SICKLE %d %d %d >%n", x, y, time);
  }
}