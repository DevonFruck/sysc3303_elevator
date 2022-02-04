/**
 * 
 */

/**
 * @author Group
 *
 */
public class ElevatorCar extends Thread {
	
	//ArrayList<> = new ArrayList<>;
	boolean active;
	InputData currentEvent;
	Scheduler scheduler;
	int currentFloor;
	
	public ElevatorCar(Scheduler scheduler) {
		active = false;
		currentEvent = null;
		this.scheduler = scheduler;
		currentFloor = 1;
	}
	
	public InputData getCurrentEvent() {
		return currentEvent;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public boolean getIsActive() {
		return active;
	}
	
	public void moveFloor(int floorEnd) {
		while(floorEnd != currentFloor) {
			try {
				if(floorEnd == currentFloor) {
					break;
				}
				else if(floorEnd > currentFloor) {
					Thread.sleep(2000);
					currentFloor += 1;
					System.out.println("Moved up to floor " + currentFloor);
				} else {
					Thread.sleep(2000);	
					currentFloor -= 1;
					System.out.println("Moved down to floor " + currentFloor);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void run() {
		while(true) {			
			while(!active && currentEvent == null) {
				currentEvent = scheduler.getElevatorEvent();
				active = true;
			}
			System.out.println("\nElevator received event");
			System.out.println("moving to starting floor " + currentEvent.getStartFloor());
			
			moveFloor(currentEvent.getStartFloor());
			
			System.out.println("arrived at starting floor. moving to floor " + currentEvent.getEndFloor());
			
			moveFloor(currentEvent.getEndFloor());
			
			System.out.println("arrived at destination floor " + currentEvent.getEndFloor());
			
			active = false;
			currentEvent = null;
			
		}
		
		
	}

}
