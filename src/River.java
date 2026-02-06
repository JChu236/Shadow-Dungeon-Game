import bagel.Image;
import bagel.util.Point;

/**
 * Hazard that applies damage to the player as long as they are on it.
 */
public class River extends GameObject {

    private final double damagePerFrame;

    /**
     * Constructs a River at the specified position.
     *
     * @param position The position of the river
     */
    public River(Point position) {
        super(position, new Image("res/river.png")); // call GameObject constructor
        damagePerFrame = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("riverDamagePerFrame"));
    }

    /**
     * Updates the river. If the player is on the river and is not a MARINE character,
     * applies damage per frame.
     *
     * @param player The player interacting with the river
     */
    public void update(Player player) {
        if (hasCollidedWith(player) && player.getCharacter() != Character.MARINE) {
            player.receiveDamage(damagePerFrame);
        }
    }
}
