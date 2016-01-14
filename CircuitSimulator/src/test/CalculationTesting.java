package test;

import static org.junit.Assert.*;
import org.junit.Test;
import Components.*;
import java.util.*;

public class CalculationTesting {

	Resistor resistor1 = new Resistor(0, 0, 5);
	Resistor resistor2 = new Resistor(0, 0, 5);
	
	LED lightbulb1 = new LED(0, 0, 6);
	LED lightbulb2 = new LED(0, 0, 6);
	Ammeter ammeter1 = new Ammeter(0,0,4);
	Ammeter ammeter2 = new Ammeter(0,0,4);
	Voltmeter voltmeter1 = new Voltmeter(0,0,3);
	Voltmeter voltmeter2 = new Voltmeter(0,0,3);
	Cell cell1 = new Cell(0,0,2);
	Cell cell2 = new Cell(0,0,2);	
	Random rand = new Random();
	
	@Test
	public void testSetResistance() {
		int i1 = rand.nextInt(50)+1;

		resistor1.setResistance(i1);
		
		int resistance_value = i1;
		int resistance_setvalue = resistor1.getResistance();
		assertEquals(resistance_value, resistance_setvalue);
	}
	
	@Test
	public void testTotalResistance() {
		int i1 = rand.nextInt(50)+1;
		int i2 = rand.nextInt(20)+1;
		resistor1.setResistance(i1);
		resistor2.setResistance(i2);
		int total_resistance = lightbulb1.getResistance() +	resistor1.getResistance() + resistor2.getResistance();
		
		int answer_resistance = i1 + i2 + 12;
				
		assertEquals(answer_resistance, total_resistance);
	}
	
	@Test
	public void testCurrent(){
		int i1 = rand.nextInt(50)+1;
		int i2 = rand.nextInt(20)+1;
		int i3 = rand.nextInt(40)+1;
		resistor1.setResistance(i1);
		resistor2.setResistance(i2);
		cell1.setVoltage(i3);
		
		int total_resistance = lightbulb1.getResistance() +	resistor1.getResistance() + resistor2.getResistance();
		int power_supply = cell1.getVoltage();

		double current = (double)power_supply/(double)total_resistance;
		double answer_current = (double)i3/(double)(lightbulb1.getResistance()+i1+i2);

		assertEquals(answer_current, current, 0.01);
	}

	@Test
	public void testMaxVoltage() {
		int i1 = rand.nextInt(50)+1;
		int i2 = rand.nextInt(20)+1;
		double max_current = (double)lightbulb1.getVoltage()/(double)lightbulb1.getResistance();
		resistor1.setResistance(i1);
		resistor2.setResistance(i2);
		double total_resistance = lightbulb1.getResistance() +	resistor1.getResistance() + resistor2.getResistance();
		
		double answer_max_voltage = 0.5 * (i1 + i2 + 12);
		// V = IR
		double max_voltage = max_current * total_resistance;
		assertEquals(answer_max_voltage, max_voltage, 0.1);
	}


	@Test
	public void testBooleanMaxVoltage(){
		int i1 = rand.nextInt(50)+1;
		int i2 = rand.nextInt(20)+1;
		double max_current = lightbulb1.getVoltage()/lightbulb1.getResistance();
		resistor1.setResistance(1);
		resistor2.setResistance(3);
		double total_resistance = lightbulb1.getResistance() +	resistor1.getResistance() + resistor2.getResistance();
		
		double setting_max_voltage = 0.5 * (i1 + i2 + 12);
		double max_voltage = max_current * total_resistance;
		

			assertTrue("light bulb blows", setting_max_voltage > max_voltage);
			assertFalse("light bulb still glows", setting_max_voltage <= max_voltage);
		
	}
	
	@Test
	public void testVoltmeterReading() {
		double  n = rand.nextDouble()*50;
		voltmeter1.setReadVoltage(n);
		double voltmeter_reading = voltmeter1.getReadVoltage();
		cell1.setVoltage((int)n);
		double circuit_voltage = (double) cell1.getVoltage();
		assertEquals(voltmeter_reading, circuit_voltage, 1);
	}
	
	@Test
	public void testAmmeterReading(){
		int i1 = rand.nextInt(50)+1;
		int i2 = rand.nextInt(20)+1;
		int i3 = rand.nextInt(40)+1;
		resistor1.setResistance(i1);
		resistor2.setResistance(i2);
		cell1.setVoltage(i3);
		double set_value = (double)i3/(double)(lightbulb1.getResistance()+i1+i2);
		ammeter1.setReadCurrent(set_value);
		int total_resistance = lightbulb1.getResistance() +	resistor1.getResistance() + resistor2.getResistance();
		int power_supply = cell1.getVoltage();

		double current = (double)power_supply/(double)total_resistance;
		double ammeter_reading = ammeter1.getReadCurrent();
		assertEquals(ammeter_reading, current,1);
	}
}
