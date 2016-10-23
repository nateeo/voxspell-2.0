package voxspell.engine;

/**
 * JavaFX thread queued event to flush after scene has been loaded
 * Created by nhur on 23/10/16.
 */
public interface QueuedEvent {
    void execute();
}
