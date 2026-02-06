import bagel.Image;
import bagel.util.Point;

/**
 * This class represents a Key that can be collected by the player.
 */
public class Key extends GameObject {

    /**
     * This method constructs a Key at the specified position.
     *
     * @param position The initial position of the Key
     */
    public Key(Point position) {
        super(position, new Image("res/key.png")); // call GameObject constructor
        this.active = true;
    }

    /**
     * This method updates the Key each frame and checks for collision with the player.
     * If the player collides with the Key, it is collected and deactivated.
     *
     * @param player The player to check for collision
     */
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            player.earnKey();
            active = false;
        }
    }
}
