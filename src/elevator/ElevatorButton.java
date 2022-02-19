package elevator;

/**
 * The ElevatorButton class is for handling button presses in a elevator 
 * @author Group
 */
public class ElevatorButton {
	private int FloorNum;
	private ElevatorLamp elevatorLamp;
	
	/**
	 * ElevatorButton constructor
	 * Uses input floor number and sets ElevatorButton to service that floor
	 * @param int floorNum
	 */
	public ElevatorButton(int floorNum) {
		this.FloorNum = floorNum;
		this.elevatorLamp = new ElevatorLamp(this.FloorNum);
	}
	
	/**
	 * Presses button for floor the elevator wants to go to
	 */
	public void pressButton() {
		if (!this.elevatorLamp.isOn())
			this.elevatorLamp.turnOn();
		else
			this.elevatorLamp.turnOff();
	}
	/**
	 * Checks whether button has been pressed
	 */
	public boolean buttonStatus() {
		return this.elevatorLamp.isOn();
	}
	
}
