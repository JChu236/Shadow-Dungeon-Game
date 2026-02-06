/**
 * Represents the different weapon types available to the player, each with a level and damage value.
 */
public enum Weapon {
    STANDARD(0, Double.parseDouble(ShadowDungeon.getGameProps().getProperty("weaponStandardDamage"))),
    ADVANCED(1, Double.parseDouble(ShadowDungeon.getGameProps().getProperty("weaponAdvanceDamage"))),
    ELITE(2, Double.parseDouble(ShadowDungeon.getGameProps().getProperty("weaponEliteDamage")));

    private final int level;
    private final double damage;

    /**
     * Constructs a Weapon with the given level and damage.
     *
     * @param level  The weapon's upgrade level
     * @param damage The amount of damage the weapon deals
     */
    Weapon(int level, double damage) {
        this.level = level;
        this.damage = damage;
    }

    /**
     * Returns the weapon's upgrade level.
     *
     * @return The level of the weapon
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the weapon's damage value.
     *
     * @return The damage of the weapon
     */
    public double getDamage() {
        return damage;
    }
}
