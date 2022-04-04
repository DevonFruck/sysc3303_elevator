package display;
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
    
    Border emptyBorder = BorderFactory.createEmptyBorder();
    Border arrivedBorder = BorderFactory.createLineBorder(Color.ORANGE,5);
    Border defaultBorder = BorderFactory.createLineBorder(Color.BLACK,2);
    
    public GUI() {
        frameObj = new JFrame();
        buttons = new JButton[NUM_OF_ELEVATORS][NUM_OF_FLOORS];
        elevatorPosition = new int[NUM_OF_ELEVATORS];
        frameObj.setLayout(new GridLayout(NUM_OF_FLOORS, NUM_OF_ELEVATORS));
        
        for(int k=0; k<NUM_OF_ELEVATORS; k++) {
            elevatorPosition[k] = 1;
        }
        
        for(int j=NUM_OF_FLOORS-1; j>=0; j--) {
            for(int i=0; i<NUM_OF_ELEVATORS; i++) {
                buttons[i][j] = new JButton((i+1)+","+(j+1));
                buttons[i][j].setForeground(Color.PINK);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setBorder(defaultBorder);
                
                frameObj.add(buttons[i][j]);
            }
        }
        
        frameObj.setSize(300, 300);
        frameObj.setVisible(true);
    }
    
    /**
     * Updates the status of the elevator
     * @param event The type of event (arrived, error, etc)
     * @param elevator The number of the active elevator.
     * @param floor The floor number the elevator is having an event happen at.
     */
    public void setFloorStatus(String event, int elevator, int floor) {
        int elevIndex = elevator-1;
        int floorIndex = floor-1;
        
        //Reset colors for elevators previous floor
        buttons[elevIndex][elevatorPosition[elevIndex]-1].setBorder(defaultBorder);
        buttons[elevIndex][elevatorPosition[elevIndex]-1].setBackground(Color.WHITE);
        
        //Update new floor location
        buttons[elevIndex][floorIndex].setBackground(Color.BLACK);
        elevatorPosition[elevIndex] = floor;
        
        if(event == "arrived") {
            buttons[elevIndex][floorIndex].setBorder(arrivedBorder);
        }
    }
    
    public void closeElevator(int elevatorId) {
        int elevIndex = elevatorId-1;
        
        for(int i=0; i<NUM_OF_FLOORS; i++) {
            buttons[elevIndex][i].setBackground(Color.GRAY);
        }
    }
    
    
//    public static void main(String args[]) {
//        GUI gui = new GUI();
//        //gui.start();
//        
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}