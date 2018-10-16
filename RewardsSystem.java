package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
		File reward = new File(Main._workDir + Main.SEP + "Reward.txt");
		if(reward.exists()) {
			BufferedReader br;
			int progress = 0;
			 try {
					br = new BufferedReader(new FileReader(reward));
					String st;
					while ((st = br.readLine()) != null)  {
						if(!st.isEmpty()) {
							progress = Integer.parseInt(st);
						}
					}
					} catch (NumberFormatException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				_progressLabel.setText("Progress: " + Integer.toString(progress) + "/40");
				_rewardBar.setProgress(progress*0.025);
		} else {
			_progressLabel.setText("Progress: 0/40");
			_rewardBar.setProgress(0);
		}
	}
}
