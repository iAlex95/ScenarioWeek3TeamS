import java.awt.Image;
import java.awt.List;
import java.util.ArrayList;


public class Component {
	
	private ArrayList<Image> img = new ArrayList<Image>();
	private int x;
	private int y;
	private boolean component;
	private boolean initial;
	private GridPoint gridPoint;
	private int rotation = 0;
	private int type;

	public Component(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	public Image getImage(int rotation) {
		return img.get(rotation);
	}
	
	public void addImage(Image img) {
		this.img.add(img);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return img.get(0).getHeight(null);
	}

	public int getHeight() {
		return img.get(0).getHeight(null);
	}
	
	public void setComponent(boolean b) {
		this.component = b;
	}
	
	public boolean getComponent() {
		return this.component;
	}
	
	public void setInitial(boolean b) {
		this.initial = b;
	}
	
	public boolean getInitial() {
		return this.initial;
	}
	
	public void setGridPoint(GridPoint gp) {
		this.gridPoint = gp;
	}
	
	public GridPoint getGridPoint() {
		return this.gridPoint;
	}
	public void setRotation(int r) {
		this.rotation = r;
	}
	public int getRotation() {
		return this.rotation;
	}
	
	public int getType() {
		return this.type;
	}

}
