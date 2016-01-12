package Components;
import java.awt.Image;

import javax.swing.ImageIcon;


public class GridPoint {
	private int x;
	private int y;
	private boolean occupied;
	private Image img;
	
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
	
	

}
