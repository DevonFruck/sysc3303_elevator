import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Group
 *
 */
public class Scheduler extends Thread {
	
	ArrayList<InputEvents> eventsQueue;
	ArrayList<ElevatorCar> elevatorList;
	
	public Scheduler() {
		eventsQueue = new ArrayList<InputEvents>();
		
		Thread[] floors = new Thread[2];
		floors[0] = new Thread(new Floor(1, this), "floor1");
		floors[1] = new Thread(new Floor(2, this), "floor2");
		
		elevatorList = new ArrayList<ElevatorCar>();
		elevatorList.add(new ElevatorCar(this));
		
		for(int i=0; i<floors.length; i++) {
			floors[i].start();
		}
			
		for(Thread elevator: elevatorList) {
			elevator.start();
		}
	}
	
	// Adds events to queue from floor subsystem
	public synchronized void addEvent(InputEvents InputEventsEvent) {
		eventsQueue.add(InputEventsEvent);
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
	
	// Function for elevator, elevators will poll for 
	// available events when inactive
	public synchronized InputEvents getElevatorEvent() {
		while(eventsQueue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		InputEvents event = eventsQueue.remove(0);
		notifyAll();
		return event;
	}
	
	public synchronized boolean setFloorLights(int floorNum) {
		return false;
	}
	
	// Gets an event to be passed to an elevator
	public synchronized InputEvents getLatestEvent() {
	   while(eventsQueue.size() == 0) {
	      try {
             wait();
          } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
          }
	   }
	   return eventsQueue.remove(0);
	}
	
	public void run() {
	   System.out.println("scheduler");
	   while(true) {
	      
	      InputEvents latestEvent = getLatestEvent();
	      int eventFloors[] = {latestEvent.getInitialFloor(), latestEvent.getDestinationFloor()};
	      
	      //eventsQueue has an item
	      for(ElevatorCar elev: elevatorList) {
	         if(elev.motor.getStatus() == motorStat.UP && latestEvent.isGoingUp()) {
	            if (elev.addFloor(eventFloors)) {
	               break;
	            }
	         }
	         else if (elev.motor.getStatus() == motorStat.IDLE || elev.motor.getStatus() == motorStat.DOWN && !latestEvent.isGoingUp()) {
	            if (elev.addFloor(eventFloors))
                   break;
	         }
	      }
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
