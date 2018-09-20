package application;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Record {
	File _tempFile = new File(PlayRecordings._filePath + System.getProperty("file.separator") + 
			"user" + System.getProperty("file.separator") + "temp.wav");
	Scene _previousScene;
	String _recordedFileName; //File path of the latest user recording
	boolean _saved = true;
	
	@FXML BorderPane _rootPane;
	@FXML private Label currentName;
	@FXML private Label recordLabel;
	@FXML private Button buttonRecordRecord;
	@FXML private Button buttonRecordPlayDatabase;
	@FXML private Button buttonRecordPlay;
	@FXML private Button buttonRecordSave;
	@FXML private Button buttonRecordBack;
	
	@FXML protected void handleRecordRecord(ActionEvent event) {
		if (!_saved) {
			Alert nameConfirm = new Alert(AlertType.CONFIRMATION, 
					"Continue without saving your previous recording?");
			Optional<ButtonType> result = nameConfirm.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				record();
			}
		} else {
			record();
		}
    }
	
	/**
	 * Play from the database
	 * @param event
	 * @throws IOException
	 */
	@FXML protected void handleRecordPlayDatabase(ActionEvent event) throws IOException {
		// Set all buttons to disabled
		disableButtons();
				
		// Play the database file
		Task<Void> recordTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				bash("ffplay -nodisp -autoexit " + PlayRecordings.getRecording().toURI().toString() +" &> /dev/null");
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
		};
		new Thread(recordTask).start();
	}
	
	@FXML protected void handleRecordPlay(ActionEvent event) throws IOException {
		// Set all buttons to disabled
		disableButtons();
				
		// Play the user recording
		if (_saved) {
			// Play the saved file; path = _recordedFileName
		} else {
			// Play the temp file = _tempFile
		}
				
		// Once done, set all buttons to enabled
		//#TODO
		enableButtons();
	}
	
	@FXML protected void handleRecordSave(ActionEvent event) {
		_saved = true;
		File file = new File(_recordedFileName);
		_tempFile.renameTo(file);
	}
	
	@FXML protected void handleRecordBack(ActionEvent event) throws IOException {
		if (!_saved) {
			// Confirmation: "Go back without saving?"
			Alert nameConfirm = new Alert(AlertType.CONFIRMATION, 
					"Go back without saving?");
			Optional<ButtonType> result = nameConfirm.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				goBack();
			}
		} else {
			goBack();
		}
	}
	
	/**
	 * Records the user's input.
	 */
	private void record() {
		// Delete any old versions
		File old = new File("temp.wav");
		old.delete();
				
		// Set all buttons to disabled
		disableButtons();
				
		// Set text to "recording"
		recordLabel.setText("Recording...");
				
		// Get the date and time
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-YYYY_HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now); 
				
		// Set the wav file name
		_recordedFileName = PlayRecordings._filePath + System.getProperty("file.separator") + 
				"user" + System.getProperty("file.separator") + "user_" + date +
				"_" + PlayRecordings._name + ".wav";
				
		// Record the thing
		Task<Void> recordTask = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				String cmd = "ffmpeg -f alsa -i default -t 5 " + _tempFile.getAbsolutePath() + "&> recording.txt";
				ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
				Process process = null;
				try {process = builder.start();} 
				catch (IOException e) {e.printStackTrace();}
				
				try {process.waitFor();} // waits until recording is finished
				catch (InterruptedException e) {e.printStackTrace();}
				
				return null;
			}
		};
				
		// Once done, set all buttons to enabled
		recordTask.setOnSucceeded(e -> {
			enableButtons();
			recordLabel.setText("Done.");
		});
				
		new Thread(recordTask).start();
		_saved = false;
	}
	
	/**
	 * Goes back to the PlayRecordings Screen
	 * @throws IOException
	 */
	private void goBack() throws IOException {
		_tempFile.delete();
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		Scene scene = stage.getScene();
		
        scene = PlayRecordings._savedScene;
        stage.setScene(scene);
        stage.show();
	}
	
	protected void disableButtons() {
		buttonRecordPlay.setDisable(true);
		buttonRecordRecord.setDisable(true);
		buttonRecordPlayDatabase.setDisable(true);
		buttonRecordBack.setDisable(true);
		buttonRecordSave.setDisable(true);
	}
	
	protected void enableButtons() {
		buttonRecordPlay.setDisable(false);
		buttonRecordRecord.setDisable(false);
		buttonRecordPlayDatabase.setDisable(false);
		buttonRecordBack.setDisable(false);
		buttonRecordSave.setDisable(false);
	}
	
	@FXML
	public void initialize() {
		currentName.setText("Recording for: " + PlayRecordings._name);
		recordLabel.setText("The recording is 5 seconds long.");
		buttonRecordPlay.setDisable(true);
		buttonRecordSave.setDisable(true);
	}
        
}
