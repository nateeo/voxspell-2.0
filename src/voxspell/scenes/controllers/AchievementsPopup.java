package voxspell.scenes.controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import voxspell.engine.Achievement;
import voxspell.engine.Achievement.Rarity;
import voxspell.engine.DataIO;
import voxspell.engine.QueuedEvent;
import voxspell.engine.SceneManager;

/**
 * Achievements popup notification - automatically saves the constructed achievement
 * Created by nhur714 on 10/10/2016.
 * TODO: add sound
 */
public class AchievementsPopup implements QueuedEvent {
	Stage stage;
	VBox vBox;
	HBox hBox;
	Achievement achievement;

	Button okButton;

    DataIO data = DataIO.getInstance();
    
    public AchievementsPopup(String name, String description, Rarity rarity) {
		// add itself to the fx queue
		achievement = new Achievement(name, description, rarity);
    	if (data.getAchievements().contains(achievement)) {
    		// already achieved, do nothing
    	} else {
    		data.addAchievement(achievement);
			SceneManager.addQueuedEvent(this); // queue popup onto JavaFX thread
		}
    }

	/**
	 * entry method
	 */
	public void execute() {
		setUp();
		show();
	}

	public void show() {
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}

    
    public void setUp() {
		stage = new Stage();
		vBox = new VBox(2);
		hBox = new HBox();

		okButton = new Button();
		okButton.setOnMouseClicked((e) -> stage.close());
    	okButton.setText("Ok");
    	okButton.setStyle("-fx-background-color: #b6e7c9; -fx-cursor: hand");
    	okButton.setPadding(new Insets(10, 20, 10, 20));
    	vBox.getChildren().addAll(achievement.renderAchievement());
    	hBox.getChildren().add(okButton);
    	hBox.setAlignment(Pos.CENTER);
    	hBox.setPadding(new Insets(10));
    	vBox.getChildren().add(hBox);
    	vBox.setAlignment(Pos.CENTER);
    	stage.setScene(new Scene(vBox));
    	stage.setTitle("Achievement unlocked!");
    }

}
