package castlevaniabot;

import castlevaniabot.control.PlayerController;
import castlevaniabot.level.Level;
import castlevaniabot.model.creativeelements.BoneTowerSegment;
import castlevaniabot.model.creativeelements.MovingPlatform;
import castlevaniabot.model.creativeelements.Whip;
import castlevaniabot.model.gameelements.Coordinates;
import castlevaniabot.model.gameelements.GameObject;
import castlevaniabot.model.gameelements.GameObjectType;
import castlevaniabot.model.gameelements.MapElement;
import castlevaniabot.model.gameelements.MapRoutes;
import castlevaniabot.substage.Substage;
import lombok.Data;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

@Data
public class GameState {

    public static final int MAX_DISTANCE = 0xFFFF;
    public static final int MAX_HEIGHT   = 0xF;

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

    private BoneTowerSegment[] boneTowerSegments;
    private int boneTowerSegmentsCount;

    public GameState() {
        movingPlatforms = new MovingPlatform[16];
        for(int i = movingPlatforms.length - 1; i >= 0; --i) {
            movingPlatforms[i] = new MovingPlatform();
        }

        boneTowerSegments = new BoneTowerSegment[16];
        for(int i = boneTowerSegments.length - 1; i >= 0; --i) {
            boneTowerSegments[i] = new BoneTowerSegment();
        }
    }

