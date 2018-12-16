package castlevaniabot.model.gameelements;

public interface Modes {
  int TITLE_SCREEN = 0x01;
  int DEMO         = 0x02;
  int INTRO        = 0x04;
  int PLAYING      = 0x05;
  int DEAD         = 0x06;
  int WOODEN_DOOR  = 0x08;
  int CASTLE_DOOR  = 0x0A;
  int CRYSTAL_BALL = 0x0C;
  int GAME_OVER    = 0x0D;
  int END_CREDITS  = 0x0F;
}