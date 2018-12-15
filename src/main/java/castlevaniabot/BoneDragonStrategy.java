package castlevaniabot;

public class BoneDragonStrategy extends Strategy {
  
  private int lastX;
  private int lastY;   

  public BoneDragonStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  @Override
  public void step() {
    
    final GameObject head = b.target;
    final int offsetX = (head.x - lastX) << 4;
    final int offsetY = (head.y - lastY) << 4;
    lastX = head.x;
    lastY = head.y; 
    
    final int playerX;
    final int playerY;
    if (b.playerX > 448) {
      switch(b.whipLength) {
        case 0:  playerX = 652; break;
        case 1:  playerX = 648; break;
        default: playerX = 632; break;
      }   
      playerY = 160;
    } else {
      switch(b.whipLength) {
        case 0:  playerX = 220; break;
        case 1:  playerX = 216; break;
        default: playerX = 200; break;
      }
      playerY = 192;
    }
    
    if (b.playerX != playerX || b.playerY != playerY || b.playerLeft) {
      b.substage.routeAndFace(playerX, playerY, false);
    } if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      b.kneel();
      if (b.kneeling && !b.weaponing) {
        b.whip();                         
      }
    } else if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (!b.weaponing) {
        b.whip();                           
      }
    } 
  }  
}