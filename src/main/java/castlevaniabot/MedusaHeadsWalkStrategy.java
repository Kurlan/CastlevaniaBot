package castlevaniabot;

import static java.lang.Math.*;
import static castlevaniabot.GameObjectType.*;

public class MedusaHeadsWalkStrategy extends Strategy {

  private static final int FAR = 48;
  private static final int NEAR = 8;
  
  private boolean left;
  private int lastX;
  private int lastY;
  
  public MedusaHeadsWalkStrategy(final CastlevaniaBot b) {
    super(b);
  }
  
  void init(final boolean left) {
    this.left = left;
  }

  @Override void step() {
    
    final GameObject head = getNearestMedusaHead();
    
    if (head == null) {
      if (left) {
        b.substage.routeLeft();
      } else {
        b.substage.routeRight();
      }      
      return;
    } 
    
    int vx = head.x - lastX;
    int vy = head.y - lastY;
    lastX = head.x;
    lastY = head.y;
    
    if (abs(vx) > 8 || abs(vy) > 8) {
      vx = vy = 0;
    }
    
    int hx1 = head.x1;
    int hy1 = head.y1;
    int hx2 = head.x2;
    int hy2 = head.y2;    
    int px1 = b.playerX - 8;
    int px2 = b.playerX + 8;
    int py1 = b.playerY - 32;
    int py2 = b.playerY;
    for(int i = FAR + NEAR; i >= 0; --i) {
      
      hx1 += vx;
      hy1 += vy;
      hx2 += vx;
      hy2 += vy;            
      
      if (left) {
        --px1;
        --px2;
        if (hx1 > px2) {
          break;
        }
      } else {
        ++px1;
        ++px2;
        if (hx2 < px1) {
          break;
        }
      }
      
      if (hx2 >= px1 && hx1 <= px2 && hy2 >= py1 && hy1 <= py2) {
        return;
      }
    }
    
    if (left) {
      b.substage.routeLeft();
    } else {
      b.substage.routeRight();
    }
  } 
  
  private GameObject getNearestMedusaHead() {
    final GameObject[] objs = b.objs;
    for(int i = b.objsCount - 1; i >= 0; --i) {
      final GameObject obj = objs[i];
      if (obj.type == MEDUSA_HEAD && obj.left != left) {
        if (left) {
          if (obj.x >= b.playerX - FAR && obj.x <= b.playerX - NEAR) {
            return obj;
          }
        } else {
          if (obj.x >= b.playerX + NEAR && obj.x <= b.playerX + FAR) {
            return obj;
          }
        }
      }
    }
    return null;
  }
}