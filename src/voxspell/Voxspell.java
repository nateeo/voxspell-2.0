package voxspell;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.HashMap;

public class Voxspell extends Application {
    private static int WINDOW_WIDTH = 600;
    private static int WINDOW_HEIGHT = 400;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scenes/main.fxml")); // change this to your scene
        primaryStage.setTitle("VOXSPELL");
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
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
