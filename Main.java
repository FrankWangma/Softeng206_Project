package application;
	
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.concurrent.Task;
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
	
}
