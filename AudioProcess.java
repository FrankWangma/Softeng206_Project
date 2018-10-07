package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AudioProcess {
	String SEP = System.getProperty("file.separator");
	
	/**
	 * Takes in a(n Array)List of files and combines them
	 * @param files
	 * @return the combined audio file
	 */
	public File concatenate(List<File> files) {
		//Folder to store concat files
		String output = Main._workDir + SEP + "combined";
		File outputFolder = new File(output);
		outputFolder.mkdirs();
		
		// process the files
		String fileName = "";
		List<File> newFiles = new ArrayList<>();
		for (int i=0; i<files.size(); i++) {
			//Build output file
			File file = files.get(i);
			fileName = fileName + file.getName().substring(0, file.getName().length()-4) + "_";
			
			//Remove silence
			newFiles.add(removeSilence(file));
		}
		
		File concatFileTemp = new File(output + SEP + fileName + "temp.wav");
		File concatFile = new File(output + SEP + fileName+ ".wav");
		
		// Make text file list from list of files
		File textFile = new File(output + SEP + fileName + ".txt");
		buildList(newFiles, textFile);
		
		// Concatenate and normalise loudness
		String concat = "ffmpeg -y -f concat -safe 0 -i " + textFile.getAbsolutePath() + " -c copy " +
				concatFileTemp.getAbsolutePath();
		String norm = "ffmpeg -y -i " + concatFileTemp.getAbsolutePath() +
				" -filter:a loudnorm " + concatFile.getAbsolutePath();
		bash(concat + ";" + norm);
		concatFileTemp.delete();
		
		return concatFile;
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
			File newFile = new File(file.getParent() + SEP + "cleaned_" + file.getName());
			String silence = "ffmpeg -y -hide_banner -i "+ file.getAbsolutePath() +" -af " + 
				"silenceremove=1:0:-35dB:1:5:-35dB:0:peak " + newFile.getAbsolutePath();
		
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
