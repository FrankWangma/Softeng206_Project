package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import application.controller.Main;

public class AudioProcess {
	private static final String SEP = System.getProperty("file.separator");
	
	/**
	 * Takes in a(n Array)List of files and combines them
	 * @param files
	 * @return the combined audio file
	 */
	public File concatenate(List<File> files) {
		// process the files
		List<File> newFiles = new ArrayList<>();
		for (int i=0; i<files.size(); i++) {
			//Remove silence
			newFiles.add(normalize(removeSilence(files.get(i))));
		}
		String fileName = getConcatFile(files);
		String textName = fileName.substring(0, fileName.length()-1) + ".txt";
		File concatFile = new File(fileName);
		
		// Make text file list from list of files
		File textFile = new File(textName);
		buildList(newFiles, textFile);
		
		// Concatenate and normalise loudness
		String concat = "ffmpeg -y -f concat -safe 0 -i \"" + textFile.getAbsolutePath() + "\" -c copy \"" +
				concatFile.getAbsolutePath() + "\"";
		bash(concat);
		
		return concatFile;
	}
	
	/**
	 * Makes the folder to store the concatenated files.
	 * @param fileName the name of the concatenated file
	 * @return the name of the folder
	 */
	private String makeFolders(String fileName) {
		//Folder to store concatenated files (and user files)
		String output = Main.getWorkDir() + SEP + "combined" + SEP + fileName;
		String outputUser = output + SEP + "user";
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
			File newFile = new File(file.getParent() + SEP + "cleaned" + 
					SEP + file.getName());
			newFile.getParentFile().mkdirs();
			String silence = "ffmpeg -y -hide_banner -i "+ file.getAbsolutePath() +" -af " + 
				"silenceremove=1:0:-50dB:1:2:-50dB " + newFile.getAbsolutePath();
		
			bash(silence);
			return newFile;
		}
		return null;
	}
	
	/**
	 * Makes the volume of the file -19 dB
	 * @param file the file object to process
	 * @return the file with adjusted audio
	 */
	private File normalize(File file) {
		if (file != null && file.getName().endsWith(".wav")) {
			float volume = detectVolume(file);
			float toAdjust = -19 - volume;
			
			File newFile = new File(file.getParent() + SEP + "norm" + 
					SEP + file.getName());
			newFile.getParentFile().mkdirs();
			String norm = "ffmpeg -y -i " + file + " -filter:a \"volume="+ toAdjust + 
					"dB\" " + newFile.getAbsolutePath();
			bash(norm);

			return newFile;
		}
		return null;
	}
	
	/**
	 * Looks at a .wav file and returns the mean volume
	 * @param file
	 * @return the mean volume of the file, otherwise 0 if the file is invalid
	 */
	private float detectVolume(File file) {
		if (file != null && file.getName().endsWith(".wav")) {
			
			File volume = new File(file.getParent() + SEP + "norm.txt");
			String detect = "ffmpeg -i "+ file.getAbsolutePath() +
					" -filter:a volumedetect -f null nul &> " + volume.getAbsolutePath();
			bash(detect);
			
			String line = null;
			try {
				Scanner s = new Scanner(volume);
			    while (s.hasNextLine()) {
			        line = s.nextLine();
			        if (line.contains("mean_volume:")) { 
			        	s.close();
			            break;
			        }
			    }
			} catch (IOException e) {e.printStackTrace();}
			
			line = line.substring(line.lastIndexOf(": ")+2, line.length()-3);
			float meanVolume = Float.valueOf(line);
			return meanVolume;
		}
		return 0;
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
	
	/**
	 * Gets the concatenated file path given a list of files
	 * @param files
	 * @return the file path to the concatenated file
	 */
	public String getConcatFile(List<File> files) {
		String fileName = "";
		for (int i=0; i<files.size(); i++) {
			//Build output file
			File file = files.get(i);
			fileName = fileName + Main.getName(file.getName()) + ",";
		}
		fileName = fileName.substring(0,fileName.length()-1);
		String output = makeFolders(fileName);
		String concatFile = output + SEP + fileName+ ".wav";
		return concatFile;
	}

}
