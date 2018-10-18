package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Class represents a .wav audio file for use in viewing
 * and playing past recordings.
 *
 */
public class AudioFile {
	private String _fileName; // what is the file called
	private String _dispName; // what is the name to be displayed
	
	public AudioFile(String fileName) {
		_fileName = fileName;
		_dispName = getDispName(fileName);
	}
	
	public String getFile() {
		return _fileName;
	}
	
	/**
	 * Given a file name of a recording, returns the
	 * display name.
	 * @param name
	 * @return the name and date to display
	 */
	private String getDispName(String name) {
		String dispName;
		if (name.contains(",")) { // is concatenated file
			dispName = name.replace(",", " ");
		} else {
			name = name.substring(name.indexOf("_")+1, name.length()-4);
			String date = name.substring(0, name.lastIndexOf('_'));
			
			// file date format
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d-M-yyyy_H-m-s");
			LocalDateTime localDate = LocalDateTime.parse(date, dtf);
			
			// new date format
			DateTimeFormatter newFormat = 
					DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
			String dispDate = 
					newFormat.format(localDate);
			dispName = name.substring(name.lastIndexOf('_')+1) +" "+ dispDate;
		}
		return dispName;
	}
	
	@Override
	public String toString() {
		return _dispName;
	}
}
