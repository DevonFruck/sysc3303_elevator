package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import elevator.*;
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
        
        //assertEquals()
        
        
    }

}
