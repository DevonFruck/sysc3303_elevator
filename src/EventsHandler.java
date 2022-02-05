import java.time.LocalTime;

/**
 * This class is responsible for handling successful elevator events/orders with "user" keyword
 * Meaning that this class handles events with no faults/errors
 * This class implements the interface InputEvents
 * @author Group
 */

public class EventsHandler implements InputEvents {
	private LocalTime time; //Event time
	private int initialFloor; //Initial floor, floor that requested the elevator
	private boolean isUp; //Direction of elevator movement
	private int destinationFloor;//Destination of the elevator/user
	
	/**
	 * EventsHandler constructor
	 * takes input event that was extracted from txt file and arranges it in the desired format
	 * example of input from txt file-> ["10:35:00.00","1","Up","2"]
	 * @param input -> input line from the txt file
	 */
	public EventsHandler(String input) {
		String[] inputs = input.split(",");
		this.time = LocalTime.parse(inputs[0]);
		this.initialFloor = Integer.valueOf(inputs[1]);
		this.isUp = inputs[2].equalsIgnoreCase("Up");
		this.destinationFloor = Integer.valueOf(inputs[3]);
	}

	//Get time
	@Override
	public LocalTime getTime() {
		return this.time;
	}


	//Get the initial floor that requested the elevator
	@Override
	public int getInitialFloor() {
		return this.initialFloor;
	}


	//Get the destination of the elevator
	@Override
	public int getDestinationFloor() {
		return this.destinationFloor;
	}


	//Check if the elevator is going up or down
	@Override
	public boolean isGoingUp() {
		return this.isUp;
	}

	/*
	 * Check if the event is faulty or not
	 * In this case, it is not. so we always return false
	 * */
	@Override
	public boolean isSeriousError() {
		return false;
	}


	//It isn't a fault so this method does not associate with PanssengerInput
	@Override
	public boolean isTrivialError() {
		return false;
	}

	/*
	 * Get the elevator
	 * returns -1 because this is only needed when we have a faulty event/elevator
	 * */
	@Override
	public int getElevator() {
		return -1;
	}
	
	//Print results
	@Override
	public String toString() {
		return "Time: " + time + "\nFloor: " + initialFloor + "\nFloor-button: " + (isUp ? "Up" : "Down") + "\nDestination: " + destinationFloor + "\n";
	}
}
