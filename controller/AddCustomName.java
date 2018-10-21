package application.controller;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
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
		_name = _customName.getText().trim();
		
		// if the user inputed any name
        if(_name != null && !_name.isEmpty()) {
        	String newName = checkIfNameExists(_name);
        	//Check if the name exists
			if(!nameExists) {
				Alert alert = new Alert(AlertType.ERROR , newName + "in " + _name + " does not exist", 
						ButtonType.OK);
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
	            alert.showAndWait();
	            _name = "";
			} 
        }
        
        // trim and capitalize the name that the user has added
        String[] splitted = _customName.getText().trim().split("\\s+");
        String capitalizedName = "";
        for(String name: splitted) {
        	capitalizedName = capitalizedName + " " + name.substring(0, 1).toUpperCase() + 
    				name.substring(1).toLowerCase();
        }
        // trim it again just to make sure it has no spaces at the ends
		_name = capitalizedName.trim();
		
		// close the window
		cancelButtonListener();
	}
	
	/**
	 * Check if the name inputed exists (checks the _name list in Main)
	 * @param name the name that is inputed
	 * @return
	 */
	public String checkIfNameExists(String name) {
		name = name.trim();
		
		//use regex to split the whitespace in the name 
		String[] splitted = name.split("\\s+");
		String inexistantName = "";
		for (String partOfName : splitted) {
			// If the names list contains the name
			if (!containsCaseInsensitive(partOfName, Main.getNames())) {
				inexistantName += "\"" + partOfName + "\" ";
				nameExists = false;
			}
		}
		
		return inexistantName;
	}
	
	/**
	 * This method handles the event for when the cancel button is pressed
	 */
	@FXML public void cancelButtonListener() {
		// clear the textfield and close the window
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
                if(observable.getValue() == null  ||observable.getValue().isEmpty() || observable.getValue().matches("\\s*")) {
                	_addButton.setDisable(true);
                } else {
                	_addButton.setDisable(false);
                }
            }
        });
	}
}
