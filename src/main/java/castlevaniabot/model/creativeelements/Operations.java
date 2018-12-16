package castlevaniabot.model.creativeelements;

public interface Operations {
  
  int OPERATIONS = 15;

  int NO_OPERATION                 =  0;
  
  int WALK_LEFT                    =  1;
  int WALK_RIGHT                   =  2;
  
  int WALK_CENTER_LEFT_JUMP        =  3;
  int WALK_RIGHT_MIDDLE_LEFT_JUMP  =  4;
  int WALK_LEFT_MIDDLE_LEFT_JUMP   =  5;
  int WALK_RIGHT_EDGE_LEFT_JUMP    =  6;
  int WALK_LEFT_EDGE_LEFT_JUMP     =  7;
  
  int WALK_CENTER_RIGHT_JUMP       =  8;
  int WALK_RIGHT_MIDDLE_RIGHT_JUMP =  9;
  int WALK_LEFT_MIDDLE_RIGHT_JUMP  = 10;  
  int WALK_RIGHT_EDGE_RIGHT_JUMP   = 11;
  int WALK_LEFT_EDGE_RIGHT_JUMP    = 12;
  
  int GO_UP_STAIRS                 = 13;
  int GO_DOWN_STAIRS               = 14;
  
  static String toString(final int operation) {
    switch(operation & 0x0F) {
      case NO_OPERATION:                 return "NO_OPERATION";
      case WALK_LEFT:                    return "WALK_LEFT";
      case WALK_RIGHT:                   return "WALK_RIGHT";
      case WALK_CENTER_LEFT_JUMP:        return "WALK_CENTER_LEFT_JUMP";
      case WALK_RIGHT_MIDDLE_LEFT_JUMP:  return "WALK_RIGHT_MIDDLE_LEFT_JUMP";
      case WALK_LEFT_MIDDLE_LEFT_JUMP:   return "WALK_LEFT_MIDDLE_LEFT_JUMP";      
      case WALK_RIGHT_EDGE_LEFT_JUMP:    return "WALK_RIGHT_EDGE_LEFT_JUMP";
      case WALK_LEFT_EDGE_LEFT_JUMP:     return "WALK_LEFT_EDGE_LEFT_JUMP";
      case WALK_CENTER_RIGHT_JUMP:       return "WALK_CENTER_RIGHT_JUMP";
      case WALK_RIGHT_MIDDLE_RIGHT_JUMP: return "WALK_RIGHT_MIDDLE_RIGHT_JUMP";
      case WALK_LEFT_MIDDLE_RIGHT_JUMP:  return "WALK_LEFT_MIDDLE_RIGHT_JUMP";      
      case WALK_RIGHT_EDGE_RIGHT_JUMP:   return "WALK_RIGHT_EDGE_RIGHT_JUMP";
      case WALK_LEFT_EDGE_RIGHT_JUMP:    return "WALK_LEFT_EDGE_RIGHT_JUMP";
      case GO_UP_STAIRS:                 return "GO_UP_STAIRS";
      case GO_DOWN_STAIRS:               return "GO_DOWN_STAIRS";     
    }
    return null;
  }
}