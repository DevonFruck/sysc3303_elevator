package scheduler;

import java.util.ArrayList;
import java.util.LinkedList;

import elevator.ElevatorCar;
import floorSubsystem.Floor;
import types.InputEvents;
import types.motorStat;

/**
 * 
 */

/**
 * @author Group
 *
 */
public class Scheduler extends Thread {
   
    final int numOfElevators = 1;
    final int numOfFloors = 10;
	
	//ArrayList<InputEvents> eventsQueue;
	ArrayList<ElevatorCar> elevatorList = new ArrayList<ElevatorCar>();
	ArrayList<Floor> floorList = new ArrayList<Floor>();
	
	//Stops for each elevator
	ArrayList<ArrayList<Integer>> elevatorStops = new ArrayList<ArrayList<Integer>>(numOfElevators);
	
	// Event queues for going up and down
	ArrayList<InputEvents> downQueue = new ArrayList<InputEvents>();
	ArrayList<InputEvents> upQueue = new ArrayList<InputEvents>();
	
	public Scheduler() {
		//eventsQueue = new ArrayList<InputEvents>();
		
		// Initialize Floors
		for(int i=1; i<=numOfFloors; i++) {
           floorList.add(new Floor(i, this));
        }
        for(Thread floor: floorList) {
           floor.start();
        }
       
       
        //Initialize Elevators
		for(int i=1; i<=numOfElevators; i++) {
			elevatorList.add(new ElevatorCar(this));
		}
		for(Thread elevator: elevatorList) {
           elevator.start();
       }
	}
	
	// Adds events to queue from floor subsystem
	public synchronized void addEvent(InputEvents request) {
		if (request.isGoingUp()) {
		   upQueue.add(request);
		} else {
		   downQueue.add(request);
		}
	   
	   //eventsQueue.add(InputEventsEvent);
		notifyAll();
	}
	
	public synchronized boolean elevatorIsApproaching(int floorNumber) {
		try {
			while(elevatorList.get(0).getCurrentFloor() != floorNumber) {
				wait();
			}
			return true;
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	public synchronized boolean setFloorLights(int floorNum) {
		return false;
	}
	
	public synchronized InputEvents getLatestEvent() {
	   while(upQueue.size()==0 & downQueue.size()==0) {
	      try {
	         wait();
          } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
          }
	   }
	   
	   if(upQueue.size() >= downQueue.size()) {
	      return upQueue.remove(0);
	   } else {
	      return downQueue.remove(0);
	   }
	}
	
	public void run() {
	   
	   InputEvents ev = getLatestEvent();
	   
	   

	}
	
	public synchronized void waiting() {
	   try {
	      wait();
       } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
       }
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		Scheduler scheduler = new Scheduler();
		scheduler.start();
	}

}
