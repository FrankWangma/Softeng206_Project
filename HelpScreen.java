package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class HelpScreen extends AbstractController implements Initializable{
	@FXML BorderPane _rootPane;
	@FXML private CheckBox _lightTheme;
	@FXML private CheckBox _darkTheme;
	@FXML private Slider _volumeSlider;
	@FXML void backButtonListener() throws IOException{
		switchScenes("MainMenu.fxml", _rootPane);
	}
	
	@FXML void lightThemeListener() {
		saveTheme("Light");
		_darkTheme.setDisable(false);
		if (_darkTheme.isSelected()) {
			_darkTheme.setSelected(false);
		}
		_lightTheme.setDisable(true);
		loadStyle(_rootPane, "LightTheme.css");
		
	}
	
	@FXML void darkThemeListener() {
		saveTheme("Dark");
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		File theme = new File(Main._workDir + System.getProperty("file.separator") + "theme.txt");
		
		if(theme.length() == 5) {
			_lightTheme.fire();
		} else {
			_darkTheme.fire();
		}
	}
	
	protected void saveTheme(String isDark) {
		// Write settings to file
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			fw = new FileWriter(Main._workDir + System.getProperty("file.separator") + 
					"theme.txt", false);
			bw = new BufferedWriter(fw);
			bw.write(isDark);

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
