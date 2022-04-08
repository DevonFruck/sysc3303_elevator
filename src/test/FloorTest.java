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
        InputEvents eventTest = new EventsHandler("12:25:15.12,1,Up,15, ");
                
        Floor floor = new Floor(subsystem, 1);
        
        assertEquals(1, floor.getFloorNumber());
        
        floor.readEvents();
        
        assertTrue(eventTest.equals(floor.getEventList().get(0)));
        
        //floor.requestElevator();        
    }
}
