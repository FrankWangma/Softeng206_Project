package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class AbstractController implements Initializable {
	
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
