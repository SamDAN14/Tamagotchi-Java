import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Musica {
    
    private Clip clip;

    public void ReproMusica(String ruta) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(ruta));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl volumen = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumen.setValue(-10.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Reproduce en bucle
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error al reproducir audio: " + e.getMessage());
        }
    }

    public void DetMusica() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    public static void ReproEfect(String ruta) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(ruta));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl volumen = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumen.setValue(-10.0f);
            clip.start(); // Reproduce el sonido una sola vez
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error al reproducir efecto: " + e.getMessage());
        }
    }
}
