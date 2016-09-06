package voxspell;

import javafx.concurrent.Task;

import java.io.IOException;

/**
 * Created by nateeo on 6/09/16.
 */
public class festival {
    Task<Void> task;
    static final String DEFAULT = "english";
    static final String WELSH = "welsh";
    static final String SPANISH = "spanish";
    private String voiceType = DEFAULT;

    /**
     * type = "english" | "welsh" | "spanish" to configure the voice of festival
     * @param type
     */
    public void setVoiceType(String type) { // TODO: remove one voice
        if (type.equals(DEFAULT) || type.equals(WELSH) || type.equals(SPANISH)) {
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
        String voiceTypeDisplay = voiceType.substring(0, 1).toUpperCase() + voiceType.substring(1);
        return voiceTypeDisplay;
    }

    /**
     * Reads a word using festival.
     * word is the Word object to read. tryAgain flag to see if user is attempting the word again.
     * @param word
     * @param tryAgain
     */
    public void read(final Word word, final boolean tryAgain) {
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                tts(word, tryAgain);
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void tts(Word word, boolean tryAgain) {

        String command = "echo ";
        if (tryAgain) {
            command += "\"Try again. " + word + ". " + word;
        } else {
            command += "\"Please spell " + word;
        }
        command += ".\" | festival --tts --language " + voiceType;

        ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
        try {
            Process process = builder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("text-to-speech error");
        }
    }
}
