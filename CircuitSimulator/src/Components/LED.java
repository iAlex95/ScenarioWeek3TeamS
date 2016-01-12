package Components;


public class LED extends Component {

	private float intensity;
	private final static int MAX_ROTATION = 2;
	
	public LED(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		maxRotation = MAX_ROTATION;
	}
	
}
