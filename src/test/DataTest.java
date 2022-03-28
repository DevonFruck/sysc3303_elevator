package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import floorSubsystem.Floor;
import floorSubsystem.FloorSubsystem;
import scheduler.Scheduler;
import types.EventsHandler;
import types.MotorState;
//import types.MotorState;

/**
 * Junit 5 testing for the all the data to be read in the system.
 * @author L4 Group 9
 */

class DataTest{

//@Test
//    void testsForElevatorCommunication() throws InterruptedException{
////     Initiating all the classes
//        FloorSubsystem subsystem;
//        
//        subsystem = new FloorSubsystem();
//        Floor floor = new Floor(subsystem, 1); 
//
//        Thread.sleep(2000);
//
//        LocalTime timeTest = LocalTime.parse("12:25:15.12");
//        EventsHandler testEvents = new EventsHandler("12:25:15.12,2,Up,3");
//
//    }
   
    @Test
    void MotorStateTest() throws InterruptedException {
        //checking values of our MotorState enum
        assertEquals("IDLE", MotorState.IDLE.name());
        
        assertEquals("UP", MotorState.UP.name());
        
        assertEquals("DOWN", MotorState.DOWN.name());
    }
    
    @Test
    void EventsHandlerTest() throws InterruptedException {
        //Initializing an UP event with no faults
        String input = "User|15:26:17.43,4,Up,7, ";
        EventsHandler event = new EventsHandler(input.split("\\|")[1]);
        
        //Testing various getter methods to ensure event was created as intended
        assertEquals(4, event.getInitialFloor());    
        assertEquals(7, event.getDestinationFloor());
        assertEquals(MotorState.UP, event.getMotorState());
        assertEquals("NA", event.getError());
        
        //Initializing a DOWN event with a trivial fault
        input = "User|13:05:27.48,7,Down,2,Trivial";
        event = new EventsHandler(input.split("\\|")[1]);
        
        //Testing various getter methods to ensure event was created as intended
        assertEquals(7, event.getInitialFloor());
        assertEquals(2, event.getDestinationFloor());
        assertEquals(MotorState.DOWN, event.getMotorState());
        assertEquals("Trivial", event.getError());
        
        //Initializing an UP event with a serious fault
        input = "User|13:19:32.45,6,Up,10,Serious";
        event = new EventsHandler(input.split("\\|")[1]);

        //Testing various getter methods to ensure event was created as intended
        assertEquals(6, event.getInitialFloor());
        assertEquals(10, event.getDestinationFloor());
        assertEquals(MotorState.UP, event.getMotorState());
        assertEquals("Serious", event.getError());
    }

//    @Test
//    void testsForEventHandlerMethods() throws InterruptedException {
//        LocalTime timeTest = LocalTime.parse("12:25:15.12");
//        EventsHandler testEvents = new EventsHandler("12:25:15.12,2,Up,3");
//
//        // Validating EventsHandler methods
//        assertEquals(3, testEvents.getDestinationFloor());
//        assertEquals(2, testEvents.getInitialFloor());
//        assertEquals(true, testEvents.isGoingUp());
//        assertEquals(timeTest, testEvents.getTime());
//    }
}