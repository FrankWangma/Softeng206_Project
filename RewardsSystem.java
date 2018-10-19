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

/**
 * This class handles the reward system window
 *
 */
public class RewardsSystem extends AbstractController {
	@FXML private Button _closeButton;
	@FXML private ProgressBar _rewardBar;
	@FXML private Label _progressLabel;
	
	/**
	 * This method listens for when the back button is pressed
	 */
	@FXML public void backButtonListener() {
		//close the window
		Stage stage = (Stage) _closeButton.getScene().getWindow();
		stage.close();
	}
	
	@Override
	protected void customInit() {
		//Check if the reward.txt file exists
		File reward = new File(Main._workDir + Main.SEP + "Reward.txt");
		if(reward.exists()) {
			BufferedReader br;
			int progress = 0;
			 try {
					br = new BufferedReader(new FileReader(reward));
					String st;
					while ((st = br.readLine()) != null)  {
						// get the progress number from the reward.txt
						if(!st.isEmpty()) {
							progress = Integer.parseInt(st);
						}
					}
					} catch (NumberFormatException | IOException e1) {
						e1.printStackTrace();
					}
			 // If it is more than 40 (over the limit), set it to 40
			 if(progress > 40) {
				 progress = 40;
			 }
			 //update the text and progress bar
				_progressLabel.setText("Progress: " + Integer.toString(progress) + "/40");
				_rewardBar.setProgress(progress*0.025);
		} else {
			//If it doesn't exist, just set the progress bar and text to 0
			_progressLabel.setText("Progress: 0/40");
			_rewardBar.setProgress(0);
		}
	}
}
