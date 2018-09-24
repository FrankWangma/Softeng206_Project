package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * 
 * Controller class for Past Recordings screen.
 *
 */
public class PastRecordings {
	// FIELDS
	String _name;
	
	@FXML BorderPane _rootPane;
	@FXML ListView<String> viewPastRecordings;
	@FXML Button buttonPlaySelected;
	@FXML Button buttonPlayDatabase;
	@FXML Button buttonBack;
	@FXML Button toggleDatabase;
	@FXML Button toggleUser;
	private Boolean isUser = true;
	@FXML Label toggleLabel;
	
	@FXML protected void handlePlaySelected(ActionEvent event) {
		int selectedIndex = viewPastRecordings.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) { 
			// Set all buttons to disabled
			disableButtons();
			// Play the database file
			String cmd;
			if (isUser) {
				cmd = "ffplay -nodisp -autoexit "  + PlayRecordings._filePath + "/user/" + 
						viewPastRecordings.getSelectionModel().getSelectedItem()+".wav &> /dev/null";
			} else {
				cmd = "ffplay -nodisp -autoexit " + PlayRecordings._filePath + "/" + 
			viewPastRecordings.getSelectionModel().getSelectedItem() +".wav &> /dev/null";
			}
			Background background = new Background();
			background.setcmd(cmd);
			Thread thread = new Thread(background);
			thread.start();
			
		}
	}
	
	@FXML protected void handlePlayDatabase(ActionEvent event) {
		// Set all buttons to disabled
		disableButtons();
		// Play the database file
		String cmd = "ffplay -nodisp -autoexit " + PlayRecordings.getRecording().toURI().toString() +" &> /dev/null";
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
				
	}
	
	@FXML protected void handleBack(ActionEvent event) throws IOException {
		Parent pane = FXMLLoader.load(getClass().getResource("PlayRecordings.fxml"));
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		Scene scene = stage.getScene();
		
		//change and show the scene
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.sizeToScene();
	}
	
	@FXML protected void handleToggleDatabase(ActionEvent event) {
		viewPastRecordings.getItems().clear();
		viewPastRecordings.getItems().addAll(getDatabaseRecordings());
		isUser = false;
		toggleLabel.setText("Database Recordings");
	}
	
	@FXML protected void handleToggleUser(ActionEvent event) {
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
		File userFolder = new File(PlayRecordings._filePath + 
				System.getProperty("file.separator") + "user");
		File[] userArray = userFolder.listFiles(Main._filter);
		List<String> userList = new ArrayList<String>();
		if(userArray != null) {
			for (int i=0;i<userArray.length;i++) {
				String name = userArray[i].getName();
				userList.add(name.substring(0, name.length()-4));
			}
		}
		return userList;
	}
	
	/**
	 * Looks at the name folder for database recordings.
	 * @return A String list of the database recording files
	 */
	private List<String> getDatabaseRecordings() {
		File dbFolder = new File(PlayRecordings._filePath);
		File[] dbArray = dbFolder.listFiles(Main._filter);
		List<String> dbList = new ArrayList<String>();
		if(dbArray != null) {
			for (int i=0;i<dbArray.length;i++) {
				String name = dbArray[i].getName();
				dbList.add(name.substring(0, name.length()-4));
			}
		}
		return dbList;
	}
	
	@FXML
	public void initialize() {
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
	
	protected void enableButtons() {
		buttonPlaySelected.setDisable(false);
		buttonPlayDatabase.setDisable(false);
		buttonBack.setDisable(false);
		toggleDatabase.setDisable(false);
		toggleUser.setDisable(false);
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
