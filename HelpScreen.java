package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 * This class is used as the controller for the help screen in the GUI
 * @author frank
 *
 */
public class HelpScreen extends AbstractController {
	@FXML private CheckBox _lightTheme;
	@FXML private CheckBox _darkTheme;
	@FXML private CheckBox _coldDarkTheme;
	@FXML private Slider _volumeSlider;
	@FXML private Button _backButton;
	@FXML private Button _howToUseButton;
	/**
	 * This method listens for when the back button is pressed
	 * @throws IOException
	 */
	@FXML void backButtonListener() throws IOException{
		switchScenes("MainMenu.fxml", _rootPane);
	}
	
	/**
	 * This method listens for when the light Theme checkbox is pressed
	 */
	@FXML public void lightThemeListener() {
		writeToText("Light", true);
		// enable the dark theme checkbox
		selectTheme("Light");
		loadStyle(_rootPane, "LightTheme.css");
		
	}
	
	/**
	 * This method listens for when the dark theme checkbox is pressed
	 */
	@FXML public void darkThemeListener() {
		writeToText("Dark", true);
		selectTheme("Dark");
		loadStyle(_rootPane, "application.css");
	}
	
	@FXML public void coldDarkThemeListener() {
		writeToText("ColdDark", true);
		selectTheme("ColdDark");
		loadStyle(_rootPane, "ColdDark.css");
	}
	/**
	 * This method loads the style of the css file that is inputed
	 * @param node
	 * @param css
	 */
	private void loadStyle(Parent node, String css) {
		_rootPane.getStylesheets().clear();
	     _rootPane.getStylesheets().add(getClass().getResource(css).toExternalForm());
	 }
	
	private void selectTheme(String theme) {
		if(theme == "Light") {
			// de-select the dark theme
			if (_darkTheme.isSelected()) {
				switchSelection(_darkTheme);
			} else if(_coldDarkTheme.isSelected()) {
				switchSelection(_coldDarkTheme);
			}
			// disable the light theme checkbox
			_lightTheme.setDisable(true);
		} else if (theme == "Dark") {
			if(_lightTheme.isSelected()) {
				switchSelection(_lightTheme);
			} else if(_coldDarkTheme.isSelected()) {
				switchSelection(_coldDarkTheme);
			}
			_darkTheme.setDisable(true);
		} else if (theme == "ColdDark") {
			if (_lightTheme.isSelected()) {
				switchSelection(_lightTheme);
			} else if(_darkTheme.isSelected()) {
				switchSelection(_darkTheme);
			}
			_coldDarkTheme.setDisable(true);
		}
	}
	
	private void switchSelection(CheckBox theme) {
		theme.setDisable(false);
		theme.setSelected(false);
	}
	/**
	 * This method listens for when the volume slider is changed
	 */
	@FXML private void volumeSliderListener() {
		writeToText(Double.toString(_volumeSlider.getValue()), false);
		// use the process builder to set the pc volume 
		String cmd = "pactl -- set-sink-volume 1 " + (int)_volumeSlider.getValue() + "%";
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
	}
	
	@FXML private void howToUseButtonListener() throws IOException {
		// open a new window
    	Parent pane = FXMLLoader.load(getClass().getResource("HowToUse.fxml"));
        Stage stage = new Stage();
        stage.setTitle("How To Use");
        stage.setScene(new Scene(pane));
        stage.showAndWait();
	}
	
	@Override
	public void customInit() {
		//Check the theme using a file, and select whichever theme was previously selected
		File theme = new File(Main._workDir + Main.SEP + "theme.txt");
		BufferedReader br1;
		try {
			br1 = new BufferedReader(new FileReader(theme));
			String st; 
			 while ((st = br1.readLine()) != null)  {
			  if(st.equals("Dark")) {
				  _darkTheme.fire();
			  } else if(st.equals("Light")) {
				  _lightTheme.fire();
			  } else if(st.equals("ColdDark")) {
				  _coldDarkTheme.fire();
			  }
			 } 
			 br1.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	
		
		// Check the previous volume selected (memory)
		File volume = new File(Main._workDir + Main.SEP + "volume.txt");
		BufferedReader br2;
		try {
			String currentLine;
			br2 = new BufferedReader(new FileReader(volume));
		 	while ((currentLine = br2.readLine()) != null) {
		 		_volumeSlider.setValue(Double.parseDouble(currentLine));
		 	} 
		 	br2.close();
		} catch (IOException e) {
			
		}
		
	}
	
	/**
	 * This method writes the inputed string into a text file
	 * @param text
	 * @param isTheme if it is meant to write to a theme
	 */
	protected void writeToText(String text, Boolean isTheme) {
		// Write settings to file
		BufferedWriter bw = null;
		FileWriter fw = null;
		String textName;
		if(isTheme) {
			textName = "theme.txt";
		} else {
			textName = "volume.txt";
		}
		try {
			fw = new FileWriter(Main._workDir + Main.SEP + textName, false);
			bw = new BufferedWriter(fw);
			bw.write(text);

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
	
}
