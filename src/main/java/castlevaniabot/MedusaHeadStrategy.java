package castlevaniabot;

// No longer used!
public class MedusaHeadStrategy extends Strategy {
  
  public MedusaHeadStrategy(final CastlevaniaBot b) {
    super(b);
  }

  @Override
  public void step() {
    
    final GameObject head = b.target;
    final MedusaHead medusaHead = b.getMedusaHead(head);
    if (medusaHead == null) {
      return;
    }
    
    int t = medusaHead.t + 16;
    int d = 0;
    if (t >= MedusaHead.WAVE.length) {
      t -= MedusaHead.WAVE.length;
      d = 126;
    }
    final int dx = MedusaHead.WAVE[t][0] + d;
    final int headX = medusaHead.left ? (medusaHead.x0 - dx) 
        : (medusaHead.x0 + dx);
    final int headY = medusaHead.y0 + MedusaHead.WAVE[t][1];
    final int offsetX = headX - head.x;
    final int offsetY = headY - head.y;
    
    if (b.isTargetInStandingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget() && !b.weaponing) {
        b.whip();                           
      }
    } else if (b.isTargetInKneelingWhipRange(offsetX, offsetY)) {
      if (b.faceTarget()) {
        b.kneel();
        if (b.kneeling && !b.weaponing) {
          b.whip();                          
        }
      }
    }
  }
}