import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CircuitSimulator extends JPanel {

	//private static final long serialVersionUID = 3114147670071466558L;
	
	private static final int GRID_START_X = 301;
	private static final int GRID_START_Y = 51;

	private static final int TILE_OFFSET_X = 50;
	private static final int TILE_OFFSET_Y = 50;
	
	private static final int VOLTMETER = 0;
	private static final int CELL = 1;
	private static final int RESISTOR = 2;

	private Image imgBackground;

	private List<Component> components = new ArrayList<Component>();
	private int[][] pointX = new int[8][8];
	private int[][] pointY = new int[8][8];
	private List<GridPoint> gridPoints = new ArrayList<GridPoint>();

	public CircuitSimulator() {
		URL urlBackgroundImg = getClass().getResource("/img/bg.png");
		this.imgBackground = new ImageIcon(urlBackgroundImg).getImage();

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				gridPoints.add(new GridPoint(GRID_START_X + TILE_OFFSET_X * j, GRID_START_Y + TILE_OFFSET_Y * i, false));
				pointX[i][j] = GRID_START_X + TILE_OFFSET_X * j;
				pointY[i][j] = GRID_START_Y + TILE_OFFSET_Y * i;
			}
		}
		
		createAndAddComponent(0);
		//createAndAddComponent(1);
		createAndAddComponent(2);
		createAndAddComponent(3);

		DragAndDropListener listener = new DragAndDropListener(this.components, this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		JFrame f = new JFrame();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
		f.setResizable(false);
		f.setSize(this.imgBackground.getWidth(null), this.imgBackground.getHeight(null));
	}

	public GridPoint checkPoint(int x, int y) {
		for (GridPoint gp : gridPoints) {
			if (x >= gp.getX() && x < gp.getX()+TILE_OFFSET_Y &&
					y >= gp.getY() && y < gp.getY()+TILE_OFFSET_Y) {
				return gp;
			}
		}
		return null;
	}
	
	public int findPointY(int y) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (y >= pointY[i][j] && y < pointY[i][j]+TILE_OFFSET_Y) {
					return pointY[i][j];
				}
			}
		}
		
		return 0;
	}
	
	public boolean checkIfOccupied(GridPoint gp) {
		if (gp.isOccupied()) return true;
		else return false;
	}
	
	public void createAndAddComponent(int type) {
		Image img = null;
		Component component = null;
		component = new Component(0, 0, type);
		if (type == 0) {
			img = new ImageIcon(getClass().getResource("/img/" + "wire.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "wire-v.png")).getImage();
			component.addImage(img);
			component.setY(0);
		}
		if (type == 1) {
			img = new ImageIcon(getClass().getResource("/img/" + "voltmeter.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "voltmeter-v.png")).getImage();
			component.addImage(img);
			component.setY(50);
		}
		if (type == 2) {
			img = new ImageIcon(getClass().getResource("/img/" + "cell.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "cell-v.png")).getImage();
			component.addImage(img);
			component.setY(100);
		}
		if (type == 3) {
			img = new ImageIcon(getClass().getResource("/img/" + "resistor.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "resistor-v.png")).getImage();
			component.addImage(img);
			component.setY(150);
		}
		component.setInitial(true);
		this.components.add(component);
	}
			
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//g.drawImage(this.imgBackground, 0, 0, null);
		for (GridPoint point : gridPoints) {
			g.drawImage(point.getImage(), point.getX(), point.getY(), null);
		}
		for (Component component : components) {
				g.drawImage(component.getImage(component.getRotation()), component.getX(), component.getY(), null);
		}
	}
	
	public void removeComponent(Component component) {
		this.components.remove(component);
	}

	public static void main(String[] args) {
		new CircuitSimulator();
	}

}
