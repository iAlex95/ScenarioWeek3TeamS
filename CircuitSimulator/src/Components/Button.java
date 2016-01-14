package Components;

public class Button extends Component {

	private boolean open;
	private final static int MAX_ROTATION = 2;
	
	public Button(int x, int y, int type) {
		super.x = x;
		super.y = y;
		super.type = type;
		maxRotation = MAX_ROTATION;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
}
