package voxspell.engine;

/**
 * Link class between the two JavaFX scenes and a save file to access and update common level data
 *
 * Created by nateeo on 16/09/16.
 */
public class LevelData {
    private static int level = -1;

    public static void setLevel(int number) {
        level = number;
    }

    public static int getLevel() {
        if (level < 1 || level > 10) {
            System.out.println("Error"); //debugging
        }
        return level;
    }

}
