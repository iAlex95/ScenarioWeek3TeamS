package Components;


public class LED extends Component {

	private float intensity;
	private int voltage;
	private int resistance;
	private int power;
	private final static int MAX_ROTATION = 2;
	
	public LED(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		voltage = 6;
		power = 3;
		resistance = 12;
		maxRotation = MAX_ROTATION;
	}

	public float getIntensity() {
		return intensity;
	}

	public int getVoltage() {
		return voltage;
	}

	public int getResistance() {
		return resistance;
	}

	public int getPower() {
		return power;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public void setVoltage(int voltage) {
		this.voltage = voltage;
	}

	public void setResistance(int resistance) {
		this.resistance = resistance;
	}

	public void setPower(int power) {
		this.power = power;
	}
	
}
