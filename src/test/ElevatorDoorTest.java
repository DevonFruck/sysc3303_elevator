package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import elevator.ElevatorCar;
import elevator.ElevatorDoor;
import static config.Config.DEFAULT;

class ElevatorDoorTest {
	
	@Test
	void testDoor() throws InterruptedException {
	    ElevatorCar elevator = new ElevatorCar(1, 2, DEFAULT, 867);
		//Create a Elevator Door
		ElevatorDoor door = new ElevatorDoor(elevator);

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
