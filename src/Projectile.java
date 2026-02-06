import bagel.util.Point;
import bagel.util.Vector2;

/**
 * Abstract class for projectiles that move in a direction.
 * Handles collision detection and movement.
 */
public abstract class Projectile extends GameObject implements Movable {
    /** Direction vector of the projectile */
    public Vector2 direction;

    /**
     * This method constructs a Projectile at the specified position.
     *
     * @param position The initial position of the projectile
     */
    public Projectile(Point position) {
        super(position); // call GameObject constructor
    }

    /**
     * Checks if the projectile has collided with a door.
     *
     * @param door The Door object to check collision against
     * @return true if collision occurs, false otherwise
     */
    public boolean hasCollidedWithDoor(Door door) {
        return getImage().getBoundingBoxAt(getPosition())
                .intersects(door.getImage().getBoundingBoxAt(door.getPosition()));
    }

    /**
     * Checks if the projectile has collided with another game object.
     *
     * @param object The GameObject to check collision against
     * @return true if collision occurs, false otherwise
     */
    public boolean hasCollidedWithObject(GameObject object) {
        return this.getImage().getBoundingBoxAt(position)
                .intersects(object.getImage().getBoundingBoxAt(object.getPosition()));
    }

    /**
     * Moves the projectile to a new position.
     *
     * @param x The new x-coordinate
     * @param y The new y-coordinate
     */
    @Override
    public void move(double x, double y) {
        position = new Point(x, y);
    }
}

