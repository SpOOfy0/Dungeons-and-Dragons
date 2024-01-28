package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;



public class SoundManager {
    
    GamePannel gp;
    private Clip clip;
    
    public SoundManager(GamePannel gp){
        this.gp = gp;
    }

    public Clip loadAudio(String filePath){
        try{
            // Load sound file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));

            // Create clip
            clip = AudioSystem.getClip();

            // Open audio input stream
            clip.open(audioInputStream);
        }catch(Exception e){
            System.out.println("Error loading audio");
        }

        return clip;
    }

    public static void play(Clip clip) {
        // Play the sound
        if (clip != null) {
            clip.start();
        }
    }

    public void loop(Clip clip) {
        // Loop the sound continuously
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop(Clip clip) {
        // Stop the sound
        if (clip != null) {
            clip.stop();
        }
    }
}
