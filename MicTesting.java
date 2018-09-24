package application;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MicTesting{
	@FXML BorderPane _rootPane;
	@FXML Button backButton;
	@FXML ProgressBar progressBar;;
	/**
	 * Method to change back to the main menu pane
	 * @throws IOException
	 */
	@FXML public void changeToMain() throws IOException {
		//back to main menu
		Parent pane = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		Scene scene = stage.getScene();
		
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
	}
	
	@FXML
	public void initialize() {
		new Thread(new Recorder(progressBar)).start();;

	}
	
	
	/**
	 * This class is used to record the microphone levels
	 * @author fwan175
	 *
	 */
	class Recorder implements Runnable {
		final ProgressBar _progressBar;
		
		Recorder(final ProgressBar progressBar) {
            this._progressBar = progressBar;
        }
		@Override
		public void run() {
	            AudioFormat fmt = new AudioFormat(44100f, 16, 1, true, false);
	            final int bufferByteSize = 2048;

	            TargetDataLine line;
	            try {
	                line = AudioSystem.getTargetDataLine(fmt);
	                line.open(fmt, bufferByteSize);
	            } catch(LineUnavailableException e) {
	                System.err.println(e);
	                return;
	            }

	            byte[] buf = new byte[bufferByteSize];
	            float[] samples = new float[bufferByteSize / 2];

	            float lastPeak = 0f;

	            line.start();
	            for(int b; (b = line.read(buf, 0, buf.length)) > -1;) {

	                // convert bytes to samples here
	                for(int i = 0, s = 0; i < b;) {
	                    int sample = 0;

	                    sample |= buf[i++] & 0xFF; // (reverse these two lines
	                    sample |= buf[i++] << 8;   //  if the format is big endian)

	                    // normalize to range of +/-1.0f
	                    samples[s++] = sample / 32768f;
	                }

	                float rms = 0f;
	                float peak = 0f;
	                for(float sample : samples) {

	                    float abs = Math.abs(sample);
	                    if(abs > peak) {
	                        peak = abs;
	                    }

	                    rms += sample * sample;
	                }

	                rms = (float)Math.sqrt(rms / samples.length);

	                if(lastPeak > peak) {
	                    peak = lastPeak * 0.875f;
	                }

	                lastPeak = peak;
	                _progressBar.setProgress(rms);
	            }
		
		}
	}
}

