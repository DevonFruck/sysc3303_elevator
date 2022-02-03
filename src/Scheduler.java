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
	
	public Scheduler() {
		eventsQueue = new ArrayList<InputData>();
	}
	
	// Adds events to queue from floor subsystem
	public synchronized void addEvent(InputData InputDataEvent) {
		eventsQueue.add(InputDataEvent);
		notifyAll();
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
		return eventsQueue.remove(0);
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scheduler scheduler = new Scheduler();
		
		Thread[] floors = new Thread[2];
		floors[0] = new Thread(new Floor(1, scheduler), "floor1");
		floors[1] = new Thread(new Floor(2, scheduler), "floor2");
		
		Thread[] elevators = new Thread[1];
		elevators[0] = new Thread(new ElevatorCar(scheduler), "elevator1");
		
		for(int i=0; i<floors.length; i++) {
			floors[i].start();
		}
			
		for(int j=0; j<elevators.length; j++) {
			elevators[j].start();
		}
	}

}
