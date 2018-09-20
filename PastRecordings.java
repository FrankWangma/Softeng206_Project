package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		for (int i=0;i<userArray.length;i++) {
			userList.add(userArray[i].getName());
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

}
