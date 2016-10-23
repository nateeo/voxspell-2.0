package voxspell.scenes.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import voxspell.Voxspell;
import voxspell.engine.Achievement.Rarity;
import voxspell.engine.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller to manage the end of spelling quiz scene
 * Created by harrylimp on 21/09/16.
 */
public class EndSessionController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button reviewButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button nextLevelButton;
    @FXML
    private Button viewStatsButton;
    @FXML
    private Button retryLevelButton;
    @FXML
    private Button playVideoButton;
    @FXML
    private PieChart piechart;
    @FXML
    private ListView<String> listView;
    @FXML
    private Label endMessage;

    private static ArrayList<String> correctList = new ArrayList<>();

    private int correct;
    private int incorrect;
    private DataIO data = DataIO.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MediaPlayer mp = new MediaPlayer(new Media(Voxspell.class.getResource("scenes/assets/congratulations.mp3").toExternalForm()));
        mp.setOnEndOfMedia(() -> SceneManager.playMusic());

        //queue the sound reward
        SceneManager.addQueuedEvent(new QueuedEvent() {
            MediaPlayer reward = mp;

            public void execute() {
                mp.play();
            }
        });

        if (LevelData.isReview()) {
            reviewButton.setText("Review again");
        }
        ArrayList<Word> currentWords = LevelData.getCurrentWordList();
        correct = 0;
        incorrect = 0;
        for (Word word : currentWords) {
            if (word.getMastered() == 1) {
                correct++;
            } else {
                incorrect++;
            }
        }
        returnButton.setOnMouseClicked(new returnHandler());
        viewStatsButton.setOnMouseClicked(new statsHandler());
        retryLevelButton.setOnMouseClicked(new retryLevelHandler());

        if (incorrect > 0) {
            reviewButton.setOnMouseClicked(new reviewHandler());
        } else {
            reviewButton.setDisable(true);
        }

        // enable next stuff
        if (correct >= currentWords.size() - 1 && !LevelData.isReview() || currentWords.size() == 1 && LevelData.isReview()) {
        	if (!LevelData.isReview()) {
        		new AchievementsPopup("First level unlocked!", "For scoring 9 or more on the first level", Rarity.COMMON);
        	} else {
        		new AchievementsPopup("Reviewing!", "For attempting to review your mistakes", Rarity.COMMON);
        	}
        	// only enable reward/next level if original quiz was 9/10 and they reviewed 1 word only (or no review)
            playVideoButton.setOnMouseClicked(new videoHandler());
            if (!(LevelData.getLevel() == LevelData.getMaxLevel())) {
                nextLevelButton.setOnMouseClicked(new nextLevelHandler());
                data.enableLevel(LevelData.getLevel() + 1);
            } else {
                // disable next level on max
                nextLevelButton.setDisable(true);
                new AchievementsPopup("All Levels", "For unlocking all levels", Rarity.EPIC);
            }
        } else {
            nextLevelButton.setDisable(true);
            playVideoButton.setVisible(false);
        }
        displayText();
        LevelData.setIsReview(false);
        showPieChart();
        showListView();
    }

    class returnHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            SceneManager.goTo("main.fxml");
        }
    }

    class reviewHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            LevelData.setIsReview(true);
            SceneManager.goTo("spelling.fxml");
        }
    }

    class retryLevelHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            SceneManager.goTo("spelling.fxml");
        }
    }

    class nextLevelHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            LevelData.setLevel(LevelData.getLevel() + 1); // safe as button will be disabled if no next level
            SceneManager.goTo("spelling.fxml");
        }
    }

    class statsHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            SceneManager.goTo("stats.fxml");
        }
    }

    class videoHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            SceneManager.goTo("video.fxml");
        }
    }

    /**
     * Displays the text that should be provided after the session has finished
     * depending on whether it was in review mode or normal mode
     */
    public void displayText() {
        if (LevelData.isReview()) {
            endMessage.setText("Review results");
        } else {
            endMessage.setText("Well done!");
        }
    }

    /**
     * PieChart is generated depending on the percentage of correct and incorrect answers.
     */
    public void showPieChart() {
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList(
                new javafx.scene.chart.PieChart.Data("Correct", correct * 10),
                new javafx.scene.chart.PieChart.Data("Incorrect", incorrect * 10)
        );
        piechart.setData(list);

        applyCustomColorSequence(
                list,
                "limegreen",
                "tomato"
        );
        
    }

    private void applyCustomColorSequence(ObservableList<PieChart.Data> pieChartData, String... pieColors) {
        int i = 0;
        for (PieChart.Data data : pieChartData) {
            data.getNode().setStyle("-fx-pie-color: " + pieColors[i % pieColors.length] + ";");
            i++;
        }
    }

    public void showListView() {
        ArrayList<Word> currentWords = LevelData.getCurrentWordList();
        ObservableList<String> words = FXCollections.observableArrayList();
        for (Word word : currentWords) {
            words.add(word.toString());
            if (word.getMastered() == 1) {
                correctList.add(word.toString());
            }
        }
        listView.setItems(words);

        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ColorRectCell();
            }
        });
    }

    static class ColorRectCell extends ListCell<String> {
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item);
                if (correctList.contains(item)) {
                    setBackground(new Background(new BackgroundFill(Color.LIMEGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    setBackground(new Background(new BackgroundFill(Color.TOMATO, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        }
    }
}