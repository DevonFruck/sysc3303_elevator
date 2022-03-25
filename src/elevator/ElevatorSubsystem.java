package elevator;

import static config.Config.*;
/**
 * @author SYSC3303 W2022 Group 9
 */
public class ElevatorSubsystem {
	
	public ElevatorSubsystem() {
		ElevatorCar elevator1 = new ElevatorCar(1, 1);
    	ElevatorCar elevator2 = new ElevatorCar(2, (int)Math.floor(NUM_OF_FLOORS/2));
    	ElevatorCar elevator3 = new ElevatorCar(3, NUM_OF_FLOORS);
    	ElevatorCar elevator4 = new ElevatorCar(4, NUM_OF_FLOORS-2);
    	
    	elevator1.start();
    	elevator2.start();
    	elevator3.start();
    	elevator4.start();
	}
    
    public static void main(String[] args) {

    }
}
