import java.time.LocalTime;
/**
 * This class is responsible for handling faulty elevator events/orders with "error" keyword
 * Meaning that this class handles events with faults/errors
 * This class implements the interface InputEvents
 * @author Laith
 */

public class ErrorHandler implements InputEvents{
	private LocalTime time;//Event time
	private int faultyElevator;// number of faulty elevator
	private boolean TrivError;//is the error trivia
	private boolean SerError;//is the error serious
	

	/**
	 * ErrorHandler constructor
	 * takes faulty input event that was extracted from txt file and arranges it in the desired format
	 * example of input from txt file-> ["13:05:27.48","Trivial","1"]
	 * @param input -> input line from the txt file
	 */
	public ErrorHandler(String input) {
		String[] inputs = input.split(",");
		this.time = LocalTime.parse(inputs[0]);
		this.TrivError = inputs[1].equalsIgnoreCase("Trivial");
		this.SerError = inputs[1].equalsIgnoreCase("Serious");
		this.faultyElevator = Integer.valueOf(inputs[2]);
	}

	//Get time
	@Override
	public LocalTime getTime() {
		return this.time;
	}

	/* Get the initial floor that requested the elevator
	 * Because faulty event, we will not be using this
	 * */
	@Override
	public int getInitialFloor() {
		return -1;
	}

	/* Get the destination of the elevator
	 * Because faulty event, we will not be using this
	 * */
	@Override
	public int getDestinationFloor() {
		return -1;
	}

	/*
	 * Checking direction of elevator
	 * Because faulty event, we will not be using this
	 * */
	@Override
	public boolean isGoingUp() {
		return false;
	}
	/*
	 *Checks if the fault/error is serious or not
	 *true = serious  || false = not serious 
	 * */
	@Override
	public boolean isSeriousError() {
		return this.SerError;
	}

	/*
	 *Checks if the fault/error is trivial or not
	 *true = trivial  || false = not trivial 
	 * */
	@Override
	public boolean isTrivialError() {
		return this.TrivError;
	}

	/*
	 * This method returns the faulty elevator's number
	 * */
	@Override
	public int getElevator() {
		return this.faultyElevator;
	}
	
	//Print results
	@Override
	public String toString() {
		return "Time: " + time + "\nTrivial-error: " + TrivError + "\nSerious-error: " + SerError + "\nElevator: " + faultyElevator + "\n";
	}

	
}
