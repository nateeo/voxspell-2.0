package voxspell.engine;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import voxspell.Voxspell;

import java.io.IOException;

/**
 * class with method(s) to change the scene
 * Created by nhur714 on 22/09/16.
 */
public class SceneManager {
    private static Stage currentStage;

    /**
     * Set stage to edit
     */
    public static void setStage(Stage stage) {
        currentStage = stage;
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
}
