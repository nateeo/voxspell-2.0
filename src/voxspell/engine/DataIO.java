package voxspell.engine;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class to read and write data to files
 * Created by nhur714 on 16/09/16.
 */
public class DataIO {
    private ArrayList<Integer> enabledLevels;
    private String voice;
    private ArrayList<ArrayList<Word>> wordData;

    private File LEVEL_DATA = new File(".levelData.ser");
    private File VOICE_DATA = new File(".voiceData.ser");
    private File WORD_DATA = new File(".wordData.ser");

    public DataIO() {
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
        if (toSaveWordData) {
            saveObject(WORD_DATA, wordData);
        }
    }

    /**
     * loadAll existing data from file (or initialize if no file exists)
     */
    private void loadAll() {
        enabledLevels = (ArrayList<Integer>) loadObject(LEVEL_DATA, new ArrayList<Integer>());
        voice = (String) loadObject(VOICE_DATA, Festival.DEFAULT);
        wordData = (ArrayList<ArrayList<Word>>) loadObject(WORD_DATA, new ArrayList<ArrayList<Word>>());
        if (wordData.isEmpty()) {
            initialiseWordData();  // initialise if wordData is empty;
        }
    }

    /**
     * Reset levels
     */
    public void delete() {
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

    // helper methods

    private void saveObject (File file, Object object) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Object loadObject (File file, Object defaultReturn) {
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
        for (int i = 0; i < 10; i++) {
            free.add(new ArrayList<Word>());
        }
        wordData = free;
    }
}
