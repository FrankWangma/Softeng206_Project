package application;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

/**
 * Controller class for the recording screen.
 */
public class Record extends AbstractController{
	private File _tempFile = new File(PlayRecordings._fileFolder + Main.SEP + 
			"user" + Main.SEP + "temp.wav");
	private String _recordedFileName; //File path of the latest user recording
	private boolean _firstRecord;
	private boolean _saved;
	private boolean _isRecording;
	private Thread _recordThread = new Thread(new Background());
	
	@FXML private Label currentName;
	@FXML private Label recordLabel;
	@FXML private Button buttonRecordRecord;
	@FXML private Button buttonRecordPlayDatabase;
	@FXML private Button buttonRecordPlay;
	@FXML private Button buttonRecordSave;
	@FXML private Button buttonRecordBack;
	@FXML private Button buttonRecordCompare;
	
	/**
	 * This method will handle the record button if the user has recorded another file for the same name 
	 * and hasn't saved it. This will prompt them to make sure if they want to override the recording
	 * @param event
	 */
	@FXML protected void handleRecordRecord(ActionEvent event) {
		if (!_isRecording) {
			if (!_saved) {
				//If the recording has not been saved, then prompt the user for confirmation
				Alert nameConfirm = new Alert(AlertType.CONFIRMATION, 
					"Continue without saving your previous recording?");
				Optional<ButtonType> result = nameConfirm.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					record();
				}
			} else {
				record();
			}
		} else {
			// stop the recording
			stopRecord();
		}
    }
	
	/**
	 * Play the file that is from the database when the play button is pressed by the user
	 * @param event
	 * @throws IOException
	 */
	@FXML protected void handleRecordPlayDatabase(ActionEvent event) throws IOException {
		// Set all buttons to disabled
		disableButtons();
		String cmd = "ffplay -nodisp -autoexit " + PlayRecordings._filePath +" &> /dev/null";
		// Play the database file
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
	}
	
	/**
	 * This method will handle the playing of the recording that the user has just created when the
	 * play record button is pressed
	 * @param event
	 * @throws IOException
	 */
	@FXML protected void handleRecordPlay(ActionEvent event) throws IOException {
		// Set all buttons to disabled
		disableButtons();
		String cmd;
		// Play the user recording
		if (_saved) {
			// Play the saved file; path = _recordedFileName
			cmd = "ffplay -nodisp -autoexit " + _recordedFileName +" &> /dev/null";
		} else {
			// Play the temp file = _tempFile
			cmd = "ffplay -nodisp -autoexit " + _tempFile +" &> /dev/null";
		}
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
	}
	
	/**
	 * This saves the recording that the user has recorded when the save button is pressed
	 * @param event
	 */
	@FXML protected void handleRecordSave(ActionEvent event) {
		//save the file
		_saved = true;
		buttonRecordSave.setDisable(true);
		File file = new File(_recordedFileName);
		_tempFile.renameTo(file);
	}
	
	/**
	 * This method handles the event when the user presses the back button, and 
	 * calls the goBack method to take the user back to the previous screen
	 * @param event
	 * @throws IOException
	 */
	@FXML protected void handleRecordBack(ActionEvent event) throws IOException {
		if (!_saved) {
			// Confirmation: "Go back without saving?"
			Alert nameConfirm = new Alert(AlertType.CONFIRMATION, 
					"Go back without saving?");
			Optional<ButtonType> result = nameConfirm.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				_tempFile.delete();
				goBack();
			}
		} else {
			goBack();
		}
	}
	
	/**
	 * This method handles the event when the user presses the compare button, and 
	 * plays the database recording + current user recording one after the other.
	 * @param event
	 */
	@FXML protected void handleRecordCompare(ActionEvent event) {
		// Set all buttons to disabled
		disableButtons();
		String cmd = "ffplay -nodisp -autoexit " + PlayRecordings._filePath +" &> /dev/null";;
		// Play both recordings
		if (_saved) {
			// Play the saved file; path = _recordedFileName
			cmd = cmd + "; ffplay -nodisp -autoexit " + _recordedFileName +" &> /dev/null";
		} else {
			// Play the temp file = _tempFile
			cmd = cmd + "; ffplay -nodisp -autoexit " + _tempFile +" &> /dev/null";
		}
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
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
		buttonRecordRecord.setText("Stop");
		
		// setup the file names
		setRecordFile();
				
		// Record the thing
		String cmd = "ffmpeg -y -f alsa -i default -t 20 " + _tempFile.getAbsolutePath() + "&> recording.txt";
		Background background = new Background();
		background.setcmd(cmd);
		_recordThread = new Thread(background);
		_recordThread.start();
		_isRecording = true;
				
		// Once done, set text
		background.setOnSucceeded(e -> {
			buttonRecordRecord.setText("Record");
			recordLabel.setText("Done.");
			_isRecording = false;
		});
		
		background.setOnCancelled(e -> {
			buttonRecordRecord.setText("Record");
			recordLabel.setText("Done.");
			_isRecording = false;
		});
		
		_saved = false;
		_firstRecord = true;
	}
	
	/**
	 * Stop the recording.
	 */
	private void stopRecord() {
		_recordThread.interrupt();
	}
	
	/**
	 * Sets the name for a recorded file, containing the 
	 * date and time.
	 */
	private void setRecordFile() {
		// Get the date and time
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-YYYY_HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now); 
						
		// Set the wav file name
		_recordedFileName = PlayRecordings._fileFolder + Main.SEP + "user" + 
				Main.SEP + "user_" + date + "_" + PlayRecordings._name + ".wav";
	}
	
	/**
	 * Goes back to the PlayRecordings Screen
	 * @throws IOException
	 */
	private void goBack() throws IOException {
		switchScenes("PlayRecordings.fxml", _rootPane);
	}
	
	/**
	 * This method disables all the buttons in the current pane
	 */
	protected void disableButtons() {
		buttonRecordPlay.setDisable(true);
		buttonRecordPlayDatabase.setDisable(true);
		buttonRecordBack.setDisable(true);
		buttonRecordSave.setDisable(true);
		buttonRecordCompare.setDisable(true);
	}
	/**
	 * This method enables all the buttons in the current pane
	 */
	@Override
	protected void enableButtons() {
		buttonRecordPlayDatabase.setDisable(false);
		buttonRecordBack.setDisable(false);
		
		// Don't re-enable play if user hasn't recorded yet
		if (_firstRecord) {
			buttonRecordPlay.setDisable(false);
			buttonRecordCompare.setDisable(false);
		}
		
		// Disable save button if already saved (or haven't recorded yet)
		if (!_saved) {
			buttonRecordSave.setDisable(false);
		}
	}
	
	@Override
	public void customInit() {
		currentName.setText("Recording for: " + PlayRecordings._name);
		recordLabel.setText("Press to start recording.");
		_isRecording = false;
		_firstRecord = false;
		_saved = true;
		buttonRecordPlay.setDisable(true);
		buttonRecordSave.setDisable(true);
		buttonRecordCompare.setDisable(true);
	}
        
}
