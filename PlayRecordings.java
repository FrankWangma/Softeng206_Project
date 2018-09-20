package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

import application.MicTesting.Background;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class PlayRecordings {
	// FIELDS
	static String _filePath;
	static String _name;
	static Scene _savedScene;
	boolean _isBad; // is it marked as bad quality
	int _index; // index of the list we are on
	
	@FXML BorderPane _rootPane;
	@FXML Label currentName;
	@FXML Button buttonPlay;
	@FXML Button buttonRecord;
	@FXML Button buttonPastRecordings;
	@FXML Button buttonNext;
	@FXML Button toggle;
	@FXML Label toggleYes; // Bad quality
	@FXML Label toggleNo; // Not bad quality (default)
	@FXML ListView<String> nameList;
	
	/**
	 * Sets the name that is playing.
	 * @param name
	 */
	public void setName(String name) throws IOException {
		_name = name;
		currentName.setText(name); //set the title
		_filePath = Main._workDir + System.getProperty("file.separator") + 
				"name_database" + System.getProperty("file.separator") + name;
		
		// Getting saved file quality
		File quality = new File(_filePath + System.getProperty("file.separator") + "info.txt");
		quality.createNewFile(); // create info file if doesn't exist
		if (quality.length() == 0) {
			_isBad = false;
			toggleYes.setVisible(false);
			toggleNo.setVisible(true);
		} else {
			_isBad = true;
			toggleYes.setVisible(true);
			toggleNo.setVisible(false);
		}
		
	}
	
	/**
	 * Looks at the available wav files and returns the latest as
	 * the database recording.
	 * @return the database .wav file
	 */
	public File getRecording() {
		File nameDir = new File(_filePath);
		File[] files = nameDir.listFiles(Main._filter);
		Arrays.sort(files);
		return files[files.length-1];
	}
	
	@FXML protected void handlePlay(ActionEvent event) {
		// Set all buttons to disabled
		disableButtons();
		// Play the audio
		String cmd = "ffplay -nodisp -autoexit " + getRecording().toURI().toString() +" &> listenfile";
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
		
	}
	
	@FXML protected void handleRecord(ActionEvent event) throws IOException {
        // GO TO RECORD VIEW
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		_savedScene = stage.getScene();
		goToView("Record.fxml"); 
	}
	
	@FXML protected void handlePast(ActionEvent event) throws IOException {
        // GO TO PAST RECORDING VIEW
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		_savedScene = stage.getScene();
		goToView("PastRecordings.fxml");
	}
	
	@FXML protected void handleNext(ActionEvent event) throws IOException {
        // Get the next name in the list
		_index++;
		
		try {
			nameList.getSelectionModel().select(_index);
			String name = nameList.getItems().get(_index);
			// Set the next name
			setName(name);
		}
		catch (IndexOutOfBoundsException e) {
			// Otherwise, go back to the main menu
			nameList.getItems().clear();
			goToView("MainMenu.fxml");
		}
	}
	
	@FXML protected void toggle(ActionEvent event) {
		// Toggle visible/invisible
		if (_isBad) {
			toggleYes.setVisible(false);
			toggleNo.setVisible(true);
			_isBad = false;
			File quality = new File(_filePath + 
					System.getProperty("file.separator") + "info.txt");
			quality.delete();
			try {quality.createNewFile();}
			catch (IOException e) {}
			
		} else {
			toggleYes.setVisible(true);
			toggleNo.setVisible(false);
			_isBad = true;
			saveQuality();
		}
	}
	
	/**
	 * Writes a "1" to the text file, meaning the recording is 
	 * tagged as bad quality
	 */
	protected void saveQuality() {
		// Write settings to file
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(_filePath + System.getProperty("file.separator") + 
					"info.txt", true);
			bw = new BufferedWriter(fw);
			bw.write("1");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {bw.close();}
				if (fw != null) {fw.close();}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	protected void goToView(String fxml) throws IOException {
		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		Scene scene = stage.getScene();
		
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
	}
	
	protected void disableButtons() {
		buttonPlay.setDisable(true);
		buttonRecord.setDisable(true);
		buttonPastRecordings.setDisable(true);
		buttonNext.setDisable(true);
		toggle.setDisable(true);
	}
	
	protected void enableButtons() {
		buttonPlay.setDisable(false);
		buttonRecord.setDisable(false);
		buttonPastRecordings.setDisable(false);
		buttonNext.setDisable(false);
		toggle.setDisable(false);
	}

	@FXML
	public void initialize() {
		nameList.getItems().addAll(chooseRecordings._selected);
		_index = 0;
		nameList.getSelectionModel().select(_index);
		String name = nameList.getItems().get(_index); //get the first name
		try {setName(name);} 
		catch (IOException e){}
		nameList.setMouseTransparent(true); //makes the list unselectable
		nameList.setFocusTraversable(false);
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
					enableButtons();
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
}
