package floorSubsystem;

import java.util.ArrayList;
import scheduler.Scheduler;
import types.InputEvents;

/**
 * @author Group
 *
 */
public class Floor extends Thread {
	
	int floorNumber;
	Scheduler scheduler;
	private ArrayList<InputEvents> events;
	private FloorButton floorButtons[];
 	
	public Floor(int floorNumber, Scheduler scheduler) {
		this.floorNumber = floorNumber;
		this.scheduler = scheduler;
		this.events = new ArrayList<InputEvents>();
		
		floorButtons = new FloorButton[] { new FloorButton(), new FloorButton()};
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
				InputEvents ev = events.remove(0);
				sendEvent(ev);
				
				if(ev.isGoingUp()) {
					floorButtons[1].pressButton();
				} else {
					floorButtons[0].pressButton();
				}
			}
			scheduler.elevatorIsApproaching(floorNumber);
		}
	}

}
