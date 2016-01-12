package Components;


public class Voltmeter extends Component {
	
	private int readVoltage;
	private final static int MAX_ROTATION = 2;

	public Voltmeter(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		maxRotation = MAX_ROTATION;
	}

	public int getReadVoltage() {
		return readVoltage;
	}

	public void setReadVoltage(int readVoltage) {
		this.readVoltage = readVoltage;
	}
	
}
