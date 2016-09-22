package voxspell.scenes;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import voxspell.engine.SceneManager;

import java.io.File;
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

        /*(Image image1 = new Image(getClass().getResourceAsStream("assets/playPic.png"));
        playButton.setGraphic(new ImageView(image1));
        Image image2 = new Image(getClass().getResourceAsStream("assets/pausePic.png"));
        pauseButton.setGraphic(new ImageView(image2));
        Image image3 = new Image(getClass().getResourceAsStream("assets/fastPic.png"));
        fastforwardButton.setGraphic(new ImageView(image3));
        Image image4 = new Image(getClass().getResourceAsStream("assets/backPic.png"));
        slowdownButton.setGraphic(new ImageView(image4));
        Image image5 = new Image(getClass().getResourceAsStream("assets/reloadPic.png"));
        reloadButton.setGraphic(new ImageView(image5));*/

    }

    class returnHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            SceneManager.goTo("main.fxml");
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
