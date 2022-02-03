/**
 * 
 */

/**
 * @author Group
 *
 */
public class ElevatorCar implements Runnable {
	
	//ArrayList<> = new ArrayList<>;
	boolean active;
	InputData currentEvent;
	Scheduler scheduler;
	
	public ElevatorCar(Scheduler scheduler) {
		active = false;
		currentEvent = null;
		this.scheduler = scheduler;
	}
	
	public InputData getCurrentTask() {
		return currentEvent;
	}
	
	public boolean getIsActive() {
		return active;
	}

	@Override
	public void run() {
		while(!active) {
			currentEvent = scheduler.getElevatorEvent();
			active = true;
		}
		
		System.out.println("Job received from elevator");
		System.out.printf("start floor: %s", currentEvent.floorStart);
		System.out.printf("\nend floor: %s", currentEvent.floorEnd);
		//System.out.printf("time: %s", currentEvent.time);
		
	}

}
