package application.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.Admin;
import application.OrganisationRepresentative;
import application.Student;

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
    
    public Student checkStudentExistence(String email) {
		try {
			this.getConnection();
			String query = "SELECT 1 FROM Student WHERE email = ?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, email);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						return new Student();
					} else {
						return null;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Student authenticateStudent(String stdEmail, String password) {
		try {
			String query = "select * from student where email = ? and password = ?;";
			this.getConnection();
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, stdEmail);
				preparedStatement.setString(2, password);
				try (ResultSet resultSet1 = preparedStatement.executeQuery()) {
					if (resultSet1.next()) {
						String rollNo = resultSet1.getString("rollNo");
						String email = resultSet1.getString("email");
						String name = resultSet1.getString("name");
						String department = resultSet1.getString("department");
						int semester = resultSet1.getInt("semester");
						double cgpa = resultSet1.getDouble("cgpa");
						Student student = new Student(rollNo, email, name, department, semester, cgpa);
						return student;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Boolean addStudent(String rollNo, String email, String name, String password, String department,
			int semester, double cgpa) {
		try {
			this.getConnection();
			System.out.println("Hello");
			String query = "Insert into student(rollNo, email, name, password, department, semester, cgpa) values(?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement preparedStatement1 = conn.prepareStatement(query)) {
				System.out.println("Hello");
				preparedStatement1.setString(1, rollNo);
				preparedStatement1.setString(2, email);
				preparedStatement1.setString(3, name);
				preparedStatement1.setString(4, password);
				preparedStatement1.setString(5, department);
				preparedStatement1.setInt(6, semester);
				preparedStatement1.setDouble(7, cgpa);
				preparedStatement1.executeUpdate();
				return true;
			} catch (Exception e) {
				System.out.println("Erro in adding ");
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
    // Verify credentials of OrganisationRepresentative
    public OrganisationRepresentative verifyOrganisationRepresentativeCredentials(String email, String password) {
        String query = "SELECT * FROM OrganisationRepresentative WHERE email = ? AND password = ?";

        try {
        	this.getConnection(); 
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs != null && rs.next()) {
                    String name = rs.getString("name");
                    String pass = rs.getString("password");
                    String emailFromDb = rs.getString("email");
                    String position = rs.getString("position");
                    String phone = rs.getString("phone");
                    String orgID = rs.getString("orgID");

                    return new OrganisationRepresentative(name, pass, emailFromDb, position, phone, orgID);
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return null;  // Return null if no matching user found or an error occurs
    }

    // Add a new Organisation Representative to the database
    public void addOrgRepresentative(String name, String phone, String password, String email, String organisation, String position) {
        String query = "INSERT INTO OrganisationRepresentative (name, password, email, position, phone, orgID) VALUES (?, ?, ?, ?, ?, ?)";

        try {
        	this.getConnection(); 
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.setString(4, position);
            statement.setString(5, phone);
            statement.setString(6, organisation);

            statement.executeUpdate();
            System.out.println("Organisation Representative added successfully.");

        } catch (SQLException e) {
            System.err.println("Error adding Organisation Representative: " + e.getMessage());
        }
    }

    // Add a new Organisation to the database
    public void addOrganisation(String name, String industry, String description, String location, String contactEmail, Boolean isVerified) {
        String orgQuery = "INSERT INTO Organisation (name, industry, description, location, contactEmail, isVerified) VALUES (?, ?, ?, ?, ?, ?)";

        try {
        	this.getConnection(); 
            PreparedStatement orgStatement = conn.prepareStatement(orgQuery);
            orgStatement.setString(1, name);
            orgStatement.setString(2, industry);
            orgStatement.setString(3, description);
            orgStatement.setString(4, location);
            orgStatement.setString(5, contactEmail);
            orgStatement.setBoolean(6, isVerified);

            orgStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Check if an Organisation Representative exists in the database
    public boolean isOrganisationRepExists(String organisation, String email) {
        boolean exists = false;
        String query = "SELECT * FROM OrganisationRepresentative WHERE orgID = ? AND email = ?";

        try {
        	this.getConnection(); 
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setString(1, organisation);
            statement.setString(2, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exists = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    // Check if an Organisation exists in the database
    public boolean isOrganisationExists(String organisationName) {
        boolean exists = false;
        String query = "SELECT * FROM Organisation WHERE name = ?";

        try {
        	this.getConnection(); 
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, organisationName);
            

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exists = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    
    public Admin getAdminInfo(String email, String password) {
    	String query = "SELECT * FROM Admin WHERE email = ? AND password = ?";
    	ResultSet rs = null;
        PreparedStatement stmt;
		try {
			this.getConnection();
			stmt = conn.prepareStatement(query);
			// Set parameters for the query
			stmt.setString(1, email);
			stmt.setString(2, password);
			// Execute the query
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				Admin a = new Admin(rs.getString("name"),rs.getString("email"));
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
