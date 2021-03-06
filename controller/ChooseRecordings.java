package application.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

public class ChooseRecordings extends AbstractController{
	static List<String> _selected = new ArrayList<String>();
	
	@FXML private Button nextButton;
	@FXML private Button backButton;
	@FXML private Button selectButton;
	@FXML private Button deselectButton;
	@FXML private ListView<String> selectionListView;
	@FXML private ListView<String> confirmListView;
	@FXML private TextField _searchText;
	@FXML private Button clearButton;
	@FXML private Button searchButton;
	@FXML private Button selectFileButton;
	@FXML private Button addCustomNameButton;
	@FXML private static String addName;
	private List<String> lowerCaseNames = new ArrayList<String>(Main.getNames());
	private ObservableList<String> selectionList;
	private FilteredList<String> filteredList;
	
	/**
	 * This changes the pane to the Main Menu pane
	 * @throws IOException
	 */
	@FXML public void changeToMain() throws IOException {
		//back to main menu
		switchScenes("MainMenu.fxml", _rootPane);
	}
	
	/**
	 * This method allows users to select which names they want to practice and handles the "select" button
	 * It takes the name and puts it onto the confirm list view
	 */
	@FXML public void pressedSelectButton() {
		String selectedItem = selectionListView.getSelectionModel().getSelectedItem();
		if (selectedItem == null || selectedItem.isEmpty()) {
			//do nothing
		} else {
			//put the name that is selected onto the confirmListView
			_searchText.clear();
			
			confirmListView.getItems().addAll(selectedItem);
			selectionList.remove(selectedItem);
		}
	}
	
	/**
	 * This method allows users to de-select which names they want to practice and handles the "de-select" button
	 * It takes the name and puts it back onto the selection list view
	 */
	@FXML public void pressedDeSelectButton() {
		String selectedItem = confirmListView.getSelectionModel().getSelectedItem();
		if (selectedItem == null || selectedItem.isEmpty()) {
			//do nothing
		} else {
			//move the selected name to the Selection view list
			// Enable the next button
			selectionList.add(selectedItem);
			confirmListView.getItems().remove(selectedItem);
		}
	}
	
	/**
	 * Goes to the play recordings screen. It is disabled until at 
	 * least 1 name has been selected.
	 * @throws IOException
	 */
	@FXML public void pressedNextButton() throws IOException {
		// Get the items from the list view and check if the names exist
		ObservableList<String> listOfNames = confirmListView.getItems();
		for (String name: listOfNames) {
			Boolean shouldBeAdded = checkIfNameExists(name);
			if (shouldBeAdded) {
				_selected.add(name);
			}
		}
		if(_selected.size() != 1) {
			// Confirmation: "Would you like to randomize recordings?"
			Alert randomizeConfirm = new Alert(AlertType.CONFIRMATION, 
					"Would you like to randomize recordings?");
			Optional<ButtonType> result = randomizeConfirm.showAndWait();
			
			//Shuffle the list if the user wanted to
			if (result.isPresent() && result.get() == ButtonType.OK) {
				Collections.shuffle(_selected);
			}
		}
			switchScenes("PlayRecordings.fxml", _rootPane);
	}
	
