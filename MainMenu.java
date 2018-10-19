package application;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

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
		switchScenes("ChooseRecordings.fxml", _rootPane); 
	}
	
	@FXML public void helpButton() throws IOException {
		//Change to help pane
		switchScenes("HelpScreen.fxml", _rootPane);
	}

	/**
	 * This method handles the rewards button when its pressed
	 * @throws IOException
	 */
	@FXML public void rewardsButton() throws IOException {
		//Open rewards screen
		// open a new window
    	Parent pane = FXMLLoader.load(getClass().getResource("RewardScreen.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Rewards");
        stage.setScene(new Scene(pane));
        stage.showAndWait();
	}
	@Override
	public void customInit() {
		//Check if the user is a new user
		File checkNewUser = new File(Main._workDir + Main.SEP + "isNewUser.txt");
		if(checkNewUser.exists()) {
			//Do Nothing
		} else {
			// warn the user to test microphone
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
