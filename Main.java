package application;
	
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Name Sayer");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("Gui.fxml"));
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
}
