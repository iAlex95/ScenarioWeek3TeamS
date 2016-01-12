import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import Components.Component;
import Components.GridPoint;

public class DragAndDropListener implements MouseListener, MouseMotionListener {

	private List<Component> components;
	private CircuitSimulator simulator;
	
	private Component dragComponent;
	private int dragOffsetX;
	private int dragOffsetY;
	private boolean needToRedraw = false;
	

	public DragAndDropListener(List<Component> components, CircuitSimulator simulator) {
		this.components = components;
		this.simulator = simulator;
	}

	@Override
	public void mousePressed(MouseEvent evt) {
		int x = evt.getPoint().x;
		int y = evt.getPoint().y;
		
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
			if(evt.getButton() == MouseEvent.BUTTON3) {
		      if (dragComponent.getRotation() != dragComponent.getMaxRotation()-1) dragComponent.setRotation(dragComponent.getRotation()+1);
		      else dragComponent.setRotation(0);
		      dragComponent = null;
		      simulator.repaint();
		    } else {
				components.remove(this.dragComponent );
				components.add(this.dragComponent);
				if (dragComponent.getGridPoint() != null) {
					dragComponent.getGridPoint().setOccupied(false);
				}
				if (dragComponent.getInitial()) {
					needToRedraw = true;
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
		if (dragComponent != null) {
			int x = evt.getPoint().x;
			int y = evt.getPoint().y;
			GridPoint checkPoint = simulator.checkPoint(x, y);
			
			if (checkPoint != null && !simulator.checkIfOccupied(checkPoint)) {
				dragComponent.setX(checkPoint.getX());
				dragComponent.setY(checkPoint.getY());
				dragComponent.setInitial(false);
				checkPoint.setOccupied(true);
				dragComponent.setGridPoint(checkPoint);
			} else {
				if (checkPoint == null || dragComponent.getInitial()) {
					simulator.removeComponent(dragComponent);
				} else {
					dragComponent.setX(dragComponent.getGridPoint().getX());
					dragComponent.setY(dragComponent.getGridPoint().getY());
				}
			}
			
			if (needToRedraw) {
				simulator.createAndAddComponent(dragComponent.getType());
				needToRedraw = false;
			}
			
			dragComponent = null;
			simulator.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if(dragComponent != null){
			dragComponent.setX(evt.getPoint().x - dragOffsetX);
			dragComponent.setY(evt.getPoint().y - dragOffsetY);
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

}
