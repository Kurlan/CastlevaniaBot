package castlevaniabot;

import castlevaniabot.level.Level;
import castlevaniabot.model.creativeelements.MovingPlatform;
import castlevaniabot.substage.Substage;
import lombok.Data;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

@Data
public class GameState {

    private Level currentLevel;
    private int stageNumber;
    private int substageNumber;
    private Substage currentSubstage;
    private boolean weaponing;

    private MovingPlatform[] movingPlatforms;
    private int movingPlatformsCount;
    int cameraX;

    public GameState() {
        movingPlatforms = new MovingPlatform[16];
        for(int i = movingPlatforms.length - 1; i >= 0; --i) {
            movingPlatforms[i] = new MovingPlatform();
        }
    }

    public void addMovingPlatformSegment(final int x, final int y) {
        for(int i = movingPlatformsCount - 1; i >= 0; --i) {
            final MovingPlatform m = movingPlatforms[i];
            if (m.y == y && abs(m.x1 - x) <= 24) {
                m.x1 = min(m.x1, x);
                m.x2 = max(m.x2, x);
                return;
            }
        }
        final MovingPlatform m = movingPlatforms[movingPlatformsCount++];
        m.y = y;
        m.x1 = x;
        m.x2 = x;
    }

    public void buildMovingPlatforms() {
        for(int i = movingPlatformsCount - 1; i >= 0; --i) {
            final MovingPlatform m = movingPlatforms[i];
            m.x2 += 7;
            final int width = m.x2 - m.x1 + 1;
            if (width < 32) {
                if (m.x2 < 128) {
                    m.x1 = m.x2 - 31;
                } else {
                    m.x2 = m.x1 + 31;
                }
            }
            m.x1 += cameraX;
            m.x2 += cameraX;
        }
    }

    public MovingPlatform getMovingPlatform(final int minX, final int maxX) {
        for(int i = movingPlatformsCount - 1; i >= 0; --i) {
            final MovingPlatform platform = movingPlatforms[i];
            if (platform.x1 >= minX && platform.x2 <= maxX) {
                return platform;
            }
        }
        return null;
    }

}
