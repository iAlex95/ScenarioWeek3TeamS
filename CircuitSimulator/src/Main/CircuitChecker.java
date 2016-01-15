package Main;
import java.util.ArrayList;
import java.util.List;

import Components.Button;
import Components.Cell;
import Components.Component;
import Components.GridPoint;
import Components.LED;
import Components.Resistor;
import Components.Switch;
import Components.TripleWire;
import Components.Voltmeter;


public class CircuitChecker {
	
	private CircuitSimulator simulator;
	private List<Component> components;
	private List<GridPoint> gridPoints;
	private List<Component> tempComponent;
	
	int counter;
	int tripleWireCount;
	int voltMeterCount;
	int[] visited;
	
	public CircuitChecker(List<Component> components, List<GridPoint> gridPoints, CircuitSimulator simulator) {
		this.simulator = simulator;
		this.components = components;
		this.gridPoints = gridPoints;
	}
	
	public int checkCircuit() {
		Component start = null;
		counter = 0;
		tripleWireCount = 0;
		voltMeterCount = 0;
		visited = new int[gridPoints.size()];
		
		for (int i = 0; i < gridPoints.size(); i++) {
			visited[i] = 0;
		}
		
		for (Component c : components) {
			if (c instanceof Cell) {
				start = c;
				if (recursiveCheck(start.getGridPoint()) == 1) {
					if (counter == components.size() && tripleWireCount == voltMeterCount*2 && counter > 2) return 2;
					else return 1;
				} else {
					return 1;
				}
			}
		}
		
		return 0;
	}
			
	public int recursiveCheck(GridPoint current) {
		visited[gridPoints.indexOf(current)] = 1;
		counter++;
		
		if (current.getComponent() instanceof Switch) {
			if (((Switch) current.getComponent()).isOpen() == true) return 0;
		}
		
		if (current.getComponent() instanceof Button) {
			if (((Button) current.getComponent()).isOpen() == true) return 0;
		}
		
		if (current.getComponent() instanceof Voltmeter) voltMeterCount++;
		
		if (current.getComponent() instanceof TripleWire) {
			tripleWireCount++;
			if (current.getTop() != null && current.getTop().getBot() != null && current.getLeft() != null && current.getLeft().getRight() != null) {
				if (current.getTop().getComponent() instanceof Cell || current.getLeft().getComponent() instanceof Cell) return 1;
				if (visited[gridPoints.indexOf(current.getTop())] == 0 && visited[gridPoints.indexOf(current.getLeft())] == 0) {
					if ((recursiveCheck(current.getTop()) == 1 || recursiveCheck(current.getLeft()) == 1) && counter == components.size()) return 1;
				}
			}
			if (current.getTop() != null && current.getTop().getBot() != null && current.getRight() != null && current.getRight().getLeft() != null) {
				if (current.getTop().getComponent() instanceof Cell || current.getRight().getComponent() instanceof Cell) return 1;
				if (visited[gridPoints.indexOf(current.getTop())] == 0 && visited[gridPoints.indexOf(current.getRight())] == 0) {
					if ((recursiveCheck(current.getTop()) == 1 || recursiveCheck(current.getRight()) == 1) && counter == components.size()) return 1;
				}
			}
			if (current.getRight() != null && current.getRight().getLeft() != null && current.getBot() != null && current.getBot().getTop() != null) {
				if (current.getRight().getComponent() instanceof Cell ||current.getBot().getComponent() instanceof Cell) return 1;
				if (visited[gridPoints.indexOf(current.getRight())] == 0 && visited[gridPoints.indexOf(current.getBot())] == 0) {
					if ((recursiveCheck(current.getRight()) == 1 || recursiveCheck(current.getBot()) == 1) && counter == components.size()) return 1;
				}
			} 
			if (current.getBot() != null && current.getBot().getTop() != null && current.getLeft() != null && current.getLeft().getRight() != null) {
				if (current.getBot().getComponent() instanceof Cell || current.getLeft().getComponent() instanceof Cell) return 1;
				if (visited[gridPoints.indexOf(current.getBot())] == 0 && visited[gridPoints.indexOf(current.getLeft())] == 0) {
					if ((recursiveCheck(current.getBot()) == 1 || recursiveCheck(current.getLeft()) == 1) && counter == components.size()) return 1;
				}
			}
			if (current.getRight() != null && current.getRight().getLeft() != null && current.getLeft() != null && current.getLeft().getRight() != null) {
				if (current.getRight().getComponent() instanceof Cell || current.getLeft().getComponent() instanceof Cell) return 1;
				if (visited[gridPoints.indexOf(current.getRight())] == 0 && visited[gridPoints.indexOf(current.getLeft())] == 0) {
					if ((recursiveCheck(current.getRight()) == 1 || recursiveCheck(current.getLeft()) == 1) && counter == components.size()) return 1;
				}
			} 
			if (current.getTop() != null && current.getTop().getBot() != null && current.getBot() != null && current.getBot().getTop() != null) {
				if (current.getTop().getComponent() instanceof Cell || current.getBot().getComponent() instanceof Cell) return 1;
				if (visited[gridPoints.indexOf(current.getTop())] == 0 && visited[gridPoints.indexOf(current.getBot())] == 0) {
					if ((recursiveCheck(current.getTop()) == 1 || recursiveCheck(current.getBot()) == 1) && counter == components.size()) return 1;
				}
			}
			
			return 0;
		}
			
		
		if (current.getTop() != null && current.getTop().getBot() != null) {
			if (current.getTop().getComponent() instanceof Cell) return 1;
			if (visited[gridPoints.indexOf(current.getTop())] == 0) {
				if (recursiveCheck(current.getTop()) == 1) return 1;
			}
		}
		if (current.getRight() != null && current.getRight().getLeft() != null) {
			if (current.getRight().getComponent() instanceof Cell) return 1;
			if (visited[gridPoints.indexOf(current.getRight())] == 0) {
				if (recursiveCheck(current.getRight()) == 1) return 1;
			}
		} 
		if (current.getBot() != null && current.getBot().getTop() != null) {
			if (current.getBot().getComponent() instanceof Cell) return 1;
			if (visited[gridPoints.indexOf(current.getBot())] == 0) {
				if (recursiveCheck(current.getBot()) == 1) return 1;
			}
		}
		if (current.getLeft() != null && current.getLeft().getRight() != null) {
			if (current.getLeft().getComponent() instanceof Cell) return 1;
			if (visited[gridPoints.indexOf(current.getLeft())] == 0) {
				if (recursiveCheck(current.getLeft()) == 1) return 1;
			}
		}
		
		return 0;
	}
	
