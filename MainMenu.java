package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

/**
 * Controller class for the main menu.
 */
public class MainMenu extends AbstractController{
	@FXML GridPane _rootPane;
	
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
	
	
	

}
