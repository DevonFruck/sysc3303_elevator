package elevator;
/**
 * This ElevatorDoor class is for controlling opening and closing of elevator doors
 * @author Group
 */

public class ElevatorDoor {
    
	private boolean doorStatus;
	/**
	 * ElevatorDoor constructor
	 * default the elevator door status to close
	 * @param int floorNum
	 */
	public ElevatorDoor() {
		this.doorStatus = false;
	}
	
	/**
	 * openDoor
	 * sets the elevator door status to true
	 */

	public void openDoor() {
		this.doorStatus = true;
	}
	
	/**
	 * closeDoor
	 * sets the elevator door status to false, close the elevator door
	 */
	public void closeDoor() {
		this.doorStatus = false;
	}
	
	/**
	 * doorIsOpen
	 * check for if elevator door is open or not
	 * @return boolean doorIsOpen
	 */
	public boolean doorIsOpen() {
		return doorStatus;
	}
}
