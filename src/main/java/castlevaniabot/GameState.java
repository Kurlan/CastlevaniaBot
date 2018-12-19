package castlevaniabot;

import castlevaniabot.control.PlayerController;
import castlevaniabot.level.Level;
import castlevaniabot.model.creativeelements.Bone;
import castlevaniabot.model.creativeelements.BoneTowerSegment;
import castlevaniabot.model.creativeelements.MedusaHead;
import castlevaniabot.model.creativeelements.MovingPlatform;
import castlevaniabot.model.creativeelements.RedBat;
import castlevaniabot.model.creativeelements.RedBones;
import castlevaniabot.model.creativeelements.Sickle;
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

    private static final int RED_BONES_THRESHOLD = 120;

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

    private Bone[] bones0;
    private Bone[] bones1;
    private int boneCount0;
    private int boneCount1;

    private RedBones[] redBones0;
    private RedBones[] redBones1;
    private int redBonesCount0;
    private int redBonesCount1;

    private RedBat[] redBats0;
    private RedBat[] redBats1;
    private int redBatsCount0;
    private int redBatsCount1;

    private MedusaHead[] medusaHeads0;
    private MedusaHead[] medusaHeads1;
    private int medusaHeadsCount0;
    private int medusaHeadsCount1;


    private Sickle[] sickles0;
    private Sickle[] sickles1;
    private int sickleCount0;
    private int sickleCount1;

    private int draculaHeadX;
    private int draculaHeadY;
    private int draculaHeadTime;
    private boolean draculaHeadLeft;

    private int crystalBallX;
    private int crystalBallY;
    private int crystalBallTime;

    private int entryDelay;
    private int pauseDelay;

    public GameState() {
        movingPlatforms = new MovingPlatform[16];
        for(int i = movingPlatforms.length - 1; i >= 0; --i) {
            movingPlatforms[i] = new MovingPlatform();
        }

        boneTowerSegments = new BoneTowerSegment[16];
        for(int i = boneTowerSegments.length - 1; i >= 0; --i) {
            boneTowerSegments[i] = new BoneTowerSegment();
        }

        bones0 = new Bone[64];
        bones1 = new Bone[64];

        for(int i = bones0.length - 1; i >= 0; --i) {
            bones0[i] = new Bone();
            bones1[i] = new Bone();
        }

        redBones0 = new RedBones[64];
        redBones1 = new RedBones[64];

        for(int i = redBones0.length - 1; i >= 0; --i) {
            redBones0[i] = new RedBones();
            redBones1[i] = new RedBones();
        }

        redBats0 = new RedBat[64];
        redBats1 = new RedBat[64];

        for(int i = redBats0.length - 1; i >= 0; --i) {
            redBats0[i] = new RedBat();
            redBats1[i] = new RedBat();
        }

        medusaHeads0 = new MedusaHead[64];
        medusaHeads1 = new MedusaHead[64];

        for(int i = medusaHeads0.length - 1; i >= 0; --i) {
            medusaHeads0[i] = new MedusaHead();
            medusaHeads1[i] = new MedusaHead();
        }

        sickles0 = new Sickle[64];
        sickles1 = new Sickle[64];

        for(int i = sickles0.length - 1; i >= 0; --i) {
            sickles0[i] = new Sickle();
            sickles1[i] = new Sickle();
        }
    }

    public GameObject getType(final GameObjectType type) {
        for(int i = getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = getGameObjects()[i];
            if (obj.type == type) {
                return obj;
            }
        }
        return null;
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

    public void addBone(final GameObjectType type, int x, int y, GameState gameState) {

        final Bone bone = bones1[boneCount1++];

        x += type.xOffset + gameState.getCameraX();
        y += type.yOffset;

        bone.x1 = x - type.xRadius;
        bone.x2 = x + type.xRadius;
        bone.y1 = y - type.yRadius;
        bone.y2 = y + type.yRadius;

        bone.x = x;
        bone.y = y;
    }

    public void buildBones() {
        for(int i = boneCount1 - 1; i >= 0; --i) {
            final Bone b1 = bones1[i];
            b1.vx = b1.vy = 0;
            for(int j = boneCount0 - 1; j >= 0; --j) {
                final Bone b0 = bones0[j];
                if (abs(b1.x1 - b0.x1) <= 8 && abs(b1.y1 - b0.y1) <= 8) {
                    b1.vx = b1.x1 - b0.x1;
                    b1.vy = b1.y1 - b0.y1;
                    if (b1.vx < 0) {
                        b1.left = true;
                    } else if (b1.vx > 0) {
                        b1.left = false;
                    }
                    break;
                }
            }
        }
        final Bone[] temp = bones0;
        bones0 = bones1;
        bones1 = temp;
        boneCount0 = boneCount1;
        boneCount1 = 0;
    }

    public Bone getHarmfulBone(BotState botState) {
        for(int i = boneCount0 - 1; i >= 0; --i) {
            final Bone bone = bones0[i];
            if (bone.vy > 0 && bone.y1 <= botState.getPlayerY() && bone.x2 >= botState.getPlayerX() - 32
                    && bone.x1 <= botState.getPlayerX() + 32) {
                return bone;
            }
        }
        return null;
    }

    public void addRedBones(final int x, final int y, GameState gameState, BotState botState) {

        final RedBones bones = redBones1[redBonesCount1++];

        bones.x = x + 8 + gameState.getCameraX();
        bones.y = y + 16;
        bones.time = abs(botState.getPlayerX() - bones.x) > 96 ? RED_BONES_THRESHOLD : 0;
    }

    public void buildRedBones(GameState gameState, Coordinates currentTile, BotState botState, PlayerController playerController) {

        for(int i = redBonesCount1 - 1; i >= 0; --i) {
            final RedBones b1 = redBones1[i];
            for(int j = redBonesCount0 - 1; j >= 0; --j) {
                final RedBones b0 = redBones0[j];
                if (abs(b1.x - b0.x) <= 4 && abs(b1.y - b0.y) <= 4) {
                    b1.time = b0.time + 1;
                    if (b1.time >= RED_BONES_THRESHOLD) {
                        gameState.addGameObject(GameObjectType.RED_BONES, b1.x - 8 - gameState.getCameraX(),
                                b1.y - 16, false, true, botState, currentTile, playerController);
                    }
                    break;
                }
            }
        }
        final RedBones[] temp = redBones0;
        redBones0 = redBones1;
        redBones1 = temp;
        redBonesCount0 = redBonesCount1;
        redBonesCount1 = 0;
    }

    public void addRedBat(final int x, final int y) {

        final RedBat bat = redBats1[redBatsCount1++];

        bat.x = x + 8 + cameraX;
        bat.y_32 = bat.y_16 = bat.y0 = bat.y = y + 16;
        bat.s = bat.t = 0;
        bat.sameYs = 1;
        bat.left = true;
    }

    public void buildRedBats() {

        for(int i = redBatsCount1 - 1; i >= 0; --i) {
            final RedBat b1 = redBats1[i];
            for(int j = redBatsCount0 - 1; j >= 0; --j) {
                final RedBat b0 = redBats0[j];
                if (abs(b1.x - b0.x) <= 8 && abs(b1.y - b0.y) <= 8) {

                    b1.left = b1.x < b0.x;

                    b1.t = b0.t + 1;
                    if (b1.t >= RedBat.WAVE.length) {
                        b1.t = 0;
                    }

                    b1.y_16 = b0.y_16;
                    b1.y_32 = b0.y_32;
                    b1.s = b0.s + 1;
                    if ((b1.s & 0xF) == 0) {
                        b1.y_32 = b1.y_16;
                        b1.y_16 = b1.y;
                    }

                    b1.x0 = b0.x0;
                    b1.y0 = b0.y0;
                    if (b1.y == b0.y) {
                        b1.sameYs = b0.sameYs + 1;
                        if (b1.sameYs >= 5) {
                            if (b1.s > 32) {
                                if (b1.y < b1.y_32) {
                                    b1.t = 11;
                                    b1.x0 = b1.left ? (b1.x + 12) : (b1.x - 12);
                                    b1.y0 = b1.y + 7;
                                } else {
                                    b1.t = 61;
                                    b1.x0 = b1.left ? (b1.x + 69) : (b1.x - 69);
                                    b1.y0 = b1.y - 7;
                                }
                            } else {
                                b1.t = 11;
                                b1.x0 = b1.left ? (b1.x + 12) : (b1.x - 12);
                                b1.y0 = b1.y + 7;
                            }
                        }
                    }
                    break;
                }
            }
        }
        final RedBat[] temp = redBats0;
        redBats0 = redBats1;
        redBats1 = temp;
        redBatsCount0 = redBatsCount1;
        redBatsCount1 = 0;
    }

    public RedBat getRedBat(final GameObject bat) {
        switch(redBatsCount0) {
            case 0:
                return null;
            case 1:
                return redBats0[0];
            default:
                for(int i = redBatsCount0 - 1; i >= 0; --i) {
                    final RedBat redBat = redBats0[i];
                    if (bat.x == redBat.x && bat.y == redBat.y) {
                        return redBat;
                    }
                }
        }
        return null;
    }

    public MedusaHead getMedusaHead(final GameObject head) {
        switch(medusaHeadsCount0) {
            case 0:
                return null;
            case 1:
                return medusaHeads0[0];
            default:
                for(int i = medusaHeadsCount0 - 1; i >= 0; --i) {
                    final MedusaHead medusaHead = medusaHeads0[i];
                    if (head.x == medusaHead.x && head.y == medusaHead.y) {
                        return medusaHead;
                    }
                }
        }
        return null;
    }


    public void addSickle(final int x, final int y) {
        final Sickle sickle = sickles1[sickleCount1++];
        sickle.x = x;
        sickle.y = y;
        sickle.time = 4;
    }

    public void buildSickles(GameState gameState, BotState botState, Coordinates currentTile, PlayerController playerController) {

        for(int i = sickleCount1 - 1; i >= 0; --i) {
            final Sickle s1 = sickles1[i];
            for(int j = sickleCount0 - 1; j >= 0; --j) {
                final Sickle s0 = sickles0[j];
                if (abs(s1.x - s0.x) <= 4 && abs(s1.y - s0.y) <= 4) {
                    s0.time = -1;
                }
            }
        }

        for(int i = sickleCount0 - 1; i >= 0; --i) {
            final Sickle s0 = sickles0[i];
            if (--s0.time > 0) {
                final Sickle s1 = sickles1[sickleCount1++];
                s1.x = s0.x;
                s1.y = s0.y;
                s1.time = s0.time;
            }
        }

        for(int i = sickleCount1 - 1; i >= 0; --i) {
            final Sickle s = sickles1[i];
            gameState.addGameObject(GameObjectType.SICKLE, s.x, s.y, false, true, botState, currentTile, playerController);
        }

        final Sickle[] temp = sickles0;
        sickles0 = sickles1;
        sickles1 = temp;
        sickleCount0 = sickleCount1;
        sickleCount1 = 0;
    }

    public void addDraculaHead(final int x, final int y, final boolean left) {
        draculaHeadX = x;
        draculaHeadY = y;
        draculaHeadLeft = left;
        draculaHeadTime = 3;
    }

    public void buildDraculaHead(GameState gameState, BotState botState, Coordinates currentTile, PlayerController playerController) {
        if (draculaHeadTime > 0) {
            --draculaHeadTime;
            gameState.addGameObject(GameObjectType.DRACULA_HEAD, draculaHeadX, draculaHeadY,
                    draculaHeadLeft, true, botState, currentTile, playerController);
        }
    }

    public void addCrystalBall(final int x, final int y) {
        crystalBallX = x;
        crystalBallY = y;
        crystalBallTime = 3;
    }

    public void buildCrystalBall(GameState gameState, BotState botState, Coordinates currentTile, PlayerController playerController) {
        if (crystalBallTime > 0) {
            --crystalBallTime;
            gameState.addGameObject(GameObjectType.CRYSTAL_BALL, crystalBallX, crystalBallY,
                    false, true, botState, currentTile, playerController);
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
