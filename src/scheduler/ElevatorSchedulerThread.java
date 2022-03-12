package scheduler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import types.InputEvents;
import types.MessageParser;
import types.MotorState;

/**
 * This thread is created for elevator request
 */
public class ElevatorSchedulerThread extends Thread {
//	private MessageParser parsedMessage;
	private String message;
	private InetAddress sourceAdd;
	private int sourcePort;
	private Scheduler scheduler;
	
	private int actualCurrentFloor;
	private boolean moreEvents;

	/**
	 * Creates a thread for the elevator operation
	 * @param parsed The operation that is to be done
	 * @param sourceAddress The address making the request
	 * @param sourcePort The port making the request
	 * @param scheduler The scheduler to make the request to
	 */
	public ElevatorSchedulerThread(InetAddress sourceAddress, int sourcePort, Scheduler scheduler) {
		this.sourceAdd = sourceAddress;
		this.sourcePort = sourcePort;
		this.scheduler = scheduler;
		
		this.actualCurrentFloor = 0;
		this.moreEvents = false;
	}
	
	/**
	 * This method calls the appropriate scheduler message and returns whatever needed via UDP 
	 */
	public void run() {
		DatagramPacket sendPacket, receivePacket;
		DatagramSocket socket;
		int minFloorDestination = 0;
		int maxFloorDestination = 0;
		
		int currentFloor;
		MotorState currentState;
		
		
		
		try {
			while (true) {
				byte data[] = new byte[100];
				receivePacket = new DatagramPacket(data, data.length);
				socket = new DatagramSocket();
				socket.receive(receivePacket);
	            String[] status = parseData(receivePacket.getData().toString());
	            currentFloor = Integer.parseInt(status[0]);
	            currentState = getState(status[1]);
	            
	            
	            
				if (MotorState.IDLE==currentState) {
					MotorState direction = this.scheduler.seekWork(currentFloor);
					this.finishRequest(direction.toString().getBytes());	
				}
				
				//if the state goes from Stop to Stop, then that means that the elevator is currently
				//at the same floor as the the floor the request was made from 
				if (currentState == MotorState.IDLE) {
					//if thats the case remove the first element from the schedules queue and add it to list of events to be completed
					
					
				}
				
				//Travels upwards and checks on each level if there is any additional requests that it could take on
				if (MotorState.UP==currentState) {
					
//					actualCurrentFloor = getLiveFloorUpdates();
					while (actualCurrentFloor <= maxFloorDestination) {
						
						
						if (actualCurrentFloor == maxFloorDestination) {
							break;
						}
						
//						moreEvents = checkForMoreEvents();
						if (moreEvents) {
//							try {
//								getEventsOnTheWay(currentState); function to keep checking the scheduler queue for events that are on the elvators way
//							} catch (ClassNotFoundException e) {
//							} catch (IOException e) {
//							}

							//ascends the floor, waits based on calculations from iteration 0
//							this.elevatorMotor.ascendFloor(this.actualCurrentFloor, this.idOfCar);update the actual current floor when moving up
							
							//Open and close the door of the elevator
//							this.elevatorDoor.openAndCloseDoor(this.idOfCar);
							
						}
						else {
							//ascends the floor, waits based on calculations from iteration 0
//							this.elevatorMotor.ascendFloor(this.actualCurrentFloor, this.idOfCar);
						}
//						this.actualCurrentFloor++;
					}
				}
				
				//Travels downwards and checks on each level if there is any additional requests that it could take on
			else if (MotorState.DOWN == currentState) {
					
					if(!scheduler.getEventsQueue().isEmpty()) {
						minFloorDestination = scheduler.getMinDestFloor(currentState);
					}
					while (actualCurrentFloor >= minFloorDestination) {
						
						//Implement elevator movement
						
						if (actualCurrentFloor == minFloorDestination) {
							break;
						}
						
						//Check for more events on the way of elevator
//						moreEvents = checkForMoreEvents();
						if (moreEvents) {
//							try {
//								getEventsOnTheWay(currentState);
//							} catch (ClassNotFoundException e) {
//							} catch (IOException e) {
//							}

							//descends the floor, waits based on calculations from iteration 0
//							this.elevatorMotor.descendFloor(this.actualCurrentFloor, this.idOfCar);

							//Open and close the door of the elevator
//							this.elevatorDoor.openAndCloseDoor(this.idOfCar);
							
						}
						else {
							//descends the floor, waits based on calculations from iteration 0
//							this.elevatorMotor.descendFloor(this.actualCurrentFloor, this.idOfCar);
						}
	
						actualCurrentFloor--;
					}
				}
				
				//now that all work is done, go back to being Stop
				currentState = MotorState.IDLE;
			}
		}catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		}
		
		

	public int moveElevator(boolean isUp) {
		if(isUp) {
			return actualCurrentFloor++;
		}else {
			return actualCurrentFloor--;
		}
	}
	
	/**
	 * Return the Id of the desired elevator to send the event to
	 * @return
	 */
	public int scheduleElevator() {
		
	}
	
	public String[] parseData(String scheduler_data){
        String[] tokens = scheduler_data.split(",");

        return tokens;
    }
	
	public MotorState getState(String str) {
		if(str.equals("IDLE")) {
			return MotorState.IDLE;
		}else if(str.equals("UP")) {
			return MotorState.UP;
		}else {
			return MotorState.DOWN;
		}
	}

	private void finishRequest(byte[] msg) throws IOException {
		DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, this.sourceAdd, this.sourcePort);
		System.out.println("SCHEDULER --> Prepared response packet to send to elevator");
		DatagramSocket socket = new DatagramSocket();
		socket.send(sendPacket);
		System.out.println("SCHEDULER --> Response Packet sent to floor");
		socket.close();
	}

}	

