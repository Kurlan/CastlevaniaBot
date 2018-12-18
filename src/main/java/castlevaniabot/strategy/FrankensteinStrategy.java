package castlevaniabot.strategy;

import castlevaniabot.BotState;
import castlevaniabot.CastlevaniaBot;
import castlevaniabot.GameState;
import castlevaniabot.control.PlayerController;
import castlevaniabot.model.gameelements.GameObject;

import java.util.concurrent.ThreadLocalRandom;

import static castlevaniabot.model.creativeelements.Weapon.AXE;
import static castlevaniabot.model.creativeelements.Weapon.BOOMERANG;
import static castlevaniabot.model.creativeelements.Weapon.DAGGER;
import static castlevaniabot.model.creativeelements.Weapon.HOLY_WATER;
import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class FrankensteinStrategy implements Strategy {

    private GameObject frank;
    private int lastFrankX;
    private int frankVx;
    private int avoidFrank;

    private GameObject igor;
    private int lastIgorX;
    private int lastIgorY;
    private int igorVx;
    private int igorVy;
    private int avoidIgor;
    private boolean igorLeft;

    private GameObject fireball;
    private int lastFireballX;
    private int lastFireballY;
    private int fireballVx;
    private int fireballVy;
    private int fireballDist;

    public boolean done;

    private final CastlevaniaBot b;
    private final BotState botState;
    private final GameState gameState;
    private final PlayerController playerController;

    public FrankensteinStrategy(final CastlevaniaBot b, final BotState botState, final GameState gameState, final PlayerController playerController) {
        this.b = b;
        this.botState = botState;
        this.gameState = gameState;
        this.playerController = playerController;
    }

    @Override
    public void init() {
        frank = igor = fireball = null;
        avoidIgor = avoidFrank = 0;
        done = false;
    }

    @Override
    public void step() {

        updateObjects();

        if (b.hearts > 0) {
            switch (botState.getWeapon()) {
                case AXE:
                    stepAxeStrategy();
                    break;
                case BOOMERANG:
                    stepBoomerangStrategy();
                    break;
                case DAGGER:
                    stepBoomerangStrategy();
                    break;
                case HOLY_WATER:
                    stepNoWeaponsStrategy();
                    break;
                default:
                    stepNoWeaponsStrategy();
                    break;
            }
        } else {
            stepNoWeaponsStrategy();
        }
    }

    private void stepAxeStrategy() {

        boolean canWalkTowardFrank = true;

        if (handleFireball()) {
            avoidIgor = avoidFrank = 0;
            canWalkTowardFrank = false;
        }

        if (handleIgor()) {
            avoidFrank = 0;
            canWalkTowardFrank = false;
        }

        if (frank != null) {
            final int offsetX = frankVx << 4;
            if (avoidFrank > 0) {
                --avoidFrank;
                gameState.getCurrentSubstage().moveAwayFromTarget(frank.x);
            } else if (frank.distanceX < 24) {
                avoidFrank = 30 + ThreadLocalRandom.current().nextInt(31);
                gameState.getCurrentSubstage().moveAwayFromTarget(frank.x);
            } else if (b.isInStandingWhipRange(frank, offsetX, 0)) {
                if (!gameState.isWeaponing() && b.face(frank)) {
                    playerController.whip(gameState);
                }
            } else if (b.isInKneelingWhipRange(frank, offsetX, 0)) {
                playerController.kneel();
                if (!gameState.isWeaponing() && b.kneeling && b.face(frank)) {
                    playerController.whip(gameState);
                }
            } else if (!gameState.isWeaponing() && b.canHitWithAxe(botState.getPlayerX() >> 4, botState.getPlayerY() >> 4,
                    offsetX, 0, frank) && b.face(frank)) {
                playerController.useWeapon(gameState);
            } else if (canWalkTowardFrank) {
                gameState.getCurrentSubstage().moveToward(frank);
            }
        }
    }

    private void stepBoomerangStrategy() {

        if (handleFireball()) {
            avoidIgor = avoidFrank = 0;
        }

        if (handleIgor()) {
            avoidFrank = 0;
        }

        if (frank != null) {
            final int offsetX = frankVx << 4;
            if (avoidFrank > 0) {
                --avoidFrank;
                gameState.getCurrentSubstage().moveAwayFromTarget(frank.x);
            } else if (frank.distanceX < 24) {
                avoidFrank = 30 + ThreadLocalRandom.current().nextInt(31);
                gameState.getCurrentSubstage().moveAwayFromTarget(frank.x);
            } else if (b.isInStandingWhipRange(frank, offsetX, 0)) {
                if (!gameState.isWeaponing() && b.face(frank)) {
                    if (botState.getPlayerY() == frank.y) {
                        playerController.whipOrWeapon(gameState, botState);
                    } else {
                        playerController.whip(gameState);
                    }
                }
            } else if (b.isInKneelingWhipRange(frank, offsetX, 0)) {
                playerController.kneel();
                if (!gameState.isWeaponing() && b.kneeling && b.face(frank)) {
                    playerController.whip(gameState);
                }
            } else {
                if (frank.x < 992 && !gameState.isWeaponing() && b.face(frank)
                        && botState.getPlayerY() == frank.y) {
                    playerController.whipOrWeapon(gameState, botState);
                } else {
                    gameState.getCurrentSubstage().moveToward(frank);
                }
            }
        }
    }

    private void stepNoWeaponsStrategy() {

        boolean canWalkTowardFrank = true;

        if (handleFireball()) {
            avoidIgor = avoidFrank = 0;
            canWalkTowardFrank = false;
        }

        if (handleIgor()) {
            avoidFrank = 0;
            canWalkTowardFrank = false;
        }

        if (frank != null) {
            final int offsetX = frankVx << 4;
            if (avoidFrank > 0) {
                --avoidFrank;
                gameState.getCurrentSubstage().moveAwayFromTarget(frank.x);
            } else if (frank.distanceX < 24) {
                avoidFrank = 30 + ThreadLocalRandom.current().nextInt(31);
                gameState.getCurrentSubstage().moveAwayFromTarget(frank.x);
            } else if (b.isInStandingWhipRange(frank, offsetX, 0)) {
                if (!gameState.isWeaponing() && b.face(frank)) {
                    playerController.whipOrWeapon(gameState, botState);
                }
            } else if (b.isInKneelingWhipRange(frank, offsetX, 0)) {
                playerController.kneel();
                if (!gameState.isWeaponing() && b.kneeling && b.face(frank)) {
                    playerController.whip(gameState);
                }
            } else if (canWalkTowardFrank) {
                gameState.getCurrentSubstage().moveToward(frank);
            }
        }
    }

    private boolean handleIgor() {

        if (igor == null || igor.distanceX > 80 || igor.y2 < botState.getPlayerY() - 56) {
            return false;
        }

        final int vx = igorVx << 4;
        final int vy = igorVy << 4;

        if (avoidIgor > 0) {
            --avoidIgor;
            if (igorLeft) {
                gameState.getCurrentSubstage().routeLeft();
            } else {
                gameState.getCurrentSubstage().routeRight();
            }
            return true;
        } else if (b.isInStandingWhipRange(igor, vx, vy)) {
            if (!gameState.isWeaponing() && b.face(igor)) {
                playerController.whip(gameState);
            }
            return true;
        } else if (b.isInKneelingWhipRange(igor, vx, vy)) {
            playerController.kneel();
            if (b.kneeling && !gameState.isWeaponing() && b.face(igor)) {
                playerController.whip(gameState);
            }
            return true;
        } else if ((vy < 0 && igor.y < botState.getPlayerY() - 16 && igor.distanceX < 56)
                || (igor.x1 + vx <= botState.getPlayerX() + 8
                && igor.x2 + vx >= botState.getPlayerX() - 8
                && igor.y2 + vy >= botState.getPlayerY() - 32
                && igor.y1 + vy <= botState.getPlayerY())) {
            avoidIgor = 23 + ThreadLocalRandom.current().nextInt(17);
            igorLeft = vx > 0;
            return true;
        }

        return false;
    }

    private boolean handleFireball() {

        if (fireball == null || fireballDist > 9216) {
            return false;
        }

        if (fireball.left) {
            if (fireball.x2 < botState.getPlayerX() - 16) {
                return false;
            }
        } else {
            if (fireball.x1 > botState.getPlayerX() + 16) {
                return false;
            }
        }

        if (fireball.y2 >= botState.getPlayerY() - 32 && fireball.y1 <= botState.getPlayerY()) {
            final int offsetX = fireballVx << 4;
            final int offsetY = fireballVy << 4;
            if (fireball.distanceX < 24) {
                final boolean flyingHigh = fireball.y < botState.getPlayerY() - 16;
                if (flyingHigh) {
                    playerController.kneel();
                    return true;
                } else if (!flyingHigh && b.canJump) {
                    playerController.jump(botState);
                    return true;
                }
            } else if (b.isInStandingWhipRange(fireball, offsetX, offsetY)) {
                if (b.face(fireball) && !gameState.isWeaponing()) {
                    playerController.whip(gameState);
                    return true;
                }
            } else if (b.isInKneelingWhipRange(fireball, offsetX, offsetY)) {
                if (b.face(fireball)) {
                    playerController.kneel();
                    if (b.kneeling && !gameState.isWeaponing()) {
                        playerController.whip(gameState);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void updateObjects() {

        final int px = botState.getPlayerX();
        final int py = botState.getPlayerY() - 16;

        frank = igor = fireball = null;
        fireballDist = Integer.MAX_VALUE;
        final GameObject[] objs = gameState.getGameObjects();
        for (int i = gameState.getObjsCount() - 1; i >= 0; --i) {
            final GameObject obj = objs[i];
            switch (obj.type) {
                case FRANKENSTEIN:
                    frank = obj;
                    frankVx = obj.x - lastFrankX;
                    lastFrankX = obj.x;
                    break;
                case FLEAMAN:
                    igor = obj;
                    igorVx = obj.x - lastIgorX;
                    igorVy = obj.y - lastIgorY;
                    lastIgorX = obj.x;
                    lastIgorY = obj.y;
                    break;
                case FIREBALL: {
                    final int dx = obj.x - px;
                    final int dy = obj.y - py;
                    final int dist = dx * dx + dy * dy;
                    if (dist < fireballDist) {
                        fireballDist = dist;
                        fireball = obj;
                    }
                    break;
                }
            }
        }
        if (fireball != null) {
            final int vx = fireball.x - lastFireballX;
            if (signum(vx) != signum(fireballVx) || abs(vx) > 8) {
                fireballVx = 0;
            } else if (abs(vx) > abs(fireballVx)) {
                fireballVx = vx;
            }
            final int vy = fireball.y - lastFireballY;
            if (signum(vy) != signum(fireballVy) || abs(vy) > 8) {
                fireballVy = 0;
            } else if (abs(vy) > abs(fireballVy)) {
                fireballVx = vy;
            }
            lastFireballX = fireball.x;
            lastFireballY = fireball.y;
        }

        if (frank == null && igor == null && fireball == null) {
            done = true;
        }
    }
}