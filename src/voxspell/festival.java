package voxspell;

import javafx.concurrent.Task;

import java.io.IOException;

/**
 * Created by nateeo on 6/09/16.
 */
public class festival {
    Task<Void> task;
    private String voiceType = "";

    public void read(final Word word) {
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                tts(word);
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void tts(Word word) {
        String command = "echo \"Please spell " + word + "\" | festival --tts";
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", command);
        try {
            Process process = builder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("text-to-speech error");
        }
    }
}