	public double getVoltmeterReading(Component c) {
		tempComponent = new ArrayList<Component>();
		for (int i = 0; i < gridPoints.size(); i++) {
			visited[i] = 0;
		}
		
		TripleWire ftp = null;
		TripleWire stp = null;
		
		if (c.getRotation() == 0) {
			ftp = (TripleWire) (searchForTripleWire(c.getGridPoint().getLeft()));
			stp = (TripleWire) (searchForTripleWire(c.getGridPoint().getRight()));
		} else {
			ftp = (TripleWire) (searchForTripleWire(c.getGridPoint().getTop()));
			stp = (TripleWire) (searchForTripleWire(c.getGridPoint().getBot()));
		}
		
		for (int i = 0; i < gridPoints.size(); i++) {
			visited[i] = 0;
		}
		
		if (ftp != null && stp != null) {
			if (ftp.getGridPoint().getTop() != null && testDirection(ftp.getGridPoint().getTop()) == 1) {
				for (int i = 0; i < gridPoints.size(); i++) {
					visited[i] = 0;
				}
				getComponentsInBetween(ftp.getGridPoint().getTop());
			}
			else if (ftp.getGridPoint().getRight() != null && testDirection(ftp.getGridPoint().getRight()) == 1) {
					for (int i = 0; i < gridPoints.size(); i++) {
						visited[i] = 0;
					}
					getComponentsInBetween(ftp.getGridPoint().getRight());
				}
			else if (ftp.getGridPoint().getBot() != null && testDirection(ftp.getGridPoint().getBot()) == 1) {
					for (int i = 0; i < gridPoints.size(); i++) {
						visited[i] = 0;
					}
					getComponentsInBetween(ftp.getGridPoint().getBot());
				}
			else if (ftp.getGridPoint().getLeft() != null && testDirection(ftp.getGridPoint().getLeft()) == 1) {
					for (int i = 0; i < gridPoints.size(); i++) {
						visited[i] = 0;
					}
					getComponentsInBetween(ftp.getGridPoint().getLeft());
				}
		}
		
		
		int resistance = 0;
		for (Component tempc : tempComponent) {
			if (tempc instanceof Resistor) {
				resistance += ((Resistor)tempc).getResistance();
			}
			if (tempc instanceof LED) {
				resistance += ((LED)tempc).getResistance();
			}
		}
		
		double voltReading = simulator.getCurrentReading() * resistance;
		
		return voltReading;
	}
	
