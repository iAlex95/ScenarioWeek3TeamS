import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import Components.Button;
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
	private static final int BUTTON = 9;

	private Image leftPanel;
	private Image rightPanel;

	private List<Component> initialComponents = new ArrayList<Component>();
	private List<Component> components = new ArrayList<Component>();
	private List<GridPoint> gridPoints = new ArrayList<GridPoint>();
	
	private JFrame f;
	private CircuitSimulator simulator = this;
	private DragAndDropListener listener;
	private boolean listenerActive = true;
	
	private int selectedComponentType = -1;
	private Component selectedComponent = null;
	private GridPoint highlightPoint = null;
	
	private sqliteConnection sqc;
	private Connection c = null;
	private boolean cellPlaced = false;
	
	private boolean circuitRunning = false;
	private double VoltageReading;
	private double CurrentReading;
	private double ResistanceReading;
	private boolean MaxVoltage;
	private JTextArea runMessage;

	public CircuitSimulator() {
		URL urlLeftPanel = getClass().getResource("/img/leftpanel.png");
		leftPanel = new ImageIcon(urlLeftPanel).getImage();
		URL urlRightPanel = getClass().getResource("/img/rightpanel.png");
		rightPanel = new ImageIcon(urlRightPanel).getImage();

		for (int i = 0; i < GRID_HEIGHT; i++) {
			for (int j = 0; j < GRID_WIDTH; j++) {
				gridPoints.add(new GridPoint(GRID_START_X + TILE_OFFSET_X * j, GRID_START_Y + TILE_OFFSET_Y * i, false));
			}
		}
			
		//setGridConnections();
		
		createInitialComponentList();

		listener = new DragAndDropListener(initialComponents, components, this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);

		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);
		f.setResizable(false);
		f.setSize(1000, 658);
		
		addMenuBar();
		
		f.setVisible(true);
		sqc = new sqliteConnection(f);
		c = sqc.dbConnector();
		
		this.setLayout(null);
		
		removeAll();
		repaint();
	}
	
	
	public void setGridConnections(Component c, GridPoint gp) {
		if (!(c instanceof CornerWire) && !(c instanceof TripleWire)) {
			if (c.getRotation() == 0) {
				if ((gridPoints.indexOf(gp)+1)%(GRID_WIDTH) != 0) gp.setRight(gridPoints.get(gridPoints.indexOf(gp)+1));
				if (gridPoints.indexOf(gp)%GRID_WIDTH != 0)gp.setLeft(gridPoints.get(gridPoints.indexOf(gp)-1));
			}
			if (c.getRotation() == 1) {
				if (gridPoints.indexOf(gp) > GRID_WIDTH-1) gp.setTop(gridPoints.get(gridPoints.indexOf(gp)-GRID_WIDTH));
				if (gridPoints.indexOf(gp) < (GRID_WIDTH * GRID_HEIGHT) - GRID_WIDTH) gp.setBot(gridPoints.get(gridPoints.indexOf(gp)+GRID_WIDTH));
			}
		}
		
		if (c instanceof CornerWire) {
			if (c.getRotation() == 0) {
				if (gridPoints.indexOf(gp) > GRID_WIDTH-1) gp.setTop(gridPoints.get(gridPoints.indexOf(gp)-GRID_WIDTH));
				if ((gridPoints.indexOf(gp)+1)%(GRID_WIDTH) != 0) gp.setRight(gridPoints.get(gridPoints.indexOf(gp)+1));
			}
			if (c.getRotation() == 1) {
				if ((gridPoints.indexOf(gp)+1)%(GRID_WIDTH) != 0) gp.setRight(gridPoints.get(gridPoints.indexOf(gp)+1));
				if (gridPoints.indexOf(gp) < (GRID_WIDTH * GRID_HEIGHT) - GRID_WIDTH) gp.setBot(gridPoints.get(gridPoints.indexOf(gp)+GRID_WIDTH));
			}
			if (c.getRotation() == 2) {
				if (gridPoints.indexOf(gp) < (GRID_WIDTH * GRID_HEIGHT) - GRID_WIDTH) gp.setBot(gridPoints.get(gridPoints.indexOf(gp)+GRID_WIDTH));
				if (gridPoints.indexOf(gp)%GRID_WIDTH != 0)gp.setLeft(gridPoints.get(gridPoints.indexOf(gp)-1));
			}
			if (c.getRotation() == 3) {
				if (gridPoints.indexOf(gp) > GRID_WIDTH-1) gp.setTop(gridPoints.get(gridPoints.indexOf(gp)-GRID_WIDTH));
				if (gridPoints.indexOf(gp)%GRID_WIDTH != 0)gp.setLeft(gridPoints.get(gridPoints.indexOf(gp)-1));
			}
		}
		
		if (c instanceof TripleWire) {
			if (c.getRotation() == 0) {
				if (gridPoints.indexOf(gp)%GRID_WIDTH != 0)gp.setLeft(gridPoints.get(gridPoints.indexOf(gp)-1));
				if (gridPoints.indexOf(gp) > GRID_WIDTH-1) gp.setTop(gridPoints.get(gridPoints.indexOf(gp)-GRID_WIDTH));
				if ((gridPoints.indexOf(gp)+1)%(GRID_WIDTH) != 0) gp.setRight(gridPoints.get(gridPoints.indexOf(gp)+1));
			}
			if (c.getRotation() == 1) {
				if (gridPoints.indexOf(gp) > GRID_WIDTH-1) gp.setTop(gridPoints.get(gridPoints.indexOf(gp)-GRID_WIDTH));
				if ((gridPoints.indexOf(gp)+1)%(GRID_WIDTH) != 0) gp.setRight(gridPoints.get(gridPoints.indexOf(gp)+1));
				if (gridPoints.indexOf(gp) < (GRID_WIDTH * GRID_HEIGHT) - GRID_WIDTH) gp.setBot(gridPoints.get(gridPoints.indexOf(gp)+GRID_WIDTH));
			}
			if (c.getRotation() == 2) {
				if ((gridPoints.indexOf(gp)+1)%(GRID_WIDTH) != 0) gp.setRight(gridPoints.get(gridPoints.indexOf(gp)+1));
				if (gridPoints.indexOf(gp) < (GRID_WIDTH * GRID_HEIGHT) - GRID_WIDTH) gp.setBot(gridPoints.get(gridPoints.indexOf(gp)+GRID_WIDTH));
				if (gridPoints.indexOf(gp)%GRID_WIDTH != 0)gp.setLeft(gridPoints.get(gridPoints.indexOf(gp)-1));
			}
			if (c.getRotation() == 3) {
				if (gridPoints.indexOf(gp) < (GRID_WIDTH * GRID_HEIGHT) - GRID_WIDTH) gp.setBot(gridPoints.get(gridPoints.indexOf(gp)+GRID_WIDTH));
				if (gridPoints.indexOf(gp) > GRID_WIDTH-1) gp.setTop(gridPoints.get(gridPoints.indexOf(gp)-GRID_WIDTH));
				if (gridPoints.indexOf(gp)%GRID_WIDTH != 0)gp.setLeft(gridPoints.get(gridPoints.indexOf(gp)-1));
			}
		}
	}
	
	public void removeGridConnections(GridPoint gp) {
		gp.setTop(null);
		gp.setRight(null);
		gp.setBot(null);
		gp.setLeft(null);
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
						JOptionPane.showMessageDialog(f, "Please Enter a Name");
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
						JOptionPane.showMessageDialog(f, "Circuit saved.");
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
					JOptionPane.showMessageDialog(f, "Circuit saved.");
					
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
					
					Object value = JOptionPane.showInputDialog(f, "Select a circuit", "Load a saved circuit", JOptionPane.OK_CANCEL_OPTION, null, r, r[0]);
					System.out.println(results);
					
					String query2 = "SELECT name, type, rotation, x, y, voltage, resistance FROM ComponentInfo WHERE name=?";
					PreparedStatement pst2 = c.prepareStatement(query2);
					pst2.setString(1, (String) value);
					ResultSet rs2 = pst2.executeQuery();
					
					resetAll();
					
					while(rs2.next()) {
											
						int type = rs2.getInt("type");
						int rotation = rs2.getInt("rotation");
						int x = rs2.getInt("x");
						int y = rs2.getInt("y");
						
						int voltage = rs2.getInt("voltage");
						int resistance = rs2.getInt("resistance");
						
						//ADD COMPONENT TO LIST
						createComponent(x,y,type,voltage,resistance,rotation, false);
						
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
		createComponent(12, 75, WIRE, 0, 0, 0, true);
		createComponent(72, 75, CORNERWIRE, 0, 0, 0, true);
		createComponent(12, 355, CELL, 0, 0, 0, true);
		createComponent(12, 215, VOLTMETER, 0, 0, 0, true);
		createComponent(72, 215, AMMETER, 0, 0, 0, true);
		createComponent(72, 355, RESISTOR, 0, 0, 0, true);
		createComponent(132, 355, LED, 0, 0, 0, true);
		createComponent(12, 415, SWITCH, 0, 0, 0, true);
		createComponent(132, 75, TRIPLEWIRE, 0, 0, 0, true);
		createComponent(72, 415, BUTTON, 0, 0, 0, true);
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
	
	public void createComponent(int x, int y, int type, int voltage, int resistance, int rotation, boolean initial) {
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
			if (!initial) ((Cell)component).setVoltage(voltage);
			img = new ImageIcon(getClass().getResource("/img/" + "cell.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "cell-v.png")).getImage();
			component.addImage(img);
			if (!initial) setCellPlaced(true);
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
			if (!initial) ((Resistor) component).setResistance(resistance);
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
		if (type == 9) {
			component = new Button(0, 0, type);
			img = new ImageIcon(getClass().getResource("/img/" + "buttonclosed.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "buttonclosed-v.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "buttonopen.png")).getImage();
			component.addImage(img);
			img = new ImageIcon(getClass().getResource("/img/" + "buttonopen-v.png")).getImage();
			component.addImage(img);
		}
		component.setX(x);
		component.setY(y);
		
		if (initial) {
			component.setInitial(true);
			this.initialComponents.add(component);
		} else {
			component.setRotation(rotation);
			GridPoint gp = checkPoint(x, y);
			component.setGridPoint(gp);
			gp.setComponent(component);
			setGridConnections(component, gp);
			this.components.add(component);
		}
	
	}
			
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); 
		Graphics2D g2d = (Graphics2D) g.create();
		g.drawImage(leftPanel, 0, 0, null);
		g.drawImage(rightPanel, 800, 0, null);
		for (GridPoint point : gridPoints) {
			g.drawImage(point.getImage(), point.getX(), point.getY(), null);
		}
		for (Component component : components) {
			if (component instanceof Switch) {
				if (((Switch)component).isOpen()) {
					g.drawImage(component.getImage(component.getRotation()+2), component.getX(), component.getY(), null);
					continue;
				}
			}
			if (component instanceof Button) {
				if (((Button)component).isOpen()) {
					g.drawImage(component.getImage(component.getRotation()+2), component.getX(), component.getY(), null);
					continue;
				}
			}
			if (component instanceof LED && circuitRunning) {}
			g.drawImage(component.getImage(component.getRotation()), component.getX(), component.getY(), null);
		}
		for (Component component : initialComponents) {
			g.drawImage(component.getImage(component.getRotation()), component.getX(), component.getY(), null);
		}
		
		if (highlightPoint != null) {
            Rectangle bounds = new Rectangle(highlightPoint.getX(), highlightPoint.getY(), 50, 50);
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.RED);
            g2d.draw(bounds);
        }
		
		addProperties();
		addButtons();
		//validate();
		
		if (circuitRunning) {
			add(runMessage);
		}
		
		setSelectedComponent(-1, null);
	}
	
	
	public void resetAll() {
		components.clear();
        initialComponents.clear();
        
        selectedComponentType = -1;
    	selectedComponent = null;
    	highlightPoint = null;
    	cellPlaced = false;
        
        createInitialComponentList();
        
        for (GridPoint gp : gridPoints) {
        	gp.setOccupied(false);
        	removeGridConnections(gp);
        }
        
        removeAll();
        repaint();
	}
	
	public void circuitValidation() {
		CircuitChecker checker = new CircuitChecker(components, gridPoints, simulator);
		
		int circuitCompletion = checker.checkCircuit();
		
		if (circuitCompletion == 0) {runMessage.setText("NO POWER SOURCE!");};
		if (circuitCompletion == 1) {runMessage.setText("BROKEN OR UNRECOGNIZED \nCIRCUIT!");};
		if (circuitCompletion == 2) {
			Calculation calculator = new Calculation(components);
			VoltageReading = calculator.voltage();
			CurrentReading = calculator.current();
			ResistanceReading = calculator.resistance();
			MaxVoltage = calculator.maxVoltageCircuit();
			
			if (MaxVoltage) { 
				runMessage.setText("VOLTAGE TOO HIGH! \nLOWER AT POWER SOURCE!");
			} else {
				runMessage.setText("CIRCUIT RUNNING.");
				
				for (Component c : components) {
					if (c instanceof Ammeter) {
						((Ammeter) c).setReadCurrent((double)Math.round(CurrentReading * 100d) / 100d);
					}
					if (c instanceof Voltmeter) {
						((Voltmeter) c).setReadVoltage((double)Math.round(checker.getVoltmeterReading(c) * 100d) / 100d);
					}
				}
			}	
		}
	}
	
	public void addButtons() {
		if (!(circuitRunning)) {
			JButton clearButton = new JButton("Clear Canvas");
			clearButton.setBounds(810-(clearButton.getWidth()/2),530-(clearButton.getHeight()/2),175,20);
			add(clearButton);
			
			clearButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
		            resetAll();
		        }          
		     });
		}
		
		if (!circuitRunning) {
			JButton runButton = new JButton("Run");
			runButton.setBounds(810-(runButton.getWidth()/2),560-(runButton.getHeight()/2),80,20);
			add(runButton);
			
			runButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					simulator.circuitRunning = true;
					simulator.listenerActive = false;
					selectedComponentType = -1;
					selectedComponent = null;
					highlightPoint = null;
					
					runMessage = new JTextArea(0, 20);
					runMessage.setEditable(false);
					runMessage.setOpaque(false);
					runMessage.setFont(new Font("Arial", Font.BOLD, 12));
					runMessage.setText("");
					runMessage.setBounds(810-(runMessage.getWidth()/2),490-(runMessage.getHeight()/2),175,50);
					
					removeAll();
			        repaint();
					
					circuitValidation();
					add(runMessage);
					
				}
			});
			
		} else {
			
			JButton stopButton = new JButton("Stop");
			stopButton.setBounds(810-(stopButton.getWidth()/2),560-(stopButton.getHeight()/2),80,20);
			add(stopButton);
			stopButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					simulator.listenerActive = true;
					
					simulator.circuitRunning = false;
					
					selectedComponentType = -1;
					selectedComponent = null;
					highlightPoint = null;
					
					for (Component c : components) {
						if (c instanceof Ammeter) {
							((Ammeter) c).setReadCurrent(0);
						}
						if (c instanceof Voltmeter) {
							((Voltmeter) c).setReadVoltage(0);
						}
					}
					
					removeAll();
			        repaint();
				}
			});
		}
	
		JButton helpButton = new JButton("Help");
		helpButton.setBounds(905-(helpButton.getWidth()/2),560-(helpButton.getHeight()/2),80,20);
		add(helpButton);
		
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "Help Information: \n"
						+ "	- Drag components from the left panel onto canvas\n"
						+ "	- Right-click on a component to rotate it\n"
						+ "	- Clicking on a component will display properties on right panel, some of which are editable\n"
						+ "\n"
						+ "Other Information: \n"
						+ "	- You may only add one power source (Cell) to the canvas\n"
						+ "	- Make sure to have a complete circuit\n"
						+ "	- Make sure to not set voltage of power source too high\n"
						+ "	- This application only recognizes series circuits\n"
						+ "\n"
						+ "Application developed by UCL Group S\n"
						+ "Jamie Law, Jasmine Lu, Yee Chong Tan, Alexander Xu";
				JOptionPane helpInfo = new JOptionPane();
				helpInfo.showMessageDialog(f, message, "Help", JOptionPane.INFORMATION_MESSAGE);
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
			
			JSlider voltage = new JSlider(JSlider.HORIZONTAL, 0, 30, cell.getVoltage());
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
			
			JSlider voltage = new JSlider(JSlider.HORIZONTAL, 0, 30, resistor.getResistance());
			voltage.setBounds(812-(voltage.getWidth()/2),100-(voltage.getHeight()/2),150,30);
			voltage.setOpaque(false);
			add(voltage);
			voltage.addChangeListener(new SliderListener(resistor, displayValue));
		}
		
		if (selectedComponentType == SWITCH) {
			Switch sw = (Switch) selectedComponent;
			JButton closeButton = new JButton("Close Switch");
			closeButton.setBounds(810-(closeButton.getWidth()/2),85-(closeButton.getHeight()/2),175,20);
			add(closeButton);
			
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sw.setOpen(false);
		            removeAll();
		            repaint();
		            setSelectedComponent(SWITCH, sw);
		            if (circuitRunning) circuitValidation();
		        }          
		    });
			
			JButton openButton = new JButton("Open Switch");
			openButton.setBounds(810-(openButton.getWidth()/2),115-(openButton.getHeight()/2),175,20);
			add(openButton);
			
			openButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sw.setOpen(true);
		            removeAll();
		            repaint();
		            setSelectedComponent(SWITCH, sw);
		            if (circuitRunning) circuitValidation();
		        }          
		    });
		}
		
		if (selectedComponentType == BUTTON) {
			Button btn = (Button) selectedComponent;
			JButton closeButton = new JButton("Button Down");
			closeButton.setBounds(810-(closeButton.getWidth()/2),85-(closeButton.getHeight()/2),175,20);
			add(closeButton);
			
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btn.setOpen(false);
		            removeAll();
		            repaint();
		            setSelectedComponent(BUTTON, btn);
		            if (circuitRunning) circuitValidation();
		        }          
		    });
			
			JButton openButton = new JButton("Button Up");
			openButton.setBounds(810-(openButton.getWidth()/2),115-(openButton.getHeight()/2),175,20);
			add(openButton);
			
			openButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					btn.setOpen(true);
		            removeAll();
		            repaint();
		            setSelectedComponent(BUTTON, btn);
		            if (circuitRunning) circuitValidation();
		        }          
		    });
		}
		
		if (selectedComponentType == LED) {
			LED led = (LED) selectedComponent;
			
			JTextArea voltageText = new JTextArea(0, 20);
			voltageText.setEditable(false);
			voltageText.setOpaque(false);
			voltageText.setFont(new Font("Arial", Font.BOLD, 12));
			voltageText.setText("VOLTAGE VALUE:  " + led.getVoltage() + "V" );
			voltageText.setBounds(824-(voltageText.getWidth()/2),85-(voltageText.getHeight()/2),175,20);
			add(voltageText);
			
			JTextArea powerText = new JTextArea(0, 20);
			powerText.setEditable(false);
			powerText.setOpaque(false);
			powerText.setFont(new Font("Arial", Font.BOLD, 12));
			powerText.setText("POWER VALUE:  " + led.getPower() + "W" );
			powerText.setBounds(824-(powerText.getWidth()/2),105-(powerText.getHeight()/2),175,20);
			add(powerText);
			
			JTextArea resistanceText = new JTextArea(0, 20);
			resistanceText.setEditable(false);
			resistanceText.setOpaque(false);
			resistanceText.setFont(new Font("Arial", Font.BOLD, 12));
			resistanceText.setText("RESISTANCE VALUE:  " + led.getResistance() + "Ω");
			resistanceText.setBounds(824-(resistanceText.getWidth()/2),125-(resistanceText.getHeight()/2),175,20);
			add(resistanceText);
		}
		
		if (selectedComponentType == VOLTMETER) {
			Voltmeter voltmeter = (Voltmeter) selectedComponent;
			
			JTextArea textArea = new JTextArea(0, 20);
			textArea.setEditable(false);
			textArea.setOpaque(false);
			textArea.setFont(new Font("Arial", Font.BOLD, 12));
			textArea.setText("VOLTAGE READING: ");
			textArea.setBounds(824-(textArea.getWidth()/2),85-(textArea.getHeight()/2),175,20);
			add(textArea);
			
			JTextArea reading = new JTextArea(0, 20);
			reading.setEditable(false);
			reading.setOpaque(false);
			reading.setFont(new Font("Arial", Font.BOLD, 24));
			reading.setText(voltmeter.getReadVoltage() + "V");
			reading.setBounds(850-(reading.getWidth()/2),115-(reading.getHeight()/2),175,40);
			add(reading);
		}
		
		if (selectedComponentType == AMMETER) {
			Ammeter ammeter = (Ammeter) selectedComponent;
			
			JTextArea textArea = new JTextArea(0, 20);
			textArea.setEditable(false);
			textArea.setOpaque(false);
			textArea.setFont(new Font("Arial", Font.BOLD, 12));
			textArea.setText("CURRENT READING: ");
			textArea.setBounds(824-(textArea.getWidth()/2),85-(textArea.getHeight()/2),175,20);
			add(textArea);
			
			JTextArea reading = new JTextArea(0, 20);
			reading.setEditable(false);
			reading.setOpaque(false);
			reading.setFont(new Font("Arial", Font.BOLD, 24));
			reading.setText(ammeter.getReadCurrent() + "A");
			reading.setBounds(850-(reading.getWidth()/2),115-(reading.getHeight()/2),175,40);
			add(reading);
		}
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
	
	public boolean getListenerActive() {
		return listenerActive;
	}

	public double getCurrentReading() {
		return CurrentReading;
	}
	
	public static void main(String[] args) {
		new CircuitSimulator();
	}

}
