package voxspell.engine;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

/**
 * Singleton class to read and write data to files
 * Created by nhur714 on 16/09/16.
 */
public class DataIO {
    private static DataIO instance = null;
    private ArrayList<Integer> enabledLevels;
    private String voice;
    private ArrayList<ArrayList<Word>> wordData;
    private Money money;
    private TreeSet<Achievement> achievements;
    private String currentWordList;

    private File LEVEL_DATA;
    private File MONEY_DATA = new File(".moneyData.ser");
    private File CURRENT_LIST_DATA = new File(".currentListData.ser");
    private File VOICE_DATA = new File(".voiceData.ser");
    private File WORD_DATA;
    private File ACHIEVEMENT_DATA;

    public static DataIO getInstance() {
        if (instance == null) {
            instance = new DataIO();
        }
        return instance;
    }

    private DataIO() {
        loadAll();
    }

    /**
     * Add level to enabled level list if not already added
     * @param level
     */
    public void enableLevel(int level) {
       if (!enabledLevels.contains(level)) { // add to enabled list if not already enabled
           enabledLevels.add(level);
           saveAll(false);
       }
    }

    /**
     * Get list of achievements
     */
    public TreeSet<Achievement> getAchievements() {
        return achievements;
    }

    /**
     * add an achievement and sort by rarity
     * @param achievement
     */
    public void addAchievement(Achievement achievement) {
        achievements.add(achievement);
        saveObject(ACHIEVEMENT_DATA, achievements);
    }

    /**
     * Get all persistent word data
     * @return 2D Arraylist of [level, list of words]
     */
    public ArrayList<ArrayList<Word>> getWordData() {
        return wordData;
    }

    /**
     * Add a wordlist to the correct level, handles duplicate words
     * @param level level 1-10
     */
    public void addWordList(ArrayList<Word> toAdd, int level) {
        int levelIndex = level - 1;
        if (wordData.get(levelIndex).isEmpty()) { // case where level is empty
            wordData.set(levelIndex, toAdd);
        } else { // add and merge duplicates
            ArrayList<Word> levelWords = wordData.get(levelIndex);
            for (Word wordToAdd : toAdd) {
                if (levelWords.contains(wordToAdd)) { // already exists
                    levelWords.get(levelWords.indexOf(wordToAdd)).merge(wordToAdd);
                } else { // word to add does not exist
                    levelWords.add(wordToAdd);
                }
            }
        }
        saveAll(true);
    }

    /**
     * return max enabled level
     * @return max enabled level
     */
    public int highestLevelEnabled() {
        if (enabledLevels.size() == 0) {
            System.out.println("enabledLevels is empty...");
            return 1; // no unlocks
        }
        return Collections.max(enabledLevels);
    }

    public void setVoice(String toSet) {
        if (toSet.equals(Festival.DEFAULT) || toSet.equals(Festival.NZ)) {
            voice = toSet;
            saveAll(false);
        }
    }

    public String getVoice() {
        return voice;
    }

    /**
     * wordData if to save word data as well
     * @param toSaveWordData
     */
    public void saveAll(boolean toSaveWordData) {
        saveObject(LEVEL_DATA, enabledLevels);
        saveObject(VOICE_DATA, voice);
        saveObject(ACHIEVEMENT_DATA, achievements);
        saveObject(CURRENT_LIST_DATA, currentWordList);
        saveMoney();
        if (toSaveWordData) {
            saveObject(WORD_DATA, wordData);
        }
    }

    /**
     * loadAll existing data from file (or initialize if no file exists)
     */
    public void loadAll() {
        currentWordList = (String) loadObject(CURRENT_LIST_DATA, "");
        if (!currentWordList.equals("")) {
            LevelData.setCurrentWordFile(currentWordList);
        }
        String currentWordFile = LevelData.currentDataID;
        // initialize data based on spelling list
        LEVEL_DATA = new File(".levelData" + currentWordFile + ".ser");
        System.out.println("loading level data from " + LEVEL_DATA);
        WORD_DATA = new File(".wordData" + currentWordFile + ".ser");
        ACHIEVEMENT_DATA = new File(".achievementData" + currentWordFile + ".ser");
        enabledLevels = (ArrayList<Integer>) loadObject(LEVEL_DATA, new ArrayList<Integer>());
        voice = (String) loadObject(VOICE_DATA, Festival.DEFAULT);
        achievements = (TreeSet<Achievement>) loadObject(ACHIEVEMENT_DATA, new TreeSet<Achievement>());
        wordData = (ArrayList<ArrayList<Word>>) loadObject(WORD_DATA, new ArrayList<ArrayList<Word>>());
        if (wordData.isEmpty()) {
            initialiseWordData();  // initialise if wordData is empty;
        }
        money = (Money) loadObject(MONEY_DATA, new Money());
    }

    /**
     * Reset levels
     */
    public void delete() {
        System.out.println("deleting level data");
        LEVEL_DATA.delete();
        enabledLevels = new ArrayList<Integer>();
    }

    /**
     * Reset all word data
     */
    public void resetStats() {
        WORD_DATA.delete();
        wordData = new ArrayList<ArrayList<Word>>();
        initialiseWordData();
    }

    /**
     * reset achievements
     */
    public void resetAchievements() {
        ACHIEVEMENT_DATA.delete();
        achievements = new TreeSet<Achievement>();
    }

    /**
     * methods to handle money
     */
    public void saveMoney() {
        saveObject(MONEY_DATA, money);
    }

    public Money getMoney() {
        return money;
    }

    // helper methods for file IO

    public void saveObject(File file, Object object) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Object loadObject(File file, Object defaultReturn) {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
            Object result = is.readObject();
            if (result == null) {
                return defaultReturn;
            } else {
                return result;
            }
        } catch (FileNotFoundException e) {
            return defaultReturn;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return defaultReturn;
    }

    /**
     * add empty arraylist to each level
     */
    private void initialiseWordData() {
        ArrayList<ArrayList<Word>> free = new ArrayList<ArrayList<Word>>();
        for (int i = 0; i < LevelData.getMaxLevel(); i++) {
            free.add(new ArrayList<Word>());
        }
        wordData = free;
    }

    /**
     * set current word list
     */
    public void setCurrentWordList(String wordList) {
        currentWordList = wordList;
        saveObject(CURRENT_LIST_DATA, currentWordList);
    }
}
