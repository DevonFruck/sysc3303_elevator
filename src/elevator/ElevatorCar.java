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

import static config.Config.*;
/**
 * @author L4 Group 9
 *
 */
public class ElevatorCar extends Thread {

	// floors the elevator must visit
	ArrayList<Integer> workList = new ArrayList<Integer>();
	ArrayList<Integer> workListInitialFloor = new ArrayList<Integer>();

	int id;
	boolean isActive, isDoorOpen, keepSeeking;
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


	public void receiveExtraWork(MotorState dir, boolean seek) {
		if(seek) {
			String sendData;
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

				String[] parsedData = responseData.split("&");
				if(this.direction==MotorState.IDLE) {
					if(parsedData[0] == "UP" ) {
						this.direction = MotorState.UP;
					} 
					else {
						this.direction = MotorState.DOWN;
					}
				}				

				//adds the floors to the work list
				String parsedFloors[] = parsedData[1].split(",");
				workList.add(Integer.parseInt(parsedFloors[0]));
				workListInitialFloor.add(Integer.parseInt(parsedFloors[1]));



			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void run() {
		DatagramPacket sendPacket, receivePacket;
		int minFloorDestination = 0;
		int maxFloorDestination = 0;

		while (true) {
			receiveExtraWork(this.direction, keepSeeking);
			while(workList.size()!=0) {
				System.out.println("ELEVATOR("+id+") ----- Current Floor: "+this.currentFloor+" -----  Work List: "+ workList);
				receiveExtraWork(this.direction, keepSeeking);
				boolean initialPicked;
				boolean reachedDistination;
				for(int i=0; i<workList.size(); i++) {
					initialPicked = false;
					reachedDistination = false;

					while(!initialPicked) {
						if(currentFloor!=workListInitialFloor.get(i)) {
							initialPicked = false;
							if(workListInitialFloor.get(i)>this.currentFloor) {
								this.direction = MotorState.UP;
							}else{
								this.direction = MotorState.DOWN;
							}
							currentFloor = motor.moveElevator(currentFloor, id, direction == MotorState.UP ? true : false);
						}else {//in intial floor
							if(workList.get(i)>this.currentFloor) {
								this.direction = MotorState.UP;
							}else {
								this.direction = MotorState.DOWN;
							}
							initialPicked = true;
						}
					}

					while(!reachedDistination && initialPicked) {
						if(currentFloor!=workList.get(i)) {
							reachedDistination = false;
							if(workList.get(i)>this.currentFloor) {
								this.direction = MotorState.UP;
							}else{
								this.direction = MotorState.DOWN;
							}
							currentFloor = motor.moveElevator(currentFloor, id, direction == MotorState.UP ? true : false);
						}
						else {//currentFloor matches destination
							System.out.println("______________________________________________________________________");
							System.out.println("Elevator("+id+") PICKED FROM FLOOR --> "+workListInitialFloor.get(i));
							System.out.println("Elevator("+id+") ARRIVED @ DESTINATION FLOOR --> "+currentFloor);
							workList.remove(i);
							workListInitialFloor.remove(i);
							elevButtons[i].pressButton();
							elevatorDoor.openCloseDoor();
							reachedDistination = true;
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
