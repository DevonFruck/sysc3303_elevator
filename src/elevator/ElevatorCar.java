package elevator;

import java.util.ArrayList;
import java.util.Collections;

import scheduler.Scheduler;
import types.InputEvents;
import types.motorStat;

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
	ElevatorButton elevButtons[] = new ElevatorButton[5];
	
	ElevatorMotor motor = new ElevatorMotor();
	
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
		motor.setStatus(motorStat.IDLE);
		
		for (int i=0; i<5; i++) {			
			elevButtons[i] = new ElevatorButton(i+1);
		}
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
	  
	   if (motor.getStatus() == motorStat.DOWN && floor[0] > currentFloor)
	      return false;
	   
	   else if (motor.getStatus() == motorStat.UP && floor[0] < currentFloor)
	      return false;
	   
	   if (!floors.contains(floor[0]))
	      floors.add(floor[0]);
	   if (!floors.contains(floor[1]))
	      floors.add(floor[1]);
	   
	   if(motor.getStatus() == motorStat.UP) {
	      Collections.sort(floors);
	   }
	   else if (motor.getStatus() == motorStat.DOWN) {
	      Collections.reverse(floors);
	   }
	   
	   // Turn on button lights for destination floor
	   elevButtons[floor[1]].pressButton();
	   
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
	
	public ElevatorMotor getMotor() {
		return motor;
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
	 * moveFloor moves the elevator up and down floors
	 * until the final destination is reached.
	 * 
	 * @param floorEnd The destination floor.
	 */
	public void moveFloor() {
		while(floors.size() > 0) {
			try {
				if(floors.get(0) == currentFloor) {
				    motor.setStatus(motorStat.IDLE);
				    int destFloor = floors.remove(0);
				    System.out.println("Arrived at a dest floor: " + currentFloor);
				}
				
				else if(floors.get(0) > currentFloor) {
				    motor.setStatus(motorStat.UP);
					Thread.sleep(2000);
					currentFloor += 1;
					System.out.println("Moved up to floor " + currentFloor);
				} 
				
				else {
				    motor.setStatus(motorStat.DOWN);
					Thread.sleep(2000);	
					currentFloor -= 1;
					System.out.println("Moved down to floor: " + currentFloor);
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while(true) {
			while(floors.isEmpty()) {
				//currentEvent = scheduler.getElevatorEvent();
				int eventFloors[] = {currentEvent.getInitialFloor(), currentEvent.getDestinationFloor()};
				addFloor(eventFloors);
				isActive = true;
			}
			
			moveFloor();
		}
	}
}
