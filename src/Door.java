import bagel.Image;
import bagel.util.Point;

/**
 * Represents a door in the game that can be locked or unlocked.
 * A door connects one room to another and may trigger transitions
 * between rooms when the player collides with it.
 */
public class Door {
    private final Point position;
    private Image image;
    public final String toRoomName;
    public BattleRoom battleRoom; // Only set if this door is inside a Battle Room
    private boolean unlocked = false;
    private boolean justEntered = false; // True when the player has just entered this room
    private boolean shouldLockAgain = false;

    private static final Image LOCKED = new Image("res/locked_door.png");
    private static final Image UNLOCKED = new Image("res/unlocked_door.png");

    /**
     * This constructor creates a door at a specified position that leads to another room.
     *
     * @param position   The position of the door
     * @param toRoomName The name of the room this door connects to
     */
    public Door(Point position, String toRoomName) {
        this.position = position;
        this.image = LOCKED;
        this.toRoomName = toRoomName;
    }

    /**
     * This constructor creates a door inside a Battle Room.
     *
     * @param position    The position of the door
     * @param toRoomName  The name of the room this door connects to
     * @param battleRoom  The BattleRoom this door belongs to
     */
    public Door(Point position, String toRoomName, BattleRoom battleRoom) {
        this.position = position;
        this.image = LOCKED;
        this.toRoomName = toRoomName;
        this.battleRoom = battleRoom;
    }

    /**
     * This method updates the door each frame.
     * It checks for collisions between the door and the player.
     *
     * @param player The player to check collision with
     */
    public void update(Player player) {
        if (hasCollidedWith(player)) {
            onCollideWith(player);
        } else {
            onNoLongerCollide();
        }
    }

    /**
     * This method draws the door image on the screen.
     */
    public void draw() {
        image.draw(position.x, position.y);
    }

    /**
     * This method unlocks the door and changes its image to unlocked.
     *
     * @param justEntered True if the player has just entered this door's room
     */
    public void unlock(boolean justEntered) {
        unlocked = true;
        image = UNLOCKED;
        this.justEntered = justEntered;
    }

    /**
     * This method checks whether the player has collided with the door.
     *
     * @param player The player to check collision with
     * @return True if the player intersects with the door
     */
    public boolean hasCollidedWith(Player player) {
        return image.getBoundingBoxAt(position)
                .intersects(player.getCurrImage().getBoundingBoxAt(player.getPosition()));
    }

    /**
     * This method handles what happens when the player collides with the door.
     *
     * @param player The player colliding with the door
     */
    private void onCollideWith(Player player) {
        // When the player just entered this room, ignore door transition
        if (unlocked && !justEntered) {
            ShadowDungeon.changeRoom(toRoomName);
        }
        // If the door is locked, push the player back
        if (!unlocked) {
            player.move(player.getPrevPosition().x, player.getPrevPosition().y);
        }
    }

    /**
     * This method handles logic when the player stops colliding with the door.
     * Used to trigger room re-locking and enemy activation in Battle Rooms.
     */
    private void onNoLongerCollide() {
        // When player just moved away from the unlocked door after walking through
        if (unlocked && justEntered) {
            justEntered = false;

            // Re-lock and activate enemies in Battle Room if conditions apply
            if (shouldLockAgain && battleRoom != null && !battleRoom.noMoreEnemies()) {
                unlocked = false;
                image = LOCKED;
                battleRoom.activateEnemies();
            }
        }
    }

    /**
     * This method locks the door and updates its image to locked.
     */
    public void lock() {
        unlocked = false;
        image = LOCKED;
    }

    /**
     * This method returns whether the door is currently unlocked.
     *
     * @return True if the door is unlocked
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * This method flags the door to lock again after the player leaves.
     */
    public void setShouldLockAgain() {
        this.shouldLockAgain = true;
    }

    /**
     * This method returns the position of the door.
     *
     * @return The position of the door
     */
    public Point getPosition() {
        return position;
    }

    /**
     * This method returns the current image representing the door.
     *
     * @return The door's image
     */
    public Image getImage() {
        return image;
    }
}
