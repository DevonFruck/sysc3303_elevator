package floorSubsystem;

/**
 * This class handles floor button presses
 * @author Group
 */
public class FloorButton {
	private boolean button;
	private FloorLamp floorButtonLamp;
	
	/**
	 * FloorButton constructor, sets button and its corresponding lamp off
	 */
	public FloorButton() {
		this.button = false;
		this.floorButtonLamp = new FloorLamp();
	}
	
	/**
	 * Presses button for this floor, turns on/off lamp accordingly
	 */
	public void pressButton() {
		this.button = !this.button;
		if (this.button)
			this.floorButtonLamp.turnLampOn();
		else
			this.floorButtonLamp.turnLampOff();
	}
	
	/**
	 * Checks whether button has been pressed or not
	 * 
	 * @return button boolean
	 */
	public boolean checkButton() {
		return this.button;
	}
	
	/**
	 * Checks whether lamp is turned on or not
	 * 
	 * @return floor lamp light boolean
	 */
	public boolean checkLamp() {
		return this.floorButtonLamp.getLampStatus();
	}
		
}	
