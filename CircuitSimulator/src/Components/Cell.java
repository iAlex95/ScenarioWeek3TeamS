package Components;


public class Cell extends Component {
	
	private int voltage;
	private final static int MAX_ROTATION = 2;
	
	public Cell(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		maxRotation = MAX_ROTATION;
		voltage = 9;
	}

	public int getVoltage() {
		return voltage;
	}

	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}
}
