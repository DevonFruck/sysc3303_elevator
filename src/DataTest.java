import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Junit testing for the all the data to be read in the system.
 * @author Azizul Hasan 101124159
 * */

class DataTest{
    
    @Test
    void test() throws InterruptedException{
        // Initiating all the classes
        Scheduler scheduler = new Scheduler();
        ElevatorCar elevatorCar = new ElevatorCar(scheduler);
        Floor floor = new Floor(1, scheduler); 

        floor.start();
        Thread.sleep(2000);
        InputData input = elevatorCar.getCurrentEvent();
        
        // Validating whether the elevator event can be sent
        assertEquals(input, scheduler.getElevatorEvent());

        // Validating whether all the data is read
        assertEquals(2,floor.inputData.size());

        floor.interrupt();
        elevatorCar.start();
        Thread.sleep(2000);

        // assertFalse(scheduler.elevatorIsApproaching(2));
        // assertEquals(input, scheduler.getElevatorEvent());

    }
}