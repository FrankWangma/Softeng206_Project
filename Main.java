package application;
	
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;




public class Main extends Application {
	static Path relativePath = Paths.get("");
	static String _workDir = relativePath.toAbsolutePath().toString();
	private Stage _primaryStage;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			_primaryStage = primaryStage;
			_primaryStage.setTitle("Name Sayer");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("MainMenu.fxml"));
			Parent layout = loader.load();
			Scene scene = new Scene(layout);
			_primaryStage.setScene(scene);
			_primaryStage.show();
		
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	/**
	 * This method changes to the mic testing pane when the button is pressed
	 */
	@FXML private void micTestButton() {
		//Change to mic test pane
		changeScene("MicTesting.fxml");
	}
	

	/**
	 * This method changes to the practice recording screen when the button is pressed
	 */
	@FXML private void practiceButton() {
		//Change to practice pane
	}
	
	private void changeScene( String fxml) {
			Parent pane;
			try {
				pane = FXMLLoader.load(getClass().getClassLoader().getResource(fxml));
				Scene scene = new Scene(pane);
				_primaryStage.setScene(scene);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}