	/**
	 * This method handles the event when the select file button is pressed
	 * @throws IOException
	 */
	@FXML public void selectFile() throws IOException {
		// use file chooser to allow the user choose a text file
		FileChooser fc = new FileChooser();
		
		// add a filter (only text files)
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fc.getExtensionFilters().add(extFilter);
		fc.setInitialDirectory(new File(Main.WORK_DIR));
		File selectedFile = fc.showOpenDialog(null);
		
		ArrayList<String> inexistantNames = new ArrayList<String>();
		// Check if the user has selected anything
		if (selectedFile != null) {
			BufferedReader br = new BufferedReader(new FileReader(selectedFile));
			String currentLine;
			List<String> names = new ArrayList<String>();
			
			// read the text file
		 	while ((currentLine = br.readLine()) != null) {
		 		//check if the name is actually in the database
		 		currentLine = currentLine.trim();
		 		if (checkIfNameExists(currentLine)) {
		 			String[] splitted = currentLine.split("\\s+");
		 			
		 			//Capitalize the name
		 			String capitalizedName = "";
		 	        for(String name: splitted) {
		 	        	capitalizedName += " " + Main.getNames().get(lowerCaseNames.indexOf(name.toLowerCase())) ;;
		 	        }
		 	        
		 	        //trim any space
		 	        capitalizedName = capitalizedName.trim();
		 	        
		 	        //check if the name is repeated or not
		 	        if(!names.contains(capitalizedName)) {
		 	        	names.add(capitalizedName);
		 	        } else {
		 	        	inexistantNames.add(capitalizedName);
		 	        }
		 		} else {
		 			inexistantNames.add(currentLine);
		 		}
		 	} 
		 	br.close();
		 	
		 	//show an error if there are some names that did not exist
		 	if(inexistantNames != null && inexistantNames.size() != 0) {
		 		Alert alert = new Alert(AlertType.ERROR, "The following names did not exist "
		 				+ "(or were repeated) and will not be displayed: \n"
		 				+ inexistantNames, ButtonType.OK);
		 		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		 		alert.showAndWait();
		 	}
		 	
		 	 // if the file is empty, tell the user
		 	if(names.isEmpty()) {
		 		Alert alert = new Alert(AlertType.ERROR, "Text file was empty or names didn't exist", ButtonType.OK);
		 		alert.showAndWait();
		 	} else {
		 		confirmListView.getItems().addAll(Main.removeRedundant(names));
		 	}
		 
		} else {
			// Error message
			Alert alert = new Alert(AlertType.ERROR , "No text file was selected", ButtonType.OK);
			 alert.showAndWait();
		}
	}
	
	/**
	 * This method handles the event when the add name button is pressed
	 */
	@FXML public void addNameButtonListener() {
	        try {
	        	// open a new window
	        	openWindow("AddCustomName.fxml", "Add Custom Name");
	            
	            // if the user inputed any name
	            if(AddCustomName.nameExists && AddCustomName._name != null) {
	            	if(AddCustomName._name.trim() != "") {
	            	     confirmListView.getItems().add(AddCustomName._name);
				         nextButton.setDisable(false);
	            	}
	            }
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        
	}
	
	/**
	 * This is a listener for when the enter button is pressed in the search bar
	 * @param ae
	 */
	@FXML
	public void onEnter(ActionEvent ae) {
		if(!filteredList.isEmpty()) {
			confirmListView.getItems().add(filteredList.get(0));
			selectionList.remove(filteredList.get(0));
		}
	}
	
	/**
	 * Check if the name inputed exists (checks the _name list in Main)
	 * @param name the name that is inputed
	 * @return
	 */
	public Boolean checkIfNameExists(String name) {
		Boolean shouldBeAdded = true;
		name = name.trim();
		//use regex to split the whitespace in the name 
		String[] splitted = name.split("\\s+");
		for (String partOfName : splitted) {
			// If the names list contains the name
			if (!containsCaseInsensitive(partOfName, Main.getNames())) {
				shouldBeAdded = false;
			}
		}
		return shouldBeAdded;
	}

	
	private void setFilterList() {
		//code retrieved from https://stackoverflow.com/questions/44735486/javafx-scenebuilder-search-listview
		filteredList = new FilteredList<>(selectionList, e -> true);
		selectionListView.setItems(filteredList);
		_searchText.textProperty().addListener((observable, oldValue, newValue) -> {
		    filteredList.setPredicate(element -> {
		        if (newValue == null || newValue.isEmpty()) {
		            return true;
		        }
		        if (element.toLowerCase().startsWith(newValue.toLowerCase())) {
		            return true; // Filter matches
		        }
		        return false; // Does not match
		    });
		  
		    selectionListView.setItems(filteredList);
		    
		});
	}
	

	@Override
	public void customInit() {
		addName = null;
		//Add the files into the list view
		_selected.clear(); // clear any previous items
		selectionList=FXCollections.observableArrayList(Main.getNames());
				
		selectionListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		confirmListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		//Initially disable the next button
		nextButton.setDisable(true);
				
		confirmListView.getItems().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends String> change) {
				    if(confirmListView.getItems().size() == 0) {
						nextButton.setDisable(true);
					} else if(nextButton.isDisabled()){
						nextButton.setDisable(false); //un-disable the next button
					}
				   	Collections.sort(selectionList);
				}
			});
		
		lowerCaseNames.replaceAll(String::toLowerCase);
		
		setFilterList();
	}
}
