import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Group
 *
 */
public class Floor extends Thread {
	
	int floorNumber;
	Scheduler scheduler;
	ArrayList<InputData> inputData;
	private ArrayList<InputEvents> events;
	
	public Floor(int floorNumber, Scheduler scheduler) {
		this.floorNumber = floorNumber;
		this.scheduler = scheduler;
		this.events = new ArrayList<InputEvents>();
		
		inputData = new ArrayList<InputData>();
		inputData.add(new InputData(null, floorNumber, 2));
	}
	
	public int getFloorNumber() {
		return floorNumber;
	}
	
	// Reads from text file to put into ArrayList
	public void readEvents() {
		events.addAll(TxtFileReader.getEvents("src/input.txt"));
		for(int i=0; i<events.size(); i++) {
			System.out.println(events.get(i));
		}
	}
	
	public void sendEvent(InputData event) {
		scheduler.addEvent(event);
	}

	@Override
	public void run() {
		readEvents();
		while(true) {
			
			while(!inputData.isEmpty()) {
				sendEvent(inputData.remove(0));
			}
			
			scheduler.elevatorIsApproaching(floorNumber);
		}
	}

}
