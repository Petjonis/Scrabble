package utility;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

/**
 * Class to play 16bit .wav audio files
 * Waiting for Listener to implement this properly
 * Audio clip kills itself when no MessageDialog comes up :/
 * Hopefully listener/threads will help out...
 */
public class AudioPlayer implements Runnable{
    // System independent path to resource folder
    private static final String path =
            System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + "resources"
                    + System.getProperty("file.separator");

    /**
     * Method to play a sound.
     *
     * @param audioFile is name of audio file.
     */
    public void playAudio(String audioFile){
        File audioFilePath = new File(path+audioFile);
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(audioFilePath);
            Clip audio = AudioSystem.getClip();
            audio.open(input);
            audio.start();
            JOptionPane.showMessageDialog(null, "Press OK to stop playing");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
    }
}
