import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Components.Cell;
import Components.Component;
import Components.Resistor;

class SliderListener implements ChangeListener {
	
	int value;
	JTextArea textArea;
	Component component;
	
	public SliderListener(Component component, JTextArea textArea) {
		this.textArea = textArea;
		this.component = component;
	}
		
	
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        value = (int)source.getValue();
        if (component instanceof Cell) {
			((Cell) component).setVoltage(value);
			textArea.setText(Integer.toString(value)+"V");
		}
		if (component instanceof Resistor) {
			((Resistor) component).setResistance(value);
			textArea.setText(Integer.toString(value)+"Ω");
		}
    }
    
    public int getValue() {
    	return value;
    }
}
