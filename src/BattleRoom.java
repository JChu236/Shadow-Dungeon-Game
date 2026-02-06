import bagel.Input;
import java.util.ArrayList;
import java.util.Map;
import bagel.util.Point;
import java.util.List;
import java.util.Properties;

/**
 * This class represents a battle room in the dungeon.
 * Enemies must be defeated to unlock doors and progress.
 */
public class BattleRoom extends Room {
    private Door primaryDoor, secondaryDoor;
    public boolean isComplete = false;
    private final String roomName;
    public ArrayList<Key> keys = new ArrayList<>();

    /**
     * This method constructs a BattleRoom with a name and the next room's name.
     *
     * @param roomName The name of this room
     * @param nextRoomName The name of the next room
     */
    public BattleRoom(String roomName, String nextRoomName) {
        this.roomName = roomName;
    }

    /**
     * This method initializes all entities in the room from the game properties.
     *
     * @param gameProperties The properties file containing room entity definitions
     */
    @Override
    public void initEntities(Properties gameProperties) {
        String roomSuffix = "." + roomName;

        for (Map.Entry<Object, Object> entry : gameProperties.entrySet()) {
            if (entry.getKey().toString().contains(roomSuffix)) {
                String objectType = entry.getKey().toString()
                        .substring(0, entry.getKey().toString().length() - roomSuffix.length());
                String propertyValue = entry.getValue().toString();
                if (propertyValue.equals("0")) continue;

                for (String coords : propertyValue.split(";")) {
                    String[] coordinates = coords.split(",");
                    switch (objectType) {
                        case "primarydoor":
                            primaryDoor = new Door(IOUtils.parseCoords(coords), coordinates[2], this);
                            doors.add(primaryDoor);
                            break;
                        case "secondarydoor":
                            secondaryDoor = new Door(IOUtils.parseCoords(coords), coordinates[2], this);
                            doors.add(secondaryDoor);
                            break;
                        case "bulletKin":
                            enemies.add(new BulletKin(IOUtils.parseCoords(coords)));
                            break;
                        case "ashenBulletKin":
                            enemies.add(new AshenBulletKin(IOUtils.parseCoords(coords)));
                            break;
                        case "wall":
                            obstacles.add(new Wall(IOUtils.parseCoords(coords)));
                            break;
                        case "treasurebox":
                            treasureBoxes.add(new TreasureBox(IOUtils.parseCoords(coords),
                                    Double.parseDouble(coordinates[2])));
                            break;
                        case "river":
                            rivers.add(new River(IOUtils.parseCoords(coords)));
                            break;
                        case "table":
                            obstacles.add(new Table(IOUtils.parseCoords(coords)));
                            break;
                        case "basket":
                            obstacles.add(new Basket(IOUtils.parseCoords(coords)));
                            break;
                    }
                }

                if (objectType.equals("keyBulletKin")) {
                    List<Point> path = new ArrayList<>();
                    for (String coord : propertyValue.split(";")) {
                        path.add(IOUtils.parseCoords(coord));
                    }
                    enemies.add(new KeyBulletKin(path));
                }
            }
        }
    }

    /**
     * This method updates all entities in the room each frame.
     * It handles enemy behavior, player updates, bullets, and interactions with objects.
     *
     * @param input The input representing player actions
     */
    @Override
    public void update(Input input) {
        checkStoreState(input);
        if (paused) return;

        primaryDoor.update(player);
        primaryDoor.draw();
        if (stopUpdatingEarlyIfNeeded()) return;

        secondaryDoor.update(player);
        secondaryDoor.draw();
        if (stopUpdatingEarlyIfNeeded()) return;

        for (River river : rivers) {
            river.update(player);
            river.draw();
        }

        for (TreasureBox treasureBox : treasureBoxes) {
            if (treasureBox.isActive()) {
                treasureBox.update(input, player);
                treasureBox.draw();
            }
        }

        for (ObstacleObject obstacle : obstacles) {
            if (obstacle.isActive()) {
                obstacle.update(player);
                obstacle.draw();
            }
        }

        for (Key key : keys) {
            if (key.isActive()) {
                key.update(player);
                key.draw();
            }
        }

        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                if (enemy.isDead()) {
                    enemy.setActive(false);
                    if (enemy instanceof KeyBulletKin) {
                        keys.add(new Key(enemy.getPosition()));
                    }
                    if (enemy instanceof AshenBulletKin) {
                        switch (player.getCharacter()) {
                            case MARINE -> player.earnCoins(AshenBulletKin.coinsGained);
                            case ROBOT -> player.earnCoins(AshenBulletKin.coinsGained + player.robotBonusCoin);
                        }
                    }
                    if (enemy instanceof BulletKin) {
                        switch (player.getCharacter()) {
                            case MARINE -> player.earnCoins(BulletKin.coinsGained);
                            case ROBOT -> player.earnCoins(BulletKin.coinsGained + player.robotBonusCoin);
                        }
                    }
                }

                if (enemy.isActive()) {
                    enemy.update(player);
                    enemy.draw();
                }
            }
        }

        if (player != null) {
            player.update(input);
            player.draw();
        }

        if (noMoreEnemies() && !isComplete) {
            isComplete = true;
            unlockAllDoors();
        }

        handleBullets(input, obstacles, doors, enemies);
        Fireball.updateAll(player, obstacles, doors);
    }

    /**
     * This method finds a door by its destination room name.
     *
     * @param roomName The name of the target room
     * @return The door leading to the target room
     */
    @Override
    public Door findDoorByDestination(String roomName) {
        if (primaryDoor.toRoomName.equals(roomName)) return primaryDoor;
        return secondaryDoor;
    }

    /**
     * This method unlocks all doors in the room.
     */
    private void unlockAllDoors() {
        primaryDoor.unlock(false);
        secondaryDoor.unlock(false);
    }

    /**
     * This method checks if all enemies in the room are dead.
     *
     * @return true if no enemies remain, false otherwise
     */
    public boolean noMoreEnemies() {
        for (Enemy enemy : enemies) {
            if (!enemy.isDead()) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method activates all enemies in the room.
     */
    public void activateEnemies() {
        for (Enemy enemy : enemies) {
            enemy.setActive(true);
        }
    }

    /**
     * This method returns whether the room has been completed.
     *
     * @return true if the room is complete
     */
    public boolean isComplete() {
        return isComplete;
    }
}