package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	@FXML private Button toggleDatabase;
	@FXML private Button toggleUser;
	@FXML private Label toggleLabel;
	
	/**
	 * User presses play.
	 */
	@FXML protected void handlePlaySelected() {
		int selectedIndex = viewPastRecordings.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) { 
			// Set all buttons to disabled
			disableButtons();
			// Play the database file
			String cmd;
			if (isUser) {
				cmd = "ffplay -nodisp -autoexit "  + 
						PlayRecordings._fileFolder + Main.SEP + "user" + Main.SEP + 
						viewPastRecordings.getSelectionModel().getSelectedItem().getFile() +
						" &> recordingUser.txt";
			} else {
				cmd = "ffplay -nodisp -autoexit " + PlayRecordings._fileFolder + Main.SEP  + 
						viewPastRecordings.getSelectionModel().getSelectedItem().getFile() + 
						" &> /dev/null";
			}
			Background background = new Background();
			background.setcmd(cmd);
			Thread thread = new Thread(background);
			thread.start();
			
		}
	}
	
	/**
	 * User presses play from the database.
	 */
	@FXML protected void handlePlayDatabase() {
		// Set all buttons to disabled
		disableButtons();
		// Play the database file
		String cmd = "ffplay -nodisp -autoexit " + PlayRecordings._filePath +" &> /dev/null";
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
				
	}
	
	/**
	 * User presses Back; go back to PlayRecordings screen.
	 * @throws IOException
	 */
	@FXML protected void handleBack() throws IOException {
		switchScenes("PlayRecordings.fxml", _rootPane);
	}
	
	/**
	 * User switches to database list.
	 */
	@FXML protected void handleToggleDatabase() {
		viewPastRecordings.getItems().clear();
		viewPastRecordings.getItems().addAll(getDatabaseRecordings());
		isUser = false;
		toggleLabel.setText("Database Recordings");
	}
	
	/**
	 * User switches to user list.
	 */
	@FXML protected void handleToggleUser() {
		viewPastRecordings.getItems().clear();
		viewPastRecordings.getItems().addAll(getUserRecordings());
		isUser = true;
		toggleLabel.setText("User Recordings");
	}
	
	/**
	 * Looks at the user folder for recordings.
	 * @return A list of the user recording files
	 */
	private List<AudioFile> getUserRecordings() {
		File userFolder = new File(PlayRecordings._fileFolder + Main.SEP + "user");
		File[] userArray = userFolder.listFiles(Main._filter);
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
	 * Looks at the name folder for database recordings.
	 * @return A list of the database recording files
	 */
	private List<AudioFile> getDatabaseRecordings() {
		File dbFolder = new File(PlayRecordings._fileFolder);
		File[] dbArray = dbFolder.listFiles(Main._filter);
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
	 	viewPastRecordings.getItems().addAll(getUserRecordings());
		viewPastRecordings.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	protected void disableButtons() {
		buttonPlaySelected.setDisable(true);
		buttonPlayDatabase.setDisable(true);
		buttonBack.setDisable(true);
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
	}
}
