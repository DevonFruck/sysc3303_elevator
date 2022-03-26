package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;

import types.EventsHandler;
import types.InputEvents;
import types.MotorState;
import scheduler.Scheduler;

class SchedulerTest {
    
    Scheduler scheduler = new Scheduler();
    
    /**
     * Tests the event queue in the scheduler.
     */
    @Test
    void acceptEventTest() throws InterruptedException {
        InputEvents event = new EventsHandler("12:25:15.12,6,Up,9, ");
        
        scheduler.acceptEvent(event);
        
        LinkedList<InputEvents> eventList = scheduler.getEventsQueue();
        int queueSize = eventList.size();
        
        assertEquals(1, queueSize);
        assertEquals(eventList.get(0), event);
        
        scheduler.removeQueueElement();
        assertEquals(0, scheduler.getEventsQueue().size());
    }
    
    /**
     * Tests the data returned when elevator is NOT eligible to receive event.
     */
    @Test
    void elevatorNotEligibleForEventTest() throws InterruptedException {
        InputEvents event = new EventsHandler("12:25:15.12,4,Up,9, ");
        scheduler.acceptEvent(event);
        
        String returnData;
        
        // Elevator is going up and gone past the initial floor
        returnData = scheduler.scheduleEvents(5, MotorState.UP);
        assertEquals("NULL", returnData);
        
        // Elevator is going down when event needs to go up
        returnData = scheduler.scheduleEvents(3, MotorState.DOWN);
        assertEquals("NULL", returnData);
        
        // Elevator is going down when needs to go up and at initial floor.
        returnData = scheduler.scheduleEvents(4, MotorState.DOWN);
        assertEquals("NULL", returnData);
        
        //reset scheduler by clearing events
        scheduler.removeQueueElement();
    }
    
    /**
     * Tests the data returned when elevator is eligible to receive event.
     */
    @Test
    void elevatorEligibleForEventTest() throws InterruptedException {
        InputEvents event1 = new EventsHandler("12:25:15.121,4,Up,9, ");
        InputEvents event2 = new EventsHandler("12:25:15.125,7,Down,1, ");
        
        scheduler.acceptEvent(event1);
        scheduler.acceptEvent(event2);
        
        String returnData;
        
        // Elevator is eligible for event 2
        returnData = scheduler.scheduleEvents(7, MotorState.DOWN);
        assertEquals("12:25:15.125,7,DOWN,1,NA", returnData);
        
        // Check if event queue decreased by 1
        assertEquals(1, scheduler.getEventsQueue().size());
    }
}
