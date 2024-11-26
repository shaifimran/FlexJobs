package application.factory;

import application.handlers.DBHandler;
import application.handlers.MySQLHandler;


public class DBFactory {
	private static DBHandler dbHandler;
	public static String name = "MySQL";

	public static DBHandler getInstance() {
		if (name == "MySQL") {
			if (dbHandler == null) {
				dbHandler = new MySQLHandler();
			} 
			
			return dbHandler;
		} else {
			System.out.println("Waiting for Extension.....");
		}
		
		return dbHandler;
	}
}
