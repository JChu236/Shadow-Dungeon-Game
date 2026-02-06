import bagel.*;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a bullet projectile fired by the player.
 * Bullets move in a straight line, damage enemies, and interact with obstacles.
 */
public class Bullet extends Projectile {
    private static final Image BULLET_IMAGE = new Image("res/bullet.png");
    private static final double SPEED = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletSpeed")); // pixels per frame
    private static final int FIRE_RATE = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("bulletFreq"));   // frames between bullets

    private final double damage;
    private static int cooldown = 0;
    private static final List<Bullet> bullets = new ArrayList<>();

    /**
     * This method constructs a Bullet object at the specified starting position,
     * moving toward a target location based on player input.
     *
     * @param start  The starting position of the bullet
     * @param target The target position the bullet is fired toward
     * @param player The player who fired the bullet
     */
    public Bullet(Point start, Point target, Player player) {
        super(start);
        setImage(BULLET_IMAGE);

        // Compute direction vector toward target
        Vector2 dir = target.asVector().sub(start.asVector());
        direction = dir.normalised();
        this.active = true;
        this.damage = player.getWeapon().getDamage();
    }

    /**
     * This method updates the bullet's position, checks for collisions,
     * and draws the bullet if still active.
     *
     * @param player     The player object
     * @param obstacles  The list of obstacles in the room
     * @param doors      The list of doors in the room
     * @param enemies    The list of enemies in the room
     */
    public void update(Player player, List<ObstacleObject> obstacles, List<Door> doors, List<Enemy> enemies) {
        if (!active) return;

        double newX = getPosition().x + direction.x * SPEED;
        double newY = getPosition().y + direction.y * SPEED;
        move(newX, newY);

        // Remove bullet if it goes off-screen
        if (newX < 0 || newX > Window.getWidth() || newY < 0 || newY > Window.getHeight()) {
            deactivate();
            return;
        }

        // Deactivate bullet upon collision
        if (checkCollision(player, obstacles, doors, enemies)) {
            deactivate();
            return;
        }

        draw();
    }

    /**
     * This method checks for collisions between the bullet and
     * other objects such as obstacles, doors, or enemies.
     *
     * @param player    The player object (for coin gain)
     * @param obstacles The list of obstacles to check collisions with
     * @param doors     The list of doors in the room
     * @param enemies   The list of enemies in the room
     * @return true if a collision occurred, false otherwise
     */
    private boolean checkCollision(Player player, List<ObstacleObject> obstacles, List<Door> doors, List<Enemy> enemies) {

        for (ObstacleObject obstacle : obstacles) {
            if (!obstacle.isActive()) continue;

            if (obstacle instanceof Wall) {
                if (hasCollidedWithObject(obstacle)) return true;
            }

            if (obstacle instanceof Table) {
                if (hasCollidedWithObject(obstacle)) {
                    obstacle.deactivate();
                    return true;
                }
            }

            if (obstacle instanceof Basket) {
                if (hasCollidedWithObject(obstacle)) {
                    obstacle.deactivate();
                    player.earnCoins(((Basket) obstacle).getCoinValue());
                    return true;
                }
            }
        }

        for (Door door : doors) {
            if (!door.isUnlocked() && hasCollidedWithDoor(door)) return true;
        }

        for (Enemy enemy : enemies) {
            if (!enemy.isActive()) continue;
            if (hasCollidedWithObject(enemy)) {
                enemy.takeDamage(damage);
                return true;
            }
        }

        return false;
    }

    /**
     * This method handles player input, firing rate control,
     * and bullet creation when the player shoots.
     *
     * @param input          The input object tracking mouse actions
     * @param playerPosition The current position of the player
     * @param player         The player firing the bullet
     */
    public static void handleFiring(Input input, Point playerPosition, Player player) {
        tickCooldown();

        if ((input.isDown(MouseButtons.RIGHT) || input.isDown(MouseButtons.LEFT)) && canFire()) {
            Point target = new Point(input.getMouseX(), input.getMouseY());
            bullets.add(new Bullet(playerPosition, target, player));
            resetCooldown();
        }
    }

    /**
     * This method updates and draws all active bullets each frame.
     * Inactive bullets are removed from the list.
     *
     * @param player     The player object
     * @param obstacles  The list of obstacles in the room
     * @param doors      The list of doors in the room
     * @param enemies    The list of enemies in the room
     */
    public static void updateAll(Player player, List<ObstacleObject> obstacles, List<Door> doors, List<Enemy> enemies) {
        Iterator<Bullet> instance = bullets.iterator();
        while (instance.hasNext()) {
            Bullet bullet = instance.next();
            bullet.update(player, obstacles, doors, enemies);
            if (!bullet.isActive()) {
                instance.remove();
            }
        }
    }

    /**
     * This method decrements the firing cooldown counter.
     */
    private static void tickCooldown() {
        if (cooldown > 0) cooldown--;
    }

    /**
     * This method checks if the player can fire another bullet.
     *
     * @return true if the cooldown has expired
     */
    private static boolean canFire() {
        return cooldown <= 0;
    }

    /**
     * This method resets the cooldown after a bullet is fired.
     */
    private static void resetCooldown() {
        cooldown = FIRE_RATE;
    }


    /**
     * This method clears all active bullets from the game.
     */
    public static void clearAll() {
        bullets.clear();
    }
}