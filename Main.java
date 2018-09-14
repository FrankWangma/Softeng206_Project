package application;
	
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
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Name Sayer");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("MainMenu.fxml"));
			Parent layout = loader.load();
			Scene scene = new Scene(layout);
			primaryStage.setScene(scene);
			primaryStage.show();
		
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	@FXML 
	/**
	 * This method changes to the mic testing pane when the button is pressed
	 */
	private void micTestButton() {
		//Change to mic test pane
	}
	
	@FXML
	/**
	 * This method changes to the practice recording screen when the button is pressed
	 */
	private void practiceButton() {
		//Change to practice pane
	}
}
