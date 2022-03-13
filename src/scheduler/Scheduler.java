package scheduler;

import java.net.SocketException;
import java.util.LinkedList;
import elevator.ElevatorSubsystem;
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
	
	
	int eventsEmptyCount = 0;

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
	}


	public static void main(String[] args) throws SocketException {
		Scheduler scheduler = new Scheduler();
		
		FloorSchedulerThread floorSchedulerSubThread = new FloorSchedulerThread(scheduler);
		floorSchedulerSubThread.start();
		
		ElevatorSchedulerThread elevatorSchedulerSubThread = new ElevatorSchedulerThread(scheduler);
		elevatorSchedulerSubThread.start();

		
		FloorSubsystem floorSystem = new FloorSubsystem();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ElevatorSubsystem elevSystem = new ElevatorSubsystem();
	}

	public synchronized String scheduleEvents(int currentFloor, MotorState dir) {
		String initialFloor="";
		String direction = "";
		String floors = "";
		if(this.events.isEmpty()) {
			if(eventsEmptyCount<3) {
				eventsEmptyCount++;
				return "NULL";
			}else {
				return "EMPTY";
			}
			
		}
		for(InputEvents e: this.events) {
			if(dir==MotorState.IDLE) {
				direction = (e.getMotorState().name())+"&";
				floors+=e.getDestinationFloor()+",";
				initialFloor += e.getInitialFloor()+",";
				this.events.remove(e);
				return direction + floors + initialFloor;
			}
			else if(currentFloor==e.getInitialFloor() && dir==e.getMotorState()){
				direction = (e.getMotorState().name())+"&";
				floors+=e.getDestinationFloor()+",";
				initialFloor += e.getInitialFloor()+",";
				this.events.remove(e);
				return direction + floors + initialFloor;
			}
		}
		if(floors.equals("")){
			return "NULL";
		}
		return "";
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



