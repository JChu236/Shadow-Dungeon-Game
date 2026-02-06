import bagel.*;
import bagel.util.Point;
import java.util.Properties;

/**
 * Main game class that manages initializing the rooms
 * and moving the player between rooms.
 */
public class ShadowDungeon extends AbstractGame {
    public static Properties gameProps;
    public static Properties messageProps;
    public static double screenWidth;
    public static double screenHeight;

    private static String currRoomName;
    private static PrepRoom prepRoom;
    private static BattleRoom battleRoomA;
    private static BattleRoom battleRoomB;
    private static EndRoom endRoom;
    private static Player player;
    private static Store store;
    private final Image background;

    public static final String PREP_ROOM_NAME = "prep";
    public static final String BATTLE_ROOM_A_NAME = "A";
    public static final String BATTLE_ROOM_B_NAME = "B";
    public static final String END_ROOM_NAME = "end";

    /**
     * Constructor for the Shadow Dungeon game.
     *
     * @param gameProps    Properties containing game settings
     * @param messageProps Properties containing message strings
     */
    public ShadowDungeon(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Dungeon");

        ShadowDungeon.gameProps = gameProps;
        ShadowDungeon.messageProps = messageProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));
        this.background = new Image("res/background.png");

        resetGameState(gameProps);
    }

    /**
     * Resets the game state and initializes all rooms and the player.
     *
     * @param gameProps Properties object containing game configuration
     */
    public static void resetGameState(Properties gameProps) {
        prepRoom = new PrepRoom();
        battleRoomA = new BattleRoom(BATTLE_ROOM_A_NAME, BATTLE_ROOM_B_NAME);
        battleRoomB = new BattleRoom(BATTLE_ROOM_B_NAME, END_ROOM_NAME);
        endRoom = new EndRoom();

        prepRoom.initEntities(gameProps);
        battleRoomA.initEntities(gameProps);
        battleRoomB.initEntities(gameProps);
        endRoom.initEntities(gameProps);

        currRoomName = PREP_ROOM_NAME;

        ShadowDungeon.player = new Player(IOUtils.parseCoords(gameProps.getProperty("player.start")));
        ShadowDungeon.store = new Store(IOUtils.parseCoords(gameProps.getProperty("store")), player);
        prepRoom.setPlayer(player);
        prepRoom.setStore(store);
        Fireball.clearAll();
        Bullet.clearAll();
    }

    /**
     * Render the relevant screen based on the keyboard input given by the user
     * and the status of the gameplay.
     *
     * @param input The current mouse/keyboard input
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        background.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        switch (currRoomName) {
            case PREP_ROOM_NAME -> prepRoom.update(input);
            case BATTLE_ROOM_A_NAME -> battleRoomA.update(input);
            case BATTLE_ROOM_B_NAME -> battleRoomB.update(input);
            default -> endRoom.update(input);
        }
    }

    /**
     * Change the current room and move the player to the corresponding door.
     *
     * @param roomName The name of the room to switch to
     */
    public static void changeRoom(String roomName) {
        Bullet.clearAll();
        Fireball.clearAll();

        Door nextDoor;
        switch (roomName) {
            case PREP_ROOM_NAME -> {
                nextDoor = prepRoom.findDoorByDestination();
                if (currRoomName.equals(BATTLE_ROOM_A_NAME)) battleRoomA.stopCurrentUpdateCall();
                currRoomName = PREP_ROOM_NAME;
                nextDoor.unlock(true);
                player.move(nextDoor.getPosition().x, nextDoor.getPosition().y);
                prepRoom.setPlayer(player);
                prepRoom.setStore(store);
            }
            case BATTLE_ROOM_A_NAME -> {
                nextDoor = battleRoomA.findDoorByDestination(currRoomName);
                if (currRoomName.equals(BATTLE_ROOM_B_NAME)) battleRoomB.stopCurrentUpdateCall();
                else if (currRoomName.equals(PREP_ROOM_NAME)) prepRoom.stopCurrentUpdateCall();
                currRoomName = BATTLE_ROOM_A_NAME;
                if (!battleRoomA.isComplete()) nextDoor.setShouldLockAgain();
                nextDoor.unlock(true);
                player.move(nextDoor.getPosition().x, nextDoor.getPosition().y);
                battleRoomA.setPlayer(player);
                battleRoomA.setStore(store);
            }
            case BATTLE_ROOM_B_NAME -> {
                nextDoor = battleRoomB.findDoorByDestination(currRoomName);
                if (currRoomName.equals(BATTLE_ROOM_A_NAME)) battleRoomA.stopCurrentUpdateCall();
                else if (currRoomName.equals(END_ROOM_NAME)) endRoom.stopCurrentUpdateCall();
                currRoomName = BATTLE_ROOM_B_NAME;
                if (!battleRoomB.isComplete()) nextDoor.setShouldLockAgain();
                nextDoor.unlock(true);
                player.move(nextDoor.getPosition().x, nextDoor.getPosition().y);
                battleRoomB.setPlayer(player);
                battleRoomB.setStore(store);
            }
            default -> {
                nextDoor = endRoom.findDoorByDestination();
                if (currRoomName.equals(BATTLE_ROOM_B_NAME)) battleRoomB.stopCurrentUpdateCall();
                currRoomName = END_ROOM_NAME;
                nextDoor.unlock(true);
                player.move(nextDoor.getPosition().x, nextDoor.getPosition().y);
                endRoom.setPlayer(player);
                endRoom.setStore(store);
            }
        }
    }

    /**
     * Changes the current room to the End Room due to game over.
     */
    public static void changeToGameOverRoom() {
        switch (currRoomName) {
            case PREP_ROOM_NAME -> prepRoom.stopCurrentUpdateCall();
            case BATTLE_ROOM_A_NAME -> battleRoomA.stopCurrentUpdateCall();
            case BATTLE_ROOM_B_NAME -> battleRoomB.stopCurrentUpdateCall();
            default -> {}
        }

        endRoom.isGameOver();
        currRoomName = END_ROOM_NAME;
        Point startPos = IOUtils.parseCoords(gameProps.getProperty("player.start"));
        player.move(startPos.x, startPos.y);
        endRoom.setPlayer(player);
        endRoom.setStore(store);
    }

    /** @return The game properties object */
    public static Properties getGameProps() {
        return gameProps;
    }

    /** @return The message properties object */
    public static Properties getMessageProps() {
        return messageProps;
    }

    /**
     * Main entry point for the game.
     *
     * @param args Command line arguments (unused)
     */
    public static void main(String[] args) {
        Properties gameProps = IOUtils.readPropertiesFile("res/app.properties");
        Properties messageProps = IOUtils.readPropertiesFile("res/message.properties");
        ShadowDungeon game = new ShadowDungeon(gameProps, messageProps);
        game.run();
    }
}
