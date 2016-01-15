package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Components.*;
import Main.CircuitChecker;
import Main.CircuitSimulator;

public class CircuitCheckerTest {
	
	CircuitSimulator simulator = new CircuitSimulator();
	
	
	@Test
	public void testResetAllForComponents() {
		
			simulator.createComponent(450,200,2,13,0,0,false);
			simulator.createComponent(400,200,1,0,0,1,false);
			simulator.createComponent(500,200,1,0,0,2,false);
			simulator.createComponent(400,250,0,0,0,1,false);
			simulator.createComponent(400,300,1,0,0,0,false);
			simulator.createComponent(500,300,1,0,0,3,false);
			simulator.createComponent(450,300,6,0,0,0,false);
			simulator.createComponent(500,250,0,0,0,1,false);

		simulator.resetAll();
		int c = simulator.components.size();
		assertEquals(0,c);
	
	}
	
	@Test
	public void testCheckNoPowerCircuit() {
		
		simulator.createComponent(400,200,1,0,0,1,false);
		simulator.createComponent(500,200,1,0,0,2,false);
		simulator.createComponent(400,250,0,0,0,1,false);
		simulator.createComponent(400,300,1,0,0,0,false);
		simulator.createComponent(500,300,1,0,0,3,false);
		simulator.createComponent(450,300,6,0,0,0,false);
		simulator.createComponent(500,250,0,0,0,1,false);

		CircuitChecker checker = new CircuitChecker(simulator.components, simulator.gridPoints, simulator);
			
		int circuitCompletion = checker.checkCircuit();
		
		assertEquals(0,circuitCompletion);
	
	}
	
	@Test
	public void testCheckInvalidCircuit() {
		
		simulator.createComponent(450,300,1,0,0,0,false);
		simulator.createComponent(500,250,2,6,0,0,false);

		CircuitChecker checker = new CircuitChecker(simulator.components, simulator.gridPoints, simulator);
			
		int circuitCompletion = checker.checkCircuit();
		
		assertEquals(1,circuitCompletion);
	
	}
	
	@Test
	public void testCheckValidCircuit() {
		
		simulator.createComponent(450,200,2,13,0,0,false);
		simulator.createComponent(400,200,1,0,0,1,false);
		simulator.createComponent(500,200,1,0,0,2,false);
		simulator.createComponent(400,250,0,0,0,1,false);
		simulator.createComponent(400,300,1,0,0,0,false);
		simulator.createComponent(500,300,1,0,0,3,false);
		simulator.createComponent(450,300,6,0,0,0,false);
		simulator.createComponent(500,250,0,0,0,1,false);

		CircuitChecker checker = new CircuitChecker(simulator.components, simulator.gridPoints, simulator);
			
		int circuitCompletion = checker.checkCircuit();
		
		assertEquals(2,circuitCompletion);
	
	}
	
	public void testResetAllForInitialComponents() {
		
		simulator.createInitialComponentList();

	simulator.resetAll();
	int c = simulator.initialComponents.size();
	assertEquals(10,c);

}
	
	@Test
	public void testCreateInitialComponentList() {
		
		simulator.createInitialComponentList();
		
		int c = simulator.initialComponents.size();
		assertEquals(20,c);
		
	}
	
	@Test
	public void testCreateComponent() {
		
		simulator.createComponent(400,300,1,0,0,0,false);
		simulator.createComponent(500,300,1,0,0,3,false);
		simulator.createComponent(450,300,6,0,0,0,false);
		simulator.createComponent(500,250,0,0,0,1,false);
		
		int c = simulator.components.size();
		assertEquals(4,c);
		
	}
	
	@Test
	public void testComplexCircuit() {
		
		simulator.createComponent(450,150,2,14,0,0,false);
		simulator.createComponent(350,150,1,0,0,1,false);
		simulator.createComponent(400,150,0,0,0,0,false);
		simulator.createComponent(500,150,0,0,0,0,false);
		simulator.createComponent(350,200,4,0,0,1,false);
		simulator.createComponent(350,300,1,0,0,0,false);
		simulator.createComponent(400,300,8,0,0,2,false);
		simulator.createComponent(450,300,6,0,0,0,false);
		simulator.createComponent(550,200,5,0,4,1,false);
		simulator.createComponent(550,250,6,0,0,1,false);
		simulator.createComponent(550,150,8,0,0,2,false);
		simulator.createComponent(600,150,1,0,0,2,false);
		simulator.createComponent(600,200,3,0,0,1,false);
		simulator.createComponent(600,250,0,0,0,1,false);
		simulator.createComponent(600,300,1,0,0,3,false);
		simulator.createComponent(550,300,8,0,0,0,false);
		simulator.createComponent(500,300,8,0,0,2,false);
		simulator.createComponent(450,350,3,0,0,0,false);
		simulator.createComponent(500,350,1,0,0,3,false);
		simulator.createComponent(400,350,1,0,0,0,false);
		simulator.createComponent(350,250,0,0,0,1,false);

		CircuitChecker checker = new CircuitChecker(simulator.components, simulator.gridPoints, simulator);
			
		int circuitCompletion = checker.checkCircuit();
		
		assertEquals(2,circuitCompletion);
	
	}

}