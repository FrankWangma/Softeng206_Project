package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	@FXML private CheckBox _blueSkyTheme;
	@FXML private CheckBox _greyOrangeTheme;
	@FXML private CheckBox _paperGreyTheme;
	@FXML private CheckBox _pinkBlueTheme;
	@FXML private CheckBox _forestGreenTheme;
	@FXML private Slider _volumeSlider;
	@FXML private Button _backButton;
	@FXML private Button _howToUseButton;
	
	private List<CheckBox> checkboxList = new ArrayList<CheckBox>();
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
		writeToText("LightTheme.css", true);
		// enable the dark theme checkbox
		selectTheme(_lightTheme);
		loadStyle(_rootPane, "LightTheme.css");
		
	}
	
	/**
	 * This method listens for when the dark theme checkbox is pressed
	 */
	@FXML public void darkThemeListener() {
		writeToText("application.css", true);
		selectTheme(_darkTheme);
		loadStyle(_rootPane, "application.css");
	}
	
	/**
	 * This method listens for when the cold dark theme checkbox is pressed
	 */
	@FXML public void coldDarkThemeListener() {
		writeToText("ColdDark.css", true);
		selectTheme(_coldDarkTheme);
		loadStyle(_rootPane, "ColdDark.css");
	}
	
	/**
	 * This method listens for when the blue sky theme checkbox is ticked
	 */
	@FXML public void blueSkyThemeListener() {
		writeToText("BlueSky.css", true);
		selectTheme(_blueSkyTheme);
		loadStyle(_rootPane, "BlueSky.css");
	}
	
	/**
	 * This method listens for when the grey orange theme checkbox is ticked
	 */
	@FXML public void greyOrangeThemeListener() {
		writeToText("GreyOrange.css", true);
		selectTheme(_greyOrangeTheme);
		loadStyle(_rootPane, "GreyOrange.css");
	}
	
	/**
	 * This method listens for when the paper grey theme checkbox is ticked
	 */
	@FXML public void paperGreyThemeListener() {
		writeToText("PaperGrey.css", true);
		selectTheme(_paperGreyTheme);
		loadStyle(_rootPane, "PaperGrey.css");
	}
	
	/**
	 * This method listens for when the pink blue theme checkbox is ticked
	 */
	@FXML public void pinkBlueThemeListener() {
		writeToText("PinkBlue.css", true);
		selectTheme(_pinkBlueTheme);
		loadStyle(_rootPane, "PinkBlue.css");
	}
	
	/**
	 * This method listens for when the forest green theme checkbox is ticked
	 */
	@FXML public void forestGreenThemeListener() {
		writeToText("ForestGreen.css", true);
		selectTheme(_forestGreenTheme);
		loadStyle(_rootPane, "ForestGreen.css");
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
	
	/**
	 * This method selects the selected theme and deselects the theme that was previously
	 * selected
	 * @param selected
	 */
	private void selectTheme(CheckBox selected) {
			// enable and de-select the previously selected theme
			switchSelection(_darkTheme);
			switchSelection(_lightTheme);
			for(CheckBox cb: checkboxList) {
				if(!cb.isDisabled() || cb.isSelected()) {
					switchSelection(cb);
				}
			}
			//select and enable the selected theme
			selected.setSelected(true);
			selected.setDisable(true);
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
	
	/**
	 * This method un-disables the themes that have been unlocked
	 * @param progress the number of points the user has
	 */
	private void unlockRewards(int progress) {
		for(int i = 0; i < progress; i++) {
			checkboxList.get(i).setVisible(true);
		}
	}
	
	@Override
	public void customInit() {
		//Make a list of the themes that are unlockable
		checkboxList.add(_coldDarkTheme);
		checkboxList.add(_paperGreyTheme);
		checkboxList.add(_greyOrangeTheme);
		checkboxList.add(_pinkBlueTheme);
		checkboxList.add(_blueSkyTheme);
		checkboxList.add(_forestGreenTheme);
		
		//Disable those themes
		for(CheckBox cb: checkboxList) {
			cb.setVisible(false);
		}
		
		File theme = new File(Main._workDir + Main.SEP + "theme.txt");
		File reward = new File(Main._workDir + Main.SEP + "Reward.txt");
		File volume = new File(Main._workDir + Main.SEP + "volume.txt");
		BufferedReader br1;
		try {
			//Check the theme using a file, and select whichever theme was previously selected
			br1 = new BufferedReader(new FileReader(theme));
			String st; 
			 while ((st = br1.readLine()) != null)  {
				  if(st.equals("application.css")) {
					  _darkTheme.fire();
				  } else if(st.equals("LightTheme.css")) {
					  _lightTheme.fire();
				  } else if(st.equals("ColdDark.css")) {
					  _coldDarkTheme.fire();
				  } else if(st.equals("BlueSky.css")) {
					  _blueSkyTheme.fire();
				  } else if(st.equals("GreyOrange.css")) {
					  _greyOrangeTheme.fire();
				  } else if(st.equals("PaperGrey.css")) {
					  _paperGreyTheme.fire();
				  } else if(st.equals("PinkBlue.css")) {
					  _pinkBlueTheme.fire();
				  } else if(st.equals("ForestGreen.css")) {
					  _forestGreenTheme.fire();
				  }
				 } 
	
				 if(reward.exists()) {
					 // Check the rewards that the user has unlocked
					 br1 = new BufferedReader(new FileReader(reward));
					 int progress = 0;
					 while((st = br1.readLine()) != null) {
						 progress = Integer.parseInt(st);
					 }
					 if(progress > 40) {
						 progress = 40;
					 }
					 unlockRewards(progress/5);
				 }
				 
				 if(volume.exists()) {
					// Check the previous volume selected (memory)
					br1 = new BufferedReader(new FileReader(volume));
					while ((st = br1.readLine()) != null) {
						_volumeSlider.setValue(Double.parseDouble(st));
					} 
				 }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	
	}
	
	/**
	 * This method writes the inputed string into a text file
	 * @param text
	 * @param isTheme if it is meant to write to a theme
	 */
	protected static void writeToText(String text, Boolean isTheme) {
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
