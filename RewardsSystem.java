package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class RewardsSystem extends AbstractController
{
	
	@FXML private Button _closeButton;
	@FXML private ProgressBar _rewardBar;
	@FXML private Label _progressLabel;
	
	@FXML public void backButtonListener() {
		Stage stage = (Stage) _closeButton.getScene().getWindow();
		stage.close();
	}
	
	@Override
	protected void customInit() {
		// TODO Auto-generated method stub
		
	}
}
