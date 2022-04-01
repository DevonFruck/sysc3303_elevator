package display;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;

public class GUI {
    JFrame frameObj;
    int elevatorNum = 4;
    int floorNum = 10;
    private JButton[][] buttons;
    private int[] elevatorPosition;
    
    public GUI() {
        frameObj = new JFrame();
        buttons = new JButton[elevatorNum][floorNum];
        elevatorPosition = new int[elevatorNum];
        frameObj.setLayout(new GridLayout(floorNum, elevatorNum));
        
        for(int k=0; k<elevatorNum; k++) {
            elevatorPosition[k] = 1;
        }
        
        for(int j=floorNum-1; j>=0; j--) {
            for(int i=0; i<elevatorNum; i++) {
                buttons[i][j] = new JButton((i+1)+","+(j+1));
                buttons[i][j].setForeground(Color.PINK);
                buttons[i][j].setBackground(Color.WHITE);
                
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
        
        buttons[elevIndex][elevatorPosition[elevIndex]-1].setBackground(Color.WHITE);
        buttons[elevIndex][floor-1].setBackground(Color.BLACK);
        
        elevatorPosition[elevIndex] = floor;
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