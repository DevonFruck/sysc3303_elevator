package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;

import types.EventsHandler;
import types.InputEvents;
import types.MotorState;

import static config.Config.*;
/**
 * @author L4 Group 9
 *
 */
public class ElevatorCar extends Thread {

	public boolean isRunning;
	private LinkedList<InputEvents> events;

	int id;
	boolean isActive, isDoorOpen, keepSeeking;
	InputEvents currentEvent;
	//ElevatorSubsystem subsys;
	int currentFloor;
	ElevatorButton elevButtons[] = new ElevatorButton[NUM_OF_FLOORS];

	ElevatorMotor motor = new ElevatorMotor(this);
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
		isRunning = true;
		this.events = new LinkedList<>();
		this.keepSeeking = true;
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
			schedulerIp = InetAddress.getByName(DEFAULT);
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
	
	public MotorState getMotorState() {
	    return this.direction;
	}
	
	public int getElevatorID() {
	    return this.id;
	}
	
	public boolean isSeeking() {
        return this.keepSeeking;
    }
	
	public void arrivedAtFloor(int floorNum, MotorState dir) {
	    String message = "arrived," + currentFloor + "," + dir.name();
	    
	    DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), schedulerIp, ELEVATOR_SCHEDULER_PORT);
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void receiveExtraWork(MotorState dir, boolean seek) {
		if(seek) {
			String message = "seekWork," + currentFloor + "," + dir.name();
			try {
				sendPacket = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(DEFAULT), ELEVATOR_SCHEDULER_PORT);
				socket.send(sendPacket);

				byte receiveData[] = new byte[100];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);

				socket.receive(receivePacket);

				String responseData = new String(receivePacket.getData()).trim();

				if(responseData.trim().equals("EMPTY")) {
					keepSeeking = false;
					return;
				}

				if(responseData.trim().equals("NULL")) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return;
				}
				
				InputEvents event = new EventsHandler(responseData);
				events.add(event);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void run() {
		while (isRunning) {
			receiveExtraWork(this.direction, keepSeeking);
			while(events.size()!=0 &&isRunning) {
				System.out.println("ELEVATOR("+id+") ----- Current Floor: "+this.currentFloor+" -----  Work List: "+ events);
				receiveExtraWork(this.direction, keepSeeking);
				boolean initialPicked;
				boolean reachedDistination;
				for(int i=0; i<events.size(); i++) {
					initialPicked = false;
					reachedDistination = false;

					while(!initialPicked && isRunning) {
						if(currentFloor!=events.get(i).getInitialFloor()) {
							initialPicked = false;
							if(events.get(i).getInitialFloor()>this.currentFloor) {
								this.direction = MotorState.UP;
							}else{
								this.direction = MotorState.DOWN;
							}
							currentFloor = motor.moveElevator(currentFloor, id, direction == MotorState.UP ? true : false, events.get(i).getError());
						}else {//in intial floor
							if(events.get(i).getDestinationFloor()>this.currentFloor) {
								this.direction = MotorState.UP;
							}else {
								this.direction = MotorState.DOWN;
							}
							initialPicked = true;
						}
					}

					while(!reachedDistination && initialPicked && isRunning) {
						if(currentFloor!=events.get(i).getDestinationFloor()) {
							reachedDistination = false;
							if(events.get(i).getDestinationFloor()>this.currentFloor) {
								this.direction = MotorState.UP;
							}else{
								this.direction = MotorState.DOWN;
							}
							currentFloor = motor.moveElevator(currentFloor, id, direction == MotorState.UP ? true : false, events.get(i).getError());
						}
						else {//currentFloor matches destination
							System.out.println("______________________________________________________________________");
							System.out.println("Elevator("+id+") PICKED FROM FLOOR --> "+events.get(i).getInitialFloor());
							System.out.println("Elevator("+id+") ARRIVED @ DESTINATION FLOOR --> "+currentFloor);
							elevatorDoor.openCloseDoor(id, events.get(i).getError());
							elevButtons[i].pressButton();
							events.remove(i);
							reachedDistination = true;
							arrivedAtFloor(currentFloor, this.direction);
						}
					}
				}

			}
			this.direction = MotorState.IDLE;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
