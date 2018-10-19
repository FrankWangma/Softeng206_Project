package application.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import application.Main;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class AbstractController {
	static final String _resourceFolder = "resources" + Main.SEP;
	
	@FXML Pane _rootPane;
	
	/**
	 * This method switches scenes, given an fxml file name
	 * @param fxml the name of the fxml file
	 * @throws IOException
	 */
	public void switchScenes(String fxml, Pane Pane) throws IOException {
		//use fxmlloader to change the fxml file
		Parent pane = FXMLLoader.load(getClass().getResource(_resourceFolder + fxml));
		Stage stage = (Stage) Pane.getScene().getWindow();
		Scene scene = stage.getScene();
		
		//change and show the scene
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.sizeToScene();
	}
	
	/**
	 * Opens a new window given an fxml file
	 * @param fxml the fxml to load
	 * @param title the title of the new window
	 * @throws IOException
	 */
	public void openWindow(String fxml, String title) throws IOException {
		// open a new window
    	Parent pane = FXMLLoader.load(getClass().getResource(_resourceFolder + fxml));
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(pane));
        stage.showAndWait();
	}
	
	/**
	 * Background worker to create the ffmpeg files and stop any freezing of the GUI
	 * 
	 */
	public class Background extends Task<Void>{
		private String _cmd;
		@Override
		protected Void call() throws Exception {
			bash(_cmd);
			return null;
		}
		
		@Override
		protected void done() {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					enableButtons();
				}
				
			});
		}
		
		/**
		 * Sets the command to pass to the bash process
		 * @param cmd the command
		 */
		public void setcmd(String cmd) {
			_cmd = cmd;
		}
		
		/**
		 * Process builder method to call a bash function
		 * @param cmd the command that needs to be input
		 */
		public void bash(String cmd) {
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			Process process = null;
			try {
				process = builder.start();
				
				//Wait for a process to finish before exiting
				int exitStatus = process.waitFor();
				if(exitStatus!=0) {
					return;
				}
			} catch (IOException e) {
				System.out.println("Error: Invalid command");
			} catch (InterruptedException e) {
				// Interrupted: stop the process
				try {
					OutputStream out = process.getOutputStream();
					out.write("q".getBytes()); //q to stop the process
					out.flush();    
				} catch (IOException ex) {}
			}
		}
		
	}
	
	/**
	 * To enable buttons after a bash process to record or play is finished. 
	 * Optional override.
	 */
	protected void enableButtons() {}
	
	/**
	 * Initialisation of the screen.
	 */
	public void initialize() {
		//load css file
		 _rootPane.getStylesheets().clear();
		 File theme = new File(Main.WORK_DIR + Main.SEP + "theme.txt");
			BufferedReader br1;
			try {
				br1 = new BufferedReader(new FileReader(theme));
				String st; 
				 while ((st = br1.readLine()) != null)  {
					  _rootPane.getStylesheets().add(getClass().getResource(_resourceFolder + 
							  st).toExternalForm());
				} 
				 br1.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
			customInit();
	}
	
	/**
	 * Check if the name exists in the inputed list
	 * @param s
	 * @param l
	 * @return
	 */
	public boolean containsCaseInsensitive(String s, List<String> l){
        return l.stream().anyMatch(x -> x.equalsIgnoreCase(s));
    }
	
	/**
	 * Class specific initialize()
	 */
	protected abstract void customInit();
	   
	
}
