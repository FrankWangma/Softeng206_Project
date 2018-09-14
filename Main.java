package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;



public class Main extends Application {
	
	private Button _micButton = new Button("Test Microphone");
	private StackPane _layout = new StackPane();
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Name Sayer");
			_layout.getChildren().add(_micButton);
			
			Scene scene = new Scene(_layout, 800,600);
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
