package floorSubsystem;

import java.io.IOException;
import java.util.ArrayList;
import scheduler.Scheduler;
import types.InputEvents;
import types.MotorState;

/**
 * @author L4 Group 9
 *
 */
public class Floor extends Thread {
    int floorNumber;
    // Scheduler scheduler;
    FloorSubsystem subsys;
    ArrayList<InputEvents> events;
    FloorButton floorButtons[];

    /**
     * Constructor for the Floor class. Initialized the floor number, a reference to
     * the scheduler, it's floor buttons, and an events queue.
     * 
     * @param floorNumber
     * @param scheduler
     */
    public Floor(FloorSubsystem subsys, int floorNumber) {
        this.floorNumber = floorNumber;
        this.subsys = subsys;
        this.events = new ArrayList<InputEvents>();

        floorButtons = new FloorButton[] {new FloorButton(), new FloorButton()};
    }

    /**
     * Returns the floor number of the floor.
     * 
     * @return Floor number.
     */
    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * Reads from the input.txt file located in the same directory and parses the
     * data into InputEvent types and adds them to the floor queue.
     */
    public void readEvents() {
        ArrayList<InputEvents> arr = new ArrayList<InputEvents>();
        arr.addAll(TxtFileReader.getEvents("src/input.txt"));
        for (int i = 0; i < arr.size(); i++) {
            InputEvents temp = arr.get(i);
            if (temp.getInitialFloor() == this.floorNumber) {
                this.events.add(temp);
            }
        }
    }

    public void requestElevator() {
        InputEvents a = events.remove(0);
        try {
            subsys.sendToScheduler(a);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (a.isGoingUp()) 
            floorButtons[0].pressButton(); 
        else 
            floorButtons[1].pressButton();
    }
    
    /**
     * Invoked by the scheduler when the requested elevator has arrived at the
     * floor.
     */
    public void elevatorArrived() {
        System.out.println("Elevator has arrived on floor: " + floorNumber);
    }

    public ArrayList<InputEvents> getEventList() {
        return events;
    }

    public void receiveEvent() throws IllegalArgumentException {
        MotorState state;
        try {
            state = subsys.getElevatorArrived(this.floorNumber-1);
            
            if(state == MotorState.DOWN) {
                floorButtons[1].pressButton();
            }
            
            else if(state == MotorState.UP) {
                floorButtons[0].pressButton();
            }
              
            this.elevatorArrived();
            
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Send a floor input event to the scheduler.
     * 
     * @param event Floor event to send to the scheduler.
     */
    public void sendEvent(InputEvents event) {
        // scheduler.addEvent(event);
    }


    @Override
    public void run() {
        while (true) {
            this.readEvents();
            try {
                this.receiveEvent();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

}
