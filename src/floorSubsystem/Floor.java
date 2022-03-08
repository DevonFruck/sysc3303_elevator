package floorSubsystem;

import java.util.ArrayList;
import scheduler.Scheduler;
import types.InputEvents;

/**
 * @author L4 Group 9
 *
 */
public class Floor extends Thread {
	
	int floorNumber;
	Scheduler scheduler;
	private ArrayList<InputEvents> events;
	private FloorButton floorButtons[];
 	
	/**
	 * Constructor for the Floor class. Initialized the floor number,
	 * a reference to the scheduler, it's floor buttons, and an events queue.
	 * @param floorNumber
	 * @param scheduler
	 */
	public Floor(int floorNumber, Scheduler scheduler) {
		this.floorNumber = floorNumber;
		this.scheduler = scheduler;
		this.events = new ArrayList<InputEvents>();
		
		floorButtons = new FloorButton[] { new FloorButton(), new FloorButton()};
	}
	
	/** Returns the floor number of the floor.
	 * 
	 * @return Floor number.
	 */
	public int getFloorNumber() {
		return floorNumber;
	}
	
	/**
	 * Reads from the input.txt file located in the same directory
	 * and parses the data into InputEvent types and adds them to the floor queue.
	 */
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
	
	
	/**
	 * Invoked by the scheduler when the requested elevator has arrived
	 * at the floor.
	 */
	public void elevatorArrived() {
	   System.out.println("People boarding elevator on floor: " + floorNumber);
	}
	
	public ArrayList<InputEvents> getEventList() {
		return events;
	}
	
	
	/**
	 * Send a floor input event to the scheduler.
	 * 
	 * @param event Floor event to send to the scheduler.
	 */
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
				
				try {
                   Thread.sleep(2000);
                } catch (InterruptedException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
                }
			}
			scheduler.floorWait();	
		}
	}

}
