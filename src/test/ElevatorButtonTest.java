package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import elevatorSubsystem.ElevatorButton;
import elevator.ElevatorLamp;

class ElevatorButtonTest {
	
	@Test
	void testButtonPressed() throws InterruptedException {
		//Create a Elevator Lamp For Floor #1
		ElevatorLamp lamp = new ElevatorLamp(1);
		
		//Create a elevator button
		ElevatorButton button = new ElevatorButton(lamp);
		
		//press the button
		button.press();
		
		//Check to see that the button's lamp is now lit
		assertEquals(true, button.getElevatorLamp().isOn());

		//Also check to see that the button's floor number is 1
		assertEquals(1, button.getElevatorLamp().getFloorNum());
	}
}
