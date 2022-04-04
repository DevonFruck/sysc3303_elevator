package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import elevator.*;
import scheduler.ElevatorSchedulerThread;
import scheduler.Scheduler;
import types.EventsHandler;
import types.MotorState;

class ElevatorCarTest {

    @Test
    void testCar() throws InterruptedException, IOException {
        //Create a Elevator Car
        ElevatorCar car = new ElevatorCar(1,2);
        
        //verify default status
        assertEquals(true, car.isRunning);
        
        //verify default status
        
        assertEquals(MotorState.IDLE, car.getMotorState());
        
        assertEquals(1, car.getElevatorID());
        
        assertEquals(true, car.isSeeking());  
    }
    
    
    @Test
    void ElevatorCarFaultTest() {
        try {
            Scheduler sched = new Scheduler();
            
            EventsHandler event = new EventsHandler("14:05:15.32,3,Up,4,Serious");
            sched.acceptEvent(event);
            
            ElevatorSchedulerThread thread = new ElevatorSchedulerThread(sched, null);
            ElevatorCar car = new ElevatorCar(1,2);
            
            car.start();
            thread.start();
            
            Thread.sleep(10000);
            
            assertFalse(car.getIsRunning());
            
            car.shutDown();
            thread.shutDown();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
