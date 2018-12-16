package castlevaniabot.strategy;

import castlevaniabot.model.creativeelements.Bone;
import castlevaniabot.CastlevaniaBot;

public class BoneStrategy extends Strategy {

  private Bone bone;
  
  public BoneStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  public void init(final Bone bone) {
    this.bone = bone;
  }

  @Override
  public void step() {
    if (!b.onStairs) {      
      if (bone.left) {
        b.substage.routeLeft();        
      } else {
        b.substage.routeRight();
      }
    }    
  }  
}