package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class PlayRecordings {
	// FIELDS
	Media databaseFile; //The current file in question. Currently a placeholder
	boolean isBad; //is it marked as bad quality
	
	@FXML Label currentName;
	@FXML Button buttonPlay;
	@FXML Button buttonRecord;
	@FXML Button buttonPastRecordings;
	@FXML Button buttonNext;
	@FXML Button toggle;
	@FXML Label toggleYes; //Bad quality
	@FXML Label toggleNo; //Not bad quality (default)
	
	/**
	 * Sets the name that is playing.
	 * @param name
	 */
	public void setName(String name) {
		currentName.setText(name);
	}
	
	@FXML protected void handlePlay(ActionEvent event) {
		// Set all buttons to disabled
		buttonPlay.setDisable(true);
		buttonRecord.setDisable(true);
		buttonPastRecordings.setDisable(true);
		buttonNext.setDisable(true);
		toggle.setDisable(true);
		
		// Play the thing
		File file = new File(Main._workDir + System.getProperty("file.separator") + 
				"test.wav"); //TESTING ONLY
		databaseFile = new Media(file.toURI().toString()); //TESTING ONLY
		MediaPlayer player = new MediaPlayer(databaseFile);
		player.play();
		
		// Once done, set all buttons to enabled
		player.setOnEndOfMedia(new Runnable() {
			public void run() {
				buttonPlay.setDisable(false);
				buttonRecord.setDisable(false);
				buttonPastRecordings.setDisable(false);
				buttonNext.setDisable(false);
				toggle.setDisable(false);
			}
		});
	}
	
	@FXML protected void handleRecord(ActionEvent event) {
        // GO TO RECORD VIEW
	}
	
	@FXML protected void handlePast(ActionEvent event) {
        // GO TO PAST RECORDING VIEW
	}
	
	@FXML protected void handleNext(ActionEvent event) {
        // Get the next name in the list
		
		// Set the label to this next name
		setName("placeholder");
		
		// Set the media file to this new name (change null)
		databaseFile = new Media("placeholder");
		
		// Otherwise, go back to the main menu
	}
	
	@FXML protected void toggle(ActionEvent event) {
		// Toggle enable/disable [doesn't work properly]
		if (isBad) {
			toggleYes.setDisable(false);
			toggleNo.setDisable(true);
			isBad = false;
		} else {
			toggleYes.setDisable(true);
			toggleNo.setDisable(false);
			isBad = true;
		}
		
		// Save settings to file
	}
	
}
