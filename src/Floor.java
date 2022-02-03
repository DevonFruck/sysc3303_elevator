import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Group
 *
 */
public class Floor implements Runnable {
	
	int floorNumber;
	Scheduler scheduler;
	ArrayList<InputData> inputData;
	
	public Floor(int floorNumber, Scheduler scheduler) {
		this.floorNumber = floorNumber;
		this.scheduler = scheduler;
		inputData = new ArrayList<InputData>();
		
		inputData.add(new InputData(null, 2, 1));
	}
	
	public int getFloorNumber() {
		return floorNumber;
	}
	
	// Reads from text file to put into ArrayList
	public void readEvents() {
		
	}
	
	public void sendEvent(InputData event) {
		scheduler.addEvent(event);
	}

	@Override
	public void run() {
		
		while(!inputData.isEmpty()) {
			sendEvent(inputData.remove(0));
		}
		// TODO Auto-generated method stub
		
	}

}
