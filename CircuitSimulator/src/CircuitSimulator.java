import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;

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

	//private Image imgBackground;
	private Image leftPanel;
	private Image rightPanel;

	private List<Component> initialComponents = new ArrayList<Component>();
	private List<Component> components = new ArrayList<Component>();
	private List<GridPoint> gridPoints = new ArrayList<GridPoint>();
	
	private JFrame f;
	
	private int selectedComponentType = -1;
	private Component selectedComponent = null;
	private GridPoint highlightPoint = null;
	
	private Connection c = null;
	private boolean cellPlaced = false;

	public CircuitSimulator() {
		c = sqliteConnection.dbConnector();
		//URL urlBackgroundImg = getClass().getResource("/img/bg.png");
		//imgBackground = new ImageIcon(urlBackgroundImg).getImage();
		URL urlLeftPanel = getClass().getResource("/img/leftpanel.png");
		leftPanel = new ImageIcon(urlLeftPanel).getImage();
		URL urlRightPanel = getClass().getResource("/img/rightpanel.png");
		rightPanel = new ImageIcon(urlRightPanel).getImage();

		for (int i = 0; i < GRID_HEIGHT; i++) {
			for (int j = 0; j < GRID_WIDTH; j++) {
				gridPoints.add(new GridPoint(GRID_START_X + TILE_OFFSET_X * j, GRID_START_Y + TILE_OFFSET_Y * i, false));
			}
		}
		
		createInitialComponentList();

		DragAndDropListener listener = new DragAndDropListener(initialComponents, components, this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
		f.setResizable(false);
		f.setSize(1000, 650);
		
		addMenuBar();
		
		f.setVisible(true);
		
		this.setLayout(null);
		
		removeAll();
	}
	
	public void addMenuBar() {
		
		JMenuBar menuBar = new JMenuBar();
		f.setJMenuBar(menuBar);
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
		JMenuItem open = new JMenuItem("Open");
		file.add(open);
		JMenuItem saveAs = new JMenuItem("Save as...");
		file.add(saveAs);
		JMenuItem save = new JMenuItem("Save");
		file.add(save);
		
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					String name = JOptionPane.showInputDialog("Name of circuit:");
					
					if (name == null) {
						JOptionPane.showMessageDialog(null, "Please Enter a Name");
					}
					else {
						
						for (Component component : components) {
							
							String query = "INSERT INTO ComponentInfo (name, type, rotation, x, y, voltage, resistance) values (?,?,?,?,?,?,?)";
							PreparedStatement pst = c.prepareStatement(query);
							pst.setString(1, name);
							pst.setInt(2, component.getType());
							pst.setInt(3, component.getRotation());
							pst.setInt(4, component.getX());
							pst.setInt(5, component.getY());
							
							if (component instanceof Cell) {
								pst.setInt(6, ((Cell)component).getVoltage());
							}

							if (component instanceof Resistor) {
								pst.setInt(7, ((Resistor)component).getResistance());
							}
							
							pst.execute();
							pst.close();
						}	
						f.setTitle(name);
						JOptionPane.showMessageDialog(null, "Circuit saved.");
					}

				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		save.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					String query = "DELETE FROM ComponentInfo WHERE name='"+f.getTitle()+"'";
					PreparedStatement pst1 = c.prepareStatement(query);
					
					pst1.execute();
					pst1.close();
					
						
					for (Component component : components) {
							
						String query1 = "INSERT INTO ComponentInfo (name, type, rotation, x, y, voltage, resistance) values (?,?,?,?,?,?,?)";
						PreparedStatement pst = c.prepareStatement(query1);
						pst.setString(1, f.getTitle());
						pst.setInt(2, component.getType());
						pst.setInt(3, component.getRotation());
						pst.setInt(4, component.getX());
						pst.setInt(5, component.getY());
						
						if (component instanceof Cell) {
							pst.setInt(6, ((Cell)component).getVoltage());
						}

						if (component instanceof Resistor) {
							pst.setInt(7, ((Resistor)component).getResistance());
						}
							
						pst.execute();
						pst.close();
					}	
					JOptionPane.showMessageDialog(null, "Circuit saved.");
					
				}catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
			
		});
		
		open.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
					String query = "SELECT name FROM ComponentInfo GROUP BY name";
					PreparedStatement pst1 = c.prepareStatement(query);
					ResultSet rs1 = pst1.executeQuery();
					
					ArrayList <String> results = new ArrayList<String>();
					
					while(rs1.next()) {
						results.add(rs1.getString(1));
					}
					
					String[] r = (String[]) results.toArray(new String[results.size()]);
					
					Object value = JOptionPane.showInputDialog(null, "Select a circuit", "Load a saved circuit", JOptionPane.OK_CANCEL_OPTION, null, r, r[0]);
					System.out.println(results);
					
					String query2 = "SELECT name, type, rotation, x, y, voltage, resistance FROM ComponentInfo WHERE name=?";
					PreparedStatement pst2 = c.prepareStatement(query2);
					pst2.setString(1, (String) value);
					ResultSet rs2 = pst2.executeQuery();
					
					components.clear();
					
					while(rs2.next()) {
											
						int type = rs2.getInt("type");
						int rotation = rs2.getInt("rotation");
						int x = rs2.getInt("x");
						int y = rs2.getInt("y");
						
						int voltage = rs2.getInt("voltage");
						int resistance = rs2.getInt("resistance");
						
						//ADD COMPONENT TO LIST
						createComponent(x,y,type,voltage,resistance,rotation);
						
						//DRAW COMPONENT
						removeAll();
						repaint();
						
					}
					
					f.setTitle((String) value);
					System.out.println(components);
					
					pst1.close();
					rs1.close();
					pst2.close();
					rs2.close();
					
				}catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
			
		});
	}

	public void createInitialComponentList() {
		createAndAddComponent(WIRE);
		createAndAddComponent(CORNERWIRE);
		createAndAddComponent(CELL);
		createAndAddComponent(VOLTMETER);
		createAndAddComponent(AMMETER);
		createAndAddComponent(RESISTOR);
		createAndAddComponent(LED);
		createAndAddComponent(SWITCH);
		createAndAddComponent(TRIPLEWIRE);
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
	
	public void createComponent(int x, int y, int type, int voltage, int resistance, int rotation) {
		Image img = null;
		Component component = null;
		if (type == 0) {
			component = new Wire(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "wire.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "wire-v.png")).getImage();
			component.addImage(img);
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
		}
		if (type == 2) {
			component = new Cell(0, 0, type);
			((Cell)component).setVoltage(voltage);
			img = new ImageIcon(getClass().getResource("/img/" + "cell.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "cell-v.png")).getImage();
			component.addImage(img);
			setCellPlaced(true);
		}
		if (type == 3) {
			component = new Voltmeter(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "voltmeter.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "voltmeter-v.png")).getImage();
			component.addImage(img);
		}
		if (type == 4) {
			component = new Ammeter(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "ammeter.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "ammeter-v.png")).getImage();
			component.addImage(img);
		}
		if (type == 5) {
			component = new Resistor(0, 0, type);
			((Resistor) component).setResistance(resistance);
			img = new ImageIcon(getClass().getResource("/img/" + "resistor.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "resistor-v.png")).getImage();
			component.addImage(img);
		}
		if (type == 6) {
			component = new LED(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "led.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "led-v.png")).getImage();
			component.addImage(img);
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
		}
		component.setX(x);
		component.setY(y);
		component.setRotation(rotation);
		GridPoint gp = checkPoint(x, y);
		component.setGridPoint(gp);
		this.components.add(component);
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
		this.initialComponents.add(component);
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
		for (Component component : initialComponents) {
			g.drawImage(component.getImage(component.getRotation()), component.getX(), component.getY(), null);
		}
		
		if (highlightPoint != null) {
			Graphics2D g2d = (Graphics2D) g.create();
            Rectangle bounds = new Rectangle(highlightPoint.getX(), highlightPoint.getY(), 50, 50);
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.RED);
            g2d.draw(bounds);
        }
		
		addProperties();
		addButtons();
		//validate();
		
		setSelectedComponent(-1, null);
	}
	
	public void addButtons() {
		JButton clearButton = new JButton("Clear Canvas");
		clearButton.setBounds(810-(clearButton.getWidth()/2),530-(clearButton.getHeight()/2),175,20);
		add(clearButton);
		
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            components.clear();
	            
	            selectedComponentType = -1;
	        	selectedComponent = null;
	        	highlightPoint = null;
	            
	            createInitialComponentList();
	            
	            for (GridPoint gp : gridPoints) {
	            	gp.setOccupied(false);
	            }
	            
	            removeAll();
	            repaint();
	        }          
	     });
		
		JButton runButton = new JButton("Run");
		runButton.setBounds(810-(runButton.getWidth()/2),560-(runButton.getHeight()/2),80,20);
		add(runButton);
		
		JButton helpButton = new JButton("Help");
		helpButton.setBounds(905-(helpButton.getWidth()/2),560-(helpButton.getHeight()/2),80,20);
		add(helpButton);
		
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane helpInfo = new JOptionPane();
				helpInfo.showMessageDialog(new JFrame(), "Eggs are not supposed to be green.", "Help", JOptionPane.INFORMATION_MESSAGE);
	        }          
	     });

	}
	
	public void addProperties() {
		if (selectedComponentType == CELL) {
			Cell cell = (Cell) selectedComponent;
			
			JTextArea textArea = new JTextArea(0, 20);
			textArea.setEditable(false);
			textArea.setOpaque(false);
			textArea.setFont(new Font("Arial", Font.BOLD, 12));
			textArea.setText("VOLTAGE");
			textArea.setBounds(824-(textArea.getWidth()/2),85-(textArea.getHeight()/2),175,20);
			add(textArea);
			
			JTextArea displayValue = new JTextArea(0, 20);
			displayValue.setEditable(false);
			displayValue.setOpaque(false);
			displayValue.setFont(new Font("Arial", Font.BOLD, 12));
			displayValue.setText(Integer.toString(cell.getVoltage())+"V");
			displayValue.setBounds(960-(displayValue.getWidth()/2),106-(displayValue.getHeight()/2),30,20);
			add(displayValue);
			
			JSlider voltage = new JSlider(JSlider.HORIZONTAL, 0, 100, cell.getVoltage());
			voltage.setBounds(812-(voltage.getWidth()/2),100-(voltage.getHeight()/2),150,30);
			voltage.setOpaque(false);
			add(voltage);
			voltage.addChangeListener(new SliderListener(cell, displayValue));
		}
		if (selectedComponentType == RESISTOR) {
			Resistor resistor = (Resistor) selectedComponent;
			
			JTextArea textArea = new JTextArea(0, 20);
			textArea.setEditable(false);
			textArea.setOpaque(false);
			textArea.setFont(new Font("Arial", Font.BOLD, 12));
			textArea.setText("RESISTANCE");
			textArea.setBounds(824-(textArea.getWidth()/2),85-(textArea.getHeight()/2),175,20);
			add(textArea);
			
			JTextArea displayValue = new JTextArea(0, 20);
			displayValue.setEditable(false);
			displayValue.setOpaque(false);
			displayValue.setFont(new Font("Arial", Font.BOLD, 12));
			displayValue.setText(Integer.toString(resistor.getResistance())+"Ω");
			displayValue.setBounds(960-(displayValue.getWidth()/2),106-(displayValue.getHeight()/2),30,20);
			add(displayValue);
			
			JSlider voltage = new JSlider(JSlider.HORIZONTAL, 0, 100, resistor.getResistance());
			voltage.setBounds(812-(voltage.getWidth()/2),100-(voltage.getHeight()/2),150,30);
			voltage.setOpaque(false);
			add(voltage);
			voltage.addChangeListener(new SliderListener(resistor, displayValue));
		}
		if (selectedComponentType == SWITCH) {}
	}
			
	public void addComponent(Component component) {
		this.components.add(component);
	}
	
	public void removeComponent(Component component) {
		this.components.remove(component);
	}
	
	public void removeInitialComponent(Component component) {
		this.initialComponents.remove(component);
	}
	
	public void setSelectedComponent(int selected, Component c) {
		selectedComponentType = selected;
		selectedComponent = c;
	}
	
	public void setHighlightPoint(GridPoint p) {
		highlightPoint = p;
	}
	
	public void setCellPlaced(boolean b) {
		cellPlaced = b;
	}
	
	public boolean getCellPlaced() {
		return cellPlaced;
	}

	public static void main(String[] args) {
		new CircuitSimulator();
	}

}
