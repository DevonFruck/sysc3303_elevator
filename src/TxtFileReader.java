import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class is responsible for handling the reading of external text files
 * @author Laith
 */
public class TxtFileReader {
	/**
	 * getEvents is a method that scans through a text file
	 * and retrieves a list of InputEvents -> a list of the events/orders for the elevator 
	 * @param filePath -> local path to the text file 
	 * @return eventList -> ArrayList<InputEvents> of events for for the elevator
	 */
	public static ArrayList<InputEvents> getEvents(String filePath) {
		ArrayList<InputEvents> eventList = new ArrayList<InputEvents>();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
			String readLine;
			readLine = fileReader.readLine();
			while (readLine != null) {
				if (readLine.split("\\|")[0].equalsIgnoreCase("User")) { //Checks if this is a user event
					//event with no errors/faults
					EventsHandler event = new EventsHandler(readLine.split("\\|")[1]);
					eventList.add(event); //adds current event to the array list
				} else {
					//Handling faulty events
					ErrorHandler event = new ErrorHandler(readLine.split("\\|")[1]);
					eventList.add(event);
				}
				readLine = fileReader.readLine();
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return eventList; //return ArrayList of InputEvents
	}
}