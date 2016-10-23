package voxspell.engine;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Static ('singleton') class to store global application state
 *
 * Created by nhur714 on 16/09/16.
 */

/**
 * TODO: saveAll voice and max level
 */
public class LevelData {
    private static int level = -1;
    private static DataIO data = DataIO.getInstance();
    private static ArrayList<Word> currentWordList;
    private static String voice = data.getVoice();
    private static boolean isReview = false;
    private static int maxLevel = -1;
    private static final String DEFAULT = "./lib/NZCER-spelling-lists.txt";
    
    public static String currentWordFile = DEFAULT;
    public static String currentDataID = uID(currentWordFile);
    public static boolean developerMode = false;

    /**
     * set currentWordList
     */
    public static void setCurrentWordList(ArrayList<Word> words) {
        currentWordList = words;
    }

    /**
     * Set and update currentWordFile (and data)
     */
    public static void setCurrentWordFile(String fileLoc) {
        currentWordFile = fileLoc;
        currentDataID = uID(currentWordFile);
    }

    private static String uID(String file) {
        String base = "default";
        for (String string : file.split("/")) {
            base = string;
        }
        return base.replace(".", "X");
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
        return level;
    }

    /**
     * Get the max level from the wordlist
     * @return
     */
    public static int getMaxLevel() {
        if (maxLevel == -1) { // calculate it
            calculateMaxLevel();
        }
        return maxLevel;
    }

    public static void calculateMaxLevel() {
        if (currentWordFile.equals(DEFAULT)) {
            maxLevel = 11;
            return;
        }
        File file = new File(currentWordFile);
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("%Level")) {
                    count++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Oops!");
            alert.setHeaderText("Could not open the new spelling list");
            alert.setContentText("Will revert to default list");
            alert.showAndWait();
            currentWordFile = DEFAULT;
        }
        maxLevel = count;
    }

    /**
     * gets maximum unlocked level from file
     * @return
     */
    public static int getMaxEnabledLevel() {
        return data.highestLevelEnabled();
    }

    /**
     * set voice
     */
    public static void setVoice(String toSet) {
        voice = toSet;
        data.setVoice(voice); // update file
    }

    /**
     * get voice
     */
    public static String getVoice() {
        return voice;
    }

    /**
     * Get words that have been faulted/failed right after a quiz
     * @param level
     * @return
     */
    public static ArrayList<Word> getReviewWords(int level) {
        ArrayList<Word> wordList = new ArrayList<Word>();
        for (Word word : currentWordList) {
            if (word.getMastered() == 0) {
                wordList.add(word);
            }
        }
        return wordList;
    }

    public static boolean isReview() {
        return isReview;
    }

    public static void setIsReview(boolean isReview) {
        LevelData.isReview = isReview;
    }

    public static void updateWordList(String update) {
        currentWordFile = update;
        currentDataID = uID(update);
    }
}
