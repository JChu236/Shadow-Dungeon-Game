import bagel.Image;
import bagel.util.Point;

/**
 * Obstacle that blocks the player from moving through it.
 */
public class Wall extends ObstacleObject {

    /**
     * Constructs a Wall at the specified position.
     *
     * @param position The location of the wall
     */
    public Wall(Point position) {
        super(position);
        setImage(new Image("res/wall.png")); // call GameObject constructor
        this.active = true;
    }

    /**
     * Updates the wall each frame. Prevents the player from moving through it by
     * resetting the player's position if a collision occurs.
     *
     * @param player The player to check collisions against
     */
    @Override
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            // Reset the player's position to its previous position
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
    }
}
