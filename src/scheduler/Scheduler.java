package scheduler;

import java.util.ArrayList;
import java.util.Collections;

import elevator.ElevatorCar;
import floorSubsystem.Floor;
import types.InputEvents;
import types.motorStat;

/**
 * @author L4 Group 9
 *
 */
public class Scheduler extends Thread {
   
    public final int numOfElevators = 2;
    public final int numOfFloors = 5;
    
    public ArrayList<ArrayList<Integer>> elevatorStops = new ArrayList<ArrayList<Integer>>(numOfElevators);
	
	ArrayList<InputEvents> eventsQueue = new ArrayList<InputEvents>();;
	ArrayList<ElevatorCar> elevatorList = new ArrayList<ElevatorCar>();
	
	ArrayList<Floor> floorList = new ArrayList<Floor>();
	
	/**
	 * Constructor for the Scheduler class. Creates all floors and elevators
	 * as threads and starts them.
	 */
	public Scheduler() {
	   
	    // Initialize elevator stops ArrayList
	    for(int j=0; j<numOfElevators; j++) {
	       elevatorStops.add(new ArrayList<Integer>());
	    }
	   
	   
		// Initialize Floors
        for(int i=1; i<=numOfFloors; i++) {
           floorList.add(new Floor(i, this));
        }
        for(Thread floor: floorList) {
           floor.start();
        }
       
       
        //Initialize Elevators
        for(int i=0; i<numOfElevators; i++) {
            elevatorList.add(new ElevatorCar(this, i));
        }
        for(Thread elevator: elevatorList) {
           elevator.start();
        }
	}
	
	/**
	 * Adds floor numbers from a floor request into an elevators active queue.
	 * 
	 * @param inputReq The floor request.
	 * @param id The ID of the elevator.
	 * @param goingUp Specifies if the elevator is going up or not.
	 */
	public synchronized void addToElev(InputEvents inputReq, int id, boolean goingUp) {
	   ArrayList<Integer> stops = elevatorStops.get(id);
	   
	   // prevent duplicates in the list
	   if (!stops.contains(inputReq.getInitialFloor()))
	      stops.add(inputReq.getInitialFloor());
	   if (!stops.contains(inputReq.getDestinationFloor()))
	      stops.add(inputReq.getDestinationFloor());
	   
	   // Sort the list ascending or descending if elevator is going up or down
	   if(goingUp) {
	      System.out.println("going up!");
	      Collections.sort(stops);
	   } else {
	      System.out.println("going down!");
	      Collections.reverse(stops);
	      //System.out.println(stops);
	   }
	   
	   System.out.println("Elevator ID: " + id + " going " + elevatorList.get(id).getMotor().getStatus());
	   System.out.println(stops);
	}
	
	/**
	 * Dummy function for the floor threads to idle. It is not required to
	 * do anything after it issues its events until we implement UDP.
	 */
	public synchronized void floorWait() {
	   try {
          wait();
       } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }
	}
	
	/**
	 * Schedules the input request to an appropriate elevator.
	 * 
	 * @param inputReq The input request from a floor.
	 */
	public synchronized void planEvent(InputEvents inputReq) {
	   for(int i=0; i<numOfElevators; i++) {
	      ElevatorCar elev = elevatorList.get(i);
	      
	      if(elev.getMotor().getStatus() == motorStat.IDLE) {
	         motorStat direction = inputReq.isGoingUp() ? motorStat.UP : motorStat.DOWN;
	         
	         elevatorList.get(i).getMotor().setStatus(direction);
	         addToElev(inputReq, i, inputReq.isGoingUp());
	         break;
	      }
	      
	      else if(inputReq.isGoingUp() && elev.getMotor().getStatus() == motorStat.UP && elev.getCurrentFloor() < inputReq.getInitialFloor()) {
	         addToElev(inputReq, i, true);
	         break;
	      }
	      
	      else if(!inputReq.isGoingUp() && elev.getMotor().getStatus() == motorStat.DOWN && elev.getCurrentFloor() > inputReq.getInitialFloor()) {
	         addToElev(inputReq, i, false);
	         break;
	      }
	   }
	}
	
	/**
	 * Adds an event to the waiting event queue.
	 * 
	 * @param inputReq Input request from a floor.
	 */
	public synchronized void addEvent(InputEvents inputReq) {
		eventsQueue.add(inputReq);
		notifyAll();
	}
	
	/**
	 * Returns the floor number that the elevator with the passed
	 * in ID must go to next.
	 * 
	 * @param id ID of the elevator.
	 * @return floor number the elevator must go to next.
	 */
	public synchronized int getNextDest(int id) {
	   while(elevatorStops.get(id).isEmpty()) {
	      try {
	         elevatorList.get(id).getMotor().setStatus(motorStat.IDLE);
             wait();
          } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
          }
	   }
	   return elevatorStops.get(id).get(0);
	}
	
	/**
	 * Invoked by an ElevatorCar when it has arrived at its
	 * destination floor.
	 * 
	 * @param id The ID of the ElevatorCar
	 */
	public synchronized void elevatorArrived(int id) {
	   int currFloor = elevatorList.get(id).getCurrentFloor();
	   
	   if(elevatorStops.get(id).get(0) == currFloor) {
	      elevatorStops.get(id).remove(0);
	      floorList.get(currFloor-1).elevatorArrived();
	      notifyAll();
	   }
	}
	
	
	/**
	 * Returns the first element in the event queue. Event queue
	 * refers to the events that haven't been assigned to an elevator.
	 * @return
	 */
	public synchronized InputEvents getLatestEvent() {
	   while(eventsQueue.isEmpty()) {
	      try {
	         wait();
          } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
          }
	   }
	   
	   return eventsQueue.remove(0);
	}
	
	
	@Override
	public void run() {
	   while(true) {
	      InputEvents request = getLatestEvent();
	      planEvent(request);
	   }
	}
	
	
	/**
	 * The program begins here. Creates the scheduler.
	 * @param args
	 */
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		scheduler.start();
	}

}
