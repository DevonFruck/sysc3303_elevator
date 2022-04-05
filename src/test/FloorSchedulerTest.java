package test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.junit.jupiter.api.Test;

import scheduler.FloorSchedulerThread;
import scheduler.Scheduler;
import types.EventsHandler;
import types.InputEvents;
import static config.Config.*;

public class FloorSchedulerTest {
    static Scheduler scheduler = new Scheduler();
    static FloorSchedulerThread thread = new FloorSchedulerThread(scheduler, null);
    
    
    @Test
    void evaluateReceiveRequestTest() {
        byte data[];
        InputEvents receivedData;
        
        data = "16:48:02.00,2,Up,5".getBytes();        
        mockSender(FLOOR_SCHEDULER_PORT, data, "127.0.0.1");
        receivedData = thread.receiveData();
        EventsHandler testEvents = new EventsHandler("16:48:02.00,2,Up,5");
        assertTrue(testEvents.equals(receivedData));

        
        data = "13:05:27.48,1,Up,9,Trivial".getBytes();
        mockSender(FLOOR_SCHEDULER_PORT, data, "127.0.0.1");
        receivedData = thread.receiveData();
        EventsHandler testEvents2 = new EventsHandler("13:05:27.48,1,Up,9,Trivial");
        assertTrue(testEvents2.equals(receivedData));
        
        
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
