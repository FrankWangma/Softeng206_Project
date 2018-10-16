package application;
	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Main class of application.
 *
 */
public class Main extends Application {
	static final String SEP = System.getProperty("file.separator");
	static Path relativePath = Paths.get("");
	static String _workDir = relativePath.toAbsolutePath().toString();
	static File _namesFile = new File(_workDir + SEP + "names.txt");
	static List<String> _names = new ArrayList<String>();
	
	// File filter for .wav files
	static FilenameFilter _filter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
           if(name.lastIndexOf('.')>0) {
              int lastIndex = name.lastIndexOf('.');
              String str = name.substring(lastIndex); //get extension
              if(str.equals(".wav")) {
                 return true;
              }
           }
           return false;
        }
     };
	
	
	@Override
	public void start(Stage primaryStage) {
		makeDatabase();
		try {
			File theme = new File(_workDir + SEP + "theme.txt");
			if (!theme.exists()) {
				HelpScreen.writeToText("application.css", true);
			}
			primaryStage.setTitle("Name Sayer");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("MainMenu.fxml"));
			Parent layout = loader.load();
			Scene scene = new Scene(layout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes the files within the input folder and stores them in its 
	 * appropriate name folder in name_database. Also adds the names
	 * to the _names list.
	 */
	public void makeDatabase() {
		File input = new File(_workDir + SEP + "input");
		input.mkdirs(); // Make the input folder if it doesn't exist
		File destination = new File(_workDir + SEP + "name_database");
		File[] listOfFiles = input.listFiles(_filter);
		String fileName;
		_names = readNamesFromFile(); // existing list
		List<String> tempNamesList = new ArrayList<String>();
		
		for (int file = 0; file < listOfFiles.length; file++) {
			int underscores = 0;
			if (listOfFiles[file].isFile()) {
			    fileName = listOfFiles[file].getName();
			    for (int c=0; c < fileName.length(  ); c++) {
			        if (fileName.charAt(c) == '_') {
			        	underscores++;
			        }
			        if (underscores == 3) {
			        	
			        	// Get the name from the file
			        	int cut = c+1;
			        	String name = fileName.substring(cut,fileName.length()-4).toLowerCase();
			        	name = name.substring(0, 1).toUpperCase() + name.substring(1);
			        	tempNamesList.add(name);
			        	
			        	// Make the name folder
			        	File nameFolder = new File(destination + SEP + name);
			        	File userFolder = new File(nameFolder + SEP + "user");
					    nameFolder.mkdirs();
					    userFolder.mkdirs();
					    listOfFiles[file].renameTo(new File(nameFolder + SEP + fileName));
					    
			        	break;
			        }
			    }
			}
		}
		_names.addAll(tempNamesList);
		_names = removeRedundant(_names);
		Collections.sort(_names, String.CASE_INSENSITIVE_ORDER);
		writeNamesToFile();
	}
	
	/**
	 * Removes redundant names in _names list.
	 */
	public static List<String> removeRedundant(List<String> list) {
		List<String> newList = new ArrayList<String>(new HashSet<String>(list));
		return newList;
	}
	
	/**
	 * Writes the names in _names, to a text file names.txt. This deletes the existing
	 * text file and makes a new one!
	 */
	public void writeNamesToFile() {
		// Write name to file
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			_namesFile.delete();
			_namesFile.createNewFile();
			
			fw = new FileWriter(_namesFile.getPath(), true);
			bw = new BufferedWriter(fw);
			for (String name : _names) {
				bw.write(name + System.getProperty("line.separator"));
			}

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
	
	public static void setUnlockRewardFile() {
		// Write name to file
				BufferedWriter bw = null;
				FileWriter fw = null;
		try {
			fw = new FileWriter(_namesFile.getPath(), true);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Reads the names from the names.txt file.
	 * @return A string list of the existing names found in the names.txt file
	 */
	public List<String> readNamesFromFile() {
		List<String> existingNames = new ArrayList<String>();
		// Read the file
		try {
			_namesFile.createNewFile();
			BufferedReader b = new BufferedReader(new FileReader(_namesFile));
			String readLine = "";
			        
			while (((readLine = b.readLine()) != null) ) {
				if ((readLine != "") && (readLine != System.getProperty("line.separator"))) {
					existingNames.add(readLine);
				}
			}
			b.close();
        
		} catch (IOException e) {e.printStackTrace();}
		return existingNames;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
