package voxspell.engine;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import voxspell.Voxspell;

import java.io.Serializable;

/**
 * represents a product that can be unlocked with gold
 */
public class Product implements Serializable {
    String name;
    String fileName;
    boolean unlocked;
    boolean isActive;
    ProductType type;
    int price;
    transient EventHandler<WorkerStateEvent> hook;

    public Product(String name, String fileName, ProductType type, int price, EventHandler<WorkerStateEvent> e) {
        hook = e;
        this.name = name;
        this.type = type;
        this.price = price;
        this.fileName = fileName;
        unlocked = false;
        isActive = false;
    }

    public Pane renderProduct(Money money, EventHandler<WorkerStateEvent> evnt) {
        AudioClip buySound = new AudioClip(Voxspell.class.getResource("scenes/assets/Coin01.mp3").toExternalForm());
        hook = evnt;
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); " +
                "-fx-border-color: black; -fx-border-width: 5; -fx-padding: 10");
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-padding: 20; -fx-align: center");
        Label title = new Label(name);
        title.setStyle("-fx-font-size: 20; -fx-text-align: center");
        Label info = new Label("This is a new " + (type == ProductType.VIDEO ? "video" : "music") + " item that you can use");
        Label purchased = new Label(unlocked ? "Bought!" : "");
        Button button = new Button();
        String buttonText = "";

        EventHandler<MouseEvent> toggleOn;
        EventHandler<MouseEvent> handleBuy;

        toggleOn = (e) -> {
            if (!isActive) {
                button.getStyleClass().remove("primary");
                button.getStyleClass().add("cancel");
                if (type == ProductType.MUSIC) {
                    SceneManager.changeMusic(fileName);
                    button.setText("Turn off");
                } else {
                    SceneManager.changeVideo(fileName);
                    button.setText("Turn off");
                }
                isActive = true;
            } else {
                button.getStyleClass().remove("cancel");
                button.getStyleClass().add("primary");
                if (type == ProductType.MUSIC) {
                    SceneManager.changeMusic("Welcome.mp3");
                    button.setText("Turn on");
                } else {
                    SceneManager.changeVideo("PC10.mp4");
                    button.setText("Turn on");
                }
                isActive = false;
            }
            hook.handle(null);
        };
        handleBuy = (e) -> {
            buySound.play();
            money.deduct(price, 1);
            if (type == ProductType.MUSIC) {
                SceneManager.changeMusic(fileName);
            } else {
                SceneManager.changeVideo(fileName);
            }
            button.setText("Turn off");
            button.getStyleClass().add("cancel");
            button.setOnMouseClicked(toggleOn);
            isActive = true;
            unlocked = true;
            purchased.setText("Bought!");
            hook.handle(null);
        };

        if (unlocked) {
            buttonText = isActive ? "Turn off" : "Turn on";
            button.getStyleClass().add(isActive ? "cancel" : "primary");
            button.setOnMouseClicked(toggleOn);
        } else {
            // buy
            buttonText = "Buy for " + price + " silver coins";
            if (!money.enoughFor(price, 1)) {
                button.setDisable(true);
            } else {
                button.setOnMouseClicked(handleBuy);
            }
        }
        button.setText(buttonText);
        vBox.getChildren().addAll(title, info, purchased, button);
        pane.getChildren().add(vBox);
        return pane;
    }
}
