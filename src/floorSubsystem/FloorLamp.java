package floorSubsystem;

/**
 * This class handles the floor lamp
 * @author Group
 */
public class FloorLamp {
	private boolean lampLight;
	
	/**
	 * FloorLamp constructor, sets it off by default
	 */
	public FloorLamp() {
		this.lampLight = false;
	}
	
	/**
	 * Turns on the lamp for the floor this lamp is in
	 */
	public void turnLampOn() {
		this.lampLight = true;
	}
	
	/**
	 * Turns off the lamp for the floor this lamp is in
	 */
	public void turnLampOff() {
		this.lampLight = false;
	}
	
	/**
	 * Gets the current status of the lamp
	 *
	 * @return lampLight boolean value
	 */
	public boolean getLampStatus() {
		return this.lampLight;
	}
}
