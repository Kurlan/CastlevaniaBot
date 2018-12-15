package castlevaniabot;

public class Sickle {
  
  int x;
  int y;
  int time;
  
  @Override public String toString() {
    return String.format("< SICKLE %d %d %d >%n", x, y, time);
  }
}