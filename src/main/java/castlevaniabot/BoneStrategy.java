package castlevaniabot;

public class BoneStrategy extends Strategy {

  private Bone bone;
  
  public BoneStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  void init(final Bone bone) {
    this.bone = bone;
  }

  @Override void step() {
    if (!b.onStairs) {      
      if (bone.left) {
        b.substage.routeLeft();        
      } else {
        b.substage.routeRight();
      }
    }    
  }  
}