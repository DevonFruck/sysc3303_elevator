package test;

import static config.Config.DEFAULT;
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
        ElevatorCar car = new ElevatorCar(1,2, DEFAULT, 990);
        
        //verify default status
        assertEquals(true, car.getIsRunning());
        
        //verify default status
        
        assertEquals(MotorState.IDLE, car.getMotorState());
        
        assertEquals(1, car.getID());
        
        car.shutDown();
    }
    
    
    @Test
    void ElevatorCarFaultTest() {
        try {
            Scheduler sched = new Scheduler();
            
            EventsHandler event = new EventsHandler("14:05:15.32,2,Up,9,Serious");
            sched.acceptEvent(event);
            
            ElevatorSchedulerThread thread = new ElevatorSchedulerThread(sched, null, DEFAULT, 9998, 9997);
            ElevatorCar car = new ElevatorCar(1,1, DEFAULT, 9998);
            
            car.start();
            thread.start();
            
            Thread.sleep(15000);
            
            assertFalse(car.getIsRunning());
            
            car.shutDown();
            thread.shutDown();
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
