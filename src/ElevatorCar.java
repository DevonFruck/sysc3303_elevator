/**
 * 
 */

/**
 * @author Group
 *
 */
public class ElevatorCar extends Thread {
	
	//ArrayList<> = new ArrayList<>;
	boolean isActive, isDoorOpen;
	InputEvents currentEvent;
	Scheduler scheduler;
	int currentFloor;
	
	
	public ElevatorCar(Scheduler scheduler) {
		isActive = false;
		currentEvent = null;
		this.scheduler = scheduler;
		currentFloor = 1;
		isDoorOpen = false;
	}
	
	public InputEvents getCurrentEvent() {
		return currentEvent;
	}
	
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(boolean val) {
	   isActive = val;
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
			while(!isActive && currentEvent == null) {
				currentEvent = scheduler.getElevatorEvent();
				isActive = true;
			}
			System.out.println("\nElevator received event");
			System.out.println("moving to starting floor " + currentEvent.getInitialFloor());
			
			moveFloor(currentEvent.getInitialFloor());
			
			System.out.println("arrived at starting floor. moving to floor " + currentEvent.getDestinationFloor());
			
			moveFloor(currentEvent.getDestinationFloor());
			
			System.out.println("arrived at destination floor " + currentEvent.getDestinationFloor());
			
			setIsActive(false);
			currentEvent = null;
			
		}
		
		
	}

}
