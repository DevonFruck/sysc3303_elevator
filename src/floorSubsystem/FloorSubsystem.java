package floorSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import scheduler.Scheduler;
import types.InputEvents;
import types.MotorState;

import static config.Config.*;

public class FloorSubsystem {
    Floor floorList [] = new Floor[NUM_OF_FLOORS];
    MotorState arrivedElevator[] = new MotorState[NUM_OF_FLOORS];
    DatagramPacket receivePacket, sendPacket;
    DatagramSocket socket;
    InetAddress ip;
    
    public FloorSubsystem () throws SocketException {
        try {
        	this.socket = new DatagramSocket();
//        	this.scheduler = scheduler;
            ip = InetAddress.getByName(DEFAULT);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        for(int i = 0; i < NUM_OF_FLOORS; i++) {
            this.arrivedElevator[i] = null;
            this.floorList[i] = (new Floor(this, i));
        }
        
        for(Thread floor : this.floorList) {
            floor.start();
        }
        
//        this.getFromScheduler();
    }
    
//    synchronized MotorState getElevatorArrived(int id) {
//        while(arrivedElevator[id] == null) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        return arrivedElevator[id];
//    }
//    
//    synchronized void setElevatorArrived(int id, MotorState state) {
//        arrivedElevator[id] = state;
//        notifyAll();
//    }
//    
    public void sendToScheduler(InputEvents data) throws IOException {
        byte[] sendData = new String(data.getTime() + ","+data.getInitialFloor() + "," + (data.isGoingUp() ? "up":"down") + "," + data.getDestinationFloor()+",").getBytes(); ///new String("test").getBytes();
        try {
//        	System.out.println("FLOOR #:"+floorNumber+ "--> Sending this message: "+data);
            this.sendPacket = new DatagramPacket(sendData, sendData.length, ip, FLOOR_SCHEDULER_PORT);
            this.socket.send(sendPacket);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    public String[] getFromScheduler() {
        byte data[] = new byte[100];
        
        try {
        	DatagramSocket newSocket =  new DatagramSocket();
            this.receivePacket = new DatagramPacket(data, data.length);
            newSocket.receive(receivePacket);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this.parseData(receivePacket.getData().toString());
//        return (receivePacket.getData());
    }
//    
    public String[] parseData(String scheduler_data){
        String[] tokens = scheduler_data.split(",");
        return tokens;
    }
        
//    public void run() {
//        while(true) {
//        	String data[] = this.getFromScheduler();
        	
//          byte[] data = this.getFromScheduler();
            
        	
//			try {
//				ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(data));
//				InputEvents parsedEvent = (InputEvents)iStream.readObject();
//				iStream.close();
//				
//				FloorSchedulerThread subFloorThread = new FloorSchedulerThread(parsedEvent, receivePacket.getAddress(), receivePacket.getPort(), this.scheduler);
//				subFloorThread.start();
//			} catch (IOException | ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//        }
//    }
    
    public static void main(String[] args) {
//        try {
//			FloorSubsystem subsys = new FloorSubsystem();
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
    }
}
