package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import floorSubsystem.FloorLamp;

class FloorLampTest {
	
	@Test
	void testFloorLamp() throws InterruptedException {
		//Create a Floor Lamp for floor #1's up direction
		FloorLamp lamp = new FloorLamp();
		
		// verify the default status of the lamp
		assertEquals(false, lamp.getLampStatus());

		//Turn the lamp on and verify the status that it is on is true
		lamp.turnLampOn();
		assertEquals(true, lamp.getLampStatus());

		//Turn the lamp on and verify the status that it is on is true
		lamp.turnLampOff();
		assertEquals(false, lamp.getLampStatus());
		
	}
}
