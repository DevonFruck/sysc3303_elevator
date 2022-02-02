import java.sql.Time;

public class InputData {
	
	Time time;
	int floorStart, floorEnd;
	
	public InputData(Time time, int floorStart, int floorEnd) {
		this.time = time;
		this.floorStart = floorStart;
		this.floorEnd = floorEnd;
	}
	
	public int getStartFloor() {
		return floorStart;
	}
	
	public int getEndFloor() {
		return floorEnd;
	}

}
