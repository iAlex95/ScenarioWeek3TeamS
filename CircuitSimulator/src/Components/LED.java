package Components;


public class LED extends Component {

	private int voltage;
	private int resistance;
	private int power;
	private boolean switchedOn = false;
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

	public int getVoltage() {
		return voltage;
	}

	public int getResistance() {
		return resistance;
	}

	public int getPower() {
		return power;
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

	public boolean isSwitchedOn() {
		return switchedOn;
	}

	public void setSwitchedOn(boolean switchedOn) {
		this.switchedOn = switchedOn;
	}
	
}
