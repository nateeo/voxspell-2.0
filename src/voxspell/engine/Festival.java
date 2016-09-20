package voxspell.engine;

import javafx.concurrent.Task;

// TODO: Festival voice changes

/**
 * This class wraps bash functions that call festival text-to-speech command
 * Created by nhur714 on 6/09/16.
 */
public class Festival {
    Task<Void> task;
    // voices
    public static final String DEFAULT = "kal_diphone";
    public static final String NZ = "akl_nz_jdt_diphone";

    // types of sentences
    public enum Operations {
        SPELL, CORRECT, TRY_AGAIN, LISTEN_AGAIN, WRONG
    }
    private String voiceType = LevelData.getVoice();

    /**
     * type = Festival.DEFAULT | Festival.NZ to configure the voice of festival
     * @param type
     */
    public void setVoiceType(String type) { // TODO: remove one voice
        if (type.equals(Festival.DEFAULT) | type.equals(Festival.NZ)) {
            voiceType = type;
        } else {
            System.out.println("Invalid type, setting to default");
            voiceType = DEFAULT;
        }
    }

    /**
     * @return A nicely formatted  string of the current voiceType
     */
    public String getVoiceType() {
        return voiceType;
    }

    /**
     * Reads a word using festival.
     * word is the Word object to read. tryAgain flag to see if user is attempting the word again.
     * @param word
     * @param op
     * @return command String that was used in the process builder
     */
    public void read(final Word word, final Operations op) {
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("read task called");
                tts(word, op);
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void tts(Word word, Operations op) {
        // TODO: FESTIVAL
       /* ProcessBuilder builder = new ProcessBuilder("festival");
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter stdin = new PrintWriter(process.getOutputStream());
        String sentence = sentenceBuilder(word, op);
        stdin.println("(voice_" + voiceType + ")");
        //System.out.println("(voice_" + voiceType + ")");
        stdin.println("(SayText \"" + sentence + "\"");
        //System.out.println("(SayText \"" + sentence + "\")");
        stdin.close();
        */
    }

    /**
     * build the correct sentence for tts
     * @param word
     * @param op
     */
    private String sentenceBuilder(Word word, Operations op) {
        switch (op) {
            case SPELL:
                return "Please spell " + word;
            case CORRECT:
                return "That is right!";
            case LISTEN_AGAIN:
                return "The word is " + word + ". " + word;
            case TRY_AGAIN:
                return "Try spelling it again. " + word + ". " + word;
            case WRONG:
                return "Sorry, that is wrong";
        }
        return "Something went wrong";
    }
}
