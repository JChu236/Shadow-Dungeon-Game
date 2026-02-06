import bagel.util.Point;

/**
 * Abstract class representing an obstacle in the game.
 * Obstacles can interact with the player in various ways.
 */
public abstract class ObstacleObject extends GameObject {

    /**
     * This method constructs an ObstacleObject at the specified position.
     *
     * @param position The initial position of the obstacle
     */
    public ObstacleObject(Point position) {
        super(position);
    }

    /**
     * This method updates the obstacle each frame, handling interactions with the player.
     *
     * @param player The player interacting with this obstacle
     */
    public abstract void update(Player player);
}
