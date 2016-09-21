package voxspell.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import voxspell.engine.DataIO;
import voxspell.engine.Festival;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        returnButton.setOnMouseClicked(new returnHandler());
        showPieChart(LevelData.getLevel());

        //initialise menubutton
        menuButton.setText("Level " + LevelData.getLevel());
        EventHandler<ActionEvent> levelMenuButtonHandler = new levelMenuButtonHandler();

    }



    class levelMenuButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent actionEvent) {
            switch((String)((MenuItem)actionEvent.getSource()).getUserData()) { // userData is voice type
                case "levelOne":
                    break;
                default:
                    break;
            }
        }
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

    /*
    get current level (or default level)
    depending on level chosen get stats for that level
    display as required
     */
    public void showPieChart(int level) {

        ArrayList<ArrayList<Word>> allWords = new DataIO().getWordData();
        ArrayList<Word> currentWords = allWords.get(level-1);
        int correct = 0;
        int incorrect = 0;
        for (Word word : currentWords) {
            correct += word.getMastered();
            incorrect += word.getFaulted() + word.getFailed();
        }

        ObservableList<javafx.scene.chart.PieChart.Data> list = FXCollections.observableArrayList(
                new javafx.scene.chart.PieChart.Data("Correct", (double)correct/(correct+incorrect)),
                new javafx.scene.chart.PieChart.Data("Incorrect", (double)incorrect/(correct+incorrect))
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

    public void newStats() {

    }

}
