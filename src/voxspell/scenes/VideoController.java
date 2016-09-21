package voxspell.scenes;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by harrylimp on 22/09/16.
 */
public class VideoController implements Initializable {

    @FXML
    private MediaView mv;
    private MediaPlayer mp;
    private Media me;

  /*  @FXML
    private Button playButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button fastforwardButton;
    @FXML
    private Button slowdownButton;
    @FXML
    private Button reloadButton;*/
    @FXML
    private Button exitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String path = new File("./lib/PC10.mp4").getAbsolutePath();
        me = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        DoubleProperty width = mv.fitWidthProperty();
        DoubleProperty height = mv.fitHeightProperty();
        width.bind(Bindings.selectDouble(mv.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(), "height"));

        exitButton.setOnMouseClicked(new VideoController.returnHandler());

        Image image1 = new Image(getClass().getResourceAsStream("playPic.png"));
        Button playButton = new Button();
        playButton.setGraphic(new ImageView(image1));
        Image image2 = new Image(getClass().getResourceAsStream("pausePic.png"));
        Button pauseButton = new Button();
        pauseButton.setGraphic(new ImageView(image2));
        Image image3 = new Image(getClass().getResourceAsStream("fastPic.png"));
        Button fastforwardButton = new Button();
        fastforwardButton.setGraphic(new ImageView(image3));
        Image image4 = new Image(getClass().getResourceAsStream("backPic.png"));
        Button slowdownButton = new Button();
        slowdownButton.setGraphic(new ImageView(image4));
        Image image5 = new Image(getClass().getResourceAsStream("reloadPic.png"));
        Button reloadButton = new Button();
        reloadButton.setGraphic(new ImageView(image5));

    }

    class returnHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            Stage stage;
            Parent root = null;
            stage = (Stage) ((Button)mouseEvent.getSource()).getScene().getWindow();

            try {
                root = FXMLLoader.load(getClass().getResource("endSession.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void play(ActionEvent event) {
        mp.play();
        mp.setRate(1);
    }

    public void pause(ActionEvent event) {
        mp.pause();
    }

    public void fast(ActionEvent event) {
        mp.setRate(2); // two times faster
    }

    public void slow(ActionEvent event) {
        mp.setRate(0.5); // two times slower
    }

    public void reload(ActionEvent event) {
        mp.seek(mp.getStartTime());
        mp.stop();
    }

}
