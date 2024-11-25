package application.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import application.Admin;
import application.Chat;
import application.ChatBox;
import application.Message;
import application.Notification;
import application.Organisation;
import application.Application;
import application.ApplicationWithOpportunity;
import application.EducationalOpportunity;
import application.JobOpportunity;
import application.Opportunity;
import application.Application;

import application.OrganisationRepresentative;
import application.Student;
import application.UnverifiedOrgs;

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

	public Boolean checkStudentExistence(String email, String rollNo) {
		try {
			this.getConnection();
			String query = "SELECT 1 FROM Student WHERE email = ? and rollNo=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
				preparedStatement.setString(1, email);
				preparedStatement.setString(2, rollNo);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						return true;
					} else {
						return false;
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
			try (PreparedStatement preparedStatement1 = conn.prepareStatement(query)) {
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

			try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
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
		return null; // Return null if no matching user found or an error occurs
	}

	// Add a new Organisation Representative to the database
	public void addOrgRepresentative(String name, String phone, String password, String email, String organisation,
			String position) {
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
	public void addOrganisation(String name, String industry, String description, String location, String contactEmail,
			Boolean isVerified) {
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
				Admin a = new Admin(rs.getString("name"), rs.getString("email"));
				return a;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		String query = "SELECT o.title, o.description, o.postedBy, j.category, j.requirements FROM Opportunity o INNER JOIN Job j ON o.opportunityID = j.opportunityID where o.opportunityID=?";

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
				String req = rs.getString("requirements");
				JobOpportunity o = new JobOpportunity((int) oppId, title, desc, category, req, "open", postedBy);
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
		String query = "insert into Application(status, studentID, opportunityID) values ('submitted', ?, ?)";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, rollNo);
			stmt.setInt(2, oppId);
			int res = stmt.executeUpdate();
			System.out.println(res);
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
		String query = "SELECT a.applicationID, a.status, a.feedback, a.studentID, a.interviewID, a.opportunityID, o.title FROM Application a WHERE a.studentID = ?";

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
				String interviewID = rs.getString("interviewID");
				int opportunityID = rs.getInt("opportunityID");
				String opportunityTitle = rs.getString("title");
				Application application = new Application(applicationID, status, feedback, studentID, interviewID,
						opportunityID, opportunityTitle);
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
				String interviewID = rs.getString("interviewId");
				String opportunityTitle = rs.getString("title");
				String opportunityDescription = rs.getString("description");
				String postedBy = rs.getString("postedBy");

				Application application = new Application(applicationID, status, feedback, rollNo, interviewID,
						opportunityID, opportunityTitle);

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

	public List<Notification> fetchStudentNotifications(String rollNo) {
		String query = "SELECT notificationId, senderId, receiverId, message, timestamp, isRead FROM Notification WHERE receiverId = ?";
		List<Notification> notifications = new ArrayList<>();
		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);

			// Assuming `rollNo` is a String; no conversion needed here.
			stmt.setString(1, rollNo);

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

	public Boolean markRead(String rollNo, int notifID) {
		String query = "UPDATE Notification SET isRead = TRUE WHERE receiverId = ? AND notificationId = ?";
		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, rollNo);
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
							repPosition, repPhone, orgName);

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
		String query = "UPDATE Organisation SET isVerified = ? WHERE name = ?";

		try {
			this.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setBoolean(1, true); // Set isVerified to true
			stmt.setString(2, org.getName());
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
			statement.setString(1, organisationName);
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
		List<Chat> chats = new ArrayList<>();
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

}
