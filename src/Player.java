import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Player character that can move around and between rooms,
 * defeat enemies, collect coins and keys.
 */
public class Player implements Movable {
    private Character character = Character.DEFAULT;
    private Weapon weapon;

    private Point prevPosition;
    private Point position;
    private Image currImage;
    private double health;
    private final double speed;
    private double coins = 0;
    private double keys = 0;
    private boolean faceLeft = false;

    public int robotBonusCoin = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("robotExtraCoin"));

    private static final Image DEFAULT_RIGHT = new Image("res/player_right.png");
    private static final Image DEFAULT_LEFT = new Image("res/player_left.png");
    private static final Image MARINE_RIGHT = new Image("res/marine_right.png");
    private static final Image MARINE_LEFT = new Image("res/marine_left.png");
    private static final Image ROBOT_RIGHT = new Image("res/robot_right.png");
    private static final Image ROBOT_LEFT = new Image("res/robot_left.png");

    /**
     * This method constructs a Player at the specified position.
     *
     * @param position The initial position of the player
     */
    public Player(Point position) {
        this.position = position;
        this.currImage = DEFAULT_RIGHT;
        this.speed = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("movingSpeed"));
        this.health = Double.parseDouble(ShadowDungeon.getGameProps().getProperty("initialHealth"));
        this.weapon = Weapon.STANDARD;
    }

    /**
     * This method updates the player's position each frame
     * based on input and ensures the player stays within window bounds.
     *
     * @param input The current input state
     */
    public void update(Input input) {
        double currX = position.x;
        double currY = position.y;

        if (input.isDown(Keys.A)) currX -= speed;
        if (input.isDown(Keys.D)) currX += speed;
        if (input.isDown(Keys.W)) currY -= speed;
        if (input.isDown(Keys.S)) currY += speed;

        faceLeft = input.getMouseX() < currX;

        Rectangle rect = currImage.getBoundingBoxAt(new Point(currX, currY));
        Point topLeft = rect.topLeft();
        Point bottomRight = rect.bottomRight();
        if (topLeft.x >= 0 && bottomRight.x <= Window.getWidth() && topLeft.y >= 0 && bottomRight.y <= Window.getHeight()) {
            move(currX, currY);
        }
    }

    /**
     * This method draws the player on the screen along with their stats.
     */
    public void draw() {
        switch (character) {
            case DEFAULT -> currImage = faceLeft ? DEFAULT_LEFT : DEFAULT_RIGHT;
            case MARINE -> currImage = faceLeft ? MARINE_LEFT : MARINE_RIGHT;
            case ROBOT -> currImage = faceLeft ? ROBOT_LEFT : ROBOT_RIGHT;
        }
        currImage.draw(position.x, position.y);
        UserInterface.drawStats(health, coins, keys, weapon.getLevel());
    }

    /**
     * This method increases the player's coin count.
     *
     * @param coins Number of coins to earn
     */
    public void earnCoins(double coins) {
        this.coins += coins;
    }

    /**
     * This method applies damage to the player and triggers game over if health reaches 0.
     *
     * @param damage Amount of damage to apply
     */
    public void receiveDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            ShadowDungeon.changeToGameOverRoom();
        }
    }

    /**
     * Getters and setters for player attributes
     */
    public Point getPosition() { return position; }

    public Image getCurrImage() { return currImage; }

    public Point getPrevPosition() { return prevPosition; }

    public Character getCharacter() { return character; }

    public Weapon getWeapon() { return this.weapon; }

    public double getKeys() { return keys; }

    public double getCoins() { return coins; }

    /**
     * This method uses a key.
     */
    public void useKey() { keys -= 1; }

    /**
     * This method adds a key.
     */
    public void earnKey() { keys += 1; }

    /**
     * This method uses coins.
     */
    public void useCoins(double coins) { this.coins -= coins; }

    /**
     * This method increases the player's health.
     *
     * @param health Amount of health to add
     */
    public void addHealth(double health) { this.health += health; }

    /**
     * This method sets the player's character type and updates the sprite.
     *
     * @param newCharacter The character type to set
     */
    public void setCharacter(Character newCharacter) {
        this.character = newCharacter;
        switch (newCharacter) {
            case MARINE -> this.currImage = MARINE_RIGHT;
            case ROBOT -> this.currImage = ROBOT_RIGHT;
        }
    }

    /**
     * This method upgrades the player's weapon if possible.
     */
    public void upgradeWeapon() {
        if (weapon == Weapon.STANDARD) weapon = Weapon.ADVANCED;
        else if (weapon == Weapon.ADVANCED) weapon = Weapon.ELITE;
    }

    /**
     * This method moves the player to the specified coordinates.
     *
     * @param x X-coordinate to move to
     * @param y Y-coordinate to move to
     */
    @Override
    public void move(double x, double y) {
        prevPosition = position;
        position = new Point(x, y);
    }
}
