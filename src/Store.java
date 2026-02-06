import bagel.Input;
import bagel.Keys;
import bagel.Image;
import bagel.util.Point;

/**
 * Store in the game where the player can upgrade weapons or buy health.
 */
public class Store {

    private static final Image STORE_IMAGE = new Image("res/store.png"); // background for store
    private static final double WEAPON_UPGRADE_COST = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("weaponPurchase"));
    private static final double HEALTH_HEAL_COST = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("healthPurchase"));
    private static final double HEALTH_HEAL_AMOUNT = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("healthBonus"));
    private static final double MAX_WEAPON_LEVEL = 2;
    private final Point position;

    private boolean visible = false;
    private final Player player;

    /**
     * Constructor for the store.
     *
     * @param position The position of the store overlay
     * @param player   Reference to the player object
     */
    public Store(Point position, Player player) {
        this.position = position;
        this.player = player;
    }

    /** Toggle store visibility on/off */
    public void toggle() {
        visible = !visible;
    }

    /**
     * @return True if the store is currently visible, false otherwise
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Update the store state each frame based on player input.
     * Handles weapon upgrades, health purchases, and restarting the game.
     *
     * @param input The current mouse/keyboard input
     */
    public void update(Input input) {
        if (!visible) return;

        // Upgrade weapon (L key)
        if (input.wasPressed(Keys.L) && player.getCoins() >= WEAPON_UPGRADE_COST && player.getWeapon().getLevel() != MAX_WEAPON_LEVEL) {
            player.useCoins(WEAPON_UPGRADE_COST);
            player.upgradeWeapon();
        }

        // Buy health (E key)
        if (input.wasPressed(Keys.E) && player.getCoins() >= HEALTH_HEAL_COST) {
            player.useCoins(HEALTH_HEAL_COST);
            player.addHealth(HEALTH_HEAL_AMOUNT);
        }

        // Restart game (P key)
        if (input.wasPressed(Keys.P)) {
            ShadowDungeon.resetGameState(ShadowDungeon.getGameProps());
        }
    }

    /** Draw the store overlay */
    public void draw() {
        if (!visible) return;
        STORE_IMAGE.draw(position.x, position.y);
    }
}
