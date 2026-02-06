import bagel.Image;
import bagel.util.Point;

/**
 * Represents a basket obstacle in the game.
 * The basket blocks the player and can give coins when destroyed.
 */
public class Basket extends ObstacleObject {
    private static final double coinValue = Double.parseDouble(
            ShadowDungeon.getGameProps().getProperty("basketCoin"));

    /**
     * This method constructs a Basket at the specified position.
     *
     * @param position The initial position of the basket
     */
    public Basket(Point position) {
        super(position);
        setImage(new Image("res/basket.png")); // set basket image
        this.active = true;
    }

    /**
     * This method updates the basket each frame.
     * If the player collides with the basket, their position is reset to the previous position.
     *
     * @param player The player to interact with
     */
    @Override
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
    }

    /**
     * This method returns the coin value earned by this basket.
     *
     * @return coin value
     */
    public double getCoinValue() {
        return coinValue;
    }
}