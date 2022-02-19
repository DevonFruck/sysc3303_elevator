package elevator;
/**
 * This ElevatorLamp class is for turning on and off the elevator lamps and checking elevator floor number
 * @author Group
 */

public class ElevatorLamp {
	
	private int floorNum;
	private boolean OnStatus;
	
	/**
	 * ElevatorLamp constructor
	 * takes input floor number and set the Elevator lamp as the number
	 * @param int floorNum
	 */
	public ElevatorLamp(int floorNum){
		this.floorNum = floorNum;
	}
	
	/**
	 * getFloorNum
	 * returns the elevator's current floor number
	 * @return int floorNum
	 */
	public int getFloorNum() {
		return floorNum;
	}
	
	/**
	 * turnOn
	 * turnOn the Elevator lamp
	 */
	public void turnOn() {
		this.OnStatus = true;
	}
	/**
	 * turnOff
	 * turn off the Elevator lamp
	 */
	public void turnOff() {
		this.OnStatus = false;
	}
	
	/**
	 * isOn
	 * check if the elevator lamp is on or not
	 * @return OnStatus boolean
	 */
	public boolean isOn() {
		return OnStatus;
	}

}
