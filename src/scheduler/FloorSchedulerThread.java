package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import types.EventsHandler;
import types.InputEvents;

import static config.Config.*;
/**
 * This thread is created and used for each new event
 */
public class FloorSchedulerThread extends Thread {
	private Scheduler scheduler;
	private DatagramSocket receiveSocket;
	
	/**
	 * Creates a new FloorSubThread for the new event
	 * @param scheduler The scheduler in which the new event is added to
	 */
	public FloorSchedulerThread(Scheduler scheduler) {
		this.scheduler = scheduler;
		try {
			this.receiveSocket = new DatagramSocket(FLOOR_SCHEDULER_PORT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method calls the scheduler's acceptEvent() method and sends a message to the source floor once the event has been added
	 */
	public InputEvents receiveData() {
	    byte[] floorInputs = new byte[100];

        DatagramPacket receivePacket =  new DatagramPacket(floorInputs, floorInputs.length);

        try {
            receiveSocket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String data = new String(receivePacket.getData()).trim();
        InputEvents newEvent = new EventsHandler(data);
        
        return newEvent;
	    
	}
	
	public void run() {
		try {
			while(true) {			
				InputEvents newEvent = receiveData();
				this.scheduler.acceptEvent(newEvent); 
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