    public void addGameObject(final GameObjectType type, int x, int y,
                              final boolean left, final boolean active, BotState botState, Coordinates currentTile, PlayerController playerController) {

        final MapRoutes mapRoutes = currentSubstage.getMapRoutes();
        final MapElement[][] map = mapRoutes.map;

        x += type.xOffset + cameraX;
        if (x < 0) {
            return;
        } else if (x >= mapRoutes.pixelsWidth) {
            return;
        }

        final int Y = y + type.yOffset;
        y += type.height;
        if (y < 0) {
            return;
        } else if (y >= mapRoutes.pixelsHeight) {
            return;
        }

        final GameObject obj = gameObjects[objsCount];
        obj.type = type;
        obj.x = x;
        obj.y = y;
        obj.distanceX = abs(x - botState.getPlayerX());
        obj.distanceY = abs(y - botState.getPlayerY());
        obj.left = left;
        obj.active = active;
        obj.playerFacing = botState.isPlayerLeft() ^ (botState.getPlayerX() < x);

        obj.x1 = x - type.xRadius;
        obj.x2 = x + type.xRadius;
        obj.y1 = Y - type.yRadius;
        obj.y2 = Y + type.yRadius;

        if (type == GameObjectType.CANDLES) {
            obj.onPlatform = false;
            obj.distance = MAX_DISTANCE;
            final int cy = (y >> 4) - 1;
            final int[] whipDistances = Whip.WHIP_DISTANCES[botState.getWhipLength() == 2 ? 1 : 0];

            for(int i = whipDistances.length - 1; i >= 0; --i) {
                final int sx = obj.x - whipDistances[i];
                final int pxLeft;
                final int pxRight;
                if ((sx & 0xF) >= 8) {
                    pxLeft = sx >> 4;
                    pxRight = pxLeft + 1;
                } else {
                    pxRight = sx >> 4;
                    pxLeft = pxRight - 1;
                }
                if (pxLeft < 0 || pxRight < 0) {
                    continue;
                }
                final int hLeft = map[cy][pxLeft].height;
                final int hRight = map[cy][pxRight].height;
                if (hLeft <= hRight && hLeft >= 1 && hLeft <= 4) {
                    final int py = cy + hLeft;
                    final int dist = mapRoutes.getDistance(pxLeft, py, currentTile);
                    if (dist < MAX_DISTANCE) {
                        if (dist < obj.distance) {
                            obj.distance = dist;
                            obj.platformX = pxLeft;
                            obj.platformY = py;
                            obj.active = whipDistances[i] > 16; // Grind with holy water
                        }
                        break;
                    }
                }
            }
            for(int i = whipDistances.length - 1; i >= 0; --i) {
                final int sx = obj.x + whipDistances[i];
                final int pxLeft;
                final int pxRight;
                if ((sx & 0xF) >= 8) {
                    pxLeft = sx >> 4;
                    pxRight = pxLeft + 1;
                } else {
                    pxRight = sx >> 4;
                    pxLeft = pxRight - 1;
                }
                if (pxLeft >= mapRoutes.width || pxRight >= mapRoutes.width) {
                    continue;
                }
                final int hLeft = map[cy][pxLeft].height;
                final int hRight = map[cy][pxRight].height;
                if (hRight <= hLeft && hRight >= 1 && hRight <= 4) {
                    final int py = cy + hRight;
                    final int dist = mapRoutes.getDistance(pxRight, py, currentTile);
                    if (dist < MAX_DISTANCE) {
                        if (dist < obj.distance) {
                            obj.distance = dist;
                            obj.platformX = pxRight;
                            obj.platformY = py;
                            obj.active = whipDistances[i] > 16; // Grind with holy water
                        }
                        break;
                    }
                }
            }
            if (obj.distance == MAX_DISTANCE) {
                final int dy = cy + 1;
                for(int i = whipDistances.length - 1; i >= 0; --i) {
                    final int sx = obj.x - whipDistances[i];
                    final int pxLeft;
                    final int pxRight;
                    if ((sx & 0xF) >= 8) {
                        pxLeft = sx >> 4;
                        pxRight = pxLeft + 1;
                    } else {
                        pxRight = sx >> 4;
                        pxLeft = pxRight - 1;
                    }
                    if (pxLeft < 0 || pxRight < 0) {
                        continue;
                    }
                    final int hLeft = map[dy][pxLeft].height;
                    final int hRight = map[dy][pxRight].height;
                    if (hLeft <= hRight && hLeft >= 1 && hLeft <= 3) {
                        final int py = dy + hLeft;
                        final int dist = mapRoutes.getDistance(pxLeft, py, currentTile);
                        if (dist < MAX_DISTANCE) {
                            if (dist < obj.distance) {
                                obj.distance = dist;
                                obj.platformX = pxLeft;
                                obj.platformY = py;
                                obj.active = false;        // Whip only
                            }
                            break;
                        }
                    }
                }
                for(int i = whipDistances.length - 1; i >= 0; --i) {
                    final int sx = obj.x + whipDistances[i];
                    final int pxLeft;
                    final int pxRight;
                    if ((sx & 0xF) >= 8) {
                        pxLeft = sx >> 4;
                        pxRight = pxLeft + 1;
                    } else {
                        pxRight = sx >> 4;
                        pxLeft = pxRight - 1;
                    }
                    if (pxLeft >= mapRoutes.width || pxRight >= mapRoutes.width) {
                        continue;
                    }
                    final int hLeft = map[dy][pxLeft].height;
                    final int hRight = map[dy][pxRight].height;
                    if (hRight <= hLeft && hRight >= 1 && hRight <= 3) {
                        final int py = dy + hRight;
                        final int dist = mapRoutes.getDistance(pxRight, py, currentTile);
                        if (dist < MAX_DISTANCE) {
                            if (dist < obj.distance) {
                                obj.distance = dist;
                                obj.platformX = pxRight;
                                obj.platformY = py;
                                obj.active = false;        // Whip only
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            obj.supportX = x;
            obj.platformX = x >> 4;
            obj.platformY = y >> 4;
            obj.onPlatform = playerController.isOnOrInPlatform(mapRoutes, x, y, currentTile);
            if (!obj.onPlatform) {
                if (obj.onPlatform == playerController.isOnOrInPlatform(mapRoutes, x - 4, y, currentTile)) {
                    obj.supportX = x - 4;
                    obj.platformX = (x - 4) >> 4;
                    obj.platformY = y >> 4;
                } else if (obj.onPlatform = playerController.isOnOrInPlatform(mapRoutes, x + 4, y, currentTile)) {
                    obj.supportX = x + 4;
                    obj.platformX = (x + 4) >> 4;
                    obj.platformY = y >> 4;
                }
            }
            if (obj.onPlatform) {
                obj.distance = mapRoutes.getDistance(obj, currentTile);
            } else {
                final int height = map[obj.platformY][obj.platformX].height;
                if (height == MAX_HEIGHT) {
                    obj.distance = MAX_DISTANCE;
                } else {
                    obj.platformY += height;
                    obj.distance = mapRoutes.getDistance(obj, currentTile);
                }
            }
        }

        obj.distTier = ((0xFFF - min(0xFFF, obj.distance)) << 8)
                | (0xFF - min(0xFF, obj.distanceX));

        objsCount++;
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


    public void addBoneTowerSegment(final int x, final int y) {
        for(int i = boneTowerSegmentsCount - 1; i >= 0; --i) {
            final BoneTowerSegment s = boneTowerSegments[i];
            if (x == s.x) {
                if (y == s.y + 16) {
                    return;
                } else if (y == s.y - 16) {
                    s.y -= 16;
                    return;
                }
            }
        }
        final BoneTowerSegment s = boneTowerSegments[boneTowerSegmentsCount++];
        s.x = x;
        s.y = y;
    }

    public void buildBoneTowers(BotState botState, Coordinates currentTile, PlayerController playerController) {
        for(int i = boneTowerSegmentsCount - 1; i >= 0; --i) {
            final BoneTowerSegment s = boneTowerSegments[i];
            addGameObject(GameObjectType.BONE_TOWER, s.x, s.y, false, true, botState, currentTile, playerController);
        }
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
