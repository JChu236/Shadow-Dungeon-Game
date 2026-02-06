import bagel.Image;
import bagel.util.Point;

/**
 * This class represents any object in the game.
 */
public abstract class GameObject {
    public Point position;
    private Image image;
    public boolean active;

    /**
     * This method constructs a GameObject at the specified position with an image.
     *
     * @param position The position of the GameObject
     * @param image The image for the GameObject
     */
    public GameObject(Point position, Image image) {
        this.position = position;
        this.image = image;
    }

    /**
     * This method constructs a GameObject at the specified position.
     *
     * @param position The position of the GameObject
     */
    public GameObject(Point position) {
        this.position = position;
    }

    /**
     * This method draws the GameObject at its current position.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

    /**
     * This method checks if the GameObject has collided with the player.
     *
     * @param player The player to check collision with
     * @return true if the GameObject collides with the player, false otherwise
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position)
                .intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }

    /**
     * This method sets the image for the GameObject.
     *
     * @param image The new image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * This method returns the current position of the GameObject.
     *
     * @return The position of the GameObject
     */
    public Point getPosition() {
        return position;
    }

    /**
     * This method returns the image of the GameObject.
     *
     * @return The image of the GameObject
     */
    public Image getImage() {
        return image;
    }

    /**
     * This method checks if the GameObject is active.
     *
     * @return true if the GameObject is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * This method deactivates the GameObject.
     */
    public void deactivate() {
        active = false;
    }
}
