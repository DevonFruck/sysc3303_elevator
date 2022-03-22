package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import types.MotorState;

import static config.Config.*;

/**
 * This thread is created for elevator request
 */
public class ElevatorSchedulerThread extends Thread {	

	private Scheduler scheduler;

	private DatagramPacket receivePacket, sendPacket;
	private DatagramSocket socket;
	InetAddress floorSubsysIp;
	
	/**
	 * Creates a thread for the elevator operation
	 * @param scheduler The scheduler to make the request to
	 */
	public ElevatorSchedulerThread(Scheduler scheduler) {
		this.scheduler = scheduler;
		try {
			this.socket= new DatagramSocket(ELEVATOR_SCHEDULER_PORT);
			this.floorSubsysIp = InetAddress.getByName(DEFAULT);
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method calls the appropriate scheduler message and returns whatever needed via UDP 
	 */
	public void run() {
		while(true) {
			byte[] data = new byte[100];

			receivePacket = new DatagramPacket(data, data.length);
			try {
				socket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String receivedData = new String(receivePacket.getData());
			String parsedData[] = receivedData.trim().split(",");
			
			String command = parsedData[0];
			int floorNum = Integer.parseInt(parsedData[1]);
			String direction = parsedData[2];

			switch (command) {
			
			// An elevator has arrived at a new floor and is requesting more jobs
			case "seekWork":
				this.handleSeekWork(floorNum, direction);
				break;
			
			// An elevator has arrived at one of its target floors
			case "arrived":
			    this.handleArrived(floorNum, direction);
			    break;
			}
		}
	}

	private void handleSeekWork(int currentFloor, String direction) {
		try {
			String response;
			if(direction.equals("IDLE")) {
				response = scheduler.scheduleEvents(currentFloor, MotorState.IDLE);
			}else if(direction.equals("UP")) {
				response = scheduler.scheduleEvents(currentFloor, MotorState.UP);
			}else {
				response = scheduler.scheduleEvents(currentFloor, MotorState.DOWN);
			}

			byte[] message = response.getBytes();
			sendPacket = new DatagramPacket(message, message.length, receivePacket.getAddress(), receivePacket.getPort());
			socket.send(sendPacket);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Handles when an elevator has arrived at a target floor.
	 * 
	 * @param currentFloor The floor the elevator is at.
	 * @param direction The direction of the elevator.
	 */
	private void handleArrived(int currentFloor, String direction) {
	    byte[] message = new String(currentFloor+ "," +direction).getBytes();
	    
	    try {
	        DatagramPacket sendPacket = new DatagramPacket(message, message.length, floorSubsysIp, FLOOR_SUBSYS_PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(sendPacket);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
