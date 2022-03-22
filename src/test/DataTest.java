package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import floorSubsystem.Floor;
import floorSubsystem.FloorSubsystem;
import scheduler.Scheduler;
import types.EventsHandler;
//import types.MotorState;

/**
 * Junit 5 testing for the all the data to be read in the system.
 * @author L4 Group 9
 */

class DataTest{

    Scheduler scheduler = new Scheduler();

    @Test
    void testsForElevatorCommunication() throws InterruptedException{
//     Initiating all the classes
        FloorSubsystem subsystem;
        
        subsystem = new FloorSubsystem();
        Floor floor = new Floor(subsystem, 1); 

        Thread.sleep(2000);

        LocalTime timeTest = LocalTime.parse("12:25:15.12");
        EventsHandler testEvents = new EventsHandler("12:25:15.12,2,Up,3");

    }

    @Test
    void FloorTest() throws InterruptedException {
        FloorSubsystem subsystem;
        
        subsystem = new FloorSubsystem();
        Floor floor = new Floor(subsystem, 7);
        
        assertEquals(7, floor.getFloorNumber());
        
    }

    @Test
    void testsForEventHandlerMethods() throws InterruptedException {
        LocalTime timeTest = LocalTime.parse("12:25:15.12");
        EventsHandler testEvents = new EventsHandler("12:25:15.12,2,Up,3");

        // Validating EventsHandler methods
        assertEquals(3, testEvents.getDestinationFloor());
        assertEquals(2, testEvents.getInitialFloor());
        assertEquals(true, testEvents.isGoingUp());
        assertEquals(timeTest, testEvents.getTime());
    }
}