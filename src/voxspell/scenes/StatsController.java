package voxspell.scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

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

    public void showPieChart (ActionEvent event) {
        // extract data from tested words
        // save into correct and incorrect variables
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList(
                new PieChart.Data("Correct", 80), // Arraylist.legnth * 10 to get the percentage?
                new PieChart.Data("Incorrect", 20)
        );
        piechart.setData(list);
    }

    public void showWords (ActionEvent event) {
        // loop through all the words
        // colour in the cell of the word depending on whether or not is is correct or incorrect
        
    }

    public void returnToMain() {

    }

}
