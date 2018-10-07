package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

public class HelpScreen extends AbstractController {
	@FXML private CheckBox _lightTheme;
	@FXML private CheckBox _darkTheme;
	@FXML private Slider _volumeSlider;
	@FXML void backButtonListener() throws IOException{
		switchScenes("MainMenu.fxml", _rootPane);
	}
	
	@FXML void lightThemeListener() {
		writeToText("Light", true);
		_darkTheme.setDisable(false);
		if (_darkTheme.isSelected()) {
			_darkTheme.setSelected(false);
		}
		_lightTheme.setDisable(true);
		loadStyle(_rootPane, "LightTheme.css");
		
	}
	
	@FXML void darkThemeListener() {
		writeToText("Dark", true);
		_lightTheme.setDisable(false);
		if(_lightTheme.isSelected()) {
			_lightTheme.setSelected(false);
		}
		_darkTheme.setDisable(true);
		loadStyle(_rootPane, "application.css");
	}
	
	private void loadStyle(Parent node, String css) {
		_rootPane.getStylesheets().clear();
	     _rootPane.getStylesheets().add(getClass().getResource(css).toExternalForm());
	 }
	
	@FXML private void volumeSliderListener() {
		writeToText(Double.toString(_volumeSlider.getValue()), false);
		String cmd = "pactl -- set-sink-volume 1 " + (int)_volumeSlider.getValue() + "%";
		System.out.println(cmd);
		Background background = new Background();
		background.setcmd(cmd);
		Thread thread = new Thread(background);
		thread.start();
	}
	
	@Override
	public void customInit() {
		File theme = new File(Main._workDir + System.getProperty("file.separator") + "theme.txt");
		if(theme.length() == 5) {
			_lightTheme.fire();
		} else {
			_darkTheme.fire();
		}
		
		File volume = new File(Main._workDir + System.getProperty("file.separator") + "volume.txt");
		BufferedReader br;
		try {
			String currentLine;
			br = new BufferedReader(new FileReader(volume));
		 	while ((currentLine = br.readLine()) != null) {
		 		_volumeSlider.setValue(Double.parseDouble(currentLine));
		 	} 
		 	br.close();
		} catch (IOException e) {
			
		}
		
	}
	
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
			fw = new FileWriter(Main._workDir + System.getProperty("file.separator") + 
					textName, false);
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
