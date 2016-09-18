package voxspell.engine;

/**
 * Static (singleton) link class between the two JavaFX scenes and a save file to access and update common level data
 *
 * Created by nateeo on 16/09/16.
 */
public class LevelData {
    private static int level = -1;
    private static DataIO data = new DataIO();

    /**
     * set current session's selected level
     * @param number
     */
    public static void setLevel(int number) {
        level = number;
    }

    /**
     * get current session's selected level
     * @return
     */
    public static int getLevel() {
        if (level < 1 || level > 10) {
            System.out.println("Error"); //debugging
        }
        return level;
    }

    /**
     * gets maximum unlocked level from file
     * @return
     */
    public static int getMaxEnabledLevel() {
        return data.highestLevelEnabled();
    }

}
