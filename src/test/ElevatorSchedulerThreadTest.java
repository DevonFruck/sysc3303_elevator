package test;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.AfterClass;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import scheduler.ElevatorSchedulerThread;
import scheduler.Scheduler;
import static config.Config.*;

class ElevatorSchedulerThreadTest {
    
    static Scheduler scheduler = new Scheduler();
    static ElevatorSchedulerThread thread = new ElevatorSchedulerThread(scheduler);
    
    /**
     * Tests the ability for the scheduler to receive data from the elevators.
     */
    @Test
    void evaluateReceiveRequestTest() {
        byte data[];
        String receivedData;
        
        data = "seekWork,4,UP".getBytes();
        mockSender(ELEVATOR_SCHEDULER_PORT, data, "127.0.0.1");
        receivedData = thread.receiveRequest();
        assertEquals("seekWork,4,UP", receivedData);
        
        data = "arrived,6,DOWN  ".getBytes();
        mockSender(ELEVATOR_SCHEDULER_PORT, data, "127.0.0.1");
        receivedData = thread.receiveRequest();
        assertEquals("arrived,6,DOWN", receivedData);
        
        
    }
    
    @AfterClass
    public static void doYourOneTimeTeardown() {
        try {
            thread.shutDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
    
    public void mockSender(int destPort, byte[] data, String sendIp) {
        try {
            DatagramSocket socket = new DatagramSocket();
            
            InetAddress ip = InetAddress.getByName(sendIp);
            DatagramPacket packet = new DatagramPacket(data, data.length, ip, destPort);
            
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

