# How To Use
Unpack the contents of the .zip file to an empty directory. 

Place the name audio files into the "input" folder. These should be .wav files.

Run the jar through the command line by typing "java -jar NameSayer.jar". Make 
sure you are in the same directory as the jar file.

------------------------------------------------------------------------------

# Screens
## Main Menu
From the main menu, the user can test the microphone or start practicing 
names from a database.

## Microphone Testing
The user is able to record input from the microphone and listen to it, 
to test if the microphone is working.

## Name Selection
The user can select which names they wish to practice. There are two lists: 
a list of the names in the database, and the list of the names they wish to 
practice. They can move names between the two lists. After they have chosen 
the names they wish to practice, they can continue to the Play Recordings screen.

## Play Recordings
This is where the user can listen to their chosen names.

There is a list on the side showing the current playlist of names. 
The current name is highlighted. For this name, the user can listen 
to the recording in the database (the latest one, if there are multiple).

If the name is bad quality, there is a button allowing them to 
mark (and unmark) it as bad quality; this information is stored 
in a text file alongside the recording.

They can access past recordings of that name, which opens in a new 
screen. To record their own pronunciations of a name, the user can 
press the Record button.

When the user has finished listening or practicing with a name, they can 
move onto the next name in the playlist. If there are no more names, they 
will be brought back to the main menu.

## Record
This allows the user to record themselves saying the current name in the playlist. 
Each recording is 5 seconds long. They can then listen to their recording before and 
after they save it (they can also discard the recording), as well as listen to the 
recording from the database. The user can make multiple recordings for the name, 
but can only listen to any recording they have just recorded. To access previous 
recordings, the user can go to Past Recordings.

## Past Recordings
This screen shows the user all the available recordings for a specific name. 
This includes all recordings from the database for that name, as well as the 
user's own recordings. These are shown in separate lists; the user can switch 
between them. Any item in the list(s) can be selected and played. There is 
also a button to play the main audio file from the database.