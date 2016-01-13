package Components;
import java.awt.Image;
import java.util.ArrayList;


public class Resistor extends Component {
	
	private int resistance;
	private final static int MAX_ROTATION = 2;
	
	public Resistor(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		resistance = 4;
		maxRotation = MAX_ROTATION;
	}

	public int getResistance() {
		return resistance;
	}

	public void setResistance(int resistance) {
		this.resistance = resistance;
	}
}
