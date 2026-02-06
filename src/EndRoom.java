import bagel.Input;
import java.util.Map;
import java.util.Properties;

/**
 * Represents the final room of the game where the ending sequence occurs.
 * The EndRoom contains a single door and a restart area that allows
 * the player to restart after finishing the game.
 */
public class EndRoom extends Room {
    private Door door;
    private RestartArea restartArea;
    public boolean isGameOver = false;

    /**
     * This method initializes all entities that belong to the End Room.
     * It reads from the game properties to create doors and restart areas.
     *
     * @param gameProperties The set of game configuration properties
     */
    @Override
    public void initEntities(Properties gameProperties) {
        String roomSuffix = "." + ShadowDungeon.END_ROOM_NAME;

        for (Map.Entry<Object, Object> entry : gameProperties.entrySet()) {
            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString()
                        .substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();

                switch (objectType) {
                    case "door":
                        String[] coords = propertyValue.split(",");
                        door = new Door(IOUtils.parseCoords(propertyValue), coords[2]);
                        doors.add(door);
                        break;
                    case "restartarea":
                        restartArea = new RestartArea(IOUtils.parseCoords(propertyValue));
                        break;
                }
            }
        }
    }

    /**
     * This method updates the End Room each frame.
     * It manages store interactions, player actions, and the door/restart area states.
     *
     * @param input The current user input
     */
    @Override
    public void update(Input input) {
        // Handle shop or store state
        checkStoreState(input);
        if (paused) return;

        // Draw the ending message (shown when game is complete)
        UserInterface.drawEndMessage(!isGameOver);

        // Lock the door once the game is over
        if (isGameOver) {
            door.lock();
        }

        // Update and draw the door
        door.update(player);
        door.draw();
        if (stopUpdatingEarlyIfNeeded()) return;

        // Update and draw the restart area
        restartArea.update(input, player);
        restartArea.draw();

        // Update and draw the player
        if (player != null) {
            player.update(input);
            player.draw();
        }

        // Handle all bullet interactions (e.g. projectiles, obstacles, enemies)
        handleBullets(input, obstacles, doors, enemies);
    }

    /**
     * This method finds the door in the End Room by its destination.
     * Since there is only one door in this room, it returns that door.
     *
     * @param roomName The name of the room the door leads to
     * @return The door in this room
     */
    @Override
    public Door findDoorByDestination(String roomName) {
        return door;
    }

    /**
     * This method marks the game as over, preventing further progression.
     */
    public void isGameOver() {
        isGameOver = true;
    }
}
