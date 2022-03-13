package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.net.SocketException;
import java.time.LocalTime;
import java.util.ArrayList;

import elevator.ElevatorCar;
import floorSubsystem.FloorSubsystem;
import floorSubsystem.Floor;
import scheduler.Scheduler;
import types.EventsHandler;
import types.InputEvents;
import types.MotorState;

/**
 * Junit 5 testing for the all the data to be read in the system.
 * @author L4 Group 9
 */

class DataTest{

    Scheduler scheduler = new Scheduler();

    @Test
    void testsForElevatorCommunication() throws InterruptedException{
    // Initiating all the classes
        FloorSubsystem subsystem;
        try {
            subsystem = new FloorSubsystem();
            Floor floor = new Floor(subsystem, 1); 
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Thread.sleep(2000);

        LocalTime timeTest = LocalTime.parse("12:25:15.12");
        EventsHandler testEvents = new EventsHandler("12:25:15.12,2,Up,3");

    }

    @Test
    void FloorTest() throws InterruptedException {
        FloorSubsystem subsystem;
        try {
            subsystem = new FloorSubsystem();
            Floor floor = new Floor(subsystem, 1);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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