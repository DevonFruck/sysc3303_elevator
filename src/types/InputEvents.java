package types;
import java.time.LocalTime;

/**
 * This interface class is for the input Events/ orders for the elevator
 * @author Group
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
	
	//Return elevator's state/moving direction
	MotorState getMotorState();
	
	//Return error code
	String getError();
}
