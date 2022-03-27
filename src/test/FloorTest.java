package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import floorSubsystem.Floor;
import floorSubsystem.FloorSubsystem;
import scheduler.Scheduler;
import types.EventsHandler;
import types.InputEvents;


class FloorTest {
    static FloorSubsystem subsystem = new FloorSubsystem();
    static Scheduler scheduler = new Scheduler();
    
    @Test
    void FloorsTest() throws InterruptedException {
        InputEvents eventTest = new EventsHandler("15:45:54.11,3,Up,6, ");
                
        Floor floor = new Floor(subsystem, 3);
        
        assertEquals(3, floor.getFloorNumber());
        
        floor.readEvents();
        
        assertTrue(eventTest.equals(floor.getEventList().get(0)));
        
        //floor.requestElevator();        
    }
}
