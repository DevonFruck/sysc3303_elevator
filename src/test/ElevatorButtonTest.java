package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import elevator.ElevatorButton;

class ElevatorButtonTest {
	
	@Test
	void testButtonPressed() throws InterruptedException {		
		//Create a elevator button
		ElevatorButton button = new ElevatorButton(1);
		
		// verify the default status of the button
		assertEquals(false, button.buttonStatus());
		
		//press the button
		button.pressButton();
		
		//Check to see that the button's lamp is now lit
		assertEquals(true, button.buttonStatus());

		//press the button
		button.pressButton();
		
		//Check to see that the button's lamp is now unlit
		assertEquals(false, button.buttonStatus());
	}
}
