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
	private MessageParser parsedMessage;
	private InetAddress sourceAddr;
	private int sourcePrt;
	private Scheduler scheduler;

	/**
	 * Creates a thread for the elevator operation
	 * @param parsed The operation that is to be done
	 * @param sourceaddr The address making the request
	 * @param sourcePrt The port making the request
	 * @param scheduler The scheduler to make the request to
	 */
	public ElevatorSchedulerThread(MessageParser parsed, InetAddress sourceaddr, int sourcePrt, Scheduler scheduler) {
		this.parsedMessage = parsed;
		this.sourceAddr = sourceaddr;
		this.sourcePrt = sourcePrt;
		this.scheduler = scheduler;
	}
	
	/**
	 * This method calls the appropriate scheduler message and returns whatever needed via UDP 
	 */
	public void run() {
		try {
			switch (parsedMessage.messageType) {
				case REQUEST_WORK:
					this.handleSeekWork(parsedMessage);
					break;
				case CHECK_FOR_MORE_EVENTS:
					this.handleLookupEvents(parsedMessage);
					break;
				case STOP_AND_TAKE_EVENTS:
					this.handleStopAndTakeEvents(parsedMessage);
					break;
				case GET_HIGHEST_FLOOR_TARGET_DESTINATION:
					this.handleGetMaxDestFloor(parsedMessage);
					break;
				case GET_LOWEST_FLOOR_TARGET_DESTINATION:
					this.handleGetMinDestFloor(parsedMessage);
					break;
				case REMOVE_FIRST_EVENT:
					this.handlePopTopEvent(parsedMessage);
					break;
				case REMOVE_EVENT:
					this.handleRemoveEvent(parsedMessage);
					break;
				case QUEUED_EVENT:
					this.handleGetQueuedEvents(parsedMessage);
					break;
			}			
		} catch (InterruptedException | IOException e) {
			System.out.println("From Scheduler: Failed to accept event");
			e.printStackTrace();
		}
	}
/**
	 *  A call to the scheduler's lookupEvents function by this method handles
	 * @param parsed The request
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void handleLookupEvents(MessageParser parsed) throws InterruptedException, IOException {
		if (parsed.floorNum == -1 || parsed.direction == null) {
			System.out.println("From Scheduler: Invalid check for more events parameters");
			return;
		}
		boolean bool = this.scheduler.lookupEvents(parsed.floorNum, parsed.direction);
		this.finishRequest(String.valueOf(bool).getBytes());
	}

	/**
	 *  A call to the scheduler's seekWork function is handled by this method.
	 * @param parsed The request
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void handleSeekWork(MessageParser parsed) throws InterruptedException, IOException {
		if (parsed.floorNum == -1) {
			System.out.println("From Scheduler: Invalid request work parameters");
			return;
		}
		MotorState direction = this.scheduler.seekWork(parsed.floorNum);
		this.finishRequest(direction.toString().getBytes());
	}
	
	/**
	 * A call to the scheduler's GetMaxDestFloor function is handled by this method
	 * @param parsed The request
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void handleGetMaxDestFloor(MessageParser parsed) throws IOException {
		if (parsed.direction == null) {
			System.out.println("From Scheduler: Invalid get highest floor target destination parameters");
			return;
		}
		int target = this.scheduler.getMaxDestFloor(parsed.direction);
		this.finishRequest(String.valueOf(target).getBytes());
	}
	
	/**
	 * A call to the scheduler's GetMinDestFloor function is handled by this method
	 * @param parsed The request
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void handleGetMinDestFloor(MessageParser parsed) throws IOException {
		if (parsed.direction == null) {
			System.out.println("From Scheduler: Invalid get lowest floor target destination parameters");
			return;
		}
		int target = this.scheduler.getMinDestFloor(parsed.direction);
		this.finishRequest(String.valueOf(target).getBytes());
	}

	/**
	 * A call to the scheduler's stopAndTakeEvents function is handled by this method
	 * @param parsed The request
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void handleStopAndTakeEvents(MessageParser parsed) throws InterruptedException, IOException {
		if (parsed.floorNum == -1 || parsed.direction == null) {
			System.out.println("From Scheduler: Invalid handle stop and take events parameters");
			return;
		}
	    
		List<InputEvents> events = this.scheduler.stopAndTakeEvents(parsed.floorNum, parsed.direction);
		ByteArrayOutputStream bStream = new ByteArrayOutputStream(2048);
		ObjectOutput objectOutput = new ObjectOutputStream(bStream); 
		objectOutput.writeObject(events);
		objectOutput.close();
		this.finishRequest(bStream.toByteArray());
	}
	
	/**
	 * A call to the scheduler's PopTopEvent function is handled by this method
	 * @param parsed The request
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void handlePopTopEvent(MessageParser parsed) throws IOException {
		InputEvents event = this.scheduler.popTopEvent();
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput objectOutput = new ObjectOutputStream(bStream); 
		objectOutput.writeObject(event);
		objectOutput.close();
		this.finishRequest(bStream.toByteArray());
	}
	
	/**
	 * A call to the scheduler's RemoveEvent function is handled by this method
	 * @param parsed The request
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void handleRemoveEvent(MessageParser parsed) throws IOException {
		InputEvents event = this.scheduler.removeEvent(parsed.floorNum);
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		ObjectOutput objectOutput = new ObjectOutputStream(bStream); 
		objectOutput.writeObject(event);
		objectOutput.close();
		this.finishRequest(bStream.toByteArray());
	}
	
	/**
	 * This method sends back the appropriate data to the source elevator
	 * @param msg The return value to be sent through the socket
	 * @throws IOException
	 */
	private void finishRequest(byte[] msg) throws IOException {
		DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, this.sourceAddr, this.sourcePrt);
		System.out.println("From Scheduler: Prepared response packet to send to elevator");
		DatagramSocket socket = new DatagramSocket();
		socket.send(sendPacket);
		System.out.println("From Scheduler: Sent response packet to floor");
		socket.close();
	}
	
	/**
	 * A call to the scheduler's RemoveEvent function is handled by this method
	 * @param parsed The request
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void handleGetQueuedEvents(MessageParser parsed) throws IOException {
		// Get queued events
		LinkedList<InputEvents> events = this.scheduler.getEventsQueue();
		ByteArrayOutputStream bStream = new ByteArrayOutputStream(2048);
		ObjectOutput objectOutput = new ObjectOutputStream(bStream); 
		objectOutput.writeObject(events);
		objectOutput.close();
		this.finishRequest(bStream.toByteArray());
	}
}