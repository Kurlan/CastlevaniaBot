package castlevaniabot;

import castlevaniabot.level.Level;
import castlevaniabot.model.creativeelements.MovingPlatform;
import castlevaniabot.model.gameelements.GameObject;
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
    private int objsCount;
    private GameObject[] gameObjects;

    private MovingPlatform[] movingPlatforms;
    private int movingPlatformsCount;
    private int cameraX;
    private int mode;

    private boolean playing;
    private boolean paused;

    private int weaponDelay;

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

/*  Unused.
    void addMedusaHead(final int x, final int y) {

        final MedusaHead head = medusaHeads1[medusaHeadsCount1++];

        head.x = x + 8 + getCameraX();
        head.y_32 = head.y_16 = head.y0 = head.y = y + 16;
        head.s = head.t = 0;
        head.sameYs = 1;
        head.left = true;
    }

    void buildMedusaHeads() {

        for(int i = medusaHeadsCount1 - 1; i >= 0; --i) {
            final MedusaHead h1 = medusaHeads1[i];
            for(int j = medusaHeadsCount0 - 1; j >= 0; --j) {
                final MedusaHead h0 = medusaHeads0[j];
                if (abs(h1.x - h0.x) <= 8 && abs(h1.y - h0.y) <= 8) {

                    h1.left = h1.x < h0.x;

                    h1.t = h0.t + 1;
                    if (h1.t >= MedusaHead.WAVE.length) {
                        h1.t = 0;
                    }

                    h1.y_16 = h0.y_16;
                    h1.y_32 = h0.y_32;
                    h1.s = h0.s + 1;
                    if ((h1.s & 0xF) == 0) {
                        h1.y_32 = h1.y_16;
                        h1.y_16 = h1.y;
                    }

                    h1.x0 = h0.x0;
                    h1.y0 = h0.y0;
                    if (h1.y == h0.y) {
                        h1.sameYs = h0.sameYs + 1;
                        if (h1.sameYs >= 3) {
                            if (h1.s > 32) {
                                if (h1.y < h1.y_32) {
                                    h1.t = 3;
                                    h1.x0 = h1.left ? (h1.x + 4) : (h1.x - 4);
                                    h1.y0 = h1.y + 32;
                                } else {
                                    h1.t = 53;
                                    h1.x0 = h1.left ? (h1.x + 66) : (h1.x - 66);
                                    h1.y0 = h1.y - 32;
                                }
                            } else {
                                h1.t = 3;
                                h1.x0 = h1.left ? (h1.x + 4) : (h1.x - 4);
                                h1.y0 = h1.y + 32;
                            }
                        }
                    }
                    break;
                }
            }
        }
        final MedusaHead[] temp = medusaHeads0;
        medusaHeads0 = medusaHeads1;
        medusaHeads1 = temp;
        medusaHeadsCount0 = medusaHeadsCount1;
        medusaHeadsCount1 = 0;
    }

    */

}
