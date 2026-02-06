import bagel.util.Point;

/**
 * Represents an abstract enemy in the game.
 * Enemies have health, can take damage, and interact with the player.
 * Specific enemy types (e.g., BulletKin, AshenBulletKin) extend this class.
 */
public abstract class Enemy extends GameObject {
    private boolean dead = false;
    private double health;

    /**
     * This method constructs an Enemy at the specified position.
     *
     * @param position The initial position of the enemy
     */
    public Enemy(Point position) {
        super(position);
        this.active = false;
    }

    /**
     * This method checks if the enemy is dead.
     *
     * @return true if the enemy is dead, false otherwise
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * This method sets whether the enemy is active in the current room.
     *
     * @param active true to activate the enemy, false to deactivate it
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * This method returns the amount of damage the enemy deals on contact with the player.
     *
     * @return The damage value inflicted upon collision
     */
    public double getDamageOnContact() {
        return 0.2;
    }

    /**
     * This method retrieves the current health of the enemy.
     *
     * @return The current health value
     */
    public double getHealth() {
        return health;
    }

    /**
     * This method applies damage to the enemy and checks if it has died.
     *
     * @param damage The amount of damage
     */
    public void takeDamage(double damage) {
        this.health -= damage;
        if (getHealth() <= 0) {
            this.dead = true;
        }
    }

    /**
     * This method sets the health of the enemy.
     *
     * @param health The new health value
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * This abstract method updates the enemy each frame.
     * It must be implemented by all subclasses to define specific behavior.
     *
     * @param player The player to interact with
     */
    public abstract void update(Player player);
}
