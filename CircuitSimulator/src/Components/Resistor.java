package Components;
import java.awt.Image;
import java.util.ArrayList;


public class Resistor extends Component {
	
	private float resistance;
	private final static int MAX_ROTATION = 2;
	
	public Resistor(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		maxRotation = MAX_ROTATION;
	}

	public float getResistance() {
		return resistance;
	}

	public void setResistance(float resistance) {
		this.resistance = resistance;
	}
}
