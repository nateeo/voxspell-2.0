package voxspell.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Is responsible for reading the file and extracting the words
 * required for testing at the specific level as chosen by the
 * user.
 *
 * Created by harrylimp on 6/09/16.
 * Updated by nhur714 on 10/10/16.
 */
public class WordList {

    private ArrayList<Word> wordList = new ArrayList<>();
    private ArrayList<String> originalList = new ArrayList<>();
    private int level;

    /**
     * Creates an ArrayList of 10 words that need to tested.
     * @param currentLevel The level that you want to test for.
     */
    public WordList(int currentLevel) {

        level = currentLevel;
        readFile("./lib/NZCER-spelling-lists.txt");
        generateWords();

    }

    /**
     * Returns an ArrayList of 10 words that need to be tested.
     * @return An ArrayList of the words.
     */
    public ArrayList<Word> getWords() {
        return new ArrayList<>(wordList);
    }

    /**
     * Extracts the ten random words required for testing and
     * stores it inside the private ArrayList of words that can be
     * accessed by the getter.
     */
    private void generateWords() {
        Collections.shuffle(originalList);
        for (int i = 0; i < 10; i++) {
            Word currentWord = new Word(originalList.get(i));
            wordList.add(currentWord);
        }
    }

    /**
     * Reads the file containing words from all ten levels and
     * only stores the words required for testing for the specific level.
     */
    private void readFile(String fileLoc) {
        BufferedReader reader = null;
        try {
            File filename = new File(fileLoc);
            reader = new BufferedReader(new FileReader(filename));
            String line;
            String findLevel = "%Level " + Integer.toString(level);
            while ((line = reader.readLine()) != null) {
                String currentLine = line.trim();
                if (currentLine.equals(findLevel)) {
                    while ((line = reader.readLine()) != null) {
                        currentLine = line.trim();
                        if (currentLine.equals("%Level " + Integer.toString(level + 1))) {
                            break;
                        }
                        originalList.add(currentLine);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
