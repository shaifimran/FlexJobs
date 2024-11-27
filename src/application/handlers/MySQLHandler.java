package application.handlers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Admin;
import application.Application;
import application.ApplicationWithOpportunity;
import application.Chat;
import application.ChatBox;
import application.EducationalOpportunity;
import application.Interview;
import application.JobOpportunity;
import application.Message;
import application.Notification;
import application.Opportunity;
import application.Organisation;
import application.OrganisationRepresentative;
import application.Student;
import application.UnverifiedOrgs;
import application.factory.UIFactory;

public class MySQLHandler implements DBHandler {
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

	public ResultSet executeQuery(String query) {
		try {
			// Create a Statement object
			this.getConnection();
			Statement statement = conn.createStatement();

			// Execute the query and return the ResultSet
			return statement.executeQuery(query);

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query execution failed: " + query);
			return null;
		}
	}

	public Boolean checkStudentExistence(String email, String rollNo) {
		try {
			this.getConnection();
			String query = "SELECT 1 FROM Student WHERE email = ? or rollNo=?;";
			PreparedStatement preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, rollNo);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return true;
				} else {
					return false;
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
			PreparedStatement preparedStatement = conn.prepareStatement(query);
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int createChatBox(String ownerType) {
		int chatBoxId = -1; // Default value in case of failure
		String query = "INSERT INTO ChatBox (ownerType) VALUES (?)";

		try {
			this.getConnection(); // Assume getConnection() provides a valid DB connection
			PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			// Set the ownerType parameter
			preparedStatement.setString(1, ownerType);

			// Execute the query
			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows > 0) {
				// Retrieve the generated key
				try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						chatBoxId = generatedKeys.getInt(1); // Get the generated chatBoxId
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Log any SQL exceptions for debugging
		}

		return chatBoxId; // Return the generated chatBoxId
	}

	public Boolean addStudent(String rollNo, String email, String name, String password, String department,
			int semester, double cgpa, String resume, int chatBoxId) {
		try {
			this.getConnection();
			String query = "Insert into student(rollNo, email, name, password, department, semester, cgpa, resume,chatBoxId) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try {
				PreparedStatement preparedStatement1 = conn.prepareStatement(query);
				preparedStatement1.setString(1, rollNo);
				preparedStatement1.setString(2, email);
				preparedStatement1.setString(3, name);
				preparedStatement1.setString(4, password);
				preparedStatement1.setString(5, department);
				preparedStatement1.setInt(6, semester);
				preparedStatement1.setDouble(7, cgpa);
				preparedStatement1.setString(8, resume);
				preparedStatement1.setInt(9, chatBoxId);
				preparedStatement1.executeUpdate();
				return true;
			} catch (Exception e) {
				System.out.println("Error in adding ");
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean updateStudentData(String email, Map<String, Object> updates) {
		StringBuilder queryBuilder = new StringBuilder("UPDATE Student SET ");

		// Append column placeholders dynamically
		for (String column : updates.keySet()) {
			queryBuilder.append(column).append(" = ?, ");
		}
		queryBuilder.setLength(queryBuilder.length() - 2);

		// Add the WHERE clause
		queryBuilder.append(" WHERE email = ?");
		try {
			this.getConnection();
//			System.out.println("Hello");
			String query = queryBuilder.toString();

			try {
				PreparedStatement preparedStatement = conn.prepareStatement(query);
				int index = 1;
				for (Object value : updates.values()) {
					preparedStatement.setObject(index++, value);
				}

				// Set the email for the WHERE clause
				preparedStatement.setString(index, email);
				int rowsUpdated = preparedStatement.executeUpdate();
				return rowsUpdated > 0;
			} catch (Exception e) {
				System.out.println("Error in adding.");
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Verify credentials of OrganisationRepresentative
	public OrganisationRepresentative verifyOrganisationRepresentativeCredentials(String email, String password,
			Boolean isVerified) {
		String query = "SELECT * FROM OrganisationRepresentative WHERE email = ? AND password = ? AND isVerified = ?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);

			stmt.setString(1, email);
			stmt.setString(2, password);
			stmt.setBoolean(3, isVerified);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs != null && rs.next()) {
					String name = rs.getString("name");
					String pass = rs.getString("password");
					String emailFromDb = rs.getString("email");
					String position = rs.getString("position");
					String phone = rs.getString("phone");
					String orgID = rs.getString("orgID");
					isVerified = rs.getBoolean("isVerified");

					return new OrganisationRepresentative(name, pass, emailFromDb, position, phone, orgID, isVerified);
				}
			}

		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
		}
		return null; // Return null if no matching user found or an error occurs
	}

	// Add a new Organisation Representative to the database
	public void addOrgRepresentative(String name, String phone, String password, String email, String organisation,
			String position, Boolean isVerified) {
		String query = "INSERT INTO OrganisationRepresentative (name, password, email, position, phone, orgID, isVerified) VALUES (?, ?, ?, ?, ?, ?,?)";

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, name);
			statement.setString(2, password);
			statement.setString(3, email);
			statement.setString(4, position);
			statement.setString(5, phone);
			statement.setString(6, organisation);
			statement.setBoolean(7, isVerified);

			statement.executeUpdate();
			System.out.println("Organisation Representative added successfully.");

		} catch (SQLException e) {
			System.err.println("Error adding Organisation Representative: " + e.getMessage());
		}
	}

	public void addOrganisation(String name, String industry, String description, String location, String contactEmail,
			Boolean isVerified) {
		String orgQuery = "INSERT INTO Organisation (name, industry, description, location, contactEmail, isVerified) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			this.getConnection();
			PreparedStatement orgStatement = conn.prepareStatement(orgQuery);
			orgStatement.setString(1, name.toLowerCase());
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
		String query = "SELECT * FROM OrganisationRepresentative WHERE ((email) = LOWER(?)) OR (email = ?)";

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);

			statement.setString(1, email);
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
	
	public boolean isOrganisationRepExists(String organisation) {
		boolean exists = false;
		String query = "SELECT * FROM OrganisationRepresentative WHERE orgID = ?";

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);

			statement.setString(1, organisation.toLowerCase());

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

	public boolean setFeedback(int applicationID, String feedback) {
		String query = "UPDATE Application SET feedback = ? WHERE applicationID = ?";
		PreparedStatement stmt = null;

		try {
			// Establish connection
			this.getConnection();

			// Prepare the statement
			stmt = conn.prepareStatement(query);

			// Set the query parameters
			stmt.setString(1, feedback);
			stmt.setInt(2, applicationID);

			// Execute the update and check if rows were affected
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Return true if the update succeeded
		} catch (SQLException e) {
			// Log the exception for debugging purposes
			e.printStackTrace();
			return false; // Return false if an exception occurs
		}
	}

	public boolean addNotification(String senderId, String receiverId, String message) {
		String query = "INSERT INTO Notification (senderId, receiverId, isRead, message, timestamp) VALUES (?, ?, false, ?, NOW())";
		PreparedStatement stmt = null;

		try {
			// Establish connection
			this.getConnection();

			// Prepare the statement
			stmt = conn.prepareStatement(query);
			// Set parameters
			stmt.setString(1, senderId);
			stmt.setString(2, receiverId);
			stmt.setString(3, message);

			// Execute the query
			int rowsInserted = stmt.executeUpdate();

			// Return true if insertion was successful
			return rowsInserted > 0;

		} catch (SQLException e) {
			e.printStackTrace(); // Log the exception
			return false;
		}
	}

//////////////////////////////////////////////////////////
	public List<Application> getApplicationList() {
		List<Application> applicationList = new ArrayList<>();
		String query = "SELECT applicationID, submitDate, status, feedback, studentID, interviewID, opportunityID FROM Application";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Application application = new Application(rs.getInt("applicationID"), rs.getDate("submitDate"),
						rs.getString("status"), rs.getString("feedback"), rs.getString("studentID"),
						rs.getInt("interviewID"), rs.getInt("opportunityID"));
				applicationList.add(application);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return applicationList;
	}

	public List<Application> getApplicationsByStatus(String status) {
		List<Application> applicationList = new ArrayList<>();
		String query = "SELECT applicationID, submitDate, status, feedback, studentID, interviewID, opportunityID FROM Application WHERE status = ?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, status);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Application application = new Application(rs.getInt("applicationID"), rs.getDate("submitDate"),
						rs.getString("status"), rs.getString("feedback"), rs.getString("studentID"),
						rs.getInt("interviewID"),
						rs.getInt("opportunityID"));
				applicationList.add(application);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return applicationList;
	}

