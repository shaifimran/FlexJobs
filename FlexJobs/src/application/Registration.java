package application;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Registration {
	private String registrationID;
	private Date registrationDate;
	private boolean status;
	private boolean isApproved;
	private String userID;

	public String getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	private String userType;

	public Registration(String registrationID, Date registrationDate, boolean status, boolean isApproved, String userID,
			String userType) {
		this.registrationID = registrationID;
		this.registrationDate = registrationDate;
		this.status = status;
		this.isApproved = isApproved;
		this.userID = userID;
		this.userType = userType;
	}

	public Registration makeNewRegistration(Map<String, Object> data) {
		// Make new registration logic
		return new Registration("ID123", new Date(), false, false, "userID", "userType");
	}

	public Student getStudentInfo() {
		// Retrieve student info
		return new Student();
	}

	public List<Document> getSupportingDocuments() {
		// Retrieve supporting documents
		return new ArrayList<>();
	}
}
