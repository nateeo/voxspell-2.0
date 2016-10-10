package voxspell.engine;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import voxspell.Voxspell;

import java.io.IOException;

/**
 * class with method(s) to change the scene, and manage scene background
 * Created by nhur714 on 22/09/16.
 */
public class SceneManager {
    private static Stage currentStage;
    private static final String MAIN_BACKGROUND = "scenes/assets/trees2.png";
    private static final String SECONDARY_BACKGROUND = "scenes/assets/trees.png";
    private static AudioClip welcome = new AudioClip(Voxspell.class.getResource("scenes/assets/Welcome.mp3").toExternalForm());

    public static boolean enableMusic = true;

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
    public static void goTo(String fxmlDestination) {
        if (currentStage != null) {
            Stage stage;
            Parent root = null;
            stage = currentStage;

            try {
                root = FXMLLoader.load(Voxspell.class.getResource("scenes/" + fxmlDestination));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

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
            welcome.play();
        }
    }

    public static void stopMusic() {
        if (welcome.isPlaying()) {
            welcome.stop();
        }
    }
}
