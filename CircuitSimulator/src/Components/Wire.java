package Components;


public class Wire extends Component {

	private final static int MAX_ROTATION = 2;
	
	public Wire(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		maxRotation = MAX_ROTATION;
	}
	
}
