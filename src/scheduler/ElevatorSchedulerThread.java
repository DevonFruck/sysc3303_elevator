package scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import display.GUI;
import types.MotorState;

import static config.Config.*;

/**
 * This thread is created for elevator request
 */
public class ElevatorSchedulerThread extends Thread {
    
    private GUI display;
    private Scheduler scheduler;
    private DatagramPacket receivePacket, sendPacket;
    private DatagramSocket socket;
    InetAddress floorSubsysIp;
    
    boolean isRunning = true;
    
    /**
     * Creates a thread for the elevator operation
     * @param scheduler The scheduler to make the request to
     */
    public ElevatorSchedulerThread(Scheduler scheduler, GUI display) {
        this.display = display;
        this.scheduler = scheduler;

        try {
            this.socket= new DatagramSocket(ELEVATOR_SCHEDULER_PORT);
            this.floorSubsysIp = InetAddress.getByName(DEFAULT);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        isRunning  = false;
    }

    public String receiveRequest() {
        byte[] data = new byte[100];

        receivePacket = new DatagramPacket(data, data.length);
        try {
            socket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(receivePacket.getData()).trim();
    }

    public void evaluateRequest(String command, int elevatorId, int floorNum, String direction) {
        switch (command) {

        // An elevator has arrived at a new floor and is requesting more jobs
        case "seekWork":
            this.handleSeekWork(elevatorId, floorNum, direction);
            break;

            // An elevator has arrived at one of its target floors
        case "arrived":
            this.handleArrived(elevatorId, floorNum, direction);
            break;
            
        case "fault":
            this.handleFault(elevatorId);
            break;
        }
    }

    /**
     * This method calls the appropriate scheduler message and returns whatever needed via UDP 
     */
    public void run() {
        while(isRunning) {
            String parsedData[] = receiveRequest().split(",");
            
            String command = parsedData[0];
            int elevatorId = Integer.parseInt(parsedData[1]);
            
            //Handle if fault before we move on
            if(command.equals("fault")) {
                handleFault(elevatorId);
            } else {
                int floorNum = Integer.parseInt(parsedData[2]);
                String direction = parsedData[3];

                evaluateRequest(command, elevatorId, floorNum, direction);
            }
        }
        System.out.println("scheduler thread shut down");
        socket.close();
    }
    
    private void handleSeekWork(int elevatorId, int currentFloor, String direction) {
        try {
            String response;

            //Convert the direction string into the enum
            MotorState state = null;
            if(direction.equals("IDLE")) {
                state = MotorState.IDLE;
            }else if (direction.equals("UP")) {
                state =  MotorState.UP;
            }else if (direction.equals("DOWN")) {
                state = MotorState.DOWN;
            }
            
            if(display != null)
                display.setFloorStatus(null, elevatorId, currentFloor);
            
            response = scheduler.scheduleEvents(currentFloor, state);
            
            byte[] message = response.getBytes();
            sendPacket = new DatagramPacket(message, message.length, receivePacket.getAddress(), receivePacket.getPort());
            socket.send(sendPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Handles when an elevator has arrived at a target floor.
     * 
     * @param currentFloor The floor the elevator is at.
     * @param direction The direction of the elevator.
     */
    private void handleArrived(int elevatorId, int currentFloor, String direction) {
        byte[] message = new String(currentFloor+ "," +direction).getBytes();
        
        try {
            DatagramPacket sendPacket = new DatagramPacket(message, message.length, floorSubsysIp, FLOOR_SUBSYS_PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(sendPacket);
            socket.close();
            
            if(display != null)
                display.setFloorStatus("arrived", elevatorId, currentFloor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleFault(int elevatorId) {
        display.closeElevator(elevatorId);
    }

}
