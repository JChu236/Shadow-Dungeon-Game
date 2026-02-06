import bagel.Image;
import bagel.util.Point;

/**
 * Represents an AshenBulletKin enemy in the game.
 * This enemy can collide with the player and shoot fireballs at a set rate.
 */
public class AshenBulletKin extends Enemy {
    private double firingRate;  // frames between shots
    private int cooldown;       // counts down each frame
    public static double coinsGained;

    /**
     * C\This method constructs an AshenBulletKin at the given position.
     * Initializes health, image, shooting frequency, and coin value.
     *
     * @param position The initial position of the enemy
     */
    public AshenBulletKin(Point position) {
        super(position);
        setHealth(Double.parseDouble(ShadowDungeon.getGameProps().getProperty("ashenBulletKinHealth")));
        setImage(new Image("res/ashen_bullet_kin.png"));
        this.firingRate = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("ashenBulletKinShootFrequency"));
        this.cooldown = 0; // ready to shoot immediately
        coinsGained = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("ashenBulletKinCoin"));
    }

    /**
     * This method updates the enemy each frame.
     * Handles collision with the player and firing fireballs.
     *
     * @param player The player to interact with
     */
    @Override
    public void update(Player player) {
        // Deal damage if colliding with player
        if (hasCollidedWith(player)) {
            player.receiveDamage(getDamageOnContact());
        }

        // Shooting logic
        if (cooldown > 0) {
            cooldown--;
        }
        if (cooldown <= 0) {
            // Spawn a fireball toward the player
            Fireball.spawn(getPosition(), player.getPosition());
            cooldown = (int) firingRate;
        }
    }
}