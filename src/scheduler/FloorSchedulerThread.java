package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import types.InputEvents;

/**
 * This thread is created and used for each new event
 */
public class FloorSchedulerThread extends Thread {
	private Scheduler scheduler;
	private int sourcePort;
	private InetAddress sourceAdd;
	
	private InputEvents event;
	
	/**
	 * Creates a new FloorSubThread for the new event
	 * @param event The added event
	 * @param sourceAdd The address making the new event request
	 * @param sourcePort The port making the new event request
	 * @param scheduler The scheduler in which the new event is added to
	 */
	public FloorSchedulerThread(InputEvents event, InetAddress sourceAdd, int sourcePort, Scheduler scheduler) {
		this.event = event;
		this.sourceAdd = sourceAdd;
		this.sourcePort = sourcePort;
		this.scheduler = scheduler;
	}
	
	/**
	 * This method calls the scheduler's acceptEvent() method and sends a message to the source floor once the event has been added
	 */
	public void run() {
		try {
			this.scheduler.acceptEvent(event); // Waits until the event has been accepted and dealt with within the scheduler
			
			System.out.println("From Schedular: Event is finished");
			
			String msg = "Event processed";
			
			DatagramPacket sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), this.sourceAdd, this.sourcePort);
			
			System.out.println("From Schedular: Acknowledgement packet to be sent to the floor");
			
			DatagramSocket socket = new DatagramSocket();
			
			socket.send(sendPacket);
			
			System.out.println("From Schedular: Sent acknowledgement packet to floor");
			
			socket.close();
		
		} catch (InterruptedException | IOException e) {
			
			System.out.println("From Schedular: Failure to accept event");
			e.printStackTrace();
		}
	}
}
