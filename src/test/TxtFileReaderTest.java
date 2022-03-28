package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import floorSubsystem.TxtFileReader;

import java.util.ArrayList;

import types.EventsHandler;
import types.InputEvents;


class TxtFileReaderTest {

    @Test
    void testFileReader() throws InterruptedException {
        ArrayList<InputEvents> arr = new ArrayList<InputEvents>();
        arr.addAll(TxtFileReader.getEvents(System.getProperty("user.dir") + "/src/floorSubsystem/input.txt"));
        
        String input = "User|12:25:15.12,6,Up,9, ";
        EventsHandler event = new EventsHandler(input.split("\\|")[1]);
              
        assertTrue(arr.get(0).equals(event));
        
    }
    
}
