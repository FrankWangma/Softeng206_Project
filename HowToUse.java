package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HowToUse extends AbstractController{
	
	@FXML private TextArea _displayUserManual;
	@FXML private Button _closeButton;
	
	@FXML public void closeButtonListener() {
		Stage stage = (Stage) _closeButton.getScene().getWindow();
		stage.close();
	}
	
	@Override
	protected void customInit() {
		
	}
	
	
	
}
