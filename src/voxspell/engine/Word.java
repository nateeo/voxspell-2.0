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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getMastered() {
        return mastered;
    }

    public int getFaulted() {
        return faulted;
    }

    public int getFailed() {
        return failed + faulted;
    }

    /**
     * add another word's stats to this one
     * @param other
     */
    public void merge(Word other) {
        mastered = mastered + other.mastered;
        faulted = faulted + other.faulted;
        failed = failed + other.failed;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Word) {
            return other.toString().equals(this.toString());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return word;
    }

}