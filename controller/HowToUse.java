package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * This class is used to display the how-to-use manual in the GUI
 * @author fwan175
 *
 */
public class HowToUse extends AbstractController{
	
	@FXML private TextArea _displayUserManual;
	@FXML private Button _closeButton;
	
	/**
	 * This handles the event when the close button is pressed
	 */
	@FXML public void closeButtonListener() {
		//Close the window
		Stage stage = (Stage) _closeButton.getScene().getWindow();
		stage.close();
	}
	
	@Override
	protected void customInit() {
		
	}
	
	
	
}
