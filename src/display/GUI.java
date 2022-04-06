package display;
import java.awt.BorderLayout;
import java.awt.Color;
import static config.Config.*;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.Border;

public class GUI {
    JFrame frameObj;
    int elevatorNum = 4;
    int floorNum = 10;
    private JButton[][] buttons;
    private int[] elevatorPosition;
    //private boolean[] trivialElevator;
    
    Border emptyBorder = BorderFactory.createEmptyBorder();
    Border arrivedBorder = BorderFactory.createLineBorder(Color.GREEN,4);
    Border defaultBorder = BorderFactory.createLineBorder(Color.BLACK,2);
    Border trivialBorder = BorderFactory.createLineBorder(Color.YELLOW,5);
    JTextArea logs;
    
    public GUI() {
        frameObj = new JFrame("SYSC3303 Group 9 Elevator GUI");
        
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        
        buttons = new JButton[NUM_OF_ELEVATORS][NUM_OF_FLOORS];
        elevatorPosition = new int[NUM_OF_ELEVATORS];
        
        frameObj.setLayout(new BorderLayout());
        
        JPanel elevatorPane = new JPanel();
        elevatorPane.setLayout(new GridLayout(NUM_OF_FLOORS, NUM_OF_ELEVATORS));
        
        
        for(int k=0; k<NUM_OF_ELEVATORS; k++) {
            elevatorPosition[k] = 1;
        }
        
        for(int j=NUM_OF_FLOORS-1; j>=0; j--) {
            for(int i=0; i<NUM_OF_ELEVATORS; i++) {
                buttons[i][j] = new JButton((i+1)+","+(j+1));
                buttons[i][j].setForeground(Color.PINK);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setBorder(defaultBorder);
                
                elevatorPane.add(buttons[i][j]);
            }
        }
        
        frameObj.add(elevatorPane);
        
        logs = new JTextArea(5,40);
        logs.setEditable(false);
        
        
        //frameObj.add(ta1);
        
        JScrollPane textPane = new JScrollPane(logs, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textPane.setBorder(BorderFactory.createTitledBorder("Logs"));
        
        container.add(elevatorPane);
        container.add(textPane);
        
        frameObj.add(container);
        frameObj.setSize(800, 800);
        frameObj.setVisible(true);
    }
    
    /**
     * Updates the status of the elevator
     * @param event The type of event (arrived, error, etc)
     * @param elevator The number of the active elevator.
     * @param floor The floor number the elevator is having an event happen at.
     */
    public synchronized void setFloorStatus(String event, int elevator, int floor) {
        int elevIndex = elevator-1;
        int floorIndex = floor-1;
        
        //Reset colors for elevators previous floor
        //buttons[elevIndex][elevatorPosition[elevIndex]-1].setBorder(defaultBorder);
        buttons[elevIndex][elevatorPosition[elevIndex]-1].setBackground(Color.WHITE);
        
        for(int i=0; i<NUM_OF_FLOORS; i++) {
            buttons[elevIndex][i].setBorder(defaultBorder);
        }
        
        //Update new floor location
        buttons[elevIndex][floorIndex].setBackground(Color.BLACK);
        elevatorPosition[elevIndex] = floor;
        
        if(event == "arrived") {
            buttons[elevIndex][floorIndex].setBorder(arrivedBorder);
        }
    }
    
    
    public synchronized void closeElevator(int elevatorId) {
        int elevIndex = elevatorId-1;
        
        for(int i=0; i<NUM_OF_FLOORS; i++) {
            buttons[elevIndex][i].setBackground(Color.RED);
        }
    }
    
    public synchronized void writeToLog(String message) {
        logs.append(message + "\n");
    }
    
    public synchronized void jamElevator(int elevatorId) {
        int elevIndex = elevatorId-1;
        
        for(int i=0; i<NUM_OF_FLOORS; i++) {
            buttons[elevIndex][i].setBorder(trivialBorder);
        }
    }
}