package elevator;
import types.MotorState;

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
}