	public boolean updateApplicationStatus(int applicationID, String status) {
		String query = "UPDATE Application SET status = ? WHERE applicationID = ?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, status);
			stmt.setInt(2, applicationID);

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // Return true if at least one row is updated
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false; // Return false if an error occurs
	}

	// Get all Application IDs
	public List<Integer> getAllApplicationIDs() {
		List<Integer> applicationIDs = new ArrayList<>();
		String query = "SELECT applicationID FROM Application";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				applicationIDs.add(rs.getInt("applicationID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return applicationIDs;
	}

	// Get all Student IDs
	public List<String> getAllStudentIDs() {
		List<String> studentIDs = new ArrayList<>();
		String query = "SELECT rollNo FROM Student";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				studentIDs.add(rs.getString("rollNo"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return studentIDs;
	}

	public boolean scheduleInterview(int applicationID, String candidateID, String dateTime, String type,
			String location) {
		String insertInterviewQuery = "INSERT INTO Interview (candidateID, timeSlot, type, status, location) VALUES (?, ?, ?, 'Scheduled', ?)";
		String updateApplicationQuery = "UPDATE Application SET interviewID = ? WHERE applicationID = ?";

		try {
			this.getConnection();

			// Step 1: Insert the interview details
			PreparedStatement stmt1 = conn.prepareStatement(insertInterviewQuery);
			stmt1.setString(1, candidateID);
			stmt1.setString(2, dateTime); // Ensure the date is properly formatted
			stmt1.setString(3, type);
			stmt1.setString(4, location);
			int rowsAffectedInterview = stmt1.executeUpdate();

			if (rowsAffectedInterview > 0) {
				// Step 2: Update the Application table with the new interviewID
				PreparedStatement stmt2 = conn.prepareStatement("SELECT LAST_INSERT_ID()");
				ResultSet rs = stmt2.executeQuery();
				int newInterviewID = 0;
				if (rs.next()) {
					newInterviewID = rs.getInt(1); // Get the last inserted interviewID
				}

				// Step 3: Update the application with the new interviewID
				PreparedStatement stmt3 = conn.prepareStatement(updateApplicationQuery);
				stmt3.setInt(1, newInterviewID);
				stmt3.setInt(2, applicationID);
				int rowsAffectedApplication = stmt3.executeUpdate();

				return rowsAffectedApplication > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int getOpportunitiesCount() {
		int count = 0;
		String query = "SELECT COUNT(*) AS count FROM Opportunity";

		try {
			this.getConnection(); // Ensure connection is established
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt("count");
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getApplicantsCount() {
		int count = 0;
		String query = "SELECT COUNT(DISTINCT studentID) AS count FROM Application";

		try {
			this.getConnection(); // Ensure connection is established
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt("count");
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}

	public String getStudentEmail(int applicationID, String studentID) {
		String email = null;
		String query;

		try {
			this.getConnection(); // Ensure the connection is established

			if (applicationID > 0) {
				query = "SELECT s.email FROM Student s " + "JOIN Application a ON s.rollNo = a.studentID "
						+ "WHERE a.applicationID = ?";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setInt(1, applicationID);
				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					email = rs.getString("email");
				}

				rs.close();
				stmt.close();
			} else if (studentID != null && !studentID.isEmpty()) {
				query = "SELECT email FROM Student WHERE rollNo = ?";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, studentID);
				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					email = rs.getString("email");
				}

				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return email; // Return email or null if not found
	}

	//////////////////////////////////////
	public List<String> getVerifiedOrganisations() {
		List<String> organisations = new ArrayList<>();
		String query = "SELECT name FROM Organisation WHERE isVerified = true";

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			// Loop through the result set and add organisation names to the list
			while (resultSet.next()) {
				String organisationName = resultSet.getString("name");
				organisations.add(organisationName);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Log the error for debugging purposes
		}

		return organisations;
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

	public boolean organisationExists(String orgName) {
		String query = "SELECT COUNT(*) FROM Organisation WHERE LOWER(name) = ?";
		try {
			this.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(query);

			preparedStatement.setString(1, orgName);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1) > 0; // Returns true if the count is greater than 0
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int insertIntoOpportunity(String title, String description, String type, String orgName) throws SQLException {
		String query = "INSERT INTO Opportunity (title, description, type, postedBy) VALUES (?, ?, ?, ?)";
		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

			statement.setString(1, title);
			statement.setString(2, description);
			statement.setString(3, type);
			statement.setString(4, orgName);

			int affectedRows = statement.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						return generatedKeys.getInt(1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void insertIntoJob(int opportunityID, String category) throws SQLException {
		String query = "INSERT INTO Job (opportunityID, category) VALUES (?, ?)";
		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);

			statement.setInt(1, opportunityID);
			statement.setString(2, category);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // Log the error for debugging purposes
		}
	}

	public void insertIntoEducational(int opportunityID) throws SQLException {
		String query = "INSERT INTO Educational (opportunityID) VALUES (?)";
		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);

			statement.setInt(1, opportunityID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // Log the error for debugging purposes
		}
	}

	public OrganisationRepresentative getOrganisationRepresentative(String email, String password) {
		String query = "SELECT * FROM OrganisationRepresentative WHERE LOWER(email) = LOWER(?) AND password = ?";
		OrganisationRepresentative representative = null;

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, email);
			statement.setString(2, password);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// Extracting data from the result set
					String name = resultSet.getString("name");
					String emailFromDb = resultSet.getString("email");
					String position = resultSet.getString("position");
					String phone = resultSet.getString("phone");
					String orgID = resultSet.getString("orgID");
					boolean isVerified = resultSet.getBoolean("isVerified");

					// Creating the OrganisationRepresentative object
					representative = new OrganisationRepresentative(name, password, emailFromDb, position, phone, orgID,
							isVerified);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return representative;
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
				Admin a = new Admin(rs.getString("name"), rs.getString("email"));
				return a;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Boolean isChatPresent(Chat chat) {
		String query = "select 1 from Chat where studentId=? and orgId=?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, chat.getStudentId());
			stmt.setString(2, chat.getOrgId());
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				return true;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}

	public int createChat(Chat chat) {
		String query = "INSERT INTO Chat (createdAt, orgId, studentId) VALUES (?, ?, ?)";
		int chatId = -1;

		try {
			this.getConnection();

			PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setTimestamp(1, chat.getCreatedAt()); // Pass the timestamp directly
			preparedStatement.setString(2, chat.getOrgId());
			preparedStatement.setString(3, chat.getStudentId());

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						chatId = generatedKeys.getInt(1); // Retrieve the auto-generated chat ID
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chatId;
	}

	public void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.err.println("Failed to close the connection: " + e.getMessage());
			}
		}
	}

	public Map<Integer, String> fetchJobTitles(String category) {
		Map<Integer, String> jobTitles = new HashMap<>();

		try {
			String query;
			this.getConnection();
			ResultSet rs = null;
			if (category.isEmpty()) {
				query = "SELECT o.opportunityID, o.title FROM Opportunity o INNER JOIN Job j ON o.opportunityID = j.opportunityID";
				Statement stmt = conn.createStatement();
				rs = stmt.executeQuery(query);
			} else {
				query = "SELECT o.opportunityID, o.title FROM Opportunity o INNER JOIN Job j ON o.opportunityID = j.opportunityID WHERE LOWER(j.category) LIKE LOWER(?)";
				PreparedStatement stmt;
				stmt = conn.prepareStatement(query);
				stmt.setString(1, "%" + category + "%");
				rs = stmt.executeQuery();
			}

			while (rs != null && rs.next()) {
				int opportunityID = rs.getInt("opportunityID");
				String title = rs.getString("title");
				jobTitles.put(opportunityID, title);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jobTitles;
	}

	public Map<Integer, String> fetchEducationTitles() {
		Map<Integer, String> jobTitles = new HashMap<>();
		String query = "SELECT o.opportunityID, o.title FROM Opportunity o INNER JOIN Educational e ON o.opportunityID = e.opportunityID";

		try {
			this.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				int opportunityID = rs.getInt("opportunityID");
				String title = rs.getString("title");
				jobTitles.put(opportunityID, title);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return jobTitles;
	}

	public EducationalOpportunity getEducationalOppDetailsById(Integer oppId) {
		String query = "SELECT o.title, o.description, o.postedBy FROM Opportunity o INNER JOIN Opportunity j ON o.opportunityID = j.opportunityID where o.opportunityID=?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, (int) oppId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String title = rs.getString("title");
				String postedBy = rs.getString("postedBy");
				String desc = rs.getString("description");
				Opportunity o = new EducationalOpportunity((int) oppId, title, desc, postedBy);
				return (EducationalOpportunity) o;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public JobOpportunity getJobById(Integer oppId) {
		String query = "SELECT o.title, o.description, o.postedBy, j.category FROM Opportunity o INNER JOIN Job j ON o.opportunityID = j.opportunityID where o.opportunityID=?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, (int) oppId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				String title = rs.getString("title");
				String postedBy = rs.getString("postedBy");
				String desc = rs.getString("description");
				String category = rs.getString("category");
				JobOpportunity o = new JobOpportunity((int) oppId, title, desc, category, "open", postedBy);
				return o;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	public Boolean checkValidity(String rollNo, int oppId) {
		String query = "select 1 from application where opportunityId=? and studentId=?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, oppId);
			stmt.setString(2, rollNo);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				return true;
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return false;
	}

	public Boolean applyForJob(String rollNo, int oppId) {
		String query = "INSERT INTO Application(status, studentID, opportunityID, submitDate) VALUES ('In Progress', ?, ?, ?)";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, rollNo); // Bind the roll number
			stmt.setInt(2, oppId); // Bind the opportunity ID

			// Bind the current date as the submit date
			stmt.setDate(3, Date.valueOf(LocalDate.now()));

			int res = stmt.executeUpdate();
			if (res > 0) {
				return true;
			}
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Application> retrieveApplications(String rollNo) {
		String query = "SELECT a.applicationID, a.status, a.feedback, a.studentID, a.interviewID, a.opportunityID,  FROM Application a WHERE a.studentID = ?";

		try {
			List<Application> applications = new ArrayList<>();
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, rollNo);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int applicationID = rs.getInt("applicationID");
				String status = rs.getString("status");
				String feedback = rs.getString("feedback");
				String studentID = rs.getString("studentID");
				int interviewID = rs.getInt("interviewID");
				int opportunityID = rs.getInt("opportunityID");

				Application application = new Application(applicationID, status, feedback, studentID, interviewID,
						opportunityID);
				applications.add(application);
				return applications;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<ApplicationWithOpportunity> retrieveApplicationsWithOpportunities(String rollNo) {
		String query = "SELECT a.applicationID, a.status, a.feedback, a.studentID, a.opportunityID,a.interviewId, o.title , o.description, o.postedBy "
				+ "FROM Application a  INNER JOIN Opportunity o ON a.opportunityID = o.opportunityID WHERE a.studentID = ?";

		List<ApplicationWithOpportunity> result = new ArrayList<>();

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, rollNo);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int applicationID = rs.getInt("applicationID");
				String status = rs.getString("status");
				String feedback = rs.getString("feedback");
				int opportunityID = rs.getInt("opportunityID");
				int interviewID = rs.getInt("interviewId");
				String opportunityTitle = rs.getString("title");
				String opportunityDescription = rs.getString("description");
				String postedBy = rs.getString("postedBy");

				Application application = new Application(applicationID, status, feedback, rollNo, interviewID,
						opportunityID);

				Opportunity opportunity = new Opportunity(opportunityID, opportunityTitle, opportunityDescription,
						postedBy);

				result.add(new ApplicationWithOpportunity(application, opportunity));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<Notification> fetchStudentNotifications(String email) {
		String query = "SELECT notificationId, senderId, receiverId, message, timestamp, isRead FROM Notification WHERE receiverId in (?, 'student')";
		List<Notification> notifications = new ArrayList<>();
		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);

			// Assuming `rollNo` is a String; no conversion needed here.
			stmt.setString(1, email);

			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				int notificationId = resultSet.getInt("notificationId");
				String senderId = resultSet.getString("senderId");
				String receiverId = resultSet.getString("receiverId");
				String message = resultSet.getString("message");
				Timestamp timestamp = resultSet.getTimestamp("timestamp");
				boolean isRead = resultSet.getBoolean("isRead");

				// Construct the Notification object with all fields
				Notification notification = new Notification(notificationId, senderId, receiverId, message, timestamp,
						isRead);
				notifications.add(notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return notifications;
	}

	public Boolean markRead(String email, int notifID) {
		String query = "UPDATE Notification SET isRead = TRUE WHERE receiverId in (?, 'student') AND notificationId = ?";
		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, email);
			stmt.setInt(2, notifID);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Map<String, Integer> getStdDashBoardInfo(String stdID) {
		try {
			this.getConnection();
			String totalApplicationsQuery = "SELECT COUNT(*) AS totalApplications FROM Application where studentID=?";
			String totalOpportunitiesQuery = "SELECT COUNT(*) AS totalOpportunities FROM Opportunity";

			Map<String, Integer> result = new HashMap<>();
			PreparedStatement stmtApp = conn.prepareStatement(totalApplicationsQuery);
			stmtApp.setString(1, stdID);
			ResultSet resultSetApp = stmtApp.executeQuery();
			if (resultSetApp.next()) {
				result.put("applications", resultSetApp.getInt("totalApplications"));
			}

			PreparedStatement stmtOpp = conn.prepareStatement(totalOpportunitiesQuery);
			ResultSet resultSetOpp = stmtOpp.executeQuery();
			if (resultSetOpp.next()) {
				result.put("opportunities", resultSetOpp.getInt("totalOpportunities"));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public UnverifiedOrgs getUnverifiedOrgsandInitialReps() {
		UnverifiedOrgs unverifiedOrgs = new UnverifiedOrgs();

		// Query to get all unverified organizations
		String orgQuery = "SELECT * FROM Organisation WHERE isVerified = false";

		try {
			this.getConnection();
			PreparedStatement orgStmt = conn.prepareStatement(orgQuery);
			ResultSet orgResultSet = orgStmt.executeQuery();

			// Fetch each unverified organization and their representative
			while (orgResultSet.next()) {
				String orgName = orgResultSet.getString("name");
				String orgIndustry = orgResultSet.getString("industry");
				String orgDescription = orgResultSet.getString("description");
				String orgLocation = orgResultSet.getString("location");
				String orgContactEmail = orgResultSet.getString("contactEmail");

				// Create an Organization object
				Organisation org = new Organisation(orgName, orgIndustry, orgDescription, orgLocation, orgContactEmail,
						false);

				// Add the organization to the UnverifiedOrgs list
				unverifiedOrgs.addOrganization(org);

				// Query to get the initial representative for this unverified organization
				String repQuery = "SELECT * FROM OrganisationRepresentative WHERE orgID = ? AND isVerified = false";
				PreparedStatement repStmt = conn.prepareStatement(repQuery);
				repStmt.setString(1, orgName);
				ResultSet repResultSet = repStmt.executeQuery();

				// If a representative exists, add them to the list of representatives for this
				// organization
				if (repResultSet.next()) {
					String repName = repResultSet.getString("name");
					String repEmail = repResultSet.getString("email");
					String repPosition = repResultSet.getString("position");
					String repPhone = repResultSet.getString("phone");

					OrganisationRepresentative rep = new OrganisationRepresentative(repName, null, repEmail,
							repPosition, repPhone, orgName, false);

					// Add the representative to the list
					unverifiedOrgs.addRepresentative(rep);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return unverifiedOrgs; // Return the populated UnverifiedOrgs object
	}

	public void updateOrganisationVerificationStatus(Organisation org) {
		// TODO Auto-generated method stub
		String query = "UPDATE Organisation SET isVerified = ?, chatBoxId=? WHERE name = ?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setBoolean(1, true); // Set isVerified to true
			stmt.setInt(2, org.getMyChatBox().getChatBoxId()); // Set isVerified to true
			stmt.setString(3, org.getName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void updateRepresentativeVerificationStatus(OrganisationRepresentative rep) {
		// TODO Auto-generated method stub
		String query = "UPDATE OrganisationRepresentative SET isVerified = ? WHERE email = ?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setBoolean(1, true); // Set isVerified to true
			stmt.setString(2, rep.getEmail());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteOrganisation(Organisation org) {
		// TODO Auto-generated method stub
		String query = "DELETE FROM Organisation WHERE name = ?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, org.getName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteOrganisationRepresentative(OrganisationRepresentative rep) {
		// TODO Auto-generated method stub
		String query = "DELETE FROM OrganisationRepresentative WHERE email = ?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, rep.getEmail());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getUnverifiedOrgCount() {
		String query = "SELECT COUNT(*) AS count FROM Organisation WHERE isVerified = false";
		int count = 0;

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt("count"); // Retrieve the count from the result set
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return "Error retrieving count";
		}

		return String.valueOf(count); // Return the count as a String
	}

	public List<Notification> getNotificationsForAdmin() {
		// TODO Auto-generated method stub
		List<Notification> notifications = new ArrayList<>();
		String query = "SELECT notificationId, senderId, message, timestamp, isRead FROM Notification WHERE receiverId = ? AND isRead = FALSE ORDER BY timestamp DESC";

		try {
			this.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(query);

			preparedStatement.setString(1, "admin");
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				Notification notification = new Notification();
				notification.setNotificationId(resultSet.getInt("notificationId"));
				notification.setSenderID(resultSet.getString("senderId"));
				notification.setMessage(resultSet.getString("message"));
				notification.setTimestamp(resultSet.getTimestamp("timestamp"));
				notification.setRead(resultSet.getBoolean("isRead"));

				notifications.add(notification);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Log the error or handle it appropriately
		}

		return notifications;
	}

	public void markNotificationAsRead(int notificationId) {
		String updateQuery = "UPDATE Notification SET isRead = TRUE WHERE notificationId = ?";

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(updateQuery);
			statement.setInt(1, notificationId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Method to get the chat box associated with a student
	public ChatBox getStudentChatBox(String studentRollNo) {
		ChatBox chatBox = null;
		String query = "SELECT chatBoxId FROM Student WHERE rollNo = ?";

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, studentRollNo);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int chatBoxId = resultSet.getInt("chatBoxId");
				chatBox = getChatBoxById(chatBoxId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chatBox;
	}

	// Helper method to get ChatBox details by ID
	public ChatBox getChatBoxById(int chatBoxId) {
		ChatBox chatBox = null;
		String query = "SELECT * FROM ChatBox WHERE chatBoxId = ?";
		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, chatBoxId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String ownerType = resultSet.getString("ownerType");
				chatBox = new ChatBox();
				chatBox.setChatBoxId(chatBoxId);
				chatBox.setOwnerType(ownerType);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chatBox;
	}

	// Method to get the list of chats associated with a student
	public List<Chat> getStudentChats(String studentRollNo) {
		List<Chat> chats = new ArrayList<>();
		String query = "SELECT c.chatID, c.createdAt, c.orgId, c.studentId "
				+ "FROM Chat c JOIN Student s ON c.studentId = s.rollNo " + "WHERE s.rollNo = ?";

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, studentRollNo);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int chatId = resultSet.getInt("chatID");
				String orgId = resultSet.getString("orgId");
				String studentId = resultSet.getString("studentId");
				Timestamp createdAt = resultSet.getTimestamp("createdAt");

				Chat chat = new Chat(chatId, createdAt, orgId, studentId);
				chats.add(chat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chats;
	}

	// Method to get the chat box associated with a organisation
	public ChatBox getOrganisationChatBox(String organisationName) {
		ChatBox chatBox = null;
		String query = "SELECT chatBoxId FROM organisation WHERE name = ?";

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, organisationName.toLowerCase());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				int chatBoxId = resultSet.getInt("chatBoxId");
				chatBox = getChatBoxById(chatBoxId);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chatBox;
	}

	// Method to get the list of chats associated with a organisation
	public List<Chat> getOrganisationChats(String organisationName) {
		List<Chat> chats = new ArrayList<Chat>();
		String query = "SELECT c.chatID, c.createdAt, c.studentId, c.orgId "
				+ "FROM Chat c JOIN organisation o ON c.orgId = o.name " + "WHERE o.name = ?";

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, organisationName);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int chatId = resultSet.getInt("chatID");
				String studentId = resultSet.getString("studentId");
				String orgId = resultSet.getString("orgId");
				Timestamp createdAt = resultSet.getTimestamp("createdAt");

				Chat chat = new Chat(chatId, createdAt, orgId, studentId);
				chats.add(chat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chats;
	}

	public void getMsgs(Chat chat) {
		String query = "SELECT senderId, receiverId, text, transmittedAt FROM Message WHERE chatID = ? ORDER BY transmittedAt ASC";

		try {
			this.getConnection();
			PreparedStatement statement = conn.prepareStatement(query);
			// Set chat ID parameter
			statement.setInt(1, chat.getChatID());

			try (ResultSet resultSet = statement.executeQuery()) {
				// Create a list to hold the messages
				List<Message> messages = new ArrayList<>();

				while (resultSet.next()) {
					// Populate Message object
					Message message = new Message();
					message.setSenderId(resultSet.getString("senderId"));
					message.setReceiverId(resultSet.getString("receiverId"));
					message.setText(resultSet.getString("text"));
					message.setTransmittedAt(resultSet.getTimestamp("transmittedAt"));

					// Add message to the list
					messages.add(message);
				}

				// Set messages list in the Chat object
				chat.setMessages(messages);
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Log exception
		}
	}

	public void storeMessage(Message message) {
		String sql = "INSERT INTO Message (senderId, receiverId, text, transmittedAt, chatID) VALUES (?, ?, ?, ?, ?)";
		try {

			this.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, message.getSenderId());
			preparedStatement.setString(2, message.getReceiverId());
			preparedStatement.setString(3, message.getText());
			preparedStatement.setTimestamp(4, message.getTransmittedAt());
			preparedStatement.setInt(5, message.getChatId());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace(); // Handle SQL exceptions
		}
	}

	public List<String> getDepartments() {
		String sql = "Select * from departments";
		try {

			this.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			ResultSet resultSet = preparedStatement.executeQuery();
			List<String> deps = new ArrayList<String>();
			while (resultSet.next()) {
				deps.add(resultSet.getString("depName"));
			}
			return deps;
		} catch (SQLException e) {
			e.printStackTrace(); // Handle SQL exceptions
		}
		return null;
	}

	public List<String> getCategories() {

		String sql = "select distinct(category) from job;";
		try {

			this.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(sql);

			ResultSet resultSet = preparedStatement.executeQuery();
			List<String> cat = new ArrayList<String>();
			while (resultSet.next()) {
				cat.add(resultSet.getString("category"));
			}
			return cat;
		} catch (SQLException e) {
			e.printStackTrace(); // Handle SQL exceptions
		}
		return null;
	}

	public Interview retrieveInterviewDetails(Application application) {
		String sql = "SELECT i.interviewID, i.timeslot, i.location, i.type, i.status FROM application a "
				+ "INNER JOIN interview i ON a.interviewID = i.interviewID WHERE a.applicationID = ?";

		try {
			this.getConnection();
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, application.getApplicationID());

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				Interview interview = new Interview(resultSet.getInt("interviewID"), application.getStudentID(),
						resultSet.getDate("timeslot"), resultSet.getString("location"), resultSet.getString("type"),
						resultSet.getString("status"));
				return interview;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
