package application;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
	
	@FXML private ListView<String> viewPastRecordings;
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
				cmd = "ffplay -nodisp -autoexit "  + PlayRecordings._fileFolder + "/user/" + 
						viewPastRecordings.getSelectionModel().getSelectedItem()+".wav &> recordingUser.txt";
			} else {
				cmd = "ffplay -nodisp -autoexit " + PlayRecordings._fileFolder + "/" + 
			viewPastRecordings.getSelectionModel().getSelectedItem() +".wav &> /dev/null";
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
	 * @return A String list of the user recording files
	 */
	private List<String> getUserRecordings() {
		File userFolder = new File(PlayRecordings._fileFolder + Main.SEP + "user");
		File[] userArray = userFolder.listFiles(Main._filter);
		List<String> userList = new ArrayList<String>();
		if(userArray != null) {
			for (int i=0;i<userArray.length;i++) {
				String name = userArray[i].getName();
				userList.add(getDispName(name));
			}
		}
		return userList;
	}
	
	/**
	 * Looks at the name folder for database recordings.
	 * @return A String list of the database recording files
	 */
	private List<String> getDatabaseRecordings() {
		File dbFolder = new File(PlayRecordings._fileFolder);
		File[] dbArray = dbFolder.listFiles(Main._filter);
		List<String> dbList = new ArrayList<String>();
		if(dbArray != null) {
			for (int i=0;i<dbArray.length;i++) {
				String name = dbArray[i].getName();
				if(!name.contains("cleaned")) {
					dbList.add(getDispName(name));
				}
			}
		}
		return dbList;
	}
	
	/**
	 * Given a file name of a recording, returns the
	 * display name.
	 * @param name
	 * @return the name and date to display
	 */
	private String getDispName(String name) {
		name = name.substring(name.indexOf("_")+1, name.length()-4);
		String date = name.substring(0, name.lastIndexOf('_'));
		
		// file date format
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d-M-yyyy_H-m-s");
		LocalDateTime localDate = LocalDateTime.parse(date, dtf);
		
		// new date format
		DateTimeFormatter newFormat = 
				DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
		String dispDate = 
				newFormat.format(localDate);
		String dispName = name.substring(name.lastIndexOf('_')+1) +" "+ dispDate;
		
		return dispName;
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
