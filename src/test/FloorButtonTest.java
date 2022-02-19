package test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import floorSubsystem.FloorButton;
import floorSubsystem.FloorLamp;
import schedulerSubsystem.Scheduler;
import dataSystems.ElevatorDirection;

class FloorButtonTest {

	/**
	 * Tests creating an up floor button
	 */
	@Test
	void testCreateFloorButtonUp() {
		FloorButton button = new FloorButton(new FloorLamp(1, ElevatorDirection.Up), new Scheduler());
		assertEquals(ElevatorDirection.Up, button.getButtonDirection());
	}
	
	/**
	 * Tests creating a down button
	 */
	@Test
	void testCreateFloorButtonDown() {
		FloorButton button = new FloorButton(new FloorLamp(1, ElevatorDirection.Down), new Scheduler());
		assertEquals(ElevatorDirection.Down, button.getButtonDirection());
	}

}
