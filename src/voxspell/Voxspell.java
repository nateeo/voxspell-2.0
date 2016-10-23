package voxspell;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import voxspell.engine.SceneManager;
import voxspell.scenes.controllers.classNames;

import java.lang.reflect.Field;
import java.util.HashMap;

import static voxspell.engine.SceneManager.WINDOW_HEIGHT;
import static voxspell.engine.SceneManager.WINDOW_WIDTH;

/**
 * Application entry point. Background loading along with the splash screen was coded with some assistance through a
 * tutorial: https://gist.github.com/jewelsea/2305098
 */
public class Voxspell extends Application {

    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 676;
    private static final int SPLASH_HEIGHT = 227;

    @Override
    public void init() {
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
        progressText = new Label();
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle(
                "-fx-padding: 5; " +
                        "-fx-background-color: cornsilk; " +
                        "-fx-border-width:5; " +
                        "-fx-border-color: " +
                        "linear-gradient(" +
                        "to bottom, " +
                        "chocolate, " +
                        "derive(chocolate, 50%)" +
                        ");"
        );
        splashLayout.setEffect(new DropShadow());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = new Stage();

        // task that loads the main screen along with all the data etc.
        final Task<Scene> load = new Task<Scene>() {

            @Override
            protected Scene call() throws Exception {
                SceneManager.setStage(primaryStage);
                updateProgress(30, 100);
                updateMessage("Loading assets");
                Parent root = FXMLLoader.load(getClass().getResource("scenes/main.fxml")); // change this to your scene
                updateMessage("Populating scene");
                updateProgress(100, 100);
                Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
                classNames.linkStyleSheet(scene);
                updateMessage("Done!");
                updateProgress(100, 100);
                return scene;
            }
        };

        load.setOnSucceeded((e) -> {
            Scene scene = load.getValue();
            loadMainStage(scene);
        });


        showSplash(primaryStage, load, () -> mainStage.show());
        new Thread(load).start();

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

    private void loadMainStage(Scene scene) {
        mainStage.setTitle("VOXSPELL");
        SceneManager.setStage(mainStage);
        mainStage.setScene(scene);
        mainStage.centerOnScreen();
    }

    // splash screen that callbacks on completing the task and subsequent fade out
    private void showSplash(final Stage initStage, Task<?> task, completionHandler ci) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(0.5), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                ci.complete();
            }
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        initStage.setScene(splashScene);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    interface completionHandler {
        void complete();
    }
}
