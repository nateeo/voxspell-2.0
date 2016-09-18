package voxspell.engine;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class to read and write data to files
 * Created by nateeo on 16/09/16.
 */
public class DataIO {
    private ArrayList<Integer> enabledLevels;
    private File LEVEL_DATA = new File(".levelData.ser");

    public DataIO() {
        load();
    }

    /**
     * Add level to enabled level list if not already added
     * @param level
     */
    public void enableLevel(int level) {
       if (!enabledLevels.contains(level)) { // add to enabled list if not already enabled
           enabledLevels.add(level);
       }
    }

    /**
     * return max enabled level
     * @return max enabled level
     */
    public int highestLevelEnabled() {
        if (enabledLevels.size() == 0) {
            return 1; // no unlocks
        }
        return Collections.max(enabledLevels);
    }

    public void save() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(LEVEL_DATA));
            os.writeObject(enabledLevels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * load existing data from file (or initialize if no file exists)
     */
    private void load() {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(LEVEL_DATA));
            enabledLevels = (ArrayList<Integer>) is.readObject();
            if (enabledLevels == null) {
                enabledLevels = new ArrayList<Integer>();
            }
        } catch (FileNotFoundException e) {
            enabledLevels = new ArrayList<Integer>();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}