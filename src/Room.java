import bagel.Input;
import bagel.Keys;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing a room in the dungeon.
 * Handles common functionality such as player, store, and entity updates.
 */
public abstract class Room {

    public Player player;
    public boolean stopCurrentUpdateCall = false;
    public Store store;
    public boolean paused = false;

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<TreasureBox> treasureBoxes = new ArrayList<>();
    public ArrayList<ObstacleObject> obstacles = new ArrayList<>();
    public ArrayList<River> rivers = new ArrayList<>();
    public ArrayList<Door> doors = new ArrayList<>();

    /**
     * Initialize all entities for the room from the properties file.
     *
     * @param gameProperties Properties object containing entity information
     */
    public abstract void initEntities(java.util.Properties gameProperties);

    /**
     * Update all entities in the room each frame.
     *
     * @param input Input object containing player actions
     */
    public abstract void update(Input input);

    /**
     * Set the player for this room.
     *
     * @param player The player object
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Set the store for this room.
     *
     * @param store The store object
     */
    public void setStore(Store store) {
        this.store = store;
    }

    /**
     * Checks and updates the state of the store when SPACE is pressed.
     *
     * @param input Input object containing player actions
     */
    public void checkStoreState(Input input) {
        if (input.wasPressed(Keys.SPACE) && store != null) {
            paused = !paused;
            store.toggle();
        }

        if (paused && store != null) {
            store.update(input);
            store.draw();
        }
    }

    /**
     * Signal to stop updating early (e.g., when switching rooms).
     */
    public void stopCurrentUpdateCall() {
        stopCurrentUpdateCall = true;
    }

    /**
     * Helper for subclasses to stop updating early.
     *
     * @return true if update should stop, false otherwise
     */
    public boolean stopUpdatingEarlyIfNeeded() {
        if (stopCurrentUpdateCall) {
            player = null;
            stopCurrentUpdateCall = false;
            return true;
        }
        return false;
    }

    /**
     * Find a door in this room by its destination room name.
     *
     * @param roomName The destination room name
     * @return The door leading to the specified room
     */
    public abstract Door findDoorByDestination(String roomName);

    /**
     * Helper for single-door rooms to use no-argument version.
     *
     * @return The door in the room
     */
    public Door findDoorByDestination() {
        return findDoorByDestination(null);
    }

    /**
     * Handles firing and updating all bullets in the room.
     *
     * @param input       Input object containing player actions
     * @param obstacles   List of obstacles in the room
     * @param doors       List of doors in the room
     * @param enemies     List of enemies in the room
     */
    public void handleBullets(Input input, List<ObstacleObject> obstacles, List<Door> doors, List<Enemy> enemies) {
        if (player != null) {
            Bullet.handleFiring(input, player.getPosition(), player);
            Bullet.updateAll(player, obstacles, doors, enemies);
        }
    }
}
