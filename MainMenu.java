package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//load css file
		 _rootPane.getStylesheets().clear();
		 File theme = new File(Main._workDir + System.getProperty("file.separator") + "theme.txt");
			
			if(theme.length() == 5) {
				  _rootPane.getStylesheets().add(getClass().getResource("LightTheme.css").toExternalForm());
			} else {
				  _rootPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			}
	}


	
	
	
	

}
