package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCustomName extends AbstractController{
	@FXML private Button _addButton;
	@FXML private Button _cancelButton;
	@FXML private TextField _customName;
	protected static String _name;
	
	@FXML public void addButtonListener() {
		if(_customName.getText() != null && !_customName.getText().isEmpty()) {
			_name = _customName.getText();
		}
		cancelButtonListener();
	}
	
	@FXML public void cancelButtonListener() {
		_customName.clear();
		Stage stage = (Stage) _cancelButton.getScene().getWindow();
		stage.close();
	}

	@Override
	protected void customInit() {
		_name = null;
	}
}
