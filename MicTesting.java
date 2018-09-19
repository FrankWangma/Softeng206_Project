package application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.Timer;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MicTesting  implements Initializable{
	@FXML BorderPane _rootPane;
	@FXML Button backButton;
	@FXML Button playButton;
	@FXML Button testButton;
	@FXML Label RecordingText;
	
	@FXML public void changeToMain() throws IOException {
		//back to main menu
		Parent pane = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		Scene scene = stage.getScene();
		
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
	}
	
	@FXML public void testMic() {
		//Disable buttons
		backButton.setDisable(true);
		playButton.setDisable(true);
		testButton.setDisable(true);
		RecordingText.setText("Recording....");
		//Use a process builder to use ffmpeg to make audios
		String cmd = " ffmpeg -y -f alsa -i default -t 5 MicTest.wav &> /dev/null";
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
	}
	
	@FXML public void playSound() {
		
		//Play the mic test file created
				String testFile = "MicTest.wav";
				File file = new File(testFile);
				if (file.exists()) {
					Media sound = new Media(file.toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(sound);
					mediaPlayer.play();
				}
	}
	
	/**
	 * Background worker to create the ffmpeg files and stop any freezing of GUi
	 * 
	 *
	 */
	public class Background extends Task<Void>{
		private String _cmd;
		@Override
		protected Void call() throws Exception {
			
			bash(_cmd);
			return null;
		}
		
		@Override
		protected void done() {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					RecordingText.setText("Done Recording");
					backButton.setDisable(false);
					playButton.setDisable(false);
					testButton.setDisable(false);
				}
				
			});
		}
		
		public void setcmd(String cmd) {
			_cmd = cmd;
		}
		
		/**
		 * Process builder method to call a bash function
		 * @param cmd the command that needs to be input
		 */
		public void bash(String cmd) {
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			try {
				Process process = builder.start();
				
				//Wait for a process to finish before exiting
				int exitStatus = process.waitFor();
				if(exitStatus!=0) {
					return;
				}
			} catch (IOException e) {
				System.out.println("Error: Invalid command");
			} catch (InterruptedException e) {
				System.out.println("Error: Interrupted");
			}
		}
		
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
}
