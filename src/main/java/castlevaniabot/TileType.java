package castlevaniabot;

public interface TileType {

  int EMPTY            = 0;
  int BACK_STAIRS      = 1;
  int FORWARD_STAIRS   = 2;
  int PLATFORM         = 3;  
  int BACK_PLATFORM    = 4;
  int FORWARD_PLATFORM = 5;
  
  static boolean isForward(final int tileType) {
    return tileType == FORWARD_STAIRS || tileType == FORWARD_PLATFORM;
  }
  
  static boolean isBack(final int tileType) {
    return tileType == BACK_STAIRS || tileType == BACK_PLATFORM;
  } 
  
  static boolean isStairsPlatform(final int tileType) {
    return tileType > PLATFORM;
  }
  
  static boolean isPlatform(final int tileType) {
    return tileType >= PLATFORM;
  }
  
  static boolean isStairs(final int tileType) {
    return !(tileType == EMPTY || tileType == PLATFORM);
  }  
}