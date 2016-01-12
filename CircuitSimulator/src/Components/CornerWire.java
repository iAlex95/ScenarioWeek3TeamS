package Components;


public class CornerWire extends Component {

	private final static int MAX_ROTATION = 4;
	
	public CornerWire(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		maxRotation = MAX_ROTATION;
	}
	
}
