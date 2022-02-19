package tests;
 

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import elevatorSubsystem.ElevatorLamp;

class ElevatorLampTest {
	
	@Test
	void testLamp() throws InterruptedException {
		//Create a Elevator Lamp For Floor #1
		ElevatorLamp lamp = new ElevatorLamp(1);
		
		//turn on the lamp
		lamp.turnOn();
		
		//Check to see that the lamp is now on
		assertEquals(true, lamp.isOn());
		
		//turn off the lamp
		lamp.turnOff();
		
		//Check to see that the lamp is now off
		assertEquals(false, lamp.isOn());

		//Also check to see that the lamps's floor number is 1
		assertEquals(1, lamp.getFloorNum());
	}
}
