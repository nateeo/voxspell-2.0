package voxspell;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static String ON_HOVER = "-fx-background-color: #83B496";
    private static String ON_EXIT = "-fx-background-color: #b6e7c9";
    private ArrayList<Button> buttons = new ArrayList<Button>();

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

    // add buttons to list

    private int getLevelNumber(String text) {
        String number = text.substring(text.length() - 1);
        if (number.equals("0")) {
            return 10;
        }
        return Integer.parseInt(number);
    }

    class levelSelect implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            String text = ((Button)event.getSource()).getText();
            int level = getLevelNumber(text);
            System.out.println("Going to level " + level);
            // TODO use level to get wordlist and move to spelling scene
        }
    }

    class hoverHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            ((Button)event.getSource()).setStyle(ON_HOVER);
        }
    }

    class exitHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event) {
            ((Button)event.getSource()).setStyle(ON_EXIT);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // create listeners
        EventHandler<MouseEvent> levelSelectionHandler = new levelSelect();
        EventHandler<MouseEvent> hoverHandler = new hoverHandler();
        EventHandler<MouseEvent> exitHandler = new exitHandler();

        // add buttons to list, then iterate through list assigning listeners
        buttons.add(level1);
        buttons.add(level2);
        buttons.add(level3);
        buttons.add(level4);
        buttons.add(level5);
        buttons.add(level6);
        buttons.add(level7);
        buttons.add(level8);
        buttons.add(level9);
        buttons.add(level10);

        for (Button button : buttons) {
            button.setOnMouseClicked(levelSelectionHandler);
            button.setOnMouseEntered(hoverHandler);
            button.setOnMouseExited(exitHandler);
        }
    }
}
