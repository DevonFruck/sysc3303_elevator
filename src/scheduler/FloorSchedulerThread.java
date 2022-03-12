package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import types.EventsHandler;
import types.InputEvents;

import static config.Config.*;
/**
 * This thread is created and used for each new event
 */
public class FloorSchedulerThread extends Thread {
	private Scheduler scheduler;
	private int port;
	private InetAddress sourceAdd;
	
	private InputEvents event;
	
	/**
	 * Creates a new FloorSubThread for the new event
	 * @param event The added event
	 * @param sourceAdd The address making the new event request
	 * @param port The port making the new event request
	 * @param scheduler The scheduler in which the new event is added to
	 */
	public FloorSchedulerThread(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	/**
	 * This method calls the scheduler's acceptEvent() method and sends a message to the source floor once the event has been added
	 */
	public void run() {
		try {
			byte[] floorInputs = new byte[100];
			
			DatagramPacket receivePacket =  new DatagramPacket(floorInputs, floorInputs.length);
			
			DatagramSocket receiveSocket = new DatagramSocket(FLOOR_SCHEDULER_PORT);
			receiveSocket.receive(receivePacket);
			
			String data = new String(receivePacket.getData());
//			"time, floor, des, direction"
			
			InputEvents newEvent = new EventsHandler(data);
			
			
			
			this.scheduler.acceptEvent(newEvent); // Waits until the event has been accepted by scheduler
//			System.out.println("SCHEDULER --> Event is finished");
			
//			String message = "Event Processed";
//			DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), this.sourceAdd, this.port);
//			
//			System.out.println("Schedular --> Acknowledgement to be sent to floor");
//			
//			DatagramSocket socket = new DatagramSocket();
//			socket.send(sendPacket);
//			System.out.println("Schedular --> Sent acknowledgement packet to floor");
//			
//			socket.close();
		
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
}