	public Component searchForTripleWire(GridPoint current) {
		visited[gridPoints.indexOf(current)] = 1;
		
		if (current.getTop() != null && current.getTop().getBot() != null) {
			if (current.getTop().getComponent() instanceof TripleWire) return current.getTop().getComponent();
			else if (visited[gridPoints.indexOf(current.getTop())] == 0 && !(current.getTop().getComponent() instanceof Voltmeter)) {
				return searchForTripleWire(current.getTop());
			}
		}
		if (current.getRight() != null && current.getRight().getLeft() != null) {
			if (current.getRight().getComponent() instanceof TripleWire) return current.getRight().getComponent();
			if (visited[gridPoints.indexOf(current.getRight())] == 0 && !(current.getRight().getComponent() instanceof Voltmeter)) {
				return searchForTripleWire(current.getRight());
			}
		} 
		if (current.getBot() != null && current.getBot().getTop() != null) {
			if (current.getBot().getComponent() instanceof TripleWire) return current.getBot().getComponent();
			if (visited[gridPoints.indexOf(current.getBot())] == 0 && !(current.getBot().getComponent() instanceof Voltmeter)) {
				return searchForTripleWire(current.getBot());
			}
		}
		if (current.getLeft() != null && current.getLeft().getRight() != null) {
			if (current.getLeft().getComponent() instanceof TripleWire) return current.getLeft().getComponent();
			if (visited[gridPoints.indexOf(current.getLeft())] == 0 && !(current.getLeft().getComponent() instanceof Voltmeter)) {
				return searchForTripleWire(current.getLeft());
			}
		}
		
		return null;
	}
	
	public int testDirection(GridPoint current) {
		visited[gridPoints.indexOf(current)] = 1;
		
		if (current.getTop() != null && current.getTop().getBot() != null) {
			if (current.getTop().getComponent() instanceof TripleWire) return 1;
			if (visited[gridPoints.indexOf(current.getTop())] == 0 && !(current.getTop().getComponent() instanceof Cell || current.getTop().getComponent() instanceof Voltmeter)) {
				if (testDirection(current.getTop()) == 1) return 1;
			}
		}
		else if (current.getRight() != null && current.getRight().getLeft() != null) {
			if (current.getRight().getComponent() instanceof TripleWire) return 1;
			if (visited[gridPoints.indexOf(current.getRight())] == 0 && !(current.getRight().getComponent() instanceof Cell || current.getRight().getComponent() instanceof Voltmeter)) {
				if (testDirection(current.getRight()) == 1) return 1;
			}
		} 
		else if (current.getBot() != null && current.getBot().getTop() != null) {
			if (current.getBot().getComponent() instanceof TripleWire) return 1;
			if (visited[gridPoints.indexOf(current.getBot())] == 0 && !(current.getBot().getComponent() instanceof Cell || current.getBot().getComponent() instanceof Voltmeter)) {
				if (testDirection(current.getBot()) == 1) return 1;
			}
		}
		else if (current.getLeft() != null && current.getLeft().getRight() != null) {
			if (current.getLeft().getComponent() instanceof TripleWire) return 1;
			if (visited[gridPoints.indexOf(current.getLeft())] == 0 && !(current.getLeft().getComponent() instanceof Cell || current.getLeft().getComponent() instanceof Voltmeter)) {
				if (testDirection(current.getLeft()) == 1) return 1;
			}
		}
		
		return 0;
	}
	
	public void getComponentsInBetween(GridPoint current) {
		
		if (current.getComponent() instanceof Resistor || current.getComponent() instanceof LED) {
			tempComponent.add(current.getComponent());
		}
		
		if (current.getTop() != null && current.getTop().getBot() != null) {
			if (current.getTop().getComponent() instanceof TripleWire) return;
			if (visited[gridPoints.indexOf(current.getTop())] == 0 && !(current.getTop().getComponent() instanceof Cell || current.getTop().getComponent() instanceof Voltmeter)) {
				getComponentsInBetween(current.getTop());
			}
		}
		else if (current.getRight() != null && current.getRight().getLeft() != null) {
			if (current.getRight().getComponent() instanceof TripleWire) return;
			if (visited[gridPoints.indexOf(current.getRight())] == 0 && !(current.getRight().getComponent() instanceof Cell || current.getRight().getComponent() instanceof Voltmeter)) {
				getComponentsInBetween(current.getRight());
			}
		} 
		else if (current.getBot() != null && current.getBot().getTop() != null) {
			if (current.getBot().getComponent() instanceof TripleWire) return;
			if (visited[gridPoints.indexOf(current.getBot())] == 0 && !(current.getBot().getComponent() instanceof Cell || current.getBot().getComponent() instanceof Voltmeter)) {
				getComponentsInBetween(current.getBot());
			}
		}
		else if (current.getLeft() != null && current.getLeft().getRight() != null) {
			if (current.getLeft().getComponent() instanceof TripleWire) return;
			if (visited[gridPoints.indexOf(current.getLeft())] == 0 && !(current.getLeft().getComponent() instanceof Cell || current.getLeft().getComponent() instanceof Voltmeter)) {
				getComponentsInBetween(current.getLeft());
			}
		}
	}
}
