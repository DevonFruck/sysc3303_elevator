/**
 * 
 */
package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import scheduler.ElevatorSchedulerThread;
import scheduler.Scheduler;
import types.MotorState;

import static config.Config.*;
/**
 * @author Group 9
 *
 */
public class ElevatorSubsystem {
	
	public ElevatorSubsystem() {
		ElevatorCar elevator1 = new ElevatorCar(1, 1);
    	ElevatorCar elevator2 = new ElevatorCar(2, (int)Math.floor(NUM_OF_FLOORS/2));
    	ElevatorCar elevator3 = new ElevatorCar(3, NUM_OF_FLOORS);
    	
    	elevator1.start();
    	elevator2.start();
    	elevator3.start();
	}
    
    public static void main(String[] args) {

    }
}
