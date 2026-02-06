import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a fireball projectile fired by enemies.
 * Fireballs travel in a straight line toward a target, dealing damage to the player
 * and deactivating upon collision or leaving the screen.
 */
public class Fireball extends Projectile {
    private static final Image FIREBALL_IMAGE = new Image("res/fireball.png");
    private static final double SPEED = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("fireballSpeed"));
    private static final double DAMAGE = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("fireballDamage"));

    /** List of all active fireballs currently in the game. */
    private static final List<Fireball> fireballs = new ArrayList<>();

    /**
     * This method constructs a Fireball that travels from a starting point toward a target point.
     *
     * @param start  The initial position of the fireball
     * @param target The target point the fireball will move toward
     */
    public Fireball(Point start, Point target) {
        super(start);
        setImage(FIREBALL_IMAGE);

        // Compute normalized direction vector toward target
        Vector2 direction = target.asVector().sub(start.asVector());
        this.direction = direction.normalised();
        this.active = true;
    }

    /**
     * This method updates the fireball’s position, handles collisions,
     * and draws it on the screen if it remains active.
     *
     * @param player    The player to check for collisions with
     * @param obstacles The list of obstacle objects to test for collisions
     * @param doors     The list of doors to check against
     */
    public void update(Player player, List<ObstacleObject> obstacles, List<Door> doors) {
        if (!active) return;

        // Move fireball
        double newX = getPosition().x + direction.x * SPEED;
        double newY = getPosition().y + direction.y * SPEED;
        move(newX, newY);

        // Remove if off-screen
        if (newX < 0 || newX > Window.getWidth() || newY < 0 || newY > Window.getHeight()) {
            deactivate();
            return;
        }

        // Check for collisions with obstacles, player, or doors
        if (checkCollision(player, obstacles, doors)) {
            deactivate();
            return;
        }

        // Draw active fireball
        draw();
    }

    /**
     * This method checks for collisions between the fireball and the player,
     * obstacles, or doors.
     *
     * @param player    The player to check for collision
     * @param obstacles The list of obstacles (walls, tables, baskets)
     * @param doors     The list of doors to check against
     * @return true if a collision occurred, false otherwise
     */
    private boolean checkCollision(Player player, List<ObstacleObject> obstacles, List<Door> doors) {
        if (hasCollidedWith(player)) {
            player.receiveDamage(DAMAGE);
            return true;
        }

        for (ObstacleObject obstacle : obstacles) {
            if (obstacle instanceof Wall || obstacle instanceof Table || obstacle instanceof Basket) {
                if (hasCollidedWithObject(obstacle)) return true;
            }
        }

        for (Door door : doors) {
            if (!door.isUnlocked() && hasCollidedWithDoor(door)) return true;
        }

        return false;
    }

    /**
     * This method creates and adds a new fireball to the active list.
     *
     * @param start  The starting position of the fireball
     * @param target The target position the fireball should move toward
     */
    public static void spawn(Point start, Point target) {
        fireballs.add(new Fireball(start, target));
    }

    /**
     * This method updates all active fireballs, removes inactive ones,
     * and handles collisions.
     *
     * @param player    The player to check for collisions
     * @param obstacles The list of obstacles in the room
     * @param doors     The list of doors in the room
     */
    public static void updateAll(Player player, List<ObstacleObject> obstacles, List<Door> doors) {
        Iterator<Fireball> instance = fireballs.iterator();
        while (instance.hasNext()) {
            Fireball fireball = instance.next();
            fireball.update(player, obstacles, doors);
            if (!fireball.isActive()) {
                instance.remove();
            }
        }
    }

    /**
     * This method clears all active fireballs (e.g., when changing rooms).
     */
    public static void clearAll() {
        fireballs.clear();
    }
}
