/** Class to load, play and change volume of 16 byte .wav audio files
 *
 * @author fpetek
 * @version 1.0
 **/
package utility;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import settings.GlobalSettings;

public class AudioPlayer implements Runnable {
  public Clip audio;

  /** Method to play a sound. Thread.sleep() method is needed so audio won't kill itself. **/
  public void playAudio() {
    try {
      audio.start();
      Thread.sleep(audio.getMicrosecondLength() / 1000);
    } catch (InterruptedException interruptedException) {
      interruptedException.printStackTrace();
    }
  }

  /**
   * Method to load an audio file into class variable.
   *
   * @param audioFile is name of file, has to be .wav
   **/
  public void loadAudio(String audioFile) {
    File audioFilePath = new File(GlobalSettings.filepath + audioFile);
    try {
      AudioInputStream input = AudioSystem.getAudioInputStream(audioFilePath);
      audio = AudioSystem.getClip();
      audio.open(input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to set volume of audio clip
   *
   * @param level has to be between 6 and -80, otherwise it throws exception
   **/
  public void setVolume(int level) {
    FloatControl volume = (FloatControl) this.audio.getControl(FloatControl.Type.MASTER_GAIN);
    volume.setValue((float) level);
  }

  @Override
  public void run() {}
}
