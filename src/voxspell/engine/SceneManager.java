package voxspell.engine;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import voxspell.Voxspell;
import voxspell.scenes.controllers.classNames;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * static class to manage global, persistent scene data and to handle transitions.
 * Created by nhur714 on 22/09/16.
 */
public class SceneManager {
    private static Stage currentStage;
    private static final String MAIN_BACKGROUND = "scenes/assets/trees2.png";
    private static final String SECONDARY_BACKGROUND = "scenes/assets/trees.png";
    private static AudioClip welcome = new AudioClip(Voxspell.class.getResource("scenes/assets/Welcome.mp3").toExternalForm());

    public static boolean enableMusic = true;
    public static int WINDOW_WIDTH = 900;
    public static int WINDOW_HEIGHT = 600;

    private static AudioClip click = new AudioClip(Voxspell.class.getResource("scenes/assets/bubble.mp3").toExternalForm());
    private static String video = new File("./lib/PC10.mp4").getAbsolutePath();
    private static String currentVideo;
    private static File VIDEO_FILE = new File(".video.ser");
    private static File AUDIO_FILE = new File(".audio.ser");
    private static String currentAudio;

    private static double VOLUME = 0.15;
    private static double CLICK_VOLUME = 0.7;
    private static ArrayList<QueuedEvent> queue = new ArrayList<>();

    /**
     * Set stage to edit
     */
    public static void setStage(Stage stage) {
        currentStage = stage;
    }

    /**
     * Methods to make backgrounds for scenes
     * @return
     */
    public static Background makeBackground() {
        return backgroundChooser(MAIN_BACKGROUND);
    }

    public static Background makeAmbientBackground() {
        return backgroundChooser(SECONDARY_BACKGROUND);
    }

    /**
     * Methods to manage the Java FX queued events
     */
    public static void addQueuedEvent(QueuedEvent e) {
        queue.add(e);
    }

    private static void flushQueue() {
        for (QueuedEvent e : queue) {
            e.execute();
        }
        queue.clear();
    }

    /**
     * Helper method to transition to new scene. Also links the main css to every scene.
     * Concurrently loads the next scene and then fades to the new scene smoothly.
     * @param fxmlDestination
     */
    public static void goTo(String fxmlDestination) {
        click.play(CLICK_VOLUME);
        Task<Scene> load = new Task<Scene>() {
            @Override
            public Scene call() {
                Scene scene = null;
                if (currentStage != null) {
                    Parent root = null;

                    try {
                        root = FXMLLoader.load(Voxspell.class.getResource("scenes/" + fxmlDestination));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.rgb(255, 255, 255, 0.1));
                    classNames.linkStyleSheet(scene);
                }
                return scene;
            }
        };

        load.setOnSucceeded((e) -> {
            showScene(load.getValue());
        });

        load.setOnFailed((e) -> load.getException().printStackTrace());

        new Thread(load).start();
    }

    /**
     * Stop the loading animation and load the scene on FX thread, flush the queue
     */
    private static void showScene(Scene scene) {
        FadeTransition fade = new FadeTransition(Duration.seconds(0.9), scene.getRoot());
        fade.setFromValue(0.4);
        fade.setToValue(1.0);
        currentStage.setScene(scene);
        fade.play();

        Platform.runLater(() -> flushQueue());
    }

    /**
     * Helper method to generate different backgrounds
     *
     * @param backgroundSource
     * @return
     */
    private static Background backgroundChooser(String backgroundSource) {
        Image image = new Image(Voxspell.class.getResource(backgroundSource).toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);
        return background;
    }

    /**
     * Methods to control global music
     */
    public static void playMusic() {
        if (enableMusic && !welcome.isPlaying()) {
            welcome.setCycleCount(AudioClip.INDEFINITE);
            welcome.play(VOLUME);
        }
    }

    public static void stopMusic() {
        if (welcome.isPlaying()) {
            welcome.stop();
        }
    }

    // returns whether or not the music is playing after it has been toggled
    public static boolean toggleMusic() {
        if (welcome.isPlaying()) {
            welcome.stop();
            enableMusic = false;
            return false;
        } else {
            enableMusic = true;
            welcome.play(VOLUME);
            return true;
        }
    }

    public static boolean isMusicPlaying() {
        return welcome.isPlaying();
    }

    public static void changeMusic(String name) {
        welcome.stop();
        welcome = new AudioClip(Voxspell.class.getResource("scenes/assets/" + name).toExternalForm());
        welcome.play(VOLUME);
        currentAudio = name;
        DataIO.getInstance().saveObject(AUDIO_FILE, currentAudio);
    }

    public static void changeVideo(String name) {
        video = new File("./lib/" + name).getAbsolutePath();
        currentVideo = name;
        DataIO.getInstance().saveObject(VIDEO_FILE, currentVideo);
    }

    public static String getVideo() {
        return video;
    }

    /**
     * Loads assets during splash screen
     */
    public static void loadAssets() {
        currentAudio = (String) DataIO.getInstance().loadObject(AUDIO_FILE, "Welcome.mp3");
        currentVideo = (String) DataIO.getInstance().loadObject(VIDEO_FILE, "PC10.mp4");
        welcome = new AudioClip(Voxspell.class.getResource("scenes/assets/" + currentAudio).toExternalForm());
        video = new File("./lib/" + currentVideo).getAbsolutePath();
    }
}
