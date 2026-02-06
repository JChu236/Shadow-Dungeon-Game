import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.List;

/**
 * This class represents a KeyBulletKin enemy that follows a path and damages the player on contact.
 */
public class KeyBulletKin extends Enemy implements Movable {

    private final List<Point> path;           // path of points
    private int currentTargetIndex;     // which point we're heading toward
    private final double speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("keyBulletKinSpeed")); // speed per frame

    /**
     * This method constructs a KeyBulletKin that moves along a given path.
     *
     * @param path The list of points the KeyBulletKin will follow
     */
    public KeyBulletKin(List<Point> path) {
        super(path.get(0));             // Enemy/GameObject expects a single Point
        this.path = path;
        this.currentTargetIndex = 1;    // start heading to the 2nd point

        setHealth(Double.parseDouble(ShadowDungeon.getGameProps().getProperty("keyBulletKinHealth")));
        setImage(new Image("res/key_bullet_kin.png"));
    }

    /**
     * This method updates the KeyBulletKin each frame, handling collisions and movement along its path.
     *
     * @param player The player to check for collision
     */
    @Override
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            player.receiveDamage(getDamageOnContact());
        }
        moveAlongPath();
    }

    /**
     * This method moves the KeyBulletKin along its predefined path, looping back to the start if necessary.
     */
    private void moveAlongPath() {
        if (path == null || path.size() < 2) {
            return; // not enough points to move
        }

        Point target = path.get(currentTargetIndex);
        Vector2 direction = target.asVector().sub(position.asVector());
        double distance = direction.length();

        if (distance <= speed) {
            position = target;

            currentTargetIndex++;
            if (currentTargetIndex >= path.size()) {
                currentTargetIndex = 0;   // restart path
            }
        } else {
            direction = direction.normalised().mul(speed);
            position = position.asVector().add(direction).asPoint();
        }
    }

    /**
     * This method moves the KeyBulletKin to the specified coordinates.
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     */
    @Override
    public void move(double x, double y) {
        position = new Point(x, y);
    }
}
