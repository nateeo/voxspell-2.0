package voxspell.scenes;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import voxspell.engine.DataIO;
import voxspell.engine.LevelData;
import voxspell.engine.Word;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by harrylimp on 19/09/16.
 */
public class StatsController implements Initializable {

    @FXML
    private Button reviewButton;
    @FXML
    private Button returnButton;
    @FXML
    private PieChart piechart;
    @FXML
    private MenuButton menuButton;
    @FXML
    private MenuItem levelOne;
    @FXML
    private MenuItem levelTwo;
    @FXML
    private MenuItem levelThree;
    @FXML
    private MenuItem levelFour;
    @FXML
    private MenuItem levelFive;
    @FXML
    private MenuItem levelSix;
    @FXML
    private MenuItem levelSeven;
    @FXML
    private MenuItem levelEight;
    @FXML
    private MenuItem levelNine;
    @FXML
    private MenuItem levelTen;
    @FXML
    private Button resetButton;
    @FXML
    private BarChart<String,Number> barChart;

    private DataIO data = new DataIO();

    private ArrayList<ArrayList<Word>> wordData = data.getWordData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = new DataIO();
        updateBarChart("1");
        resetButton.setOnMouseClicked(new resetHandler());
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

    class resetHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            data.resetStats();
            wordData = data.getWordData();
            String levelSelected = (Integer.toString(LevelData.getLevel()));
            updateBarChart(levelSelected);
            updateTextArea(levelSelected);
        }
    }

    class levelHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            String levelSelected = ((MenuItem)mouseEvent.getSource()).getText();
            updateBarChart(levelSelected);
            updateTextArea(levelSelected);
        }
    }

    public void updateBarChart(String level) {
        ArrayList<Word> selectedWords = extractWords(Integer.parseInt(level));
        int correct = 0;
        int wrong = 0;
        for (Word word : selectedWords) {
            correct += word.getMastered();
            wrong += word.getFaulted() + word.getFailed();
            System.out.println(word.toString() + word.getMastered() + " | " + word.getFaulted() + " | " + word.getFailed());
        }
        double correctPercentage = ((double)correct/(correct+wrong)) * 100;
        XYChart.Series series = new XYChart.Series();
        System.out.println(correctPercentage);
        series.setName("HELLO");
        series.getData().add(new XYChart.Data("correct", correctPercentage));
        series.getData().add(new XYChart.Data("wrong", 100 - correctPercentage));

        barChart.getData().addAll(series);
    }

    public void updateTextArea(String level) {

    }

    private ArrayList<Word> extractWords(int level) {
        return wordData.get(level-1);
    }


}
