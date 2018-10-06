package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

public class ChooseRecordings extends AbstractController{
	static List<String> _selected = new ArrayList<String>();
	
	@FXML private Button nextButton;
	@FXML private Button backButton;
	@FXML private Button selectButton;
	@FXML private Button deselectButton;
	@FXML HBox _rootPane;
	@FXML private ListView<String> selectionListView;
	@FXML private ListView<String> confirmListView;
	@FXML private TextField _searchText;
	@FXML private Button clearButton;
	@FXML private Button searchButton;
	@FXML private Button selectFileButton;
	
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
	
	
	/**
	 * This method filters the list of the listView
	 */
	@FXML public void filterList() {
		if (_searchText != null || _searchText.getText().trim().length() != 0) {
			String[] parts = _searchText.getText().toLowerCase().split(" ");
			
			ObservableList<String> searchedNames = FXCollections.observableArrayList();
			for (Object names: Main._names) {
				boolean match = true;
				String name = (String) names; 
				for(String part: parts) {
					if ((!name.toLowerCase().contains(part))) {
						match = false;
						break;
					}
				}
				if (match) {
					searchedNames.add(name);
				}
			}
			selectionListView.getItems().clear();
			selectionListView.setItems(searchedNames);
		}
	}
	
	@FXML public void clearSearch() {
		selectionListView.getItems().clear();
		selectionListView.getItems().addAll(Main._names);
		_searchText.setText("");
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
	
	
	public void initialize() {
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//load css file
		 _rootPane.getStylesheets().clear();
		 File theme = new File(Main._workDir + System.getProperty("file.separator") + "theme.txt");
			
			if(theme.length() == 5) {
				  _rootPane.getStylesheets().add(getClass().getResource("LightTheme.css").toExternalForm());
			} else {
				  _rootPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			}
	   
		
		//Add the files into the list view
				_selected.clear(); // clear any previous items
				selectionListView.getItems().addAll(Main._names);
				selectionListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				confirmListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				//Initially disable the next button
				nextButton.setDisable(true);
	}
}
