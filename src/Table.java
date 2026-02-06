import bagel.Image;
import bagel.util.Point;

/**
 * Table is a static obstacle that blocks player movement.
 */
public class Table extends ObstacleObject {

    /**
     * Constructor for the Table object.
     *
     * @param position The position of the table in the room
     */
    public Table(Point position) {
        super(position);
        setImage(new Image("res/table.png")); // call GameObject constructor
        this.active = true;
    }

    /**
     * Update the table state each frame.
     * Checks for collision with the player and prevents movement through it.
     *
     * @param player The player object
     */
    @Override
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            // set the player back to its previous position if colliding
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
    }
}
