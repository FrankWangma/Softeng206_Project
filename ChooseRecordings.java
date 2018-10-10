package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
		ObservableList<String> listOfSelectedItems = selectionListView.getSelectionModel().getSelectedItems();
		if (listOfSelectedItems.isEmpty()) {
			//do nothing
		} else {
			//put the name that is selected onto the confirmListView
			for(String names : listOfSelectedItems) {
				nextButton.setDisable(false); //un-disable the next button
				_selected.add(names);
				confirmListView.getItems().addAll(names);
				selectionListView.getItems().remove(names);
			}
		}
	}
	
	/**
	 * This method allows users to de-select which names they want to practice and handles the "de-select" button
	 * It takes the name and puts it back onto the selection list view
	 */
	@FXML public void pressedDeSelectButton() {
		ObservableList<String> listOfSelectedItems = confirmListView.getSelectionModel().getSelectedItems();
		if (listOfSelectedItems.isEmpty()) {
			//do nothing
		} else {
			//move the selected name to the Selection view list
			for(String names : listOfSelectedItems) {
				_selected.remove(names); 
				// Enable the next button
				if (_selected.isEmpty()) {
					nextButton.setDisable(true);
				}
				selectionListView.getItems().addAll(names);
				confirmListView.getItems().remove(names);
			}
		}
	}
	
	/**
	 * Goes to the play recordings screen. It is disabled until at 
	 * least 1 name has been selected.
	 * @throws IOException
	 */
	@FXML public void pressedNextButton() throws IOException {
		if(_selected.size() != 1) {
			// Confirmation: "Would you like to randomize recordings?"
			Alert randomizeConfirm = new Alert(AlertType.CONFIRMATION, 
					"Would you like to randomize recordings?");
			Optional<ButtonType> result = randomizeConfirm.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK) {
				Collections.shuffle(_selected);
			}
		}
			switchScenes("PlayRecordings.fxml", _rootPane);
	}
	
	@FXML public void selectFile() throws IOException {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(Main._workDir));
		File selectedFile = fc.showOpenDialog(null);
		
		if (selectedFile != null) {
			BufferedReader br = new BufferedReader(new FileReader(selectedFile));
			String currentLine;
	
		 	while ((currentLine = br.readLine()) != null) {
		 		_selected.add(currentLine);
		 	} 
		 	br.close();
		  
		 	if(_selected.isEmpty()) {
		 		Alert alert = new Alert(AlertType.ERROR, "Text file was empty", ButtonType.OK);
		 		alert.showAndWait();
		 	} else {
		 		pressedNextButton();
		 	}
		} else {
			// some error message?
			Alert alert = new Alert(AlertType.ERROR , "No text file was selected", ButtonType.OK);
			 alert.showAndWait();
		}
	}
	
	@FXML public void addNameButtonListener() {
	        try {
	        	Parent pane = FXMLLoader.load(getClass().getResource("AddCustomName.fxml"));
	            Stage stage = new Stage();
	            stage.setTitle("Add Custom Name");
	            stage.setScene(new Scene(pane));
	            stage.showAndWait();
	            if(AddCustomName._name != null && !AddCustomName._name.isEmpty()) {
		            confirmListView.getItems().add(AddCustomName._name);
		            _selected.add(AddCustomName._name);
		            nextButton.setDisable(false);
	            }
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        
	}
	
	
	@Override
	public void customInit() {
		addName = null;
		//Add the files into the list view
				_selected.clear(); // clear any previous items
				selectionListView.getItems().addAll(Main._names);
				selectionListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				confirmListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				//Initially disable the next button
				nextButton.setDisable(true);
				//code retrieved from https://stackoverflow.com/questions/44735486/javafx-scenebuilder-search-listview
				ObservableList<String> names = selectionListView.getItems();
				FilteredList<String> filteredList = new FilteredList<>(names, e -> true);
				_searchText.textProperty().addListener((observable, oldValue, newValue) -> {
				    filteredList.setPredicate(element -> {
				        if (newValue == null || newValue.isEmpty()) {
				            return true;
				        }

				        if (element.toLowerCase().startsWith(newValue.toLowerCase())) {
				            return true; // Filter matches
				        }
				        //Add your filtering conditions here

				        return false; // Does not match
				    });
				    selectionListView.setItems(filteredList);
				});
				
	}
}
