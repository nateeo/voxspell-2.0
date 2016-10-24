package voxspell.engine;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents shop state
 * Created by nateeo on 24/10/16.
 */
public class Shop implements Serializable {

    private ArrayList<Product> products;
    private transient EventHandler<WorkerStateEvent> hook;

    public Shop(EventHandler<WorkerStateEvent> e) {
        hook = e;
        makeNewProducts();
    }

    public void setListener(EventHandler<WorkerStateEvent> e) {
        hook = e;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    private void makeNewProducts() {
        products = new ArrayList<>();
        products.add(new Product("Elephants Dream Video Reward", "Elephants Dream-Mobile.mp4", ProductType.VIDEO, 25, hook));
        products.add(new Product("New Background Music", "Welcome2.mp3", ProductType.MUSIC, 10, hook));

    }
}

