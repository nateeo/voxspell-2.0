package voxspell.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import voxspell.engine.LevelData;
import voxspell.engine.Word;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by harrylimp on 21/09/16.
 */
public class SessionController implements Initializable {


    @FXML
    private Button reviewButton;
    @FXML
    private Button returnButton;
    @FXML
    private PieChart piechart;
    @FXML
    private ListView<String> listView;
    @FXML
    private Label endMessage;

    private static ArrayList<String> correctList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        returnButton.setOnMouseClicked(new SessionController.returnHandler());
        reviewButton.setOnMouseClicked(new SessionController.statsHandler());
        showPieChart();
        showListView();
        displayText();
    }

    class returnHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            Stage stage;
            Parent root = null;
            stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();

            try {
                root = FXMLLoader.load(getClass().getResource("main.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    class statsHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            Stage stage;
            Parent root = null;
            stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();

            try {
                root = FXMLLoader.load(getClass().getResource("stats.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void displayText() {
        ArrayList<Word> currentWords = LevelData.getCurrentWordList();
        int correct = 0;
        for (Word word : currentWords) {
            if (word.getMastered() == 1) {
                correct++;
            }
        }
        endMessage.setText("Congratulations, you got " + correct + " out of 10!");
    }

    public void showPieChart() {
        ArrayList<Word> currentWords = LevelData.getCurrentWordList();
        int correct = 0;
        int incorrect = 0;
        for (Word word : currentWords) {
            if (word.getMastered() == 1) {
                correct++;
            } else {
                incorrect++;
            }
        }
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