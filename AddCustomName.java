package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *  This class is the controller for the Add Custom Name window
 *
 */
public class AddCustomName extends AbstractController{
	@FXML private Button _addButton;
	@FXML private Button _cancelButton;
	@FXML private TextField _customName;
	protected static String _name;
	
	/**
	 * This method handles the event when the add button is pressed
	 */
	@FXML public void addButtonListener() {
		_name = _customName.getText().substring(0, 1).toUpperCase() + 
				_customName.getText().substring(1);
		cancelButtonListener();
	}
	
	/**
	 * This method handles the event for when the cancel button is pressed
	 */
	@FXML public void cancelButtonListener() {
		_customName.clear();
		Stage stage = (Stage) _cancelButton.getScene().getWindow();
		stage.close();
	}

	@Override
	protected void customInit() {
		_name = null;
		_addButton.setDisable(true);
		
		// Listener code retrieved from : https://stackoverflow.com/questions/29594746/how-to-determine-if-the-text-has-changed-or-no-in-the-textfield-with-javafx
		_customName.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(observable.getValue() == null  ||observable.getValue().isEmpty()) {
                	_addButton.setDisable(true);
                } else {
                	_addButton.setDisable(false);
                }
            }
        });
	}
}
