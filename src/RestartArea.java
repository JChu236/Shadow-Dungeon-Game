import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/**
 * Represents an area in the Prep or End Room where the player can trigger a game reset.
 */
public class RestartArea extends GameObject {

    /**
     * Constructs a RestartArea at the specified position.
     *
     * @param position The position of the restart area
     */
    public RestartArea(Point position) {
        super(position, new Image("res/restart_area.png")); // call GameObject constructor
    }

    /**
     * Updates the restart area. If the player collides with it and presses ENTER,
     * the game state is reset.
     *
     * @param input  The current input from the player
     * @param player The player interacting with the area
     */
    public void update(Input input, Player player) {
        if (hasCollidedWith(player) && input.wasPressed(Keys.ENTER)) {
            ShadowDungeon.resetGameState(ShadowDungeon.getGameProps());
        }
    }
}
