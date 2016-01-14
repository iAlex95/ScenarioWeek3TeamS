import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import Components.Ammeter;
import Components.Button;
import Components.Cell;
import Components.Component;
import Components.GridPoint;
import Components.LED;
import Components.Resistor;
import Components.Switch;
import Components.Voltmeter;

public class DragAndDropListener implements MouseListener, MouseMotionListener {

	private List<Component> initialComponents;
	private List<Component> components;
	private CircuitSimulator simulator;
	
	private Component dragComponent;
	private int dragOffsetX;
	private int dragOffsetY;
	private int initialX;
	private int initialY;
	private boolean needToRedraw = false;
	
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
	
	public DragAndDropListener(List<Component> initialComponents, List<Component> components, CircuitSimulator simulator) {
		this.initialComponents = initialComponents;
		this.components = components;
		this.simulator = simulator;
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		if (simulator.getListenerActive()) {
			simulator.setHighlightPoint(null);
			simulator.setSelectedComponent(-1, null);
			
			int x = evt.getPoint().x;
			int y = evt.getPoint().y;
			
			for (int i = initialComponents.size()-1; i >= 0; i--) {
				Component component = initialComponents.get(i);
				
				if(mouseOverComponent(component,x,y)){
					if  ((component instanceof Cell && !simulator.getCellPlaced()) || !(component instanceof Cell)) {
						dragOffsetX = x - component.getX();
						dragOffsetY = y - component.getY();
						initialX = component.getX();
						initialY = component.getY();
						dragComponent = component;
						if(evt.getButton() == MouseEvent.BUTTON1 ) {
							simulator.addComponent(component);
							simulator.removeInitialComponent(component);
						}
						break;
					}
				}
			}
			for (int i = components.size()-1; i >= 0; i--) {
				Component component = components.get(i);
				
				if(mouseOverComponent(component,x,y)){
					dragOffsetX = x - component.getX();
					dragOffsetY = y - component.getY();
					dragComponent = component;
					break;
				}
			}
			
			// move drag component to the top of the list
			if(dragComponent != null){
				if(evt.getButton() == MouseEvent.BUTTON3 ) {
			      if (dragComponent.getRotation() != dragComponent.getMaxRotation()-1) dragComponent.setRotation(dragComponent.getRotation()+1);
			      else dragComponent.setRotation(0);
			      
			      if (dragComponent.getInitial() == false) {
				      GridPoint checkPoint = simulator.checkPoint(x, y);
				      checkAndSetSelectedComponent(dragComponent);
				      simulator.setHighlightPoint(checkPoint);
				      
				      if (!(dragComponent.getInitial())) {
				    	  simulator.removeGridConnections(checkPoint);
				    	  simulator.setGridConnections(dragComponent, checkPoint);
				      }
			      }
					
			      dragComponent = null;
			    } else {
					components.remove(this.dragComponent);
					components.add(this.dragComponent);
					if (dragComponent.getGridPoint() != null) {
						dragComponent.getGridPoint().setOccupied(false);
					}
					if (dragComponent.getInitial()) {
						needToRedraw = true;
					}
			    }
			}
			simulator.removeAll();
		    simulator.repaint();
		} else { 
			int x = evt.getPoint().x;
			int y = evt.getPoint().y;
			GridPoint checkPoint = simulator.checkPoint(x, y);
			
			for (int i = components.size()-1; i >= 0; i--) {
				Component component = components.get(i);
				
				if(mouseOverComponent(component,x,y)){
					dragOffsetX = x - component.getX();
					dragOffsetY = y - component.getY();
					dragComponent = component;

					if (dragComponent instanceof Voltmeter || dragComponent instanceof Ammeter
							|| dragComponent instanceof Switch || dragComponent instanceof Button
							|| dragComponent instanceof Cell || dragComponent instanceof Resistor) {
						checkAndSetSelectedComponent(dragComponent);
						simulator.setHighlightPoint(checkPoint);
					}
					
					dragComponent = null;
					break;
				} else {
					checkAndSetSelectedComponent(null);
					simulator.setHighlightPoint(null);
				}
					
			}
		}
	}

	private boolean mouseOverComponent(Component component, int x, int y) {
		return component.getX() <= x 
			&& component.getX()+component.getWidth() >= x
			&& component.getY() <= y
			&& component.getY()+component.getHeight() >= y;
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		if(evt.getButton() == MouseEvent.BUTTON1) {
			if (dragComponent != null) {
				int x = evt.getPoint().x;
				int y = evt.getPoint().y;
				GridPoint checkPoint = simulator.checkPoint(x, y);
				
				if (checkPoint != null && !simulator.checkIfOccupied(checkPoint)) {
					dragComponent.setX(checkPoint.getX());
					dragComponent.setY(checkPoint.getY());
					dragComponent.setInitial(false);
					checkPoint.setOccupied(true);
					if (dragComponent.getGridPoint() != null) {
						simulator.removeGridConnections(dragComponent.getGridPoint());
					}
					dragComponent.setGridPoint(checkPoint);
					checkPoint.setComponent(dragComponent);
					checkAndSetSelectedComponent(dragComponent);
					simulator.setHighlightPoint(checkPoint);
					simulator.setGridConnections(dragComponent, checkPoint);
					
					if (dragComponent instanceof Cell) {
				    	simulator.setCellPlaced(true);
				    }
				} else {
					if (checkPoint == null || dragComponent.getInitial()) {
						simulator.removeComponent(dragComponent);
						if (dragComponent.getGridPoint() != null) {
							simulator.removeGridConnections(dragComponent.getGridPoint());
						}
						if (dragComponent instanceof Cell) {
					    	simulator.setCellPlaced(false);
					    }
					} else {
						dragComponent.setX(dragComponent.getGridPoint().getX());
						dragComponent.setY(dragComponent.getGridPoint().getY());
					}
				}
				
				if (needToRedraw) {
					simulator.createComponent(initialX, initialY, dragComponent.getType(), 0, 0, 0, true);
					needToRedraw = false;
				}
				
				dragComponent = null;
			}
			simulator.removeAll();
			simulator.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if(dragComponent != null){
			dragComponent.setX(evt.getPoint().x - dragOffsetX);
			dragComponent.setY(evt.getPoint().y - dragOffsetY);
			
			GridPoint checkPoint = simulator.checkPoint(evt.getPoint().x, evt.getPoint().y);
			simulator.setHighlightPoint(checkPoint);
			
			simulator.repaint();
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {}
	
	public void checkAndSetSelectedComponent(Component c) {
		if (c instanceof Cell) {
			simulator.setSelectedComponent(CELL, c);
		}
		if (c instanceof Resistor) {
			simulator.setSelectedComponent(RESISTOR, c);
		}
		if (c instanceof Switch) {
			simulator.setSelectedComponent(SWITCH, c);
		}
		if (c instanceof Button) {
			simulator.setSelectedComponent(BUTTON, c);
		}
		if (c instanceof LED) {
			simulator.setSelectedComponent(LED, c);
		}
		if (c instanceof Voltmeter) {
			simulator.setSelectedComponent(VOLTMETER, c);
		}
		if (c instanceof Ammeter) {
			simulator.setSelectedComponent(AMMETER, c);
		}
	}
		

}
