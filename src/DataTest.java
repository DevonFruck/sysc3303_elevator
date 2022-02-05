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
        
        LocalTime timeTest = LocalTime.parse("12:25:15.12");
        EventsHandler testEvents = new EventsHandler("12:25:15.12,2,Up,3");
        
        //InputEvents input = elevatorCar.getCurrentEvent();
        
        // Validating EventsHandler methods
        assertEquals(3, testEvents.getDestinationFloor());
        assertEquals(2, testEvents.getInitialFloor());
        assertEquals(true, testEvents.isGoingUp());
        assertEquals(timeTest, testEvents.getTime());
       
        // Validating ElevatorCar methods
        assertNull(elevatorCar.getCurrentEvent());
        assertEquals(1, elevatorCar.getCurrentFloor());
        elevatorCar.moveFloor(2);
        assertEquals(2, elevatorCar.getCurrentFloor());
        // Validating the elevator is not active by default
        assertFalse(elevatorCar.getIsActive());
        
        // Validating the floor methods
        assertEquals(1, floor.getFloorNumber());
        
        
        // Validating the values for the methods
        assertFalse(scheduler.setFloorLights(1));
        //assertTrue(scheduler.elevatorIsApproaching(1));

    }
}