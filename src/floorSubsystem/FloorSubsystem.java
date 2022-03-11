package floorSubsystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import types.InputEvents;
import types.motorStat;

import static config.Config.NUM_OF_FLOORS;

public class FloorSubsystem implements Runnable{
    Floor floorList [] = new Floor[NUM_OF_FLOORS];
    motorStat arrivedElevator[] = new motorStat[NUM_OF_FLOORS];
    DatagramPacket receivePacket, sendPacket;
    DatagramSocket socket;
    InetAddress ip;
    
    public FloorSubsystem () throws SocketException {
        try {
            ip = InetAddress.getByName("127.0.0.2");
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
        
        this.getFromScheduler();
    }
    
    synchronized motorStat getElevatorArrived(int id) {
        while(arrivedElevator[id] == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return arrivedElevator[id];
    }
    
    synchronized void setElevatorArrived(int id, motorStat state) {
        arrivedElevator[id] = state;
        notifyAll();
    }
    
    public void sendToScheduler(InputEvents data) throws IOException {
        byte sendData[] = new String(data.getInitialFloor() + "," + data.getDestinationFloor() + "," + (data.isGoingUp() ? "U":"D")).getBytes(); ///new String("test").getBytes();
        try {
            this.sendPacket = new DatagramPacket(sendData, sendData.length, ip, 50);
            this.socket.send(sendPacket);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    public String[] getFromScheduler() {
        byte data[] = new byte[100];
        try {
            this.receivePacket = new DatagramPacket(data, data.length);
            this.socket.receive(receivePacket);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this.parseData(receivePacket.getData().toString());
    }
    
    public String[] parseData(String scheduler_data){
        String[] tokens = scheduler_data.split(",");
        
        return tokens;
    }
        
    public void run() {
        while(true) {
            String data[] = this.getFromScheduler();
        }
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            FloorSubsystem subsys = new FloorSubsystem();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
