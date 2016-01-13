import Components.*;

import java.util.*;



public class Calculation {
	//the circuit is valid
	
	private List<Component> components = new ArrayList<Component>();
	
	public Calculation(List<Component> components) {
		this.components = components;
	}
	
	private double resistanceResistor(){
		
		double resistance_resistor = 0;
		//for each resistor
		for(Component component : components) {
			if (component instanceof Resistor)
				resistance_resistor += ((Resistor) component).getResistance();
		}
		return resistance_resistor;
	}
	
	private double resistanceLED(){
		
		double resistance_LED = 0;
		//for each LED
		for(Component component : components) {
			if (component instanceof LED)
			resistance_LED += ((LED) component).getResistance();
		}
		return resistance_LED;
	}
	
	public double resistance() {
		double resistance_total = 0;

		resistance_total = resistanceResistor() + resistanceLED();		
		return resistance_total;
	}
	
	public double voltage() {
		//voltage = power supply
		double source = 0;
		
		for (Component c : components){
			if (c instanceof Cell) {
				source = ((Cell) c).getVoltage();
			}
		}
		return source;
	}
	

	
	public double current(){
		
		double current = voltage()/resistance();
		return current;
	}
	
	public boolean maxVoltageCircuit(){
		double maxVoltage = 0.5 * resistance();
		
		if(voltage() > maxVoltage){
			return true;
		} else {
			return false;
		}
	}
	
}
