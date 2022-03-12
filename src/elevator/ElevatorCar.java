package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import types.InputEvents;
import types.MotorState;

//import static config.Config.SCHEDULER_IP;
//import static config.Config.NUM_OF_FLOORS;
import static config.Config.*;
/**
 * @author L4 Group 9
 *
 */
public class ElevatorCar extends Thread {
	
    // floors the elevator must visit
	ArrayList<Integer> workList = new ArrayList<Integer>();
	
	int id;
	boolean isActive, isDoorOpen;
	InputEvents currentEvent;
	//ElevatorSubsystem subsys;
	int currentFloor;
	ElevatorButton elevButtons[] = new ElevatorButton[NUM_OF_FLOORS];
	
	ElevatorMotor motor = new ElevatorMotor();
	MotorState currentState = MotorState.IDLE;
	MotorState direction = MotorState.IDLE;
	
	DatagramPacket receivePacket, sendPacket;
    DatagramSocket socket;
    InetAddress schedulerIp;
    ElevatorDoor elevatorDoor = new ElevatorDoor();
	
	/**
	 * Constructor for the ElevatorCar class.
	 * Defines default values for the elevator.
	 * 
	 * @param scheduler The elevator scheduler the class interacts with.
	 */
	public ElevatorCar(int id, int initialFloor) {
	    this.id = id;
		isActive = false;
		currentEvent = null;
		currentFloor = initialFloor;
		isDoorOpen = false;
		motor.setStatus(MotorState.IDLE);
		
		for (int i=0; i<NUM_OF_FLOORS; i++) {
			elevButtons[i] = new ElevatorButton(i+1);
		}
		
		
		try {
            schedulerIp = InetAddress.getByName(SCHEDULER_IP);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	/**
	 * Getter for the elevator's current event.
	 * 
	 * @return its in progress event.
	 */
	public InputEvents getCurrentEvent() {
		return currentEvent;
	}
	
	// Returns final floor it will arrive to
//	public Integer getFinalFloor() {
//	   return floors.get(floors.size()-1);
//	}
	
//	public void receiveEvent() throws IllegalArgumentException, IOException {
////		String data[];
////		data = subsys.getFromScheduler();
////		
////        if (Integer.parseInt(data[0]) != this.id) {
////        	throw new IllegalArgumentException("Data sent to wrong elevator");
////        }
////        
////        int floorNum = Integer.parseInt(data[1]);
////        //U,D,I,O
////        String state = data[2];
////        this.elevatorMovement(floorNum);
//		int floor = subsys.getNextFloor(this.id);
//		elevatorMovement(floor);
//	}
	
	/**
	 * Getter for the elevator's current floor.
	 * 
	 * @return The number of the floor the elevator is at.
	 */
	public int getCurrentFloor() {
		return currentFloor;
	}
	
	/**
	 * Getter for if elevator is active. Deprecated.
	 * 
	 * @return boolean for if it is active
	 */
//	public boolean getIsActive() {
//		return isActive;
//	}
//	
//	public ElevatorMotor getMotor() {
//		return motor;
//	}
	
//	public MotorState getMotorState() {
//	    return this.currentState;
//	}
	
	/**
	 * Setter for setting the active state of elevator.
	 * 
	 * @param val boolean state for elevator.
	 */
//	public void setIsActive(boolean val) {
//	   isActive = val;
//	}
	
	
	public void receiveExtraWork(MotorState dir) {
	    String sendData;
	    if(dir == MotorState.UP) {
	        //TODO: call scheduler to stop and take up events
	    } else {
	      //TODO: call scheduler to stop and take down events
	    }
	    
	    //sendPacket = new DatagramPacket();
	    //sendPacket = new DatagramPacket();
	    //socket.send(data);
	    
	    byte receiveData[] = new byte[100];
	    receivePacket = new DatagramPacket(receiveData, receiveData.length);
	    try {
            socket.receive(receivePacket);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    
	    String receivedData = new String(receivePacket.getData());
	    
	    if(receivedData == "null") {
	        return;
	    }
	    
	    String parsedData[] = receivedData.split(",");
	    
	    for(String floor : parsedData) {	        
	        this.workList.add(Integer.parseInt(floor));
	    }
	}
	
	public void updateWorkList() {
	    for(int i=0; i<workList.size(); i++) {
	        if(currentFloor == workList.get(i)) {
	            workList.remove(i);
	            System.out.println("Elevator ID: " +id+ " arrived at floor " + currentFloor);
	            elevButtons[i].pressButton();
	            elevatorDoor.openCloseDoor();
	        }
	    }
	}
	
	
	public void run() {
        DatagramPacket sendPacket, receivePacket;
        int minFloorDestination = 0;
        int maxFloorDestination = 0;

        try {
            while (true) {
                if (MotorState.IDLE==this.direction) {
                    //socket = new socket();
                    String message = "requestWork," + currentFloor;
                    sendPacket = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(SCHEDULER_IP), ELEVATOR_SCHEDULER_PORT);
                    socket.send(sendPacket);
                    byte[] schedulerData = new byte[1024];
                    receivePacket = new DatagramPacket(schedulerData, schedulerData.length);
                    //System.out.println("ELEVATOR #" + this.carID + ": waiting to receive from scheduler");
                    socket.receive(receivePacket);
                    
                    String parsedData[] = new String(receivePacket.getData()).split("&");
                        
                    if(parsedData[0] == "UP") {
                        this.direction = MotorState.UP;
                    } 
                    else {
                        this.direction = MotorState.DOWN;
                    }
                    
                    //adds the floors to the work list
                    String parsedFloors[] = parsedData[1].split(",");
                    for(int i=0; i<parsedFloors.length; i++) {
                        workList.add(Integer.parseInt(parsedFloors[i]));
                    }
                    
                    
                    //this.currentState = MotorState.valueOf(new String(receivePacket.getData()).trim());
                    //socket.close();
                }
                
                while(!workList.isEmpty()) {
                    currentFloor = motor.moveElevator(currentFloor, id, direction == MotorState.UP ? true : false);
                    
                    if(workList.contains(currentFloor)) {
                        updateWorkList();
                        
                        String message = "arrived," + currentFloor;
                        sendPacket = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(SCHEDULER_IP), ELEVATOR_SCHEDULER_PORT);
                        socket.send(sendPacket);
                        //byte[] schedulerData = new byte[1024];
                        //TODO: acknowledgement of receiving?
                        
                    }
                    //request any extra work we can get on the way
                    receiveExtraWork(this.direction);
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
	}
}
