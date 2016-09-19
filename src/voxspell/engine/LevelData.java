package voxspell.engine;

import java.util.ArrayList;

/**
 * Static ('singleton') class to store global application state
 *
 * Created by nhur714 on 16/09/16.
 */
public class LevelData {
    private static int level = -1;
    private static DataIO data = new DataIO();
    private static ArrayList<Word> currentWordList;

    /**
     * set currentWordList
     */
    public static void setCurrentWordList(ArrayList<Word> words) {
        currentWordList = words;
    }

    /**
     * get currentWordList
     */
    public static ArrayList<Word> getCurrentWordList() {
        return currentWordList;
    }

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
