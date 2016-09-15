package voxspell;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button level1;
    @FXML
    private Button level2;
    @FXML
    private Button level3;
    @FXML
    private Button level4;
    @FXML
    private Button level5;
    @FXML
    private Button level6;
    @FXML
    private Button level7;
    @FXML
    private Button level8;
    @FXML
    private Button level9;
    @FXML
    private Button level10;

    private int getLevelNumber(String text) {
        String number = text.substring(text.length() - 1);
        return Integer.parseInt(number);
    }

    class levelSelect implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            String text = ((Button)event.getSource()).getText();
            int level = getLevelNumber(text);
            // TODO use level to get wordlist and move to spelling scene
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventHandler<ActionEvent> level = new levelSelect();
        level1.setOnAction(level);
        level2.setOnAction(level);
        level3.setOnAction(level);
        level4.setOnAction(level);
        level5.setOnAction(level);
        level6.setOnAction(level);
        level7.setOnAction(level);
        level8.setOnAction(level);
        level9.setOnAction(level);
        level10.setOnAction(level);
    }
}
