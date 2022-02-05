import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Junit testing for the all the data to be read in the system.
 * @author Group
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

        // Validating the floor number methods
        assertEquals(1, floor.getFloorNumber());
        assertEquals(1, elevatorCar.getCurrentFloor());

        // Validating the elevator is not active by default
        assertFalse(elevatorCar.getIsActive());

        // Validating the values for the methods
        assertFalse(scheduler.setFloorLights(1));
        assertTrue(scheduler.elevatorIsApproaching(1));

        // Validating whether all the data is read
        assertEquals(7,floor.inputData.size());

        floor.interrupt();
        elevatorCar.start();
        Thread.sleep(2000);

    }
}