package application.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.AudioProcess;
import application.Main;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

/**
 * Controller class for Play Recordings screen.
 */
public class PlayRecordings extends AbstractController{
	// FIELDS
	static String _filePath; //URI path to the audio file
	static String _name;
	static String _fileFolder; //path to the name folder
	
	private File _quality;
	boolean _isBad; // is it marked as bad quality
	boolean _isConcat; // is it a combined file
	int _index; // index of the list we are on
	
	@FXML private Label currentName;
	@FXML private Button buttonPlay;
	@FXML private Button buttonRecord;
	@FXML private Button buttonPastRecordings;
	@FXML private Button buttonNext;
	@FXML private Button toggle;
	@FXML private Button previousButton;
	@FXML private Button backMainMenuButton;
	@FXML private Label toggleYes; // Bad quality
	@FXML private Label toggleNo; // Not bad quality (default)
	@FXML private ListView<String> nameList;
	
	/**
	 * This method handles the event of the play button being pressed
	 * and plays the database recording of the name
	 */
	@FXML protected void handlePlay() {
		// Set all buttons to disabled
		disableButtons();
		// Play the audio
		String cmd = "ffplay -nodisp -autoexit \"" + _filePath + "\" &> /dev/null";
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
		
	}
	
	/**
	 * This method handles the event of the record button getting pressed and goes
	 * to the record view
	 * @throws IOException
	 */
	@FXML protected void handleRecord() throws IOException {
        // Go To Record View
		switchScenes("Record.fxml", _rootPane); 
	}
	
	/**
	 * This method handles the event of the Past Recordings button getting pressed, and 
	 * goes to the past recordings view
	 * @throws IOException
	 */
	@FXML protected void handlePast() throws IOException {
        // Go To Past Recording View
		switchScenes("PastRecordings.fxml", _rootPane);
	}
	
	/**
	 * This method handles the event of the Next button being pressed, and goes to
	 * the next name on the list
	 * @throws IOException
	 */
	@FXML protected void handleNext() throws IOException {
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
	 */
	@FXML protected void toggle() {
		// Toggle visible/invisible
		if (_isBad) {
			toggleYes.setVisible(false);
			toggleNo.setVisible(true);
			_isBad = false;
			_quality.delete();
			try {_quality.createNewFile();}
			catch (IOException e) {}
			
		} else {
			toggleYes.setVisible(true);
			toggleNo.setVisible(false);
			_isBad = true;
			saveQuality();
		}
		
	}
	
	/**
	 *  This method handles the previous name button, and when its pressed, it
	 *  will go the the previous name on the list
	 * @throws IOException
	 */
	@FXML protected void handlePreviousName() throws IOException {
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
	 * Goes to the main menu upon a button press.
	 * @throws IOException
	 */
	@FXML protected void backToMainMenu() throws IOException {
		nameList.getItems().clear();
		switchScenes("MainMenu.fxml", _rootPane);
	}
	
	/**
	 * Sets the name that is playing.
	 * @param name
	 */
	public void setName(String name) throws IOException {
		_name = name;
		currentName.setText(name); //set the title
		
		if (!name.contains(" ")) {
			_isConcat = false;
			_fileFolder = Main.getWorkDir() + Main.getSEP() + "name_database" + Main.getSEP() + name;
			File nameFile = getRecording(_fileFolder);
			_filePath = nameFile.toURI().toString();
			
			// Make the toggle quality button visible
			toggle.setVisible(true);
		
			// Getting saved file quality
			_quality = getQualityFile(nameFile);
			
			if (!isBad(nameFile)) {
				_isBad = false;
				toggleYes.setVisible(false);
				toggleNo.setVisible(true);
			} else {
				_isBad = true;
				toggleYes.setVisible(true);
				toggleNo.setVisible(false);
			}
		} else {
			_isConcat = true;
			
			// make rating not possible by making buttons invisible
			toggle.setVisible(false); // the button
			toggleYes.setVisible(false); // the text
			toggleNo.setVisible(false); // the text
			
			//Set the file paths
			List<File> files = getIndividual(name);
			AudioProcess concat = new AudioProcess();
			File concatFile = new File(concat.getConcatFile(files));
			_filePath = concatFile.toURI().toString();
			_fileFolder = concatFile.getParentFile().getAbsolutePath();
		}
	}
	
	/**
	 * Looks at the available wav files and returns the 
	 * database recording, given a File representing the folder.
	 * @param filePath the directory where the audio files are
	 * @return the database .wav file
	 */
	public File getRecording(String filePath) {
		File nameDir = new File(filePath);
		File[] files = nameDir.listFiles(Main.getFilter());
		
		// sorts in alphabetical order
		Arrays.sort(files);
		
		// find the good quality file
		if (!_isConcat) {
			for (int i=0; i<files.length; i++) {
				File candidate = files[i];
				if (!isBad(candidate)) {
					return candidate;
				}
			}
		}
		// they are all bad, return any
		return files[0];
	}
	
	/**
	 * Gets the quality file for a wav file
	 * @param file the recording file
	 * @return the quality file
	 */
	private static File getQualityFile(File file) {
		File quality = new File(file + ".txt");
		return quality;
	}
	
	/**
	 * Checks if the file is bad quality
	 * @param candidate the recording file to check
	 * @return true if it is bad quality
	 */
	private static boolean isBad(File candidate) {
		File quality = getQualityFile(candidate);
		try {
			// create info file if doesn't exist
			quality.createNewFile();
		} catch (IOException e) {e.printStackTrace();} 

		if (quality.length() != 0) {
			// bad quality
			return true;
		}
		return false;
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
			fw = new FileWriter(_quality, true);
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
	 * Turns a combined name into a list of files with each
	 * discrete name as a separate file
	 * @param name a combined name
	 * @return List of files containing all parts of a combined name
	 */
	public List<File> getIndividual(String name) {
		// get the individual names
    	List<File> files = new ArrayList<>();
		String[] split = name.trim().split("\\s+");
		for (int i=0; i<split.length; i++) {
			File file = getRecording(Main.getWorkDir() + Main.getSEP() + 
					"name_database" + Main.getSEP() + split[i]);
			files.add(file);
		}
		return files;
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
		previousButton.setDisable(true);
		backMainMenuButton.setDisable(true);
	}
	
	/**
	 * Helper method to enable the buttons
	 */
	@Override
	protected void enableButtons() {
		buttonPlay.setDisable(false);
		buttonRecord.setDisable(false);
		buttonPastRecordings.setDisable(false);
		buttonNext.setDisable(false);
		toggle.setDisable(false);
		previousButton.setDisable(false);
		backMainMenuButton.setDisable(false);
	}
	
	@Override
	public void customInit() {
		nameList.getItems().addAll(ChooseRecordings._selected);
		_index = 0;
		nameList.getSelectionModel().select(_index);
		
		//concatenate any combined files
		for (int i=0; i< nameList.getItems().size();i++) {
			String name = nameList.getItems().get(i);
			if (name.contains(" ")) {
				Task<Void> concatTask = new Task<Void>() {
	    		    @Override
	    		    public Void call() throws Exception {
	    		    	List<File> files = getIndividual(name);
	    		    	
						// concatenate
						AudioProcess concat = new AudioProcess();
						concat.concatenate(files);
				
						return null;
	    		    }
	    		};
	    		new Thread(concatTask).start();
			}
		}
		
		String name = nameList.getItems().get(_index); //get the first name
		try {setName(name);} 
		catch (IOException e){}
		// disable selection (but allows wheel scrolling)
		nameList.addEventFilter(MouseEvent.ANY, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseEvent.consume();
            }
        });
		
	}
	
}
