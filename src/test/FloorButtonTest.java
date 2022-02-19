package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import floorSubsystem.FloorButton;

class FloorButtonTest {

	/**
	 * Tests for the floor button
	 */
	@Test
	void testCreateFloorButton() {
		FloorButton button = new FloorButton();

		// verify the default status of the button and lamp
		assertEquals(false, button.checkButton());
		assertEquals(false, button.checkLamp());
		
		// verify button status is true and lamp is turned on
		button.pressButton();
		assertEquals(true, button.checkButton());
		assertEquals(true, button.checkLamp());

		// verify button status is false and lamp is turned off
		button.pressButton();
		assertEquals(false, button.checkButton());
		assertEquals(false, button.checkLamp());

	}
	

}
