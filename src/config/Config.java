/**
 * 
 */
package config;

/**
 * @author Group 9
 * 
 * Configuration class contains the configurations
 * of the system to be able to change values quickly.
 *
 */
public final class Config {

    /**
     * @param args
     */
    private Config() {}
    
    public static final int NUM_OF_ELEVATORS = 5;
    public static final int NUM_OF_FLOORS = 10;
    public static final int ELEV_CHANNEL_PORT = 50;
    public static final int ELEV_SUBSYS_PORT = 25;
    public static final int FLOOR_SUBSYS_PORT = 75;

}
