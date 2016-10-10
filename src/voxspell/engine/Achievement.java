package voxspell.engine;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import voxspell.Voxspell;

import java.io.Serializable;

/**
 * Class to represent an achievement
 * Created by nhur714 on 10/10/2016.
 */
public class Achievement implements Serializable, Comparable<Achievement> {
    private String name;
    private String description;
    private Rarity rarity;
    int points;

    public enum Rarity {

        COMMON("white", 10),
        RARE("#3232ff", 50),
        EPIC("#9966cc", 100),
        LEGENDARY("#FFD700", 500);

        private final String color;
        private final int points;
        Rarity(final String color, final int points) {
            this.color = color;
            this.points = points;
        }
        public String color() {
            return this.color;
        }

        public int points() {
            return this.points;
        }
    }

    public Achievement(String name, String description, Rarity rarity, int points) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.points = points;
    }

    /**
     * Pack an achievement into a pane
     * @return Pane pane that contains the achievement
     */
    public Pane renderAchievement() {
        Pane pane = new Pane();
        VBox vBox = new VBox();

        // initialise components
        ImageView image = new ImageView(new Image(Voxspell.class.getResource("scenes/assets/star.png").toExternalForm()));
        image.prefWidth(100);
        image.prefHeight(100);
        Label title = new Label(this.name);
        title.setStyle("-fx-font-size: 18; -fx-font-color: " + rarity.color());

        Label description = new Label(this.description);
        description.setText(this.description);
        description.setStyle("-fx-font-size: 13");

        vBox.getChildren().addAll(image, title, description);
        pane.getChildren().add(vBox);

        return pane;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Achievement) {
            return ((Achievement)other).name.equals(this.name);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Achievement other) {
        return this.rarity.points() - other.rarity.points();
    }
}
