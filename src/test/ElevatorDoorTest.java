package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import elevator.ElevatorDoor;

class ElevatorDoorTest {
	
	@Test
	void testDoor() throws InterruptedException {
		//Create a Elevator Door
		ElevatorDoor door = new ElevatorDoor();

		//verify default status
		assertEquals(false, door.doorIsOpen());

		//verify openDoor method
		door.openDoor();
		assertEquals(true, door.doorIsOpen());

		//verify closeDoor method
		door.closeDoor();
		assertEquals(false, door.doorIsOpen());

	}
}
