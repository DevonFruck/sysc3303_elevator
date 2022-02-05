import java.util.ArrayList;

/**
 * @author Group
 *
 */
public class Floor extends Thread {
	
	int floorNumber;
	Scheduler scheduler;
//	ArrayList<InputEvents> InputEvents;
	private ArrayList<InputEvents> events;
	
	public Floor(int floorNumber, Scheduler scheduler) {
		this.floorNumber = floorNumber;
		this.scheduler = scheduler;
		this.events = new ArrayList<InputEvents>();
//		InputEvents = new ArrayList<InputEvents>();
	}
	
	public int getFloorNumber() {
		return floorNumber;
	}
	
	// Reads from text file to put into ArrayList
	public void readEvents() {
		ArrayList<InputEvents> arr = new ArrayList<InputEvents>();
		arr.addAll(TxtFileReader.getEvents("src/input.txt"));
		for(int i=0; i<arr.size(); i++) {
			InputEvents temp = arr.get(i);
			if(temp.getInitialFloor()==this.floorNumber) {
				this.events.add(temp);
			}
		}
//		this.getEventList(2);
	}
	
	public ArrayList<InputEvents> getEventList() {
		return events;
	}
	
	public void sendEvent(InputEvents event) {
		scheduler.addEvent(event);
	}

	@Override
	public void run() {
		readEvents();
		while(true) {
			
			while(!events.isEmpty()) {
				sendEvent(events.remove(0));
			}
			scheduler.elevatorIsApproaching(floorNumber);
		}
	}

}
