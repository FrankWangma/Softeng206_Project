package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class chooseRecordings {
	static List<String> _selected = new ArrayList<String>();
	
	@FXML private Button nextButton;
	@FXML private Button backButton;
	@FXML private Button selectButton;
	@FXML private Button deselectButton;
	@FXML HBox _rootPane;
	@FXML private ListView<String> selectionListView;
	@FXML private ListView<String> confirmListView;
	
	/**
	 * This changes the pane to the Main Menu pane
	 * @throws IOException
	 */
	@FXML public void changeToMain() throws IOException {
		//back to main menu
		goToView("MainMenu.fxml");
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
		if(confirmListView.getItems().size() != 1) {
			// Confirmation: "Would you like to randomize recordings?"
			Alert randomizeConfirm = new Alert(AlertType.CONFIRMATION, 
					"Would you like to randomize recordings?");
			Optional<ButtonType> result = randomizeConfirm.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK) {
				Collections.shuffle(_selected);
			}
		}
			goToView("PlayRecordings.fxml");
	}
	
	/**
	 * Helper method to change the the pane of the stage
	 * @param fxml
	 * @throws IOException
	 */
	protected void goToView(String fxml) throws IOException {
		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		stage.getScene().setRoot(pane);;
        stage.sizeToScene();
	}

	public void initialize() {
		//Add the files into the list view
		_selected.clear(); // clear any previous items
		selectionListView.getItems().addAll(Main._names);
		selectionListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		confirmListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		//Initially disable the next button
		nextButton.setDisable(true);
	}
}
