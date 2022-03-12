package scheduler;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import elevator.ElevatorCar;
import elevator.ElevatorSubsystem;
import floorSubsystem.Floor;
import floorSubsystem.FloorSubsystem;
import types.MotorState;
import types.InputEvents;
/**
 * Scheduler Class
 * This class is responsible for handling communication between floors and elevators
 * This class initiates a sub-thread that is responsible for communications on the elevator side
 * and a sub-thread responsible for communications on the floors side
 *
 */
public class Scheduler {
	//A queue that stores a list of requested events that came from floors
	private LinkedList<InputEvents> events;

	/**
	 * Constructor for Scheduler
	 */
	public Scheduler() {
		this.events = new LinkedList<>();
	}

	/**
	 * Getter
	 * @return this.events linked list object
	 */
	public synchronized LinkedList<InputEvents> getEventsQueue() {
		notifyAll();
		return this.events;
	}

	/**
	 * method that allows a floor to add an event to the events queue
	 * @param event coming from the floor
	 * @throws InterruptedException 
	 */
	public synchronized void acceptEvent(InputEvents event) throws InterruptedException {
		this.events.add(event);
		notifyAll();

		// Wait until the event has been processed
		while (this.events.contains(event)) {
			wait();
		}
	}

	/**
	 * This method is for changing the state of the floor to make it move or stay idle
	 * @param currentFloor
	 * @return MotorState (Up/Down/Idle)
	 * @throws InterruptedException
	 */
	public synchronized MotorState seekWork(int currentFloor) throws InterruptedException {
		while (events.size() == 0) {
			wait();
		}

		//Go through list of events and decide the new state of the elevator
		for (InputEvents event: events) {
			if (!event.isElevatorTaken()) {
				event.elevatorTakeEvent();
				// If the elevator is already idle in the current floor, stay idle
				if (event.getInitialFloor() == currentFloor) {
					return MotorState.IDLE;
				}
				//Move to the next event from the queue, either up or down depending on the elevator's current floor
				return currentFloor < event.getInitialFloor() ? MotorState.UP : MotorState.DOWN;	
			}
		}
		//No events in the queue, stay Idle
		return MotorState.IDLE;
	}

	/**
	 * Look for events in the events queue that are coming from the floor we are currently at
	 * @param currentFloor floor which elevator is currently at
	 * @param elevDirection elevator's movement direction
	 * @return True if current floor has an event in the same direction we are going
	 */
	public synchronized boolean lookupEvents(int currentFloor, MotorState elevDirection) throws InterruptedException {
		return this.events.stream().anyMatch(event -> event.getMotorState() == elevDirection && event.getInitialFloor() == currentFloor + (elevDirection == MotorState.UP ? 1 : -1));
	}

	/**
	 * Method for stopping at a floor and take an event from it
	 * @param currentFloor
	 * @param elevDirection direction of movement
	 * @return e A list of taken events
	 * @throws InterruptedException
	 */
	public synchronized List<InputEvents> stopAndTakeEvents(int currentFloor, MotorState elevDirection) throws InterruptedException {
		List<InputEvents> e = this.events.stream().filter(event -> event.getMotorState() == elevDirection && event.getInitialFloor() == currentFloor + (elevDirection == MotorState.UP ? 1 : -1)).collect(Collectors.toList());
		this.events.removeAll(e);

		//wake up any sleeping floor threads
		notifyAll();
		return e;
	}

	/**
	 * Method for finding the max destination floor
	 * @param elevDirection
	 * @return maxFloor, int representing the max destination floor level
	 */
	public synchronized int getMaxDestFloor(MotorState elevDirection) {
		int maxFloor = 0;
		for (InputEvents event: this.events) {
			if ((event.getDestinationFloor() > maxFloor) && event.getMotorState() == elevDirection)
				maxFloor = event.getDestinationFloor();
		}
		return maxFloor;
	}

	/**
	 * Method for finding the min destination floor
	 * @param elevDirection
	 * @return maxFloor, int representing the min destination floor level
	 */
	public synchronized int getMinDestFloor(MotorState elevDirection) {
		int minFloor = 6; //Number of floors in building aka the highest floor
		for (InputEvents event: this.events) {
			if ((event.getDestinationFloor() < minFloor) && event.getMotorState() == elevDirection)
				minFloor = event.getDestinationFloor();
		}
		return minFloor;
	}

	/**
	 * elevator removes the top element in the queue and return it to the elevator
	 * @return first input event in the queue
	 */
	public synchronized InputEvents popTopEvent() {
		InputEvents event = this.events.pop();
		notifyAll();
		return event;
	}

	/**
	 * Removes an event that is being executed by the elevator from the queue
	 * @return A input event
	 */
	public synchronized InputEvents removeEvent(int floorNum) {
		InputEvents event = null;// this.events.pop();
		for (InputEvents e: events) {
			if (e.isElevatorTaken() && e.getInitialFloor()==floorNum) {
				event = e;
				this.events.remove(e);
			}
		}
		notifyAll();
		return event;
	}

	public static void main(String[] args) throws SocketException {
		Scheduler scheduler = new Scheduler();
		
		FloorSchedulerThread floorSchedulerSubThread = new FloorSchedulerThread(scheduler);
		floorSchedulerSubThread.start();
		
		ElevatorSchedulerThread elevatorSchedulerSubThread = new ElevatorSchedulerThread(scheduler);
		elevatorSchedulerSubThread.start();

		
		FloorSubsystem floorSystem = new FloorSubsystem();
		ElevatorSubsystem elevSystem = new ElevatorSubsystem();
	}

	public synchronized String scheduleEvents(MotorState dir, int currentFloor) {
		String direction = "";
		String floors = "";
		for(InputEvents e: this.events) {
			if(dir==MotorState.IDLE) {
				direction = (e.getMotorState().name())+"&";
				floors+=e.getDestinationFloor()+",";
				return direction + floors;
			}
			else if(dir==e.getMotorState() && currentFloor==e.getInitialFloor()){
				direction = (e.getMotorState().name())+"&";
				floors+=e.getDestinationFloor()+",";
			}
		}
		if(floors.equals("")){
			return "NULL";
		}
		return direction + floors;
	}

}





/**
 * elevators: (1): 1   (2): 5,  (3):10
 * queue of events:		from 1 to 5,   from 3 to 1,  from 5 to 2,   from 2 to 4   
 * 
 * Loop through queue
 * Check if elevator a/b/c match initial floor
 * (direction == same || direction == idle)
 * 
 * No eligible events return "NULL"						
 */

/**
 * arrived(currentFloor, direction)
 * 
 * arrived, cuurentFloor, Direction
 * 
 * Scheduler class:
 * array booleans floors[floors]  --> []
 * 
 * notifyFloor(currentFloor, direction){
 * 		
 * }
 * 
 */



