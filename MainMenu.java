package application;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller class for the main menu.
 */
public class MainMenu extends AbstractController{
	
	/**
	 * This method changes to the mic testing pane when the button is pressed
	 */
	@FXML public void micTestButton() throws IOException {
		//Change to mic test pane
		switchScenes("MicTesting.fxml", _rootPane);
	}
	

	/**
	 * This method changes to the practice recording screen when the button is pressed
	 */
	@FXML public void practiceButton() throws IOException {
		//Change to practice pane
		switchScenes("chooseRecordings.fxml", _rootPane); 
	}
	
	@FXML public void helpButton() throws IOException {
		//Change to help pane
		switchScenes("HelpScreen.fxml", _rootPane);
	}

	@Override
	public void customInit() {
		File checkNewUser = new File(Main._workDir + Main.SEP + "isNewUser.txt");
		if(checkNewUser.exists()) {
			//Do Nothing
		} else {
			Alert alert = new Alert(AlertType.WARNING , "New User Detected \nPlease test your microphone first", ButtonType.OK);
            alert.showAndWait();
            try {
				checkNewUser.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
