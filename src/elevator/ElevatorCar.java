package elevator;

import java.io.IOException;
import java.util.ArrayList;

import scheduler.Scheduler;
import types.InputEvents;
import types.motorStat;
import elevator.ElevatorSubsystem;
/**
 * @author L4 Group 9
 *
 */
public class ElevatorCar extends Thread {
	
    // floors the elevator must visit
	ArrayList<Integer> floors = new ArrayList<Integer>();
	
	int id;
	boolean isActive, isDoorOpen;
	InputEvents currentEvent;
	ElevatorSubsystem subsys;
	int currentFloor;
	ElevatorButton elevButtons[] = new ElevatorButton[5];
	
	ElevatorMotor motor = new ElevatorMotor();
	
	/**
	 * Constructor for the ElevatorCar class.
	 * Defines default values for the elevator.
	 * 
	 * @param scheduler The elevator scheduler the class interacts with.
	 */
	public ElevatorCar(ElevatorSubsystem subsys, int id) {
	    this.id = id;
		isActive = false;
		currentEvent = null;
		this.subsys = subsys;
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
	
	public void receiveEvent() throws IllegalArgumentException, IOException {
//		String data[];
//		data = subsys.getFromScheduler();
//		
//        if (Integer.parseInt(data[0]) != this.id) {
//        	throw new IllegalArgumentException("Data sent to wrong elevator");
//        }
//        
//        int floorNum = Integer.parseInt(data[1]);
//        //U,D,I,O
//        String state = data[2];
//        this.elevatorMovement(floorNum);
		int floor = subsys.getNextFloor(this.id);
		elevatorMovement(floor);
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
	 * Incorporate the movement of the elevator. Depending on whether the floor
	 * parameter is lower or higher than the current floor of the elevator, the
	 * elevator will move up or down one floor, or signal the scheduler if it has
	 * arrived.
	 * 
	 * @param nextFloor the floor number of the destination floor.
	 */
	public void elevatorMovement(int nextFloor) {
	   if(currentFloor == nextFloor) {
	      isDoorOpen = true;
	      System.out.println("made it!");
		  String data[];
		  data = subsys.getFromScheduler();
		
          //U,D,I,O
          String state = data[2];
	      // Send to scheduler that we made it
	      //TODO: update scheduler of our position
		  try {
			subsys.sendToScheduler(nextFloor, state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      return;
	   }
	   
	   try {
	      Thread.sleep(5000);
       } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }
	   
	   if(nextFloor > currentFloor) {
	      motor.setStatus(motorStat.UP);
	      currentFloor ++;
	   } else {
	      motor.setStatus(motorStat.DOWN);
	      currentFloor --;
	   }
	   
	   System.out.printf("Elevator %d at floor: %d\n", id, currentFloor);
	}
	
	
	@Override
	public void run() {
		while(true) {
			try {
                this.receiveEvent();
            } catch (IllegalArgumentException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			
		}
	}
}
