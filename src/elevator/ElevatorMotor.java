package elevator;

import java.util.Random;
import types.MotorState;
import static config.Config.*;
/**
 * This class is controlling Elevator up/down directions
 * @author ElevatorMotor
 */
public class ElevatorMotor {
	
	private MotorState status;
	private ElevatorCar elevator;
	private Random rng;
	
	/**
	 * ElevatorMotor constructor
	 * Defaults the elevator motor to idle 
	 */
	public ElevatorMotor(ElevatorCar elevator) {
		this.elevator = elevator;
		status = MotorState.IDLE;
		
		rng = new Random();
	}
	
	/**
	 * getStatus
	 * returns the status(enum) of elevator motor, up/down 
	 * @return mortorStat
	 */
	public MotorState getStatus() {
		return status;
	}
	
	/**
	 * setStatus
	 * set the status(enum) of elevator motor, up/down 
	 */
	public void setStatus(MotorState status) {
		this.status = status;
	}
	
	/**
	 * Actually moves the elevator
	 */
	public int moveElevator(int currFloor, int id, boolean isUp, int faultFlag) {
		int time = 3000;
		if(faultFlag > 0) {
		    if(rng.nextInt(3) == 1) {
		        System.out.println("Fatal Error, Shutting down elevator: "+id);
	            elevator.decrementFaultFlag();
		        this.elevator.shutDown();
	            return currFloor;
		    }
	    	
	    }
		try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	    
	    int newCurrFloor = currFloor;
	    if(isUp && currFloor < NUM_OF_FLOORS) {
	        newCurrFloor = currFloor+1;
	        //System.out.println("Moving elevator(" +id+ ") from " +currFloor+ " to " +newCurrFloor);
	    } else {
	    	if(currFloor > 1) {
	        newCurrFloor = currFloor-1;
	        //System.out.println("Moving elevator(" +id+ ") from " +currFloor+ " to " +newCurrFloor);
	    	}
	    }
	    
	    return newCurrFloor;
	}
}
