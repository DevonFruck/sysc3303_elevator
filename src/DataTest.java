import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

/**
 * Junit testing for the all the data to be read in the system.
 * @author Group
 * */

class DataTest{
    
    @Test
    void testsForElevatorCommunication() throws InterruptedException{
        // Initiating all the classes
        Scheduler scheduler = new Scheduler();
        ElevatorCar elevatorCar = new ElevatorCar(scheduler);
        Floor floor = new Floor(1, scheduler); 

        Thread.sleep(2000);
        
//      LocalTime timeTest = LocalTime.parse("12:25:15.12");
        EventsHandler testEvents = new EventsHandler("12:25:15.12,2,Up,3");
        
        InputEvents input = elevatorCar.getCurrentEvent();
        
        // Validating whether the elevator event can be sent
        assertEquals(testEvents, scheduler.getElevatorEvent());

        // Validating the floor number methods
        assertEquals(1, floor.getFloorNumber());
        assertEquals(1, elevatorCar.getCurrentFloor());

        // Validating the elevator is not active by default
        assertFalse(elevatorCar.getIsActive());

        // Validating the values for the methods
        assertFalse(scheduler.setFloorLights(1));
        assertTrue(scheduler.elevatorIsApproaching(1));

    }
}