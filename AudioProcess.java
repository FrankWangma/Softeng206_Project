package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AudioProcess {
	
	/**
	 * Takes in a(n Array)List of files and combines them
	 * @param files
	 * @return the combined audio file
	 */
	public File concatenate(List<File> files) {
		// process the files
		String fileName = "";
		List<File> newFiles = new ArrayList<>();
		for (int i=0; i<files.size(); i++) {
			//Build output file
			File file = files.get(i);
			fileName = fileName + Main.getName(file.getName()) + ",";
			
			//Remove silence
			newFiles.add(removeSilence(file));
		}
		fileName = fileName.substring(0,fileName.length()-1);

		String output = makeFolders(fileName);
		File concatFileTemp = new File(output + Main.SEP + fileName + "temp.wav");
		File concatFile = new File(output + Main.SEP + fileName+ ".wav");
		
		// Make text file list from list of files
		File textFile = new File(output + Main.SEP + fileName + ".txt");
		buildList(newFiles, textFile);
		
		// Concatenate and normalise loudness
		String concat = "ffmpeg -y -f concat -safe 0 -i \"" + textFile.getAbsolutePath() + "\" -c copy \"" +
				concatFile.getAbsolutePath() + "\"";
		bash(concat);
		concatFileTemp.delete();
		
		return concatFile;
	}
	
	/**
	 * Makes the folder to store the concatenated files.
	 * @param fileName the name of the concatenated file
	 * @return the name of the folder
	 */
	private String makeFolders(String fileName) {
		//Folder to store concatenated files (and user files)
		String output = Main._workDir + Main.SEP + "combined" + Main.SEP + fileName;
		String outputUser = output + Main.SEP + "user";
		File outputFolderUser = new File(outputUser);
		outputFolderUser.mkdirs();
		
		return output;
	}
	
	/**
	 * Writes the files to concatenate to a text file
	 * @param files the list of files to concatenate
	 * @param output the output folder containing all combined files
	 */
	private void buildList(List<File> files, File textFile) {
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			textFile.delete();
			textFile.createNewFile();
			
			fw = new FileWriter(textFile.getPath(), true);
			bw = new BufferedWriter(fw);
			for (File file : files) {
				bw.write("file '"+ file.getAbsolutePath() + "'" + System.getProperty("line.separator"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {bw.close();}
				if (fw != null) {fw.close();}
			} catch (IOException ex) {ex.printStackTrace();}
		}
	}
	
	/**
	 * Removes silence in an audio file.
	 * @param file the file to process
	 * @return The new audio file with the silence removed
	 */
	File removeSilence(File file) {
		if (file != null && file.getName().endsWith(".wav")) {
			File newFile = new File(file.getParent() + Main.SEP + "cleaned" + 
					Main.SEP + file.getName());
			newFile.getParentFile().mkdirs();
			String silence = "ffmpeg -y -hide_banner -i "+ file.getAbsolutePath() +" -af " + 
				"silenceremove=1:0:-50dB:1:2:-50dB " + newFile.getAbsolutePath();
		
			bash(silence);
			return newFile;
		}
		return null;
	}
	
	
	private void bash(String cmd) {
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
