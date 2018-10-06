package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

/**
 * Controller class for Play Recordings screen.
 * 
 *
 */
public class PlayRecordings extends AbstractController{
	// FIELDS
	static String _filePath;
	static String _name;
	boolean _isBad; // is it marked as bad quality
	int _index; // index of the list we are on
	
	@FXML BorderPane _rootPane;
	@FXML Label currentName;
	@FXML Button buttonPlay;
	@FXML Button buttonRecord;
	@FXML Button buttonPastRecordings;
	@FXML Button buttonNext;
	@FXML Button toggle;
	@FXML Button previousButton;
	@FXML Button backMainMenuButton;
	@FXML Label toggleYes; // Bad quality
	@FXML Label toggleNo; // Not bad quality (default)
	@FXML ListView<String> nameList;
	
	/**
	 * Sets the name that is playing.
	 * @param name
	 */
	public void setName(String name) throws IOException {
		_name = name;
		currentName.setText(name); //set the title
		_filePath = Main._workDir + System.getProperty("file.separator") + 
				"name_database" + System.getProperty("file.separator") + name;
		
		// Getting saved file quality
		File quality = new File(_filePath + System.getProperty("file.separator") + "info.txt");
		quality.createNewFile(); // create info file if doesn't exist
		if (quality.length() == 0) {
			_isBad = false;
			toggleYes.setVisible(false);
			toggleNo.setVisible(true);
		} else {
			_isBad = true;
			toggleYes.setVisible(true);
			toggleNo.setVisible(false);
		}
		
	}
	
	/**
	 * Looks at the available wav files and returns the latest as
	 * the database recording.
	 * @return the database .wav file
	 */
	public static File getRecording() {
		File nameDir = new File(_filePath);
		File[] files = nameDir.listFiles(Main._filter);
		Arrays.sort(files);
		return files[files.length-1];
	}
	
	/**
	 * This method handles the event of the play button being pressed
	 * and plays the database recording of the name
	 * @param event
	 */
	@FXML protected void handlePlay(ActionEvent event) {
		// Set all buttons to disabled
		disableButtons();
		// Play the audio
		String cmd = "ffplay -nodisp -autoexit " + getRecording().toURI().toString() +" &> /dev/null";
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
		
	}
	
	/**
	 * This method handles the event of the record button getting pressed and goes
	 * to the record view
	 * @param event
	 * @throws IOException
	 */
	@FXML protected void handleRecord(ActionEvent event) throws IOException {
        // GO TO RECORD VIEW
		switchScenes("Record.fxml", _rootPane); 
	}
	
	/**
	 * This method handles the event of the Past Recordings button getting pressed, and 
	 * goes to the past recordings view
	 * @param event
	 * @throws IOException
	 */
	@FXML protected void handlePast(ActionEvent event) throws IOException {
        // GO TO PAST RECORDING VIEW
		switchScenes("PastRecordings.fxml", _rootPane);
	}
	
	/**
	 * This method handles the event of the Next button being pressed, and goes to
	 * the next name on the list
	 * @param event
	 * @throws IOException
	 */
	@FXML protected void handleNext(ActionEvent event) throws IOException {
        // Get the next name in the list
		_index++;
		
		try {
			nameList.getSelectionModel().select(_index);
			String name = nameList.getItems().get(_index);
			// Set the next name
			setName(name);
		}
		catch (IndexOutOfBoundsException e) {
			_index--;
		}
	}
	
	/**
	 * This method handles the event of the toggle button being pressed, and 
	 * toggles the "bad quality" attribute of the recording
	 * @param event
	 */
	@FXML protected void toggle(ActionEvent event) {
		// Toggle visible/invisible
		if (_isBad) {
			toggleYes.setVisible(false);
			toggleNo.setVisible(true);
			_isBad = false;
			File quality = new File(_filePath + 
					System.getProperty("file.separator") + "info.txt");
			quality.delete();
			try {quality.createNewFile();}
			catch (IOException e) {}
			
		} else {
			toggleYes.setVisible(true);
			toggleNo.setVisible(false);
			_isBad = true;
			saveQuality();
		}
		
	}
	
	/**
	 *  This event handles the previous name button, and when its pressed, it
	 *  will go the the previous name on the list
	 * @param event
	 * @throws IOException
	 */
	@FXML protected void handlepreviousName(ActionEvent event) throws IOException {
		//Button will only work if the current index is not at 0
		if(_index > 0) {
			_index--;
		}
		try {
			//Go to the previous name
			nameList.getSelectionModel().select(_index);
			String name = nameList.getItems().get(_index);
			// Set the next name
			setName(name);
		}
		catch (IndexOutOfBoundsException e) {
		//Do nothing
		}
	}
	/**
	 * Writes a "1" to the text file, meaning the recording is 
	 * tagged as bad quality
	 */
	protected void saveQuality() {
		// Write settings to file
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(_filePath + System.getProperty("file.separator") + 
					"info.txt", true);
			bw = new BufferedWriter(fw);
			bw.write("1");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {bw.close();}
				if (fw != null) {fw.close();}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Helper method to disable the buttons
	 */
	protected void disableButtons() {
		buttonPlay.setDisable(true);
		buttonRecord.setDisable(true);
		buttonPastRecordings.setDisable(true);
		buttonNext.setDisable(true);
		toggle.setDisable(true);
	}
	
	/**
	 * Helper method to enable the buttons
	 */
	protected void enableButtons() {
		buttonPlay.setDisable(false);
		buttonRecord.setDisable(false);
		buttonPastRecordings.setDisable(false);
		buttonNext.setDisable(false);
		toggle.setDisable(false);
	}
	
	@FXML
	protected void backToMainMenu() throws IOException {
		nameList.getItems().clear();
		switchScenes("MainMenu.fxml", _rootPane);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//load css file
		 _rootPane.getStylesheets().clear();
		 File theme = new File(Main._workDir + System.getProperty("file.separator") + "theme.txt");
			
			if(theme.length() == 5) {
				  _rootPane.getStylesheets().add(getClass().getResource("LightTheme.css").toExternalForm());
			} else {
				  _rootPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			}
	     
		nameList.getItems().addAll(ChooseRecordings._selected);
		_index = 0;
		nameList.getSelectionModel().select(_index);
		
		String name = nameList.getItems().get(_index); //get the first name
		try {setName(name);} 
		catch (IOException e){}
		nameList.setMouseTransparent(true); //makes the list unselectable
		nameList.setFocusTraversable(false);
		
	}
	
	/**
	 * Background worker to create the ffmpeg files and stop any freezing of GUi
	 * 
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
		
		public void setcmd(String cmd) {
			_cmd = cmd;
		}
		
		/**
		 * Process builder method to call a bash function
		 * @param cmd the command that needs to be input
		 */
		public void bash(String cmd) {
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			try {
				Process process = builder.start();
				
				//Wait for a process to finish before exiting
				int exitStatus = process.waitFor();
				if(exitStatus!=0) {
					return;
				}
			} catch (IOException e) {
				System.out.println("Error: Invalid command");
			} catch (InterruptedException e) {
				System.out.println("Error: Interrupted");
			}
		}
		
	}



	
}
