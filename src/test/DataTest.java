package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;

import elevator.ElevatorCar;
import floorSubsystem.Floor;
import scheduler.Scheduler;
import types.EventsHandler;
import types.InputEvents;
import types.MotorState;
import types.motorStat;

/**
 * Junit 5 testing for the all the data to be read in the system.
 * @author L4 Group 9
 */

class DataTest{

    Scheduler scheduler = new Scheduler();

    @Test
    void testsForElevatorCommunication() throws InterruptedException{
        // Initiating all the classes
        Floor floor = new Floor(1, scheduler); 

        Thread.sleep(2000);

        //LocalTime timeTest = LocalTime.parse("12:25:15.12");
        //EventsHandler testEvents = new EventsHandler("12:25:15.12,2,Up,3");
        //InputEvents input = elevatorCar.getCurrentEvent();

        // Validating the floor methods
        assertEquals(1, floor.getFloorNumber());


        // Validating the values for the methods
        //assertFalse(scheduler.setFloorLights(1));
        //assertTrue(scheduler.elevatorIsApproaching(1));
    }

    @Test
    void FloorTest() throws InterruptedException {
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
        
        ElevatorCar elevator = new ElevatorCar(scheduler, 0);
        
        assertEquals(MotorState.IDLE, elevator.getMotor().getStatus());
        
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