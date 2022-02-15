import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;

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
        Floor floor = new Floor(1, scheduler); 

        Thread.sleep(2000);
        
        //LocalTime timeTest = LocalTime.parse("12:25:15.12");
        //EventsHandler testEvents = new EventsHandler("12:25:15.12,2,Up,3");
        //InputEvents input = elevatorCar.getCurrentEvent();
        
        // Validating the floor methods
        assertEquals(1, floor.getFloorNumber());
        
        
        // Validating the values for the methods
        assertFalse(scheduler.setFloorLights(1));
        //assertTrue(scheduler.elevatorIsApproaching(1));
    }
    
    @Test
    void FloorTest() throws InterruptedException{
    	Scheduler scheduler = new Scheduler();
    	Floor floor = new Floor(1, scheduler);
    	
    	//test getFloorNumber()
    	assertEquals(1, floor.getFloorNumber());
    	
    	//test readEvents() and getEventList()
    	assertEquals(new ArrayList<InputEvents>(), floor.getEventList());
    	floor.readEvents();
    	assertNotNull(floor.getEventList());
    }
    
    @Test
    void testsForElevatorCarMethods() throws InterruptedException{
    	// Validating ElevatorCar methods
    	Scheduler scheduler = new Scheduler();
        ElevatorCar elevatorCar = new ElevatorCar(scheduler);
    	assertNull(elevatorCar.getCurrentEvent());
        assertEquals(1, elevatorCar.getCurrentFloor());
        elevatorCar.moveFloor(2);
        assertEquals(2, elevatorCar.getCurrentFloor());
        // Validating the elevator is not active by default
        assertFalse(elevatorCar.getIsActive()); 
    }
    
    @Test
    void testsForEventHandlerMethods() throws InterruptedException{
    	LocalTime timeTest = LocalTime.parse("12:25:15.12");
        EventsHandler testEvents = new EventsHandler("12:25:15.12,2,Up,3");
                
        // Validating EventsHandler methods
        assertEquals(3, testEvents.getDestinationFloor());
        assertEquals(2, testEvents.getInitialFloor());
        assertEquals(true, testEvents.isGoingUp());
        assertEquals(timeTest, testEvents.getTime());
    }
}