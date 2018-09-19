package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ChooseRecordings implements Initializable{
	@FXML private Button nextButton;
	@FXML private Button backButton;
	@FXML private Button selectButton;
	@FXML private Button deselectButton;
	@FXML HBox _rootPane;
	@FXML private ListView<String> selectionListView;
	@FXML private ListView<String> confirmListView;
	static List<String> _selectedNames = new ArrayList<String>();
	
	
	@FXML public void changeToMain() throws IOException {
		//back to main menu
		Parent pane = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		Scene scene = stage.getScene();
		
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
	}
	
	@FXML public void pressedSelectButton() {
		ObservableList<String> listOfSelectedItems = selectionListView.getSelectionModel().getSelectedItems();
		if (listOfSelectedItems.isEmpty()) {
			//do nothing
		} else {
			for(String names : listOfSelectedItems) {
				confirmListView.getItems().addAll(names);
				selectionListView.getItems().remove(names);
			}
		}
	}
	
	@FXML public void pressedDeSelectButton() {
		ObservableList<String> listOfSelectedItems = confirmListView.getSelectionModel().getSelectedItems();
		if (listOfSelectedItems.isEmpty()) {
			//do nothing
		} else {
			for(String names : listOfSelectedItems) {
				selectionListView.getItems().addAll(names);
				confirmListView.getItems().remove(names);
			}
		}
	}
	
	@FXML public void pressedNextButton() throws IOException {
		_selectedNames = confirmListView.getItems();
		Parent pane = FXMLLoader.load(getClass().getResource("PlayRecordings.fxml"));
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		Scene scene = stage.getScene();
		
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Add the files into the list view
		for(String name : Main._names) {
			selectionListView.getItems().addAll(name);
			selectionListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		}
		
		confirmListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
}
