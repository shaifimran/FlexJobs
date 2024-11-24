package application.handlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Admin;
import application.Application;
import application.ApplicationWithOpportunity;
import application.EducationalOpportunity;
import application.JobOpportunity;
import application.Notification;
import application.Opportunity;
import application.OrganisationRepresentative;
import application.Student;

public class DBHandler {
	private final String dbUrl = "jdbc:mysql://localhost:3306/flexjobs";
	private final String dbUser = "root";
	private final String dbPassword = "wasay";
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

	public Boolean addStudent(String rollNo, String email, String name, String password, String department,
			int semester, double cgpa, String resume) {
		try {
			this.getConnection();
//			System.out.println("Hello");
			String query = "Insert into student(rollNo, email, name, password, department, semester, cgpa, resume) values(?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement preparedStatement1 = conn.prepareStatement(query)) {
				System.out.println("Hello");
				preparedStatement1.setString(1, rollNo);
				preparedStatement1.setString(2, email);
				preparedStatement1.setString(3, name);
				preparedStatement1.setString(4, password);
				preparedStatement1.setString(5, department);
				preparedStatement1.setInt(6, semester);
				preparedStatement1.setDouble(7, cgpa);
				preparedStatement1.setString(8, resume);
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

	public void closeConnection(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				System.err.println("Failed to close the connection: " + e.getMessage());
			}
		}
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

}
