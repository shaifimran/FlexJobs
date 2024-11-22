package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHandler {
    private final String dbUrl = "jdbc:mysql://localhost:3306/flexjobs";
    private final String dbUser = "root";
    private final String dbPassword = "Shaif2004.";
    private Connection conn;
    /**
     * Creates and returns a connection to the database.
     * 
     * @return Connection object
     * @throws SQLException if the connection fails
     */
    public Connection getConnection() throws SQLException {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
        }

        // Establish and return the connection
        
        if (conn != null) {
        	return conn;
        } else {
        	try {
        		this.conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        	} catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("Unable to connect to the database!");
	        }
        	return conn;
        }
    }
    
    
    public Admin getAdminInfo(String email) {
    	String query = "SELECT * FROM Admin WHERE email = ?";
    	ResultSet rs = null;
        PreparedStatement stmt;
		try {
			this.getConnection();
			stmt = conn.prepareStatement(query);
			// Set parameters for the query
			stmt.setString(1, email);
			// Execute the query
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				Admin a = new Admin(rs.getString("name"),rs.getString("email"),rs.getString("password"));
				return a;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return null;        
    }
    /**
     * Closes the given connection if it is not null.
     * 
     * @param connection The connection to close
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Failed to close the connection: " + e.getMessage());
            }
        }
    }
}
