package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
		Parent pane = FXMLLoader.load(getClass().getResource("PlayRecordings.fxml"));
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		Scene scene = stage.getScene();
		
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
	}

}
