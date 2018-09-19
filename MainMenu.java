package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Controller class for the main menu.
 * 
 *
 */
public class MainMenu implements Initializable {
	@FXML GridPane _rootPane;
	
	/**
	 * This method changes to the mic testing pane when the button is pressed
	 */
	@FXML public void micTestButton() throws IOException {
		//Change to mic test pane
		switchScenes("MicTesting.fxml");
	}
	

	/**
	 * This method changes to the practice recording screen when the button is pressed
	 */
	@FXML public void practiceButton() throws IOException {
		//Change to practice pane
		switchScenes("chooseRecordings.fxml"); 
	}
	
	/**
	 * This method switches scenes, given an fxml file name
	 * @param fxml the name of the fxml file
	 * @throws IOException
	 */
	public void switchScenes(String fxml) throws IOException {
		//use fxmlloader to change the fxml file
		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
		Stage stage = (Stage) _rootPane.getScene().getWindow();
		Scene scene = stage.getScene();
		
		//change and show the scene
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
