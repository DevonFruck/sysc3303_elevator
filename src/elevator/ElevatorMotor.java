package elevator;
import types.MotorState;
import static config.Config.*;
/**
 * This class is controlling Elevator up/down directions
 * @author ElevatorMotor
 */
public class ElevatorMotor {
	
	private MotorState status;
	/**
	 * ElevatorMotor constructor
	 * Defaults the elevator motor to idle 
	 */
	public ElevatorMotor() {
		status = MotorState.IDLE;
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
	public int moveElevator(int currFloor, int id, boolean isUp) {
	    try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    
	    int newCurrFloor = currFloor;
	    if(isUp && currFloor < NUM_OF_FLOORS) {
	        newCurrFloor = currFloor+1;
	        System.out.println("Moving elevator(" +id+ ") from " +currFloor+ " to " +newCurrFloor);
	    } else {
	    	if(currFloor > 1) {
	        newCurrFloor = currFloor-1;
	        System.out.println("Moving elevator(" +id+ ") from " +currFloor+ " to " +newCurrFloor);
	    	}
	    }
	    
	    return newCurrFloor;
	}
}
