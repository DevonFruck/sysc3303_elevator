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
import types.MessageParser;
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
		//		this.sourceAdd = sourceAddress;
		//		this.sourcePort = sourcePort;
		this.scheduler = scheduler;
		try {
			this.socket= new DatagramSocket(ELEVATOR_SCHEDULER_PORT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String receivedData = new String(receivePacket.getData());
				String parsedData[] = receivedData.split(",");

				switch (parsedData[0]) {
				case "seekWork":
					this.handleSeekWork(Integer.parseInt(parsedData[1]));
					break;
//				case "arrived":
//					this.handleArrived();
//					break;
				}
			}
	}

	private void handleSeekWork(int currentFloor) {
		try {
			String response = scheduler.scheduleEvents(MotorState.IDLE, currentFloor);

			byte[] message = response.getBytes();
			sendPacket = new DatagramPacket(message, message.length, receivePacket.getAddress(), receivePacket.getPort());
			socket.send(sendPacket);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	/**
//	 * This method handles a call to the scheduler's requestWork function
//	 * 
//	 * @param parsed The request
//	 * @throws InterruptedException
//	 * @throws IOException
//	 */
//	private void handleRequestWork(MessageParser parsed) throws InterruptedException, IOException {
//		if (parsed.floorNum == -1) {
//			System.out.println("From Scheduler: Invalid request work parameters");
//			return;
//		}
//		ElevatorDirection direction = this.scheduler.requestWork(parsed.floorNum);
//		this.finishRequest(direction.toString().getBytes());
//	}
//
//	/**
//	 * This method handles a call to the scheduler's checkForMoreEvents function
//	 * 
//	 * @param parsed The request
//	 * @throws InterruptedException
//	 * @throws IOException
//	 */
//	private void handleCheckForMoreEvents(MessageParser parsed) throws InterruptedException, IOException {
//		if (parsed.floorNum == -1 || parsed.direction == null) {
//			System.out.println("From Scheduler: Invalid check for more events parameters");
//			return;
//		}
//		boolean bool = this.scheduler.checkForMoreEvents(parsed.floorNum, parsed.direction);
//		this.finishRequest(String.valueOf(bool).getBytes());
//	}
//
//	/**
//	 * This method handles a call to the scheduler's stopAndTakeEvents function
//	 * 
//	 * @param parsed The request
//	 * @throws InterruptedException
//	 * @throws IOException
//	 */
//	private void handleStopAndTakeEvents(MessageParser parsed) throws InterruptedException, IOException {
//		if (parsed.floorNum == -1 || parsed.direction == null) {
//			System.out.println("From Scheduler: Invalid handle stop and take events parameters");
//			return;
//		}
//
//		List<InputInformation> events = this.scheduler.stopAndTakeEvents(parsed.floorNum, parsed.direction);
//		ByteArrayOutputStream bStream = new ByteArrayOutputStream(2048);
//		ObjectOutput objectOutput = new ObjectOutputStream(bStream);
//		objectOutput.writeObject(events);
//		objectOutput.close();
//		this.finishRequest(bStream.toByteArray());
//	}
//
//	/**
//	 * This method handles a call to the scheduler's
//	 * getHighestFloorTargetDestination function
//	 * 
//	 * @param parsed The request
//	 * @throws InterruptedException
//	 * @throws IOException
//	 */
//	private void handleGetHighestFloorTargetDestination(MessageParser parsed) throws IOException {
//		if (parsed.direction == null) {
//			System.out.println("From Scheduler: Invalid get highest floor target destination parameters");
//			return;
//		}
//		int target = this.scheduler.getHighestFloorTargetDestination(parsed.direction);
//		this.finishRequest(String.valueOf(target).getBytes());
//	}
//
//	/**
//	 * This method handles a call to the scheduler's getLowestFloorTargetDestination
//	 * function
//	 * 
//	 * @param parsed The request
//	 * @throws InterruptedException
//	 * @throws IOException
//	 */
//	private void handleGetLowestFloorTargetDestination(MessageParser parsed) throws IOException {
//		if (parsed.direction == null) {
//			System.out.println("From Scheduler: Invalid get lowest floor target destination parameters");
//			return;
//		}
//		int target = this.scheduler.getLowestFloorTargetDestination(parsed.direction);
//		this.finishRequest(String.valueOf(target).getBytes());
//	}
//
//	/**
//	 * This method handles a call to the scheduler's removeFirstEvent function
//	 * 
//	 * @param parsed The request
//	 * @throws InterruptedException
//	 * @throws IOException
//	 */
//	private void handleRemoveFirstEvent(MessageParser parsed) throws IOException {
//		InputInformation event = this.scheduler.removeFirstEvent();
//		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//		ObjectOutput objectOutput = new ObjectOutputStream(bStream);
//		objectOutput.writeObject(event);
//		objectOutput.close();
//		this.finishRequest(bStream.toByteArray());
//	}
//
//	/**
//	 * This method handles a call to the scheduler's removeEvent function
//	 * 
//	 * @param parsed The request
//	 * @throws InterruptedException
//	 * @throws IOException
//	 */
//	private void handleRemoveEvent(MessageParser parsed) throws IOException {
//		InputInformation event = this.scheduler.removeEvent(parsed.floorNum);
//		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//		ObjectOutput objectOutput = new ObjectOutputStream(bStream);
//		objectOutput.writeObject(event);
//		objectOutput.close();
//		this.finishRequest(bStream.toByteArray());
//	}
//
//	/**
//	 * This method handles a call to the scheduler's getQueuedEvents function
//	 * 
//	 * @param parsed The request
//	 * @throws InterruptedException
//	 * @throws IOException
//	 */
//	private void handleGetQueuedEvents(MessageParser parsed) throws IOException {
//		// Get queued events
//		LinkedList<InputInformation> events = this.scheduler.getQueuedEvents();
//		ByteArrayOutputStream bStream = new ByteArrayOutputStream(2048);
//		ObjectOutput objectOutput = new ObjectOutputStream(bStream);
//		objectOutput.writeObject(events);
//		objectOutput.close();
//		this.finishRequest(bStream.toByteArray());
//	}
//
//	/**
//	 * This method sends back the appropriate data to the source elevator
//	 * 
//	 * @param msg The return value to be sent through the socket
//	 * @throws IOException
//	 */
//	private void finishRequest(byte[] msg) throws IOException {
//		DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, this.sourceAddress, this.sourcePort);
//		System.out.println("From Scheduler: Prepared response packet to send to elevator");
//		DatagramSocket socket = new DatagramSocket();
//		socket.send(sendPacket);
//		System.out.println("From Scheduler: Sent response packet to floor");
//		socket.close();
//	}
}
