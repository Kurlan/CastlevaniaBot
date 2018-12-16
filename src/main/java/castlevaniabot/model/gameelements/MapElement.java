package castlevaniabot.model.gameelements;

import java.io.*;

public class MapElement {
  public final int height;
  public final int tileType;

  public MapElement(final int height, final int tileType) {
    this.height = height;
    this.tileType = tileType;
  }
  
  public MapElement(final int value) {
    height = (value >> 4) & 0x0F;
    tileType = value & 0x0F;
  }
  
  public void write(final DataOutputStream out) throws Throwable {    
    out.write(((height & 0x0F) << 4) | (tileType & 0x0F));
  }  
  
  @Override
  public String toString() {
    return String.format("[ %d %d ]", tileType, height);
  }
}