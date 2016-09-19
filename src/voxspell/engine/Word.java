package voxspell.engine;

import java.io.Serializable;

/**
 * An object that contains the information required
 * for a single word that is being tested.
 *
 * Created by harrylimp on 6/09/16.
 */
public class Word implements Serializable {

    private String word;
    private int mastered;
    private int faulted;
    private int failed;

    public Word(String wordIn) {
        word = wordIn;
    }

    public void incrementMastered() {
        mastered++;
    }

    public void incrementFaulted() {
        faulted++;
    }

    public void incrementFailed() {
        failed++;
    }

    public int getMastered() {
        return mastered;
    }

    public int getFaulted() {
        return faulted;
    }

    public int getFailed() {
        return failed;
    }

    @Override
    public String toString() {
        return word;
    }

}