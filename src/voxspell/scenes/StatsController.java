package voxspell.scenes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
        data = new DataIO();
        updateBarChart("1");
        updateTableView("1");
        resetButton.setOnMouseClicked(new resetHandler());
        returnButton.setOnMouseClicked(new returnHandler());

        //set up choicebox
        levelList = FXCollections.observableArrayList("Level 1","Level 2", "Level 3", "Level 4", "Level 5",
                "Level 6", "Level 7", "Level 8", "Level 9", "Level 10");
        levelChoice.setItems(levelList);
        levelChoice.setValue("Level " + LevelData.getLevel());
        levelChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String level = newValue.substring(5).trim();
                System.out.println("updating the bar chart");
                updateBarChart(level);
            }
        });

        //
        updateTableView("1");

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
            System.out.println(word.toString() + word.getMastered() + " | " + word.getFaulted() + " | " + word.getFailed());
        }
        double correctPercentage = ((double)correct/(correct+wrong)) * 100;
        XYChart.Series series = new XYChart.Series();
        series.getData().add(new XYChart.Data("correct", correctPercentage));
        series.getData().add(new XYChart.Data("wrong", 100 - correctPercentage));
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

    }

    private ArrayList<Word> extractWords(int level) {
        return wordData.get(level-1);
    }

}