package scheduler;

import java.net.SocketException;
import java.util.LinkedList;
import elevator.ElevatorSubsystem;
import floorSubsystem.FloorSubsystem;
import types.MotorState;
import types.InputEvents;
import display.GUI;
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
	
	public InputEvents removeQueueElement() {
	    return events.remove(0);
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
		
		GUI display = new GUI();
		
		FloorSchedulerThread floorSchedulerSubThread = new FloorSchedulerThread(scheduler);
		floorSchedulerSubThread.start();
		
		ElevatorSchedulerThread elevatorSchedulerSubThread = new ElevatorSchedulerThread(scheduler, display);
		elevatorSchedulerSubThread.start();

		
		Thread floorSystem = new Thread(new FloorSubsystem());
		floorSystem.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		@SuppressWarnings("unused")
        ElevatorSubsystem elevSystem = new ElevatorSubsystem();
	}

	
	//Pick up event on the way
	public synchronized String scheduleEvents(int currentFloor, MotorState dir) {
		String time="";
		String initialFloor="";
		String direction = "";
		String floors = "";
		String errorCode = "";
		if(this.events.isEmpty()) {
			if(eventsEmptyCount<3) {
				eventsEmptyCount++;
				return "NULL";
			}else {
				return "EMPTY";
			}
		}
		
		// Give first event to an idle elevator
		for(InputEvents e: this.events) {
			if(dir==MotorState.IDLE) {
				time += e.getTime().toString()+",";
				direction = (e.getMotorState().name())+",";
				floors+=e.getDestinationFloor()+",";
				initialFloor += e.getInitialFloor()+",";
				errorCode += e.getError();
				this.events.remove(e);
				return time + initialFloor+ direction + floors  + errorCode;
			}
			
			// If elevator is not idle, elevator must be going in same direction and at the initial event floor
			else if(currentFloor==e.getInitialFloor() && dir==e.getMotorState()){
				time += e.getTime().toString()+",";
				direction = (e.getMotorState().name())+",";
				floors+=e.getDestinationFloor()+",";
				initialFloor += e.getInitialFloor()+",";
				errorCode += e.getError();
				this.events.remove(e);
				return time + initialFloor+ direction + floors  + errorCode;
			}
		}
		if(floors.equals("")){
			return "NULL";
		}
		return "";
	}
	
	
	//Pick up event slightly past the floor you're moving to. Usually invoked when
	//Elevator is moving to the initial floor of its only active event.
	public synchronized String pickUpFartherEvent(int elevatorId, int currentFloor, MotorState dir) {
	    String time="";
        String initialFloor="";
        String direction = "";
        String floors = "";
        String errorCode = "";
        
	    for(InputEvents e: this.events) {
	        int floorDiff;
	        if(dir == MotorState.UP) {
	            floorDiff = currentFloor - e.getInitialFloor();
	        } else {
	            floorDiff = e.getInitialFloor() - currentFloor;
	        }
	        
	        // Only accept the event if it's within reasonable distance from initial event
	        boolean goingUp = (dir == MotorState.UP);
	        if(floorDiff > 0 && floorDiff <=4 && goingUp == e.isGoingUp()) {
	            time += e.getTime().toString()+",";
                direction = (e.getMotorState().name())+",";
                floors+=e.getDestinationFloor()+",";
                initialFloor += e.getInitialFloor()+",";
                errorCode += e.getError();
                
                System.out.println("Elevator " +elevatorId+ "took event: " + e);
                this.events.remove(e);
                return time + initialFloor+ direction + floors  + errorCode;
	        }
	    }
        return "NULL";
	}
}
