import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Group
 *
 */
public class Scheduler {
	
	ArrayList<InputData> eventsQueue;
	ArrayList<ElevatorCar> elevatorList;
	
	public Scheduler() {
		eventsQueue = new ArrayList<InputData>();
		
		Thread[] floors = new Thread[2];
		floors[0] = new Thread(new Floor(1, this), "floor1");
		floors[1] = new Thread(new Floor(2, this), "floor2");
		
		elevatorList = new ArrayList<ElevatorCar>();
		elevatorList.add(new ElevatorCar(this));
		
		for(int i=0; i<floors.length; i++) {
			floors[i].start();
		}
			
		for(Thread elevator: elevatorList) {
			elevator.start();
		}
	}
	
	// Adds events to queue from floor subsystem
	public synchronized void addEvent(InputData InputDataEvent) {
		eventsQueue.add(InputDataEvent);
		notifyAll();
	}
	
	public synchronized boolean elevatorIsApproaching(int floorNumber) {
		try {
			while(elevatorList.get(0).getCurrentFloor() != floorNumber) {
				wait();
			}
			return true;
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	// Function for elevator, elevators will poll for 
	// available events when inactive
	public synchronized InputData getElevatorEvent() {
		while(eventsQueue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		InputData event = eventsQueue.remove(0);
		notifyAll();
		return event;
	}
	
	public synchronized boolean setFloorLights(int floorNum) {
		return false;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		@SuppressWarnings("unused")
		Scheduler scheduler = new Scheduler();

	}

}
