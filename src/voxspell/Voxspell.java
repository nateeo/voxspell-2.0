package voxspell;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import voxspell.engine.SceneManager;
import voxspell.scenes.classNames;

import java.lang.reflect.Field;
import java.util.HashMap;

import static voxspell.engine.SceneManager.WINDOW_HEIGHT;
import static voxspell.engine.SceneManager.WINDOW_WIDTH;

public class Voxspell extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.setStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("scenes/main.fxml")); // change this to your scene
        primaryStage.setTitle("VOXSPELL");
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        classNames.linkStyleSheet(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        /**
         * START OF MAC WORKAROUND TODO: remove when finished
         */
        try {
            Class<?> macFontFinderClass = Class.forName("com.sun.t2k.MacFontFinder");
            Field psNameToPathMap = macFontFinderClass.getDeclaredField("psNameToPathMap");
            psNameToPathMap.setAccessible(true);
            psNameToPathMap.set(null, new HashMap<String, String>());
        } catch (Exception e) {
            // ignore
        }
        /**
         * END OF MAC WORKAROUND
         */
        launch(args);
    }
}
