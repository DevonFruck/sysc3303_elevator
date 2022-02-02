/**
 * 
 */

/**
 * @author Group
 *
 */
public class Scheduler implements Runnable {
	
	public Scheduler() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scheduler scheduler = new Scheduler();
		
		Thread[] floors = new Thread[1];
		floors[0] = new Thread(new Floor(1, scheduler), "floor1");
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
