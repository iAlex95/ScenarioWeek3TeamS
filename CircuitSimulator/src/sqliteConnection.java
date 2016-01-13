import java.sql.*;
import javax.swing.*;

public class sqliteConnection {
	
	Connection connection = null;
	
	public static Connection dbConnector() {
		
		try{
			
			Class.forName("org.sqlite.JDBC");
			//change FILEPATH
			Connection connection = DriverManager.getConnection("jdbc:sqlite:/Users/burns/Desktop/ScenarioWeek3TeamS/ComponentData.sqlite");
			JOptionPane.showMessageDialog(null, "Database Connected");
			return connection;
			
		}catch(Exception e) {
			
			JOptionPane.showMessageDialog(null, e);
			return null;
			
		}
		
	}
	
}
