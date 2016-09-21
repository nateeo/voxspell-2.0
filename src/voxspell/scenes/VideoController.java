package voxspell.scenes;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.awt.*;
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
        mp.play();
    }

    public void start(ActionEvent event) {
        mp.seek(mp.getStartTime());
        mp.stop();
    }

    public void last(ActionEvent event) {
        mp.seek(mp.getTotalDuration());
        mp.stop();
    }
}
