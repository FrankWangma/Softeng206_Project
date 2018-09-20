package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
	
	@FXML protected void handlePlaySelected(ActionEvent event) {
		int selectedIndex = viewPastRecordings.getSelectionModel().getSelectedIndex();
		if (selectedIndex != -1) { 
			// DO THINGS
		}
	}
	
	@FXML protected void handlePlayDatabase(ActionEvent event) {
		// DO THINGS
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
	
	@FXML protected void handleBack(ActionEvent event) throws IOException {
        // GO TO PLAY VIEW
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		Scene scene = stage.getScene();
		
        scene = PlayRecordings._savedScene;
        stage.setScene(scene);
        stage.show();
	}
	
	@FXML protected void handleToggleDatabase(ActionEvent event) {
		viewPastRecordings.getItems().clear();
		viewPastRecordings.getItems().addAll(getDatabaseRecordings());
	}
	
	@FXML protected void handleToggleUser(ActionEvent event) {
		viewPastRecordings.getItems().clear();
		viewPastRecordings.getItems().addAll(getUserRecordings());
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
				userList.add(userArray[i].getName());
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
		for (int i=0;i<dbArray.length;i++) {
			dbList.add(dbArray[i].getName());
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
}
