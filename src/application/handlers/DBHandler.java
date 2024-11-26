package application.handlers;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

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
import application.Organisation;
import application.OrganisationRepresentative;
import application.Student;
import application.UnverifiedOrgs;

public interface DBHandler {
	public Connection getConnection() throws SQLException;

	public ResultSet executeQuery(String query);

	public Student authenticateStudent(String stdEmail, String password);

	public int createChatBox(String ownerType);

	public Boolean addStudent(String rollNo, String email, String name, String password, String department,
			int semester, double cgpa, String resume, int chatBoxId);

	public Boolean updateStudentData(String email, Map<String, Object> updates);

	// Verify credentials of OrganisationRepresentative
	public OrganisationRepresentative verifyOrganisationRepresentativeCredentials(String email, String password,
			Boolean isVerified);

	// Add a new Organisation Representative to the database
	public void addOrgRepresentative(String name, String phone, String password, String email, String organisation,
			String position);

	public void addOrganisation(String name, String industry, String description, String location, String contactEmail,
			Boolean isVerified);

	// Check if an Organisation Representative exists in the database
	public boolean isOrganisationRepExists(String organisation, String email);

	public boolean setFeedback(int applicationID, String feedback);

	public boolean addNotification(String senderId, String receiverId, String message);

	public List<Application> getApplicationList();

	public List<Application> getApplicationsByStatus(String status);

	public boolean updateApplicationStatus(int applicationID, String status);

	public List<String> getVerifiedOrganisations();

	// Check if an Organisation exists in the database
	public boolean isOrganisationExists(String organisationName);

	public boolean organisationExists(String orgName);

	public int insertIntoOpportunity(String title, String description, String type) throws SQLException;

	public void insertIntoJob(int opportunityID, String category) throws SQLException;

	public void insertIntoEducational(int opportunityID) throws SQLException;

	public OrganisationRepresentative getOrganisationRepresentative(String email, String password);

	public Admin getAdminInfo(String email, String password);

	public int createChat(Chat chat);
	
	public Boolean isChatPresent(Chat chat);

	public void closeConnection(Connection connection);

	public Map<Integer, String> fetchJobTitles(String category);

	public Map<Integer, String> fetchEducationTitles();

	public EducationalOpportunity getEducationalOppDetailsById(Integer oppId);

	public JobOpportunity getJobById(Integer oppId);

	public Boolean checkValidity(String rollNo, int oppId);

	public Boolean applyForJob(String rollNo, int oppId);

	public List<Application> retrieveApplications(String rollNo);

	public List<ApplicationWithOpportunity> retrieveApplicationsWithOpportunities(String rollNo);

	public List<Notification> fetchStudentNotifications(String email);

	public Boolean markRead(String email, int notifID);

	public Map<String, Integer> getStdDashBoardInfo(String stdID);

	public UnverifiedOrgs getUnverifiedOrgsandInitialReps();

	public void updateOrganisationVerificationStatus(Organisation org);

	public void updateRepresentativeVerificationStatus(OrganisationRepresentative rep);

	public void deleteOrganisation(Organisation org);

	public void deleteOrganisationRepresentative(OrganisationRepresentative rep);

	public String getUnverifiedOrgCount();

	public List<Notification> getNotificationsForAdmin();

	public void markNotificationAsRead(int notificationId);

	// Method to get the chat box associated with a student
	public ChatBox getStudentChatBox(String studentRollNo);

	// Helper method to get ChatBox details by ID
	public ChatBox getChatBoxById(int chatBoxId);

	// Method to get the list of chats associated with a student
	public List<Chat> getStudentChats(String studentRollNo);

	// Method to get the chat box associated with a organisation
	public ChatBox getOrganisationChatBox(String organisationName);

	// Method to get the list of chats associated with a organisation
	public List<Chat> getOrganisationChats(String organisationName);

	public void getMsgs(Chat chat);

	public void storeMessage(Message message);

	public Boolean checkStudentExistence(String email, String rollNo);

	public List<String> getDepartments();

	public List<String> getCategories();

	public Interview retrieveInterviewDetails(Application application);

}
