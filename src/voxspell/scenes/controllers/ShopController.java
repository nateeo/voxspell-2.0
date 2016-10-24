package voxspell.scenes.controllers;

import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import voxspell.engine.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the shop scene
 * Created by nateeo on 24/10/16.
 */
public class ShopController implements Initializable {
    @FXML
    private Button mainMenuButton;
    @FXML
    private Label goldLabel;
    @FXML
    private Label silverLabel;
    @FXML
    private Label bronzeLabel;
    @FXML
    private HBox shopBox;

    private DataIO data = DataIO.getInstance();

    private Shop shop;

    private Money money;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //initialise listeners
        mainMenuButton.setOnMouseClicked((e) -> SceneManager.goTo("main.fxml"));
        EventHandler<WorkerStateEvent> updateMoney = (e) -> {
            // update display and save
            data.saveObject(new File(".shopData.ser"), shop);
            data.saveMoney();
            updateMoney();
        };

        // load the shop
        money = data.getMoney();
        updateMoney();
        shop = (Shop) data.loadObject(new File(".shopData.ser"), new Shop(updateMoney));
        shop.setListener(updateMoney); // if already existing
        ObservableList<Node> list = shopBox.getChildren();
        for (Product product : shop.getProducts()) {
            list.add(product.renderProduct(data.getMoney(), updateMoney));
        }
    }

    /**
     * Update the coin values
     */
    private void updateMoney() {
        goldLabel.setText("" + money.getGold());
        silverLabel.setText("" + money.getSilver());
        bronzeLabel.setText("" + money.getBronze());
    }
}
