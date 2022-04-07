package elevator;

import static config.Config.*;
import java.util.Random;

/**
 * @author SYSC3303 W2022 Group 9
 */
public class ElevatorSubsystem {
	
    /**
     * Constructor for elevator subsystem. Creates the socket to receive data
     * and initializes the elevators
     */
	public ElevatorSubsystem() {
	    // Randomly generate where elevator will start
	    Random rng = new Random();
	    int startingFloor;
	    
	    ElevatorCar elevators[] = new ElevatorCar[NUM_OF_ELEVATORS];
	    
	    for(int i=1; i<=NUM_OF_ELEVATORS; i++) {
	        startingFloor = rng.nextInt(NUM_OF_FLOORS)+1;
	        elevators[i-1] = new ElevatorCar(i, startingFloor, DEFAULT, ELEVATOR_SCHEDULER_PORT);
	    }
	    
	    for(ElevatorCar elevator: elevators) {
            elevator.start();
        }
	}
}
