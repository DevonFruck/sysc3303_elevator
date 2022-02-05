import java.time.LocalTime;

/**
 * This interface class is for the input Events/ orders for the elevator
 * @author Laith
 */
public interface InputEvents {
	//Get the event time
	LocalTime getTime(); 
	//toString method, object --> string transformations 
	String toString();
	//Method to get/request elevator 
	int getElevator();
	//Method for determining which floor requested the elevator/pressed the button
	int getInitialFloor();
	//Method to get the destination of the elevator
	int getDestinationFloor();
	//Boolean method that checks the elevator move direction true=up, false=down
	boolean isGoingUp();
	//Boolean method to see if the error/fault is serious
	boolean isSeriousError();
	//Boolean method to see if the error/fault is trivial 
	boolean isTrivialError();
	
}
