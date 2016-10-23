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
                    scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.rgb(71, 196, 249));
                    classNames.linkStyleSheet(scene);
                }
                return scene;
            }
        };

        load.setOnSucceeded((e) -> {
            System.out.println("DONE");
            transitionScene(load.getValue());
        });

        new Thread(load).start();
    }

    private static void transitionScene(Scene scene) {
        FadeTransition fade = new FadeTransition(Duration.seconds(0.2), currentStage.getScene().getRoot());
        fade.setFromValue(0.2);
        fade.setToValue(0.0);
        fade.setOnFinished((e) -> showScene(scene));
        fade.play();
    }

    /**
     * Stop the loading animation and load the scene on FX thread, flush the queue
     */
    private static void showScene(Scene scene) {
        currentStage.setScene(scene);
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
}
