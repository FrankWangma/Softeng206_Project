package application;
	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;




public class Main extends Application {
	static Path relativePath = Paths.get("");
	static String _workDir = relativePath.toAbsolutePath().toString();
	static File _namesFile = new File(_workDir + System.getProperty("file.separator") + "names.txt");
	static List<String> _names = new ArrayList<String>();
	private Stage _primaryStage;
	
	
	@Override
	public void start(Stage primaryStage) {
		makeDatabase();
		try {
			_primaryStage = primaryStage;
			_primaryStage.setTitle("Name Sayer");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("MainMenu.fxml"));
			Parent layout = loader.load();
			Scene scene = new Scene(layout);
			_primaryStage.setScene(scene);
			_primaryStage.show();
		
			
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
		String sep = System.getProperty("file.separator");
		File input = new File(_workDir + sep + "input");
		File destination = new File(_workDir + sep + "name_database");
		File[] listOfFiles = input.listFiles();
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
			        	
			        	int cut = c+1;
			        	String name = fileName.substring(cut,fileName.length()-4);
			        	tempNamesList.add(name);
			        	
			        	File nameFolder = new File(destination + sep + name);
					    nameFolder.mkdirs();
					    listOfFiles[file].renameTo(new File(nameFolder + sep + fileName));
					    
			        	break;
			        }
			    }
			}
		}
		_names.addAll(tempNamesList);
		_names = removeRedundant(_names);
		writeNamesToFile();
	}
	
	/**
	 * Removes redundant names in _names list.
	 */
	
	public List<String> removeRedundant(List<String> list) {
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
