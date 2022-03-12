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
import types.MessageParser;
import types.MotorState;

import static config.Config.NUM_OF_ELEVATORS;
/**
 * @author Group 9
 *
 */
public class ElevatorSubsystem implements Runnable {
	private Scheduler scheduler;
	
    ElevatorCar elevatorList[] = new ElevatorCar[NUM_OF_ELEVATORS];
    
    DatagramPacket receivePacket, sendPacket;
    DatagramSocket socket;
    
    InetAddress ip;
    
    // The next immediate floor each elevator has to visit.
    // Elevator ID correlates to the index of the array.
    int nextFloor[] = new int[NUM_OF_ELEVATORS];

    /**
     * @param args
     */
    public ElevatorSubsystem(Scheduler scheduler) throws SocketException {
        try {
        	this.scheduler = scheduler;
            ip = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Initialize Elevators and set next floor to default
        for(int i=0; i<NUM_OF_ELEVATORS; i++) {
            elevatorList[i] = new ElevatorCar(this, i);
            nextFloor[i] = 0;
        }
        for(Thread elevator: elevatorList) {
           elevator.start();
        }
        
        
        getFromScheduler();
    }
    
    /**
     * Invoked by the elevators to find their next
     * immediate floor to visit.
     * 
     * @param id The ID of the elevator.
     * @return The next floor.
     */
    synchronized int getNextFloor(int id) {
        try {
            while(nextFloor[id] == 0) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return nextFloor[id];
    }
    
    synchronized void setNextFloor(int id, int floor) {
        nextFloor[id] = floor;
        notifyAll();
    }
    
    
    MotorState getElevatorState(int id) {
        try {
            while(nextFloor[id] == 0) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return elevatorList[id].getMotor().getStatus();
    }
    
    /*
        Parses the data to send to Elevator car
    */
    public String[] parseData(String scheduler_data){
        String[] tokens = scheduler_data.split(",");

        return tokens;
    }

    public void sendToScheduler(int floornum, String state) throws IOException {
        String newFloor = Integer.toString(floornum);
        String send_Data = newFloor+ "," + state;

        byte data[] = send_Data.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, 50);
        
        socket.send(sendPacket);
    }
    
    public byte[] getFromScheduler() {
        try {
            byte data[] = new byte[100];
            DatagramPacket receivePacket = new DatagramPacket(data, data.length);
            socket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return this.parseData(receivePacket.getData().toString());
        return (receivePacket.getData());
    }
    
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            //Assuming data[0] is ID and data[1] is the next floor
            byte[] data = getFromScheduler();
//            setNextFloor(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
            
			String request = new String(data);
			MessageParser parsed = new MessageParser(request);
			System.out.println("From Scheduler: successfully parsed request from elevator with address " + receivePacket.getAddress() + ", port " + receivePacket.getPort());
			ElevatorSchedulerThread subElevThread = new ElevatorSchedulerThread(parsed, receivePacket.getAddress(), receivePacket.getPort(), this.scheduler);
			subElevThread.start();
			
        }
    }
    
    public static void main(String[] args) {
//        try {
//            ElevatorSubsystem subsys = new ElevatorSubsystem();
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
    }
}
