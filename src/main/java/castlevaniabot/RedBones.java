package castlevaniabot;

public class RedBones {

  public int x;
  public int y;
  public int time;
  
  @Override public String toString() {
    return String.format("< RED_BONES %d %d %d >", x, y, time);
  }
}