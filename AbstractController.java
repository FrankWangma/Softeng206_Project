package application;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class AbstractController {
	
	static ArrayList<Pane> _rootList;
	/**
	 * This method switches scenes, given an fxml file name
	 * @param fxml the name of the fxml file
	 * @throws IOException
	 */
	public void switchScenes(String fxml, Pane Pane) throws IOException {
		//use fxmlloader to change the fxml file
		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
		Stage stage = (Stage) Pane.getScene().getWindow();
		Scene scene = stage.getScene();
		
		//change and show the scene
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.sizeToScene();
	}
	
}
