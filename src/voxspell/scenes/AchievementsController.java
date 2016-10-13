package voxspell.scenes;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import voxspell.engine.Achievement;
import voxspell.engine.DataIO;
import voxspell.engine.SceneManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeSet;

/**
 * Created by nhur714 on 10/10/2016.
 */
public class AchievementsController implements Initializable {
    @FXML
    private VBox vBox;
    @FXML
    private HBox hBox;
    @FXML
    private Button mainMenuButton;

    private DataIO dataIO = new DataIO();
    private TreeSet<Achievement> achievements;

    public void initialize(URL location, ResourceBundle resources) {
        vBox.setBackground(SceneManager.makeBackground());
        // get achievements from data and render them into a list
        achievements = dataIO.getAchievements();
        ObservableList<Node> list = hBox.getChildren();

        if (achievements.isEmpty()) {
            Label empty = new Label("No achievements unlocked yet.\nTry improving your spelling to unlock them!");
            hBox.setAlignment(Pos.CENTER);
            empty.setStyle("-fx-font-size: 18px; -fx-font-family: \"Comic Sans MS\"; -fx-text-alignment: center; -fx-background-color: rgba(255,255,255, 0.4)");
            // maybe examples
            list.add(empty);
        } else {
            for (Achievement achievement : achievements) {
                list.add(achievement.renderAchievement());
            }
        }

        mainMenuButton.setOnMouseClicked((e) -> SceneManager.goTo("main.fxml"));
        //music button
    }
}
