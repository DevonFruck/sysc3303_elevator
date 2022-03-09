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
import java.util.ArrayList;

import static config.Config.NUM_OF_ELEVATORS;
/**
 * @author Group 9
 *
 */
public class ElevatorSubsystem {
    ArrayList<ElevatorCar> elevatorList = new ArrayList<ElevatorCar>();
    
    DatagramPacket receivePacket, sendPacket;
    DatagramSocket socket;
    
    InetAddress ip;
    
    

    /**
     * @param args
     */
    public ElevatorSubsystem() throws SocketException {
        try {
            ip = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Initialize Elevators
        for(int i=0; i<NUM_OF_ELEVATORS; i++) {
            elevatorList.add(new ElevatorCar(this, i));
        }
        for(Thread elevator: elevatorList) {
           elevator.start();
        }
    }
    /*
        Parses the data to send to Elevator car
    */
    public String[] parseData(String scheduler_data){
        String[] tokens = scheduler_data.split(",");

        return tokens;

    }
    


    public void sendToScheduler() throws IOException {
        byte data[] = new String("test").getBytes();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, ip, 50);
        
        socket.send(sendPacket);
    }
    
    public String[] getFromScheduler() throws IOException {
        byte data[] = new byte[100];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        socket.receive(receivePacket);
        return this.parseData(receivePacket.getData().toString());
    }
    
    public void sendtoElevator(String[] data){
        int elevatorNumber = Integer.parseInt(data[0]);

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        
        

    }

}
