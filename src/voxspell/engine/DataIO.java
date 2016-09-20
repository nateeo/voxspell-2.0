package voxspell.engine;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class to read and write data to files
 * Created by nhur714 on 16/09/16.
 */
public class DataIO {
    private ArrayList<Integer> enabledLevels;
    private String voice;

    private File LEVEL_DATA = new File(".levelData.ser");
    private File VOICE_DATA = new File(".voiceData.ser");

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
           save();
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

    public String getVoice() {
        return voice;
    }

    public void save() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(LEVEL_DATA));
            os.writeObject(enabledLevels);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(VOICE_DATA));
            os.writeObject(voice);
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

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(VOICE_DATA));
            voice = (String) is.readObject();
            if (voice == null) {
                voice = Festival.DEFAULT;
            }
        } catch (FileNotFoundException e) {
            voice = Festival.DEFAULT;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset levels
     */
    public void delete() {
        LEVEL_DATA.delete();
        enabledLevels = new ArrayList<Integer>();
    }
}
