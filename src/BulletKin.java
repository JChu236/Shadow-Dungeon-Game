import bagel.Image;
import bagel.util.Point;

/**
 * This class represents a Bullet Kin enemy that can shoot fireballs
 * toward the player at a fixed firing rate.
 */
public class BulletKin extends Enemy {
    private final double firingRate;   // Frames between shots
    private int cooldown;              // Countdown until next shot
    public static double coinsGained;

    /**
     * This method constructs a BulletKin enemy at the specified position.
     * It initializes its health, image, firing rate, and coin reward.
     *
     * @param position The initial position of the BulletKin
     */
    public BulletKin(Point position) {
        super(position);
        setHealth(Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletKinHealth")));
        setImage(new Image("res/bullet_kin.png"));
        this.firingRate = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletKinShootFrequency"));
        this.cooldown = 0; // Ready to shoot immediately
        coinsGained = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("bulletKinCoin"));
    }

    /**
     * This method updates the BulletKin each frame.
     * It checks for collisions with the player and handles shooting behavior.
     *
     * @param player The player currently in the room
     */
    @Override
    public void update(Player player) {
        // Check for collision with player and apply damage if in contact
        if (hasCollidedWith(player)) {
            player.receiveDamage(getDamageOnContact());
        }

        // Decrease cooldown timer
        if (cooldown > 0) {
            cooldown--;
        }

        // Shoot a fireball toward the player when cooldown expires
        if (cooldown <= 0) {
            Fireball.spawn(getPosition(), player.getPosition());
            cooldown = (int) firingRate;
        }
    }
}