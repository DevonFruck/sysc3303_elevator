import java.util.ArrayList;

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
	
	public void getEventList(int floorNum) {
		if(this.floorNumber == floorNum) {
			for(int i=0; i<this.events.size(); i++) {
				System.out.println(this.events.get(i));
			}
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
