import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Components.Ammeter;
import Components.Cell;
import Components.Component;
import Components.CornerWire;
import Components.GridPoint;
import Components.LED;
import Components.Resistor;
import Components.Switch;
import Components.TripleWire;
import Components.Voltmeter;
import Components.Wire;

public class CircuitSimulator extends JPanel {
	private static final int GRID_START_X = 200;
	private static final int GRID_START_Y = 0;

	private static final int TILE_OFFSET_X = 50;
	private static final int TILE_OFFSET_Y = 50;
	
	private static final int GRID_WIDTH = 12;
	private static final int GRID_HEIGHT = 12;
	
	private static final int WIRE = 0;
	private static final int CORNERWIRE = 1;
	private static final int CELL = 2;
	private static final int VOLTMETER = 3;
	private static final int AMMETER = 4;
	private static final int RESISTOR = 5;
	private static final int LED = 6;
	private static final int SWITCH = 7;
	private static final int TRIPLEWIRE = 8;

	private Image imgBackground;
	private Image leftPanel;
	private Image rightPanel;

	private List<Component> components = new ArrayList<Component>();
	private List<GridPoint> gridPoints = new ArrayList<GridPoint>();
	
	private JFrame f;
	private JPanel panel;

	public CircuitSimulator() {
		URL urlBackgroundImg = getClass().getResource("/img/bg.png");
		imgBackground = new ImageIcon(urlBackgroundImg).getImage();
		URL urlLeftPanel = getClass().getResource("/img/leftpanel.png");
		leftPanel = new ImageIcon(urlLeftPanel).getImage();
		URL urlRightPanel = getClass().getResource("/img/rightpanel.png");
		rightPanel = new ImageIcon(urlRightPanel).getImage();

		for (int i = 0; i < GRID_HEIGHT; i++) {
			for (int j = 0; j < GRID_WIDTH; j++) {
				gridPoints.add(new GridPoint(GRID_START_X + TILE_OFFSET_X * j, GRID_START_Y + TILE_OFFSET_Y * i, false));
			}
		}
		
		createAndAddComponent(WIRE);
		createAndAddComponent(CORNERWIRE);
		createAndAddComponent(CELL);
		createAndAddComponent(VOLTMETER);
		createAndAddComponent(AMMETER);
		createAndAddComponent(RESISTOR);
		createAndAddComponent(LED);
		createAndAddComponent(SWITCH);
		createAndAddComponent(TRIPLEWIRE);

		DragAndDropListener listener = new DragAndDropListener(this.components, this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		f = new JFrame();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
		f.setResizable(false);
		//f.setSize(this.imgBackground.getWidth(null), this.imgBackground.getHeight(null));
		f.setSize(800, 630);
		//f.pack();
		f.setTitle("CIRCUIT SIMULATOR - GROUP S");
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
	
	public boolean checkIfOccupied(GridPoint gp) {
		if (gp.isOccupied()) return true;
		else return false;
	}
	
	public void createAndAddComponent(int type) {
		Image img = null;
		Component component = null;
		if (type == 0) {
			component = new Wire(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "wire.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "wire-v.png")).getImage();
			component.addImage(img);
			component.setY(75);
			component.setX(12);
		}
		if (type == 1) {
			component = new CornerWire(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "wirecorner1.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "wirecorner2.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "wirecorner3.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "wirecorner4.png")).getImage();
			component.addImage(img);
			component.setY(75);
			component.setX(72);
		}
		if (type == 2) {
			component = new Cell(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "cell.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "cell-v.png")).getImage();
			component.addImage(img);
			component.setY(355);
			component.setX(12);
		}
		if (type == 3) {
			component = new Voltmeter(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "voltmeter.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "voltmeter-v.png")).getImage();
			component.addImage(img);
			component.setY(215);
			component.setX(12);
		}
		if (type == 4) {
			component = new Ammeter(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "ammeter.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "ammeter-v.png")).getImage();
			component.addImage(img);
			component.setY(215);
			component.setX(72);
		}
		if (type == 5) {
			component = new Resistor(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "resistor.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "resistor-v.png")).getImage();
			component.addImage(img);
			component.setY(355);
			component.setX(72);
		}
		if (type == 6) {
			component = new LED(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "led.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "led-v.png")).getImage();
			component.addImage(img);
			component.setY(355);
			component.setX(132);
		}
		if (type == 7) {
			component = new Switch(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "switchclosed.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "switchclosed-v.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "switchopen.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "switchopen-v.png")).getImage();
			component.addImage(img);
			component.setY(415);
			component.setX(12);
		}
		if (type == 8) {
			component = new TripleWire(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "triplewire1.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "triplewire2.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "triplewire3.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "triplewire4.png")).getImage();
			component.addImage(img);
			component.setY(75);
			component.setX(132);
		}
		component.setInitial(true);
		this.components.add(component);
	}
			
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(leftPanel, 0, 0, null);
		g.drawImage(rightPanel, 800, 0, null);
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
