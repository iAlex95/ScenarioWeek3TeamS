package Components;

public class TripleWire extends Component {
	
	private final static int MAX_ROTATION = 4;
	
	public TripleWire(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		maxRotation = MAX_ROTATION;
	}

}
