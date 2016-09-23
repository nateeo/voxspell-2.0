package voxspell.scenes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import voxspell.engine.DataIO;
import voxspell.engine.LevelData;
import voxspell.engine.SceneManager;
import voxspell.engine.Word;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by harrylimp on 19/09/16.
 */
public class StatsController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button returnButton;
    @FXML
    private ChoiceBox<String> levelChoice;
    @FXML
    private Button resetButton;
    @FXML
    private BarChart<String,Number> barChart;
    @FXML
    private TableView<Word> tableView;
    @FXML
    private TableColumn<Word, String> wordColumn;
    @FXML
    private TableColumn<Word, Integer> correctColumn;
    @FXML
    private TableColumn<Word, Integer> wrongColumn;

    private DataIO data = new DataIO();
    private ObservableList<String> levelList;
    private ArrayList<ArrayList<Word>> wordData = data.getWordData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        anchorPane.setBackground(SceneManager.makeBackground());

        data = new DataIO();
        updateBarChart("1");
        updateTableView("1");
        resetButton.setOnMouseClicked(new resetHandler());
        returnButton.setOnMouseClicked(new returnHandler());

        //set up choicebox
        levelList = FXCollections.observableArrayList("Level 1","Level 2", "Level 3", "Level 4", "Level 5",
                "Level 6", "Level 7", "Level 8", "Level 9", "Level 10");
        levelChoice.setItems(levelList);
        if (LevelData.getLevel() == -1) {
            levelChoice.setValue("Level 1");
        } else {
            levelChoice.setValue("Level " + LevelData.getLevel());
        }
        levelChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String level = newValue.substring(5).trim();
                System.out.println("updating the bar chart");
                updateBarChart(level);
                updateTableView(level);
            }
        });

    }

    class returnHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            SceneManager.goTo("main.fxml");
        }
    }

    class resetHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            String levelSelected = (Integer.toString(LevelData.getLevel()));
            data.resetStats();
            wordData = data.getWordData();
            updateBarChart(levelSelected);
            updateTableView(levelSelected);
        }
    }

    public void updateBarChart(String level) {
        barChart.getData().clear();
        ArrayList<Word> selectedWords = extractWords(Integer.parseInt(level));
        int correct = 0;
        int wrong = 0;
        for (Word word : selectedWords) {
            correct += word.getMastered();
            wrong += word.getFaulted() + word.getFailed();
        }
        double correctPercentage;
        double wrongPercentage;
        if (correct + wrong == 0) {
            correctPercentage = 0; // safe
            wrongPercentage = 0;
        } else {
            correctPercentage = ((double)correct / (correct + wrong) * 100);
            wrongPercentage = 100 - correctPercentage;
        }

        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("correct", correctPercentage));
        series.getData().add(new XYChart.Data("wrong", wrongPercentage));

        barChart.getData().addAll(series);
    }

    public void updateTableView(String level) {

        ArrayList<Word> selectedWords = extractWords(Integer.parseInt(level));

        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        correctColumn.setCellValueFactory(new PropertyValueFactory<>("mastered"));
        wrongColumn.setCellValueFactory(new PropertyValueFactory<>("failed"));

        ObservableList<Word> words = FXCollections.observableArrayList();
        for (Word word : selectedWords) {
            words.add(word);
        }
        tableView.setItems(words);
        tableView.setPlaceholder(new Label("Level not completed!"));

    }

    private ArrayList<Word> extractWords(int level) {
        return wordData.get(level-1);
    }

}