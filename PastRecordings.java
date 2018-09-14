package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class PastRecordings {
	// FIELDS
	Stage _stage;
	
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
	
	@FXML protected void handleBack(ActionEvent event) {
        // GO TO PLAY VIEW
	}

}
