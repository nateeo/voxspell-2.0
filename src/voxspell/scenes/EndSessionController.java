package voxspell.scenes;

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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import voxspell.engine.LevelData;
import voxspell.engine.SceneManager;
import voxspell.engine.Word;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by harrylimp on 21/09/16.
 */
public class EndSessionController implements Initializable {


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LevelData.setIsReview(false);
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
        reviewButton.setOnMouseClicked(new reviewHandler());
        viewStatsButton.setOnMouseClicked(new statsHandler());
        retryLevelButton.setOnMouseClicked(new retryLevelHandler());

        // enable next stuff
        if (correct >= 9) {
            playVideoButton.setOnMouseClicked(new videoHandler());
            nextLevelButton.setOnMouseClicked(new nextLevelHandler());
        } else {
            nextLevelButton.setDisable(true);
            playVideoButton.setVisible(false);
        }
        showPieChart();
        showListView();
        displayText();
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

    public void displayText() {
        endMessage.setText("Well done!");
        if (correct <= 9) {
            playVideoButton.setDisable(true);
        }
    }

    public void showPieChart() {
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList(
                new javafx.scene.chart.PieChart.Data("Correct", correct * 10),
                new javafx.scene.chart.PieChart.Data("Incorrect", incorrect * 10)
        );
        piechart.setData(list);

        applyCustomColorSequence(
                list,
                "green",
                "red"
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
                    setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        }
    }
}