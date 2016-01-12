package Components;


public class Cell extends Component {
	
	private float voltage;
	private final static int MAX_ROTATION = 2;
	
	public Cell(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		maxRotation = MAX_ROTATION;
	}

	public float getVoltage() {
		return voltage;
	}

	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}
}
