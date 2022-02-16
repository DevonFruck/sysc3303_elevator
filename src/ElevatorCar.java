import java.util.ArrayList;
import java.util.Collections;


/**
 * @author Group
 *
 */
public class ElevatorCar extends Thread {
	
    // floors the elevator must visit
	ArrayList<Integer> floors = new ArrayList<Integer>();
	
	boolean isActive, isDoorOpen;
	InputEvents currentEvent;
	Scheduler scheduler;
	int currentFloor;
	int motor;
	
	// Directions for elevatorCar motor
	int IDLE = 0;
	int UP = 1;
	int DOWN = 2;
	
	/**
	 * Constructor for the ElevatorCar class.
	 * Defines default values for the elevator.
	 * 
	 * @param scheduler The elevator scheduler the class interacts with.
	 */
	public ElevatorCar(Scheduler scheduler) {
		isActive = false;
		currentEvent = null;
		this.scheduler = scheduler;
		currentFloor = 1;
		isDoorOpen = false;
		motor = IDLE;
	}
	
	/**
	 * Getter for the elevator's current event.
	 * 
	 * @return its in progress event.
	 */
	public InputEvents getCurrentEvent() {
		return currentEvent;
	}
	
	// Returns final floor it will arrive to
	public Integer getFinalFloor() {
	   return floors.get(floors.size()-1);
	}
	
	// Add a floor for the elevator to visit
	public boolean addFloor(int floor[]) {
	  
	   if (motor == DOWN && floor[0] > currentFloor)
	      return false;
	   
	   else if (motor == UP && floor[0] < currentFloor)
	      return false;
	   
	   if (!floors.contains(floor[0]))
	      floors.add(floor[0]);
	   if (!floors.contains(floor[1]))
	      floors.add(floor[1]);
	   
	   if(motor == UP) {
	      Collections.sort(floors);
	   }
	   else if (motor == DOWN) {
	      Collections.reverse(floors);
	   }
	   return true;
	}
	
	/**
	 * Getter for the elevator's current floor.
	 * 
	 * @return The number of the floor the elevator is at.
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	/**
	 * Getter for if elevator is active. Deprecated.
	 * 
	 * @return boolean for if it is active
	 */
	public boolean getIsActive() {
		return isActive;
	}
	
	/**
	 * Setter for setting the active state of elevator.
	 * 
	 * @param val boolean state for elevator.
	 */
	public void setIsActive(boolean val) {
	   isActive = val;
	}
	
	/**
	 * Getter for the activity of the elevator motor
	 * @return 0 for idle, 1 for up, 2 for down.
	 */
	public int getMotor() {
	   return motor;
	}
	
	/**
	 * moveFloor moves the elevator up and down floors
	 * until the final destination is reached.
	 * 
	 * @param floorEnd The destination floor.
	 */
	public void moveFloor() {
		while(floors.size() > 0) {
			try {
				if(floors.get(0) == currentFloor) {
				    motor = IDLE;
				    floors.remove(0);
				    System.out.println("Arrived at a dest floor: " + currentFloor);
				}
				
				else if(floors.get(0) > currentFloor) {
				    motor = UP;
					Thread.sleep(2000);
					currentFloor += 1;
					System.out.println("Moved up to floor " + currentFloor);
				} 
				
				else {
				    motor = DOWN;
					Thread.sleep(2000);	
					currentFloor -= 1;
					System.out.println("Moved down to floor: " + currentFloor);
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
			while(floors.isEmpty()) {
				currentEvent = scheduler.getElevatorEvent();
				int eventFloors[] = {currentEvent.getInitialFloor(), currentEvent.getDestinationFloor()};
				addFloor(eventFloors);
				isActive = true;
			}
			
			moveFloor();
			
			
			/*
			System.out.println("\nElevator received event");
			System.out.println("moving to starting floor " + currentEvent.getInitialFloor());
			
			moveFloor(currentEvent.getInitialFloor());
			
			System.out.println("arrived at starting floor. moving to floor " + currentEvent.getDestinationFloor());
			
			moveFloor(currentEvent.getDestinationFloor());
			
			System.out.println("arrived at destination floor " + currentEvent.getDestinationFloor());
			
			setIsActive(false);
			currentEvent = null;
			*/
			
		}
	}
}
