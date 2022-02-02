/**
 * 
 */

/**
 * @author Group
 *
 */
public class ElevatorCar {
	
	//ArrayList<> = new ArrayList<>;
	boolean active;
	InputData currentTask;
	
	public ElevatorCar() {
		active = false;
		currentTask = null;
	}
	
	public InputData getCurrentTask() {
		return currentTask;
	}
	
	public boolean getIsActive() {
		return active;
	}

}
