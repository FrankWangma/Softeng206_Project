package application;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class HomeController {
	@FXML
	private Text text;
	
	@FXML 
	private void micTestButton() {
		text.setText("Mic test 1 2 3");
	}
}
