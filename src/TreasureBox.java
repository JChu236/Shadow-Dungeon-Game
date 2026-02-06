import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/**
 * TreasureBox can be unlocked by the player using a key to earn coins.
 */
public class TreasureBox extends GameObject {
    private final double coinValue;

    /**
     * Constructor for TreasureBox.
     *
     * @param position  The position of the treasure box
     * @param coinValue The amount of coins awarded when unlocked
     */
    public TreasureBox(Point position, double coinValue) {
        super(position, new Image("res/treasure_box.png")); // call GameObject constructor
        this.coinValue = coinValue;
        this.active = true;
    }

    /**
     * Update the treasure box state each frame.
     * Unlocks the box if the player collides with it and presses the unlock key,
     * given they have a key available.
     *
     * @param input  The current keyboard/mouse input
     * @param player The player object
     */
    public void update(Input input, Player player) {
        if (hasCollidedWith(player) && input.wasPressed(Keys.K) && player.getKeys() > 0) {
            player.earnCoins(coinValue);
            active = false;
            player.useKey();
        }
    }
}
