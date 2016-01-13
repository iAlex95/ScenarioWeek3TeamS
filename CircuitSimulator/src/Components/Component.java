package Components;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JSlider;


public abstract class Component {
	
	protected int x;
	protected int y;
	protected boolean initial;
	protected GridPoint gridPoint;
	protected ArrayList<Image> img = new ArrayList<Image>();
	protected int rotation = 0;
	protected int maxRotation = 0;
	protected int type;

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

	public int getMaxRotation() {
		return maxRotation;
	}

	public void setMaxRotation(int maxRotation) {
		this.maxRotation = maxRotation;
	}

}
