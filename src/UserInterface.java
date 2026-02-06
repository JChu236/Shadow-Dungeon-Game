import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

/**
 * Helper class to display information and messages for the player.
 */
public class UserInterface {

    /**
     * Draw the player stats on the screen.
     *
     * @param health      Player's current health
     * @param coins       Player's current coins
     * @param keys        Player's current keys
     * @param weaponLevel Player's weapon level
     */
    public static void drawStats(double health, double coins, double keys, double weaponLevel) {
        int fontSize = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize"));
        drawData(String.format("%s %.1f", ShadowDungeon.getMessageProps().getProperty("healthDisplay"), health), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("healthStat")));
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("coinDisplay"), coins), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("coinStat")));
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("keyDisplay"), keys), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("keyStat")));
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("weaponDisplay"), weaponLevel), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("weaponStat")));
    }

    /**
     * Draw the start screen showing character selection and instructions.
     */
    public static void drawStartScreen() {
        int charFontSize = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize"));
        final Image robotSprite = new Image("res/robot_sprite.png");
        final Image marineSprite = new Image("res/marine_sprite.png");
        Point robotPosition = IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Robot"));
        Point marinePosition = IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Marine"));

        drawTextCentered("title", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("title.fontSize")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("title.y")));
        drawTextCentered("moveMessage", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("moveMessage.y")));
        drawTextCentered("selectMessage", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("selectMessage.y")));
        drawData(String.format("%s", ShadowDungeon.getMessageProps().getProperty("marineDescription")), charFontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("marineMessage")));
        drawData(String.format("%s", ShadowDungeon.getMessageProps().getProperty("robotDescription")), charFontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("robotMessage")));
        robotSprite.draw(robotPosition.x, robotPosition.y);
        marineSprite.draw(marinePosition.x, marinePosition.y);
    }

    /**
     * Draw the end-game message (win or lose) on the screen.
     *
     * @param win True if the player won, false if lost
     */
    public static void drawEndMessage(boolean win) {
        drawTextCentered(win ? "gameEnd.won" : "gameEnd.lost",
                Integer.parseInt(ShadowDungeon.getGameProps().getProperty("title.fontSize")),
                Double.parseDouble(ShadowDungeon.getGameProps().getProperty("title.y")));
    }

    /**
     * Draw a string centered horizontally at a specific Y coordinate.
     *
     * @param textPath The property path for the text to display
     * @param fontSize Font size
     * @param posY     Vertical position
     */
    public static void drawTextCentered(String textPath, int fontSize, double posY) {
        Font font = new Font("res/wheaton.otf", fontSize);
        String text = ShadowDungeon.getMessageProps().getProperty(textPath);
        double posX = (Window.getWidth() - font.getWidth(text)) / 2;
        font.drawString(text, posX, posY);
    }

    /**
     * Draw arbitrary data at a specific screen location.
     *
     * @param data     Text to draw
     * @param fontSize Font size
     * @param location Screen coordinates
     */
    public static void drawData(String data, int fontSize, Point location) {
        Font font = new Font("res/wheaton.otf", fontSize);
        font.drawString(data, location.x, location.y);
    }
}
