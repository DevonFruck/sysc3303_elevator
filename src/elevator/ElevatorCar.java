package elevator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import types.EventsHandler;
import types.InputEvents;
import types.MotorState;

import static config.Config.*;
/**
 * @author L4 Group 9
 *
 */
public class ElevatorCar extends Thread {
    
    private int id;
    private boolean isRunning;
    int currentFloor;
    private LinkedList<InputEvents> events;
    
    ElevatorDoor elevatorDoor = new ElevatorDoor(this);
    ElevatorButton elevButtons[] = new ElevatorButton[NUM_OF_FLOORS];
    ElevatorMotor motor = new ElevatorMotor(this);
    MotorState direction = MotorState.IDLE;

    DatagramPacket receivePacket, sendPacket;
    DatagramSocket socket;
    InetAddress schedulerIp;
    
    int elevSchedulerPort;
    int faultFlag = 0;
    int trivialFlag = 0;
    Long startTime, endTime;

    /**
     * Constructor for the ElevatorCar class.
     * Defines default values for the elevator.
     * 
     * @param scheduler The elevator scheduler the class interacts with.
     */
    public ElevatorCar(int id, int initialFloor, String schedulerIp, int elevSchedulerPort) {
        isRunning = true;
        this.events = new LinkedList<>();
        this.id = id;
        this.elevSchedulerPort = elevSchedulerPort; 
        currentFloor = initialFloor;
        motor.setStatus(MotorState.IDLE);

        for (int i=0; i<NUM_OF_FLOORS; i++) {
            elevButtons[i] = new ElevatorButton(i+1);
        }
        
        try {
            this.schedulerIp = InetAddress.getByName(schedulerIp);
            socket = new DatagramSocket();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }
    
    public void decrementFaultFlag() {
        faultFlag --;
    }
    
    public void incrementFaultFlag() {
        faultFlag ++;
    }
    
    public void decrementTrivialFlag() {
        trivialFlag --;
    }
    
    public void incrementTrivialFlag() {
        trivialFlag ++;
    }
    
    /**
     * Get method for if elevator is running.
     * @return If elevator is running.
     */
    public boolean getIsRunning() {
        return isRunning;
    }
    
    /**
     * Shuts down the elevator
     */
    public void shutDown() {
        isRunning = false;
        sendFault(true);
    }
    
    /**
     * Get method for the elevator's direction.
     * @return Elevator's direction.
     */
    public MotorState getMotorState() {
        return this.direction;
    }
    
    /**
     * Get method for the elevator's ID.
     * @return Elevator ID.
     */
    public int getID() {
        return this.id;
    }
    
    
    /**
     * Get method for getting the elevator's current floor.
     * @return the elevator's current floor.
     */
    public int getCurrentFloor() {
        return currentFloor;
    }
    
    
    /**
     * Sends a floor arrival notification to the elevator scheduler.
     * 
     * @param floorNum The floor the elevator has arrived at.
     * @param dir The direction the elevator is heading.
     */
    public void arrivedAtFloor(int floorNum, MotorState dir) {
        String message = "arrived,"+ id  +","+ currentFloor +","+ dir.name();

        DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), schedulerIp, elevSchedulerPort);
        try {
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Sends a fault message to the elevator scheduler thread and shuts down.
     */
    public void sendFault(boolean serious) {
        String message;
        if(serious) {            
            message = "fault,"+ id;
        } else {
            message = "trivial,"+ id;
        }

        try {
            sendPacket = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(DEFAULT), elevSchedulerPort);
            socket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * Find the farthest initial event floor. If the elevator direction is heading
     * up, it will find the lowest floor, and vice versa. Returns null if at farthest floor.
     */
    public InputEvents getFurthestInitialFloorEvent(MotorState dir, int floor) {
        InputEvents event = null;
        int farthest = floor;
        
        /* Find the difference between current floor and event initial floor.
         * Difference calculation depends if elevator is going up or down.
         */
        if(dir == MotorState.UP) {
            for(InputEvents e: events) {
                int initialFloor = e.getInitialFloor();
                if(initialFloor < farthest) {
                    farthest = initialFloor;
                    event = e;
                }
            }
        } else {
            for(InputEvents e: events) {
                int initialFloor = e.getInitialFloor();
                if(initialFloor > farthest) {
                    farthest = initialFloor;
                    event = e;
                }
            }
        }
        return event;
    }

    public void receiveExtraWork(MotorState dir, boolean seek) {
        if(seek) {
            String message = "seekWork," + id  +","+ currentFloor + "," + dir.name();
            String responseData = sendAndReceivePacket(message);

            if(responseData.equals("NULL") || responseData.equals("EMPTY")) {

                //If no events received and no current events in progress, pause for 2 seconds
                if(events.isEmpty()) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }

            // Create event object and add to active events
            InputEvents event = new EventsHandler(responseData);
            events.add(event);
            
            // Set direction of elevator
            this.direction = event.getMotorState();
        }

    }

    /**
     * Sends request to elevator scheduler thread for any jobs that are nearby its
     * initial floor event and is going the same direction.
     * 
     * @param dir The direction the elevator will go to complete its event(s).
     */
    public void getFartherJob(MotorState dir) {
        String message = "seekFartherWork," + id  +","+ currentFloor + "," + dir.name();

        String responseData = sendAndReceivePacket(message);

        if(responseData.equals("EMPTY") || responseData.equals("NULL")) {
            return;
        }

        InputEvents event = new EventsHandler(responseData);
        events.add(event);
    }

    
    /**
     * Takes the string message from the parameter and sends it to the elevator scheduler thread, then waits for a response.
     * 
     * @param message The string data to send on the socket.
     * @return The response data of the elevator scheduler thread.
     */
    public String sendAndReceivePacket(String message) {
        try {
            Long startTime = System.nanoTime(); 
            sendPacket = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(DEFAULT), elevSchedulerPort);
            socket.send(sendPacket);
            Long endTime = System.nanoTime();
            System.out.println("Elevator -> time to send packet (ns): " + (endTime-startTime));
            
            byte receiveData[] = new byte[100];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);

            socket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Pull data from packet, convert to string, and trim whitespace
        return new String(receivePacket.getData()).trim();
    }

    
    /**
     * Checks if the elevator has arrived at any of the floors of its events.
     * Opens and closes the doors and removes the event from the queue is it is completed.
     * 
     * @param reachedFarthestFloor Flag for if the elevator has picked all its passengers up.
     */
    public void checkHasArrived(boolean reachedFarthestFloor) {
        if(!reachedFarthestFloor) {
            return;
        }
        
        // Iterate through elevator's active events. Triggers doors and buttons if necessary
        for(InputEvents e: events) {
            if(e.getDestinationFloor() == this.currentFloor || e.getInitialFloor() == this.currentFloor) {
                
                if(e.getInitialFloor() == this.currentFloor && e.getError().equals("Serious")) {
                    faultFlag++;
                }
                
                this.startTime = System.nanoTime(); 
                elevatorDoor.openCloseDoor(id, e.getError());
                elevButtons[this.currentFloor-1].pressButton();
                arrivedAtFloor(this.currentFloor, this.direction);
                this.endTime = System.nanoTime(); 
                System.out.println("Elevator -> open/close doors and send fault signal (ns): " + (endTime-startTime));
                
                // Remove event from active events when it is completed
                if(e.getDestinationFloor() == this.currentFloor) {
                    events.remove(e);
                }
                break;
            }
        }
        return;
    }

    @Override
    public void run() {
        Long startTime1 = (long) 0, endTime1 = (long) 0;
        
        boolean reachedFarthestFloor = false;
        while (isRunning) {
            //Poll for new events until one is received
            while(events.isEmpty()) {
                this.direction = MotorState.IDLE;
                reachedFarthestFloor = false;
                this.receiveExtraWork(this.direction, isRunning);
            }
            
            this.startTime = System.nanoTime();
            
            // Moving to the first event's initial floor
            InputEvents nextEvent = getFurthestInitialFloorEvent(this.direction, this.currentFloor);

            if(nextEvent == null) {
                reachedFarthestFloor = true;
            }
            checkHasArrived(reachedFarthestFloor);

            boolean goingUp;
            // null means the farthest floor has been reached
            if(nextEvent != null && !reachedFarthestFloor) {
                // not null means that the elevator is heading to its event's initial floor
                goingUp = (this.currentFloor < nextEvent.getInitialFloor());
                currentFloor = motor.moveElevator(currentFloor, id, goingUp, faultFlag);
                endTime1 = System.nanoTime();
                
                //Seek for farther events
                getFartherJob(this.direction);
            }

            else if(nextEvent == null) {
                reachedFarthestFloor = true;
                goingUp = (this.direction == MotorState.UP ? true : false);
                currentFloor = motor.moveElevator(currentFloor, id, goingUp, faultFlag);
                endTime1 = System.nanoTime();
                
                // Seek for events on the way
                receiveExtraWork(this.direction, isRunning);

            }

            else {
                goingUp = (this.direction == MotorState.UP ? true : false);
                currentFloor = motor.moveElevator(currentFloor, id, goingUp, faultFlag);
                endTime1 = System.nanoTime();
                
                // Seek for events on the way
                receiveExtraWork(this.direction, isRunning);
            }
            System.out.println("Elevator -> move up or down floor: " + (endTime1-startTime1));
        }
    }
}
