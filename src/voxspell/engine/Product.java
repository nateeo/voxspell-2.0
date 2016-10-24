package voxspell.engine;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
        hook = evnt;
        Pane pane = new Pane();
        VBox vBox = new VBox(5);
        Label title = new Label(name);
        Label info = new Label("This is a new " + (type == ProductType.VIDEO ? "video" : "music") + "item that you can use");
        Label purchased = new Label(unlocked ? "Bought!" : "");
        Button button = new Button();
        String buttonText = "";

        EventHandler<MouseEvent> toggleOn;
        EventHandler<MouseEvent> handleBuy;

        toggleOn = (e) -> {
            if (!isActive) {
                if (type == ProductType.MUSIC) {
                    SceneManager.changeMusic(fileName);
                    button.setText("Turn off");
                } else {
                    SceneManager.changeVideo(fileName);
                    button.setText("Turn off");
                }
                isActive = true;
            } else {
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
            money.deduct(price, 1);
            if (type == ProductType.MUSIC) {
                SceneManager.changeMusic(fileName);
            } else {
                SceneManager.changeVideo(fileName);
            }
            button.setText("Turn off");
            button.setOnMouseClicked(toggleOn);
            isActive = true;
            unlocked = true;
            purchased.setText("Bought!");
            hook.handle(null);
        };

        if (unlocked) {
            buttonText = isActive ? "Turn off" : "Turn on";
            button.setOnMouseClicked(toggleOn);
        } else {
            // buy
            buttonText = "Buy";
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
