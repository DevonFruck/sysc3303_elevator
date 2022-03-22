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

public class FloorSubsystem implements Runnable {
    Floor floorList[] = new Floor[NUM_OF_FLOORS];
    MotorState arrivedElevator[] = new MotorState[NUM_OF_FLOORS];
    DatagramPacket receivePacket, sendPacket;

    // socket for sendToScheduler for sending to scheduler. Should be made every
    // time in function
    // receiveSocket is made in constructor, for receiving data from scheduler
    DatagramSocket socket, receiveSocket;
    InetAddress ip;

    /**
     * Constructor for the FloorSubsystem class. Creates all the floors and buttons, 
     * as well as a UDP socket for receiving data from the scheduler.
     */
    public FloorSubsystem() {
        try {
            this.receiveSocket = new DatagramSocket(FLOOR_SCHEDULER_PORT);
            this.socket = new DatagramSocket();
            this.ip = InetAddress.getByName(DEFAULT);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < NUM_OF_FLOORS; i++) {
            this.arrivedElevator[i] = null;
            this.floorList[i] = (new Floor(this, i));
        }

        for (Thread floor : this.floorList) {
            floor.start();
        }
    }


    public void sendToScheduler(InputEvents data) throws IOException {
        byte[] sendData = new String(data.getTime() + "," + data.getInitialFloor() + ","
                + (data.isGoingUp() ? "up" : "down") + "," + data.getDestinationFloor() + ",").getBytes();
        
        try {
            this.sendPacket = new DatagramPacket(sendData, sendData.length, ip, FLOOR_SCHEDULER_PORT);
            this.socket.send(sendPacket);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Waits to receive a packet from the scheduler and notifies the 
     * proper floor of an elevators arrival
     */
    public void getFromScheduler() {
        byte data[] = new byte[100];

        try {
            this.receivePacket = new DatagramPacket(data, data.length);
            receiveSocket.receive(receivePacket);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        String receivedData[] = new String(receivePacket.getData()).trim().split(",");

        // ASSUMING index 0 is floorNumber/id and index 1 is if going up;
        boolean isGoingUp = false;

        if (receivedData[1].equals("up")) {
            isGoingUp = true;
        }

        floorList[Integer.parseInt(receivedData[0])].elevatorArrived(isGoingUp);
    }


    public String[] parseData(String schedulerData) {
        String[] tokens = schedulerData.split(",");
        return tokens;
    }
    
    
    @Override
    public void run() {
        while(true) {
            getFromScheduler();
        }

    }
    
    
    public static void main(String[] args) {
        //FloorSubsystem subsys = new FloorSubsystem();
    }
}
