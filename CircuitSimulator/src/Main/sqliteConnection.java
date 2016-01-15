package Main;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class sqliteConnection {
	
	Connection connection = null;
	JFrame f;
	
	public sqliteConnection(JFrame f) {
		this.f = f;
	}
	
	public Connection dbConnector() {
		
		try{
			
			Class.forName("org.sqlite.JDBC");
			//change FILEPATH
			Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/burns/Desktop/ScenarioWeek3TeamS/CircuitSimulator/ComponentData.sqlite");
			JOptionPane.showMessageDialog(f, "Database Connected");
			return connection;
			
		}catch(Exception e) {
			
			JOptionPane.showMessageDialog(null, e);
			return null;
			
		}
		
	}
	
}
