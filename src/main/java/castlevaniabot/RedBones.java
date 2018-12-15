package castlevaniabot;

public class RedBones {

  int x;
  int y;
  int time;
  
  @Override public String toString() {
    return String.format("< RED_BONES %d %d %d >", x, y, time);
  }
}