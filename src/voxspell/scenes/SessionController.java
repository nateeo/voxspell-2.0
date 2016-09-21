package voxspell.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        returnButton.setOnMouseClicked(new SessionController.returnHandler());

        //showPieChart();
        //showListView();
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
