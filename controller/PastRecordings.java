package application.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import application.audio.AudioFile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

/**
 * 
 * Controller class for Past Recordings screen.
 *
 */
public class PastRecordings extends AbstractController {
	// FIELDS
	String _name;
	private Boolean isUser = true;
	
	@FXML private ListView<AudioFile> viewPastRecordings;
	@FXML private Button buttonPlaySelected;
	@FXML private Button buttonPlayDatabase;
	@FXML private Button buttonBack;
	@FXML private Button buttonCompare;
	@FXML private Button toggleDatabase;
	@FXML private Button toggleUser;
	@FXML private Label toggleLabel;
	
	/**
	 * This method handles the event for when the user pressed the Play button
	 */
	@FXML protected void handlePlaySelected() {
		int selectedIndex = viewPastRecordings.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) { 
			// Set all buttons to disabled
			disableButtons();
			// Play the database file
			String cmd;
			if (isUser) {
				cmd = "ffplay -nodisp -autoexit \""  + 
						PlayRecordings._fileFolder + Main.SEP + "user" + Main.SEP + 
						viewPastRecordings.getSelectionModel().getSelectedItem().getFile() +
						"\" &> /dev/null";
			} else {
				cmd = "ffplay -nodisp -autoexit \"" + PlayRecordings._fileFolder + Main.SEP  + 
						viewPastRecordings.getSelectionModel().getSelectedItem().getFile() + 
						"\" &> /dev/null";
			}
			Background background = new Background();
			background.setcmd(cmd);
			Thread thread = new Thread(background);
			thread.start();
			
		}
	}
	
	/**
	 * This method handles the event for when the user presses the Play Database button
	 */
	@FXML protected void handlePlayDatabase() {
		// Set all buttons to disabled
		disableButtons();
		// Play the database file
		String cmd = "ffplay -nodisp -autoexit \"" + PlayRecordings._filePath + "\" &> /dev/null";
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();	
	}
	
	/**
	 * This method handles the event for when the user presses the back button
	 * @throws IOException
	 */
	@FXML protected void handleBack() throws IOException {
		//Go back to the play recordings screen
		switchScenes("PlayRecordings.fxml", _rootPane);
	}
	
	/**
	 * This method handles the event for when the user presses the Compare Button
	 */
	@FXML protected void handleCompare() {
		int selectedIndex = viewPastRecordings.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) {
			disableButtons();
		
			// Play both files
			String database = "ffplay -nodisp -autoexit \"" + PlayRecordings._filePath + "\" &> /dev/null";
			String user = "";
			if (isUser) {
				user = "ffplay -nodisp -autoexit \"" + PlayRecordings._fileFolder + Main.SEP + "user" + 
						Main.SEP + viewPastRecordings.getSelectionModel().getSelectedItem().getFile() +
						"\" &> /dev/null";
			}
			// Run the command in a background thread
			String cmd = database + ";" + user;
			Background background = new Background();
			background.setcmd(cmd);
			Thread thread = new Thread(background);
			thread.start();	
		}
	}
	
	/**
	 * This method handles the event for when the user presses the Database Button
	 */
	@FXML protected void handleToggleDatabase() {
		//Change to the database list
		viewPastRecordings.getItems().clear();
		viewPastRecordings.getItems().addAll(getDatabaseRecordings());
		isUser = false;
		toggleLabel.setText("Database Recordings");
		
		// Disable compare button
		buttonCompare.setDisable(true);
	}
	
	/**
	 * This method handles the event for when the user presses the User button
	 */
	@FXML protected void handleToggleUser() {
		//Change to the user list
		viewPastRecordings.getItems().clear();
		viewPastRecordings.getItems().addAll(getUserRecordings());
		isUser = true;
		toggleLabel.setText("User Recordings");
		
		// enable compare button
		buttonCompare.setDisable(false);
	}
	
	/**
	 * This method looks at the user folder of the current name for recordings.
	 * @return A list of the user recording files
	 */
	private List<AudioFile> getUserRecordings() {
		File userFolder = new File(PlayRecordings._fileFolder + Main.SEP + "user");
		File[] userArray = userFolder.listFiles(Main.getFilter());
		List<AudioFile> userList = new ArrayList<AudioFile>();
		if(userArray != null) {
			for (int i=0;i<userArray.length;i++) {
				AudioFile file = new AudioFile(userArray[i].getName());
				userList.add(file);
			}
		}
		return userList;
	}
	
	/**
	 * This method looks at the name folder for database recordings.
	 * @return A list of the database recording files
	 */
	private List<AudioFile> getDatabaseRecordings() {
		File dbFolder = new File(PlayRecordings._fileFolder);
		File[] dbArray = dbFolder.listFiles(Main.getFilter());
		List<AudioFile> dbList = new ArrayList<AudioFile>();
		if(dbArray != null) {
			for (int i=0;i<dbArray.length;i++) {
				AudioFile file = new AudioFile(dbArray[i].getName());
				dbList.add(file);
			}
		}
		return dbList;
	}
	
	@Override
	public void customInit() {
		// add the names list to the list view
	 	viewPastRecordings.getItems().addAll(getUserRecordings());
		viewPastRecordings.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	protected void disableButtons() {
		buttonPlaySelected.setDisable(true);
		buttonPlayDatabase.setDisable(true);
		buttonBack.setDisable(true);
		buttonCompare.setDisable(true);
	    toggleDatabase.setDisable(true);
		toggleUser.setDisable(true);
	}
	
	@Override
	protected void enableButtons() {
		buttonPlaySelected.setDisable(false);
		buttonPlayDatabase.setDisable(false);
		buttonBack.setDisable(false);
		toggleDatabase.setDisable(false);
		toggleUser.setDisable(false);
		
		// Check if the user list is shown
		if (isUser) {
			buttonCompare.setDisable(false);
		}
	}
}
