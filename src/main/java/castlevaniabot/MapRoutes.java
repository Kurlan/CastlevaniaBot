package castlevaniabot;

public class MapRoutes {
  
  public static int getDistance(final int route) {
    return (route >> 16) & 0xFFFF;
  }
  
  public static int getStepX(final int route) {
    return (route >> 8) & 0xFF;
  }
  
  public static int getStepY(final int route) {
    return (route >> 4) & 0xF;
  }  
  
  public static int getOperation(final int route) {
    return route & 0xF;
  }  
  
  public final String name;
  public final int width;
  public final int height;
  public final int pixelsWidth;
  public final int pixelsHeight;
  public final MapElement[][] map;
  public final int[][][][] routes;
  
  private final CastlevaniaBot b;

  public MapRoutes(final CastlevaniaBot b, final String name, final int width, 
      final int height, final MapElement[][] map, final int[][][][] routes) {
    
    this.b = b;
    this.name = name;
    this.width = width;
    this.height = height;
    this.map = map;
    this.routes = routes;
    
    pixelsWidth = width << 4;
    pixelsHeight = height << 4;
  }
   
  public int getDistance(final int platformX, final int platformY) {
    return (routes[b.tileY][b.tileX][platformY][platformX] >> 16) & 0xFFFF;
  }  
  
  public int getDistance(final GameObject obj) {
    return (routes[b.tileY][b.tileX][obj.platformY][obj.platformX] >> 16) 
        & 0xFFFF;
  }  
  
  public int getDistance(final int startX, final int startY, final int endX, 
      final int endY) {
    return (routes[startY][startX][endY][endX] >> 16) & 0xFFFF;
  }
  
  public int getStepX(final int startX, final int startY, final int endX, 
      final int endY) {
    return (routes[startY][startX][endY][endX] >> 8) & 0xFF;
  }
  
  public int getStepY(final int startX, final int startY, final int endX, 
      final int endY) {
    return (routes[startY][startX][endY][endX] >> 4) & 0xF;
  }  
  
  public int getOperation(final int startX, final int startY, final int endX, 
      final int endY) {
    return routes[startY][startX][endY][endX] & 0xF;
  }  
}