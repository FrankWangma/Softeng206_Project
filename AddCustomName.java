package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 *  This class is the controller for the Add Custom Name window
 *
 */
public class AddCustomName extends AbstractController{
	@FXML private Button _addButton;
	@FXML private Button _cancelButton;
	@FXML private TextField _customName;
	protected static Boolean nameExists = true;
	protected static String _name;
	
	/**
	 * This method handles the event when the add button is pressed
	 */
	@FXML public void addButtonListener() {
		_name = _customName.getText();
		// if the user inputted any name
        if(_name != null && !_name.isEmpty()) {
        	String newName = checkIfNameExists(_name);
        	//Check if the name exists
			if(!nameExists) {
				_name = "";
				Alert alert = new Alert(AlertType.ERROR , newName + " does not exist", ButtonType.OK);
	            alert.showAndWait();
			} 
        }
        String[] splitted = _customName.getText().split("\\s+");
        String capitalizedName = "";
        for(String name: splitted) {
        	capitalizedName = capitalizedName + " " + name.substring(0, 1).toUpperCase() + 
    				name.substring(1).toLowerCase();
        }
		_name = capitalizedName.trim();
		cancelButtonListener();
	}
	
	/**
	 * Check if the name inputted exists (checks the _name list in Main)
	 * @param name the name that is inputted
	 * @return
	 */
	public String checkIfNameExists(String name) {
	
		
		String newName = "";
		//use regex to split the whitespace in the name 
		String[] splitted = name.split("\\s+");
		for (String partOfName : splitted) {
			// If the names list contains the name
			if (!containsCaseInsensitive(partOfName, Main._names)) {
				partOfName = "\"" + partOfName + "\"";
				nameExists = false;
			}
		}
		
		return newName;
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
		nameExists = true;
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
