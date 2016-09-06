package tests;

import voxspell.Word;
import voxspell.WordList;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 *
 * Created by harrylimp on 6/09/16.
 */
public class WordListTest {

    @org.junit.Test
    public void getWords() throws Exception {
        WordList listOne = new WordList(4);
        WordList listTwo = new WordList(4);
        ArrayList<Word> wordsOne = listOne.getWords();
        ArrayList<Word> wordsTwo = listTwo.getWords();
        Boolean different = false;
        for (int i = 0; i < 10; i++) {
            Word wordOne = wordsOne.get(i);
            Word wordTwo = wordsTwo.get(i);
            if (!wordOne.toString().equals(wordTwo.toString())) {
                different = true;
            }
        }
        assertEquals(true,different);
        assertEquals(10,wordsOne.size());
    }

}