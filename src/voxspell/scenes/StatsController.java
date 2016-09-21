package voxspell.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import voxspell.engine.LevelData;
import voxspell.engine.Word;

import java.util.ArrayList;

/**
 * Created by harrylimp on 19/09/16.
 */
public class StatsController {

    @FXML
    private Button reviewButton;
    @FXML
    private Button returnButton;
    @FXML
    private PieChart piechart;
    @FXML
    private ListView<String> listView;

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
        ObservableList<javafx.scene.chart.PieChart.Data> list = FXCollections.observableArrayList(
                new javafx.scene.chart.PieChart.Data("Correct", correct * 10),
                new javafx.scene.chart.PieChart.Data("Incorrect", incorrect * 10)
        );
        piechart.setData(list);
    }

    public void showListView() {
        ArrayList<Word> currentWords = LevelData.getCurrentWordList();
        ListView<String> list = new ListView<>();
        ObservableList<String> words = FXCollections.observableArrayList();
        for (Word word : currentWords) {
            words.add(word.toString());
        }
        list.setItems(words);
        list.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            class ColorRectCell extends ListCell<String> {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Rectangle rect = new Rectangle(100, 20);
                    int position = LevelData.getCurrentWordList().indexOf(item);
                    Word word = LevelData.getCurrentWordList().get(position);
                    if (word.getMastered() == 1) {
                        rect.setFill(Color.GREEN);
                        setGraphic(rect);
                    } else {
                        rect.setFill(Color.RED);
                        setGraphic(rect);
                    }
                }
            }

            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ColorRectCell();
            }
        });
    }
}
