package elevator;
/**
 * This ElevatorDoor class is for controlling opening and closing of elevator doors
 * @author Group
 */

public class ElevatorDoor {
    
	private boolean doorStatus;
	/**
	 * ElevatorDoor constructor
	 * default the elevator door status to close
	 * @param int floorNum
	 */
	
	ElevatorCar elevator;
	int doorTime = 2000;
	int trivialTime = 6000;
	
	public ElevatorDoor(ElevatorCar elevator) {
		this.doorStatus = false;
		this.elevator = elevator;
	}
	
	/**
	 * openDoor
	 * sets the elevator door status to true
	 */

	public void openDoor() {
		this.doorStatus = true;
	}
	
	public void openCloseDoor(int id, String error) {
	    this.doorStatus = true;
	    
	    try {
            Thread.sleep(doorTime);
            
            if(error.equals("Trivial")) {
                System.out.println("Elevator("+ id+") door is jammed on floor "+elevator.getCurrentFloor()+", door not closing");
                elevator.sendFault(false);
                
                Thread.sleep(trivialTime);
                System.out.println("Elevator("+id+") door is working again after apprx "+((doorTime+trivialTime)/1000)+" seconds, now closing");
            } else {
                System.out.println("Elevator("+ id+") door close");
            }
            this.doorStatus = false;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    
//	    try {
//            Thread.sleep(time);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//	    if(time!=500) {
//	    	System.out.println("Elevator("+id+") door is working again after apprx "+(time/1000)+" seconds, now closing");
//	    }else {
//	    	System.out.println("Elevator("+ id+") door close");
//	    }
	}
	
	/**
	 * closeDoor
	 * sets the elevator door status to false, close the elevator door
	 */
	public void closeDoor() {
		this.doorStatus = false;
	}
	
	/**
	 * doorIsOpen
	 * check for if elevator door is open or not
	 * @return boolean doorIsOpen
	 */
	public boolean doorIsOpen() {
		return doorStatus;
	}
}
