package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import display.GUI;
import types.EventsHandler;
import types.InputEvents;
import types.MotorState;

/**
 * This thread is created and used for each new event
 */
public class FloorSchedulerThread extends Thread {
	private Scheduler scheduler;
	private DatagramSocket receiveSocket;
	private GUI display;
	/**
	 * Creates a new FloorSubThread for the new event
	 * 
	 * @param scheduler The scheduler in which the new event is added to.
	 * @param display The GUI for the application.
	 * @param receivePort The port at which this class receives requests.
	 */
	public FloorSchedulerThread(Scheduler scheduler, GUI display, int receivePort) {
		this.scheduler = scheduler;
		this.display = display;
		try {
			this.receiveSocket = new DatagramSocket(receivePort);
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
        
        //Test cases don't have a GUI, so only update when using GUI
        if(display != null) {
            String direction = newEvent.getMotorState()==MotorState.UP ? "up" : "down";
            display.writeToLog("Floor " +newEvent.getInitialFloor()+ " pressed the " + direction + " button");
        }
        return newEvent;
	    
	}
	
	@Override
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
