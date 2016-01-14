package Components;
import java.awt.Image;

import javax.swing.ImageIcon;


public class GridPoint {
	private int x;
	private int y;
	private boolean occupied;
	private Image img;
	private Component component;
	
	private GridPoint top;
	private GridPoint bot;
	private GridPoint left;
	private GridPoint right;

	public GridPoint(int x, int y, boolean occupied) {
		this.x = x;
		this.y = y;
		this.occupied = occupied;
		img =  new ImageIcon(getClass().getResource("/img/" + "gridsquare.png")).getImage();
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isOccupied() {
		return occupied;
	}
	
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	public void setImage(Image img) {
		this.img = img;
	}
	
	public Image getImage() {
		return this.img;
	}
	
	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public GridPoint getTop() {
		return top;
	}

	public void setTop(GridPoint top) {
		this.top = top;
	}

	public GridPoint getBot() {
		return bot;
	}

	public void setBot(GridPoint bot) {
		this.bot = bot;
	}

	public GridPoint getLeft() {
		return left;
	}

	public void setLeft(GridPoint left) {
		this.left = left;
	}

	public GridPoint getRight() {
		return right;
	}

	public void setRight(GridPoint right) {
		this.right = right;
	}
	
	

}
