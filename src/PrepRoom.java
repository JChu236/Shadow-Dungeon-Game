import bagel.Input;
import bagel.Keys;
import java.util.Map;
import java.util.Properties;

/**
 * Room where the game starts.
 * Allows the player to choose their character and enter the game.
 */
public class PrepRoom extends Room {
    private Door door;
    private RestartArea restartArea;

    /**
     * This method initializes all entities in the PrepRoom
     * based on the provided game properties.
     *
     * @param gameProperties The properties defining the objects in this room
     */
    @Override
    public void initEntities(Properties gameProperties) {
        String roomSuffix = "." + ShadowDungeon.PREP_ROOM_NAME;

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
     * This method updates the PrepRoom each frame,
     * handling player input, drawing, and interactions.
     *
     * @param input The current input state
     */
    @Override
    public void update(Input input) {
        checkStoreState(input);
        if (paused) return;

        UserInterface.drawStartScreen();

        door.update(player);
        door.draw();
        if (stopUpdatingEarlyIfNeeded()) return;

        restartArea.update(input, player);
        restartArea.draw();

        if (player != null) {
            player.update(input);
            player.draw();
        }

        if (input.wasPressed(Keys.M)) {
            player.setCharacter(Character.MARINE);
        }
        if (input.wasPressed(Keys.R)) {
            player.setCharacter(Character.ROBOT);
        }

        // Unlock door after character selection
        if ((input.wasPressed(Keys.R) || input.wasPressed(Keys.M)) && !door.isUnlocked()) {
            door.unlock(false);
        }

        handleBullets(input, obstacles, doors, enemies);
    }

    /**
     * This method finds the door leading to a given room.
     *
     * @param roomName The name of the destination room
     * @return The Door object leading to that room
     */
    @Override
    public Door findDoorByDestination(String roomName) {
        return door;
    }
}
