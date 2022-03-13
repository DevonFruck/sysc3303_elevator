package scheduler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

import types.InputEvents;
import types.MotorState;

import static config.Config.*;

/**
 * This thread is created for elevator request
 */
public class ElevatorSchedulerThread extends Thread {	

	private InetAddress sourceAdd;
	private int sourcePort;
	private Scheduler scheduler;

	private DatagramPacket receivePacket, sendPacket;
	private DatagramSocket socket;

	/**
	 * Creates a thread for the elevator operation
	 * @param parsed The operation that is to be done
	 * @param sourceAddress The address making the request
	 * @param sourcePort The port making the request
	 * @param scheduler The scheduler to make the request to
	 */
	public ElevatorSchedulerThread(Scheduler scheduler) {
		this.scheduler = scheduler;
		try {
			this.socket= new DatagramSocket(ELEVATOR_SCHEDULER_PORT);
		} catch (SocketException e) {
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
			String parsedData[] = receivedData.split(",");

			switch (parsedData[0]) {
			case "seekWork":
				this.handleSeekWork(Integer.parseInt(parsedData[1]), parsedData[2].trim());
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

}
