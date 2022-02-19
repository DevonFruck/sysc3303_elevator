
public class ElevatorMotor {
	
	private motorStat status;
	
	public ElevatorMotor() {
		status = motorStat.IDLE;
	}
	
	public motorStat getStatus() {
		return status;
	}
	
	public void setStatus(motorStat status) {
		this.status = status;
	}
}
